package com.study.basicboard.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.basicboard.domain.dto.BoardCntDto;
import com.study.basicboard.domain.dto.BoardCreateRequest;
import com.study.basicboard.domain.dto.BoardDto;
import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.entity.Comment;
import com.study.basicboard.domain.entity.Like;
import com.study.basicboard.domain.entity.UploadImage;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.BoardCategory;
import com.study.basicboard.domain.enum_class.UserRole;
import com.study.basicboard.repository.BoardRepository;
import com.study.basicboard.repository.CommentRepository;
import com.study.basicboard.repository.LikeRepository;
import com.study.basicboard.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final S3UploadService s3UploadService;
    // private final UploadImageService uploadImageService; => 로컬 디렉토리에 저장할 때 사용 => S3UploadService 대신 사용

    public Page<Board> getBoardList(BoardCategory category, Pageable pageable) {
        // 데이터베이스에서 페이징 적용된 게시물 목록을 가져옵니다
        List<Board> boards = boardRepository.findByCategoryWithPaging(
                category, pageable.getPageSize(), pageable.getOffset());

        // 총 게시물 수를 가져옵니다
        int totalCount = boardRepository.countByCategory(category);

        // PageImpl을 사용하여 Page 객체를 생성합니다
        return new PageImpl<>(boards, pageable, totalCount);
    }

    public List<Board> getNotice(BoardCategory category) {
        return boardRepository.findAllByCategoryAndUserUserRole(category, UserRole.ADMIN);
    }

    public BoardDto getBoard(Long boardId, String category) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        return BoardDto.of(optBoard.get());
    }

    @Transactional
    public Long writeBoard(BoardCreateRequest req, BoardCategory category, String loginId, Authentication auth) throws IOException {
        User loginUser = userRepository.findByLoginId(loginId);
        
        if (loginUser == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }

        // `Board` 엔티티를 만들고 저장
        Board board = req.toEntity(category, loginUser);
        boardRepository.save(board);

        // 이미지 업로드 및 연결
        UploadImage uploadImage = s3UploadService.saveImage(req.getUploadImage(), board);
        if (uploadImage != null) {
            board.setUploadImage(uploadImage);
        }

        // 카테고리에 따른 사용자 랭크 업
        if (category.equals(BoardCategory.GREETING)) {
            loginUser.rankUp(UserRole.SILVER, auth);
        }

        return board.getId();
    }

    @Transactional
    public Long editBoard(Long boardId, String category, BoardDto dto) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();
        // 게시글에 이미지가 있었으면 삭제
        if (board.getUploadImage() != null) {
            s3UploadService.deleteImage(board.getUploadImage());
            board.setUploadImage(null);
        }

        UploadImage uploadImage = s3UploadService.saveImage(dto.getNewImage(), board);
        if (uploadImage != null) {
            board.setUploadImage(uploadImage);
        }
        board.update(dto);

        return board.getId();
    }

    @Transactional
    public Long deleteBoard(Long boardId, String category) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();
        User boardUser = board.getUser();
        boardUser.likeChange(boardUser.getReceivedLikeCnt() - board.getLikeCnt());
        if (board.getUploadImage() != null) {
            // 로컬 파일 시스템에서 이미지 삭제
            deleteImage(board.getUploadImage());  // deleteImage() 메서드를 호출
            board.setUploadImage(null);
        }
        boardRepository.deleteById(boardId);
        return boardId;
    }

    private void deleteImage(UploadImage uploadImage) {
		// TODO Auto-generated method stub
		
	}

	public String getCategory(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        return board.getCategory().toString().toLowerCase();
    }

    public List<Board> findMyBoard(String category, String loginId) {
        if (category.equals("board")) {
            return boardRepository.findAllByUserLoginId(loginId);
        } else if (category.equals("like")) {
            List<Like> likes = likeRepository.findAllByUserLoginId(loginId);
            List<Board> boards = new ArrayList<>();
            for (Like like : likes) {
                boards.add(like.getBoard());
            }
            return boards;
        } else if (category.equals("comment")) {
            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
            List<Board> boards = new ArrayList<>();
            HashSet<Long> commentIds = new HashSet<>();

            for (Comment comment : comments) {
                if (!commentIds.contains(comment.getBoard().getId())) {
                    boards.add(comment.getBoard());
                    commentIds.add(comment.getBoard().getId());
                }
            }
            return boards;
        }
        return null;
    }

    public BoardCntDto getBoardCnt(){
        return BoardCntDto.builder()
                .totalBoardCnt(boardRepository.count())
                .totalNoticeCnt(boardRepository.countAllByUserUserRole(UserRole.ADMIN))
                .totalGreetingCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.GREETING, UserRole.ADMIN))
                .totalFreeCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.FREE, UserRole.ADMIN))
                .totalGoldCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.GOLD, UserRole.ADMIN))
                .build();
    }
}


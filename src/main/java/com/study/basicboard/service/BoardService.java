package com.study.basicboard.service;

import com.study.basicboard.domain.dto.BoardCntDto;
import com.study.basicboard.domain.dto.BoardCreateRequest;
import com.study.basicboard.domain.dto.BoardDto;
import com.study.basicboard.domain.entity.*;
import com.study.basicboard.domain.enum_class.BoardCategory;
import com.study.basicboard.domain.enum_class.UserRole;
import com.study.basicboard.repository.BoardRepository;
import com.study.basicboard.repository.CommentRepository;
import com.study.basicboard.repository.LikeRepository;
import com.study.basicboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final S3UploadService s3UploadService;
    // private final UploadImageService uploadImageService; => 로컬 디렉토리에 저장할 때 사용 => S3UploadService 대신 사용

    public Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword) {
        if (searchType != null && keyword != null) {
            if (searchType.equals("title")) {
                return boardRepository.findAllByCategoryAndTitleContainsAndUserUserRoleNot(category, keyword, UserRole.ADMIN, pageRequest);
            } else {
                return boardRepository.findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot(category, keyword, UserRole.ADMIN, pageRequest);
            }
        }
        return boardRepository.findAllByCategoryAndUserUserRoleNot(category, UserRole.ADMIN, pageRequest);
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
        User loginUser = userRepository.findByLoginId(loginId).get();

        Board savedBoard = boardRepository.save(req.toEntity(category, loginUser));

        UploadImage uploadImage = s3UploadService.saveImage(req.getUploadImage(), savedBoard);
        if (uploadImage != null) {
            savedBoard.setUploadImage(uploadImage);
        }

        if (category.equals(BoardCategory.GREETING)) {
            loginUser.rankUp(UserRole.SILVER, auth);
        }
        return savedBoard.getId();
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
            s3UploadService.deleteImage(board.getUploadImage());
            board.setUploadImage(null);
        }
        boardRepository.deleteById(boardId);
        return boardId;
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

/*
 * getBoardList() : 해당 카테고리에 있는 글 list를 return 이 때, 검색을 했다면 제목, 유저 닉네임에 키워드가 포함되는 글을 return 
 * 
 * writeBoard() : 글 작성 Board 저장 -> 이미지가 있다면 이미지 저장 후 저장된 Board에 해당 이미지 정보 추가 삽입 만약 가입 인사 게시판에 글이 저장된 경우라면 
 * 					로그인 한 유저의 등급을 SILVER로 조정
 * 
 * findMyBoard() : 마이 페이지에서 내가 작성한 글, 내가 좋아요 누른 글, 내가 댓글을 추가한 글을 category로 분류해
 * 					List를 return 해주는 메소드 내가 좋아요 누른 글은 로그인 한 유저의 loginId를 가지는 Like를 모두 불러온 후 Like에
 * 					저장된 Board들을 List로 변환 후 return 내가 댓글을 추가한 글도 좋아요와 비슷하긴 하지만 하나의 글에 여러개의 댓글을 단 경우, 
 * 					한번만 return 되게 하기 위해 HashSet을 사용하여 중복을 제거해서 return
 */

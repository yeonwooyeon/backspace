package com.study.basicboard.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.study.basicboard.domain.dto.UserCntDto;
import com.study.basicboard.domain.dto.UserDto;
import com.study.basicboard.domain.dto.UserJoinRequest;
import com.study.basicboard.domain.entity.Comment;
import com.study.basicboard.domain.entity.Like;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;
import com.study.basicboard.repository.CommentRepository;
import com.study.basicboard.repository.LikeRepository;
import com.study.basicboard.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;

    public BindingResult joinValid(UserJoinRequest req, BindingResult bindingResult)
    {
        if (req.getLoginId().isEmpty()) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 비어있습니다."));
        } else if (req.getLoginId().length() > 10) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 10자가 넘습니다."));
        } else if (userRepository.existsByLoginId(req.getLoginId())) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 중복됩니다."));
        }

        if (req.getPassword().isEmpty()) {
            bindingResult.addError(new FieldError("req", "password", "비밀번호가 비어있습니다."));
        }

        if (!req.getPassword().equals(req.getPasswordCheck())) {
            bindingResult.addError(new FieldError("req", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (req.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 비어있습니다."));
        } else if (req.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (userRepository.existsByNickname(req.getNickname())) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 중복됩니다."));
        }

        
        return bindingResult;
    }

    public void join(UserJoinRequest req) {
    	// 비밀번호만 암호화하고 이메일은 그대로 저장
        User user = req.toEntity(encoder.encode(req.getPassword())); // toEntity 메서드 호출
        userRepository.save(user); // 저장
    }
    public User myInfo(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public BindingResult editValid(UserDto dto, BindingResult bindingResult, String loginId)
    {
        User loginUser = userRepository.findByLoginId(loginId);

        if (dto.getNowPassword().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 비어있습니다."));
        } else if (!encoder.matches(dto.getNowPassword(), loginUser.getPassword())) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 틀렸습니다."));
        }

        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            bindingResult.addError(new FieldError("dto", "newPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (dto.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 비어있습니다."));
        } else if (dto.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (!dto.getNickname().equals(loginUser.getNickname()) && userRepository.existsByNickname(dto.getNickname())) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 중복됩니다."));
        }
		return bindingResult;
        
       
    }

    @Transactional
    public void edit(UserDto dto, String loginId) {
        User loginUser = userRepository.findByLoginId(loginId);

        if (dto.getNewPassword().equals("")) {
            loginUser.edit(loginUser.getPassword(), dto.getNickname());
        } else {
            loginUser.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
        }
    }

    @Transactional
    public Boolean delete(String loginId, String nowPassword) {
        User loginUser = userRepository.findByLoginId(loginId);

        if (encoder.matches(nowPassword, loginUser.getPassword())) {
            List<Like> likes = likeRepository.findAllByUserLoginId(loginId);
            for (Like like : likes) {
                like.getBoard().likeChange( like.getBoard().getLikeCnt() - 1 );
            }

            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
            for (Comment comment : comments) {
                comment.getBoard().commentChange( comment.getBoard().getCommentCnt() - 1 );
            }

            userRepository.delete(loginUser);
            return true;
        } else {
            return false;
        }
    }

    public Page<User> findAllByNickname(String keyword, PageRequest pageRequest) {
        return userRepository.findAllByNicknameContains(keyword, pageRequest);
    }

    @Transactional
    public void changeRole(Long userId) {
        User user = userRepository.findById(userId);
        user.changeRole();
    }

    public UserCntDto getUserCnt() {
        return UserCntDto.builder()
                .totalUserCnt(userRepository.count())
                .totalAdminCnt(userRepository.countAllByUserRole(UserRole.ADMIN))
                .totalBronzeCnt(userRepository.countAllByUserRole(UserRole.BRONZE))
                .totalSilverCnt(userRepository.countAllByUserRole(UserRole.SILVER))
                .totalGoldCnt(userRepository.countAllByUserRole(UserRole.GOLD))
                .totalBlacklistCnt(userRepository.countAllByUserRole(UserRole.BLACKLIST))
                .build();
    }
    
    public String findUserId(String nickname, String email) {
        User user = userRepository.findByNicknameAndEmail(nickname, email);
        if (user != null) {
            return user.getLoginId(); // 아이디 반환
        }
        return null; // 사용자를 찾지 못한 경우
    }

    @Transactional
    public boolean resetPassword(String loginId, String newPassword) {
        User user = userRepository.findByLoginId(loginId);
        if (user != null) {
            user.setPassword(encoder.encode(newPassword)); // 비밀번호 변경
            userRepository.updatePassword(user); // 변경 사항 저장
            return true; // 성공
        }
        return false; // 실패한 경우
    }
    

	

}
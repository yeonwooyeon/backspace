package com.study.basicboard.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;

@Mapper
/* @Repository */
public interface UserRepository { // extends JpaRepository<User, Long> {

	/*
	 * Optional<User> findByLoginId(String loginId); Page<User>
	 * findAllByNicknameContains(String nickname, PageRequest pageRequest); Boolean
	 * existsByLoginId(String loginId); Boolean existsByNickname(String nickname);
	 * Long countAllByUserRole(UserRole userRole); User save(User user); void
	 * delete(User loginUser); Optional<User> findById(Long userId); Long count();
	 */

	User findByLoginId(@Param("loginId") String loginId);

	List<User> findAllByNicknameContains(@Param("nickname") String nickname);

	Boolean existsByLoginId(@Param("loginId") String loginId);

	Boolean existsByNickname(@Param("nickname") String nickname);

	Long countAllByUserRole(@Param("userRole") UserRole userRole);

	void save(User user);

	void delete(@Param("userId") User loginUser);

	User findById(@Param("userId") Long userId);

	Long count();

	Page<User> findAllByNicknameContains(String keyword, PageRequest pageRequest);

	User findByNicknameAndEmail(String nickname, String email); // 닉네임과 이메일로 사용자 찾기

	Boolean existsByEmail(String email); // 이메일 체크 메서드

	void updatePassword(User user);
}

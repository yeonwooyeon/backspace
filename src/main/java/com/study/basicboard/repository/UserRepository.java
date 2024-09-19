package com.study.basicboard.repository;

import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

@Mapper
/*@Repository*/
public interface UserRepository{ //extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
    Page<User> findAllByNicknameContains(String nickname, PageRequest pageRequest);
    Boolean existsByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
    Long countAllByUserRole(UserRole userRole);
    User save(User user);
	void delete(User loginUser);
	Optional<User> findById(Long userId);
	Long count();
}

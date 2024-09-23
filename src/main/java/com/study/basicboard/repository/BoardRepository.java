package com.study.basicboard.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;

import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.enum_class.BoardCategory;
import com.study.basicboard.domain.enum_class.UserRole;

@Mapper
public interface BoardRepository {

    List<Board> findAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole, PageRequest pageRequest);
    List<Board> findAllByCategoryAndTitleContainsAndUserUserRoleNot(
            @Param("category") BoardCategory category,
            @Param("keyword") String keyword,
            @Param("userRole") UserRole userRole,
            @Param("pageRequest") PageRequest pageRequest);

    List<Board> findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot(BoardCategory category, String nickname, UserRole userRole, PageRequest pageRequest);
    List<Board> findAllByUserLoginId(String loginId);
    List<Board> findAllByCategoryAndUserUserRole(BoardCategory category, UserRole userRole);
    Long countAllByUserUserRole(UserRole userRole);
    Long countAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole);
	Optional<Board> findById(Long boardId);
	void deleteById(Long boardId);
	void save(Board entity);
	Long count();
	//fgf
	
	 // 페이징된 요소를 가져오기 위한 메서드
	List<Board> findByCategoryWithPaging(BoardCategory category, int pageSize, long offset);
	 // 전체 데이터 개수를 가져오기 위한 메서드
int countByCategory(@Param("category") BoardCategory category);

}


package com.study.basicboard.repository;

import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.enum_class.BoardCategory;
import com.study.basicboard.domain.enum_class.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole, PageRequest pageRequest);
    Page<Board> findAllByCategoryAndTitleContainsAndUserUserRoleNot(BoardCategory category, String title, UserRole userRole, PageRequest pageRequest);
    Page<Board> findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot(BoardCategory category, String nickname, UserRole userRole, PageRequest pageRequest);
    List<Board> findAllByUserLoginId(String loginId);
    List<Board> findAllByCategoryAndUserUserRole(BoardCategory category, UserRole userRole);
    Long countAllByUserUserRole(UserRole userRole);
    Long countAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole);
}
/*
 * findAllByCategoryAndUserUserRoleNot() : 해당 카테고리에 있는 게시글을 페이지에 맞게 조회, 이 때
 * ADMIN이 작성한 글(공지)는 포함 X findAllByCategoryAndUserUserRole() : 해당 카테고리에 있는 공지 글
 * 조회 findAllByCategoryAndTitleContainsAndUserUserRoleNot(),
 * findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot() : 검색 기능에 사용
 * findAllByUserLoginId() : 유저의 마이 페이지에서 내가 작성한 글 조회 시 사용
 * countAllByUserUserRole() : 전체 공지글이 몇개 있는지 조회 시 사용
 * countAllByCategoryAndUserUserRoleNot() : 해당 카테고리에 공지글을 제외한 글이 몇개 있는지 조회 시 사용
 */
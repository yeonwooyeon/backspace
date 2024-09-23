package com.study.basicboard.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.basicboard.domain.dto.UserDto;
import com.study.basicboard.domain.dto.UserJoinRequest;
import com.study.basicboard.domain.dto.UserLoginRequest;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.service.BoardService;
import com.study.basicboard.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "users/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UserJoinRequest req, BindingResult bindingResult, Model model) {

        // Validation
        if (userService.joinValid(req, bindingResult).hasErrors()) {
            return "users/join";
        }

        userService.join(req);
        model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
        model.addAttribute("nextUrl", "/users/login");
        return "printMessage";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        // 로그인 성공 시 이전 페이지로 redirect 되게 하기 위해 세션에 저장
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login") && !uri.contains("/join")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        request.getSession().setAttribute("loggedIn", true);

        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "users/login";
    }
    
	/*
	 * @PostMapping("/login") public String login(@Valid @ModelAttribute
	 * UserLoginRequest req, HttpServletRequest request, BindingResult
	 * bindingResult, Model model) { // 유효성 검사 및 인증 로직 추가 if
	 * (bindingResult.hasErrors()) { return "users/login"; }
	 * 
	 * boolean success = userService.authenticate(req); if (success) { String
	 * prevPage = (String) request.getSession().getAttribute("prevPage"); return
	 * prevPage != null ? "redirect:" + prevPage : "redirect:/home"; } else {
	 * model.addAttribute("message", "로그인 실패. 아이디 또는 비밀번호를 확인하세요."); return
	 * "users/login"; } }
	 */

    @GetMapping("/myPage/{category}")
    public String myPage(@PathVariable String category, Authentication auth, Model model) {
        model.addAttribute("boards", boardService.findMyBoard(category, auth.getName()));
        model.addAttribute("category", category);
        model.addAttribute("user", userService.myInfo(auth.getName()));
        return "users/myPage";
    }

    @GetMapping("/edit")
    public String userEditPage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/edit";
    }

    @PostMapping("/edit")
    public String userEdit(@Valid @ModelAttribute UserDto dto, BindingResult bindingResult,
                           Authentication auth, Model model) {

        // Validation
        if (userService.editValid(dto, bindingResult, auth.getName()).hasErrors()) {
            return "users/edit";
        }

        userService.edit(dto, auth.getName());

        model.addAttribute("message", "정보가 수정되었습니다.");
        model.addAttribute("nextUrl", "/users/myPage/board");
        return "printMessage";
    }

    @GetMapping("/delete")
    public String userDeletePage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/delete";
    }

    @PostMapping("/delete")
    public String userDelete(@ModelAttribute UserDto dto, Authentication auth, Model model) {
        Boolean deleteSuccess = userService.delete(auth.getName(), dto.getNowPassword());
        if (deleteSuccess) {
            model.addAttribute("message", "탈퇴 되었습니다.");
            model.addAttribute("nextUrl", "/users/logout");
            return "printMessage";
        } else {
            model.addAttribute("message", "현재 비밀번호가 틀려 탈퇴에 실패하였습니다.");
            model.addAttribute("nextUrl", "/users/delete");
            return "printMessage";
        }
    }

    @GetMapping("/admin")
    public String adminPage(@RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "") String keyword,
                            Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<User> users = userService.findAllByNickname(keyword, pageRequest);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "users/admin";
    }

    @GetMapping("/admin/{userId}")
    public String adminChangeRole(@PathVariable Long userId,
                                  @RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "") String keyword) throws UnsupportedEncodingException {
        userService.changeRole(userId);
        return "redirect:/users/admin?page=" + page + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
    }
    
    @GetMapping("/find/id")
    public String findIdPage() {
        return "users/findId"; // 아이디 찾기 페이지 교체
    }

    @PostMapping("/find/id")
    public String findId(@RequestParam String nickname, @RequestParam String email, Model model) {
        String loginId = userService.findUserId(nickname, email);
        if (loginId != null) {
            model.addAttribute("message", "아이디: " + loginId);
        } else {
            model.addAttribute("error", "해당 사용자를 찾을 수 없습니다.");
        }
        return "users/findResult"; // 결과 페이지
    }

    @GetMapping("/find/password")
    public String findPasswordPage() {
        return "users/findPassword"; // 비밀번호 찾기 페이지
    }

    @PostMapping("/find/password")
    public String findPassword(@RequestParam String loginId, @RequestParam String newPassword, Model model) {
        boolean success = userService.resetPassword(loginId, newPassword);
        if (success) {
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            model.addAttribute("error", "해당 아이디를 찾지 못했습니다.");
        }
        return "users/findResult"; // 결과 페이지
    }


}

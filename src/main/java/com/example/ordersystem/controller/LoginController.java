package com.example.ordersystem.controller;

import com.example.ordersystem.controller.form.LoginForm;
import com.example.ordersystem.dto.MemberDto;
import com.example.ordersystem.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@RequiredArgsConstructor
@Controller
public class LoginController {
    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, RedirectAttributes rttr, @ModelAttribute LoginForm loginForm) {
        Optional<MemberDto> member = memberRepository.findByUserId(loginForm.getId());
        if (member.isPresent()) {
            MemberDto loginMember = member.get();
            if (loginMember.getPwd().equals(loginForm.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("MEMBER", loginMember);
                return "redirect:/main";
            } else {
                rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
                return "redirect:/login";
            }
        } else {
            rttr.addFlashAttribute("msg", "회원 정보가 존재하지 않습니다.");
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/login";
    }
}

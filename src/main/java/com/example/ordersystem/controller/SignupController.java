package com.example.ordersystem.controller;

import com.example.ordersystem.controller.form.SignupForm;
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
public class SignupController {
    private final MemberRepository memberRepository;

    @GetMapping("/signup")
    public String signup() {
        return "/signup";
    }

    @PostMapping("/signup")
    public String signup(HttpServletRequest request, RedirectAttributes rttr, @ModelAttribute SignupForm signupForm) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        String msg = memberRepository.save(signupForm.getId(), signupForm.getPassword(), signupForm.getEmail());
        rttr.addFlashAttribute("msg", msg);
        return "redirect:/signup";
    }
}

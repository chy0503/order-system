package com.example.ordersystem.controller;

import com.example.ordersystem.dto.MemberDto;
import com.example.ordersystem.dto.ProductDto;
import com.example.ordersystem.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping("/")
    public String intro() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        model.addAttribute("member", member);

        List<ProductDto> products = productRepository.findAll();
        products.forEach(p -> p.setRating(productRepository.calculateRating(p.getId())));
        model.addAttribute("products", products);
        return "/main";
    }
}

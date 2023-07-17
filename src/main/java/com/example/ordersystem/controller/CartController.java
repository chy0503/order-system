package com.example.ordersystem.controller;

import com.example.ordersystem.dto.CartDto;
import com.example.ordersystem.dto.CartWithProductDto;
import com.example.ordersystem.dto.MemberDto;
import com.example.ordersystem.repository.CartRepository;
import com.example.ordersystem.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @GetMapping("/cart")
    public String cart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<CartDto> carts = cartRepository.findByUserId(member.getId());
        List<CartWithProductDto> cartList = carts.stream().map(c -> CartWithProductDto.from(c, productRepository.findById(c.getProductId()).get())).collect(Collectors.toList());
        model.addAttribute("cartList", cartList);
        return "/cart";
    }

    @PostMapping("/addCart")
    public String addCart(HttpServletRequest request, RedirectAttributes rttr, @RequestParam String productId) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        cartRepository.save(member.getId(), Long.parseLong(productId));
        rttr.addFlashAttribute("cartMsg", "장바구니에 추가되었습니다.\n장바구니로 이동하시겠습니까?");
        return "redirect:/main";
    }

    @PostMapping("/deleteCart")
    public String deleteCart(HttpServletRequest request, RedirectAttributes rttr, @RequestParam Long cartId) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = cartRepository.delete(member.getId(), cartId);
        rttr.addFlashAttribute("deleteMsg", msg);
        return "redirect:/cart";
    }

    @PostMapping("/deleteAllCart")
    public String deleteAllCart(HttpServletRequest request, RedirectAttributes rttr) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = cartRepository.deleteAll(member.getId());
        rttr.addFlashAttribute("deleteAllMsg", msg);
        return "redirect:/cart";
    }
}

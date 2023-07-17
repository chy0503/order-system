package com.example.ordersystem.controller;

import com.example.ordersystem.controller.form.ReviewForm;
import com.example.ordersystem.dto.MemberDto;
import com.example.ordersystem.dto.ProductDto;
import com.example.ordersystem.dto.ReviewDto;
import com.example.ordersystem.dto.ReviewWithDetailDto;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.repository.ProductRepository;
import com.example.ordersystem.repository.ReviewRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @GetMapping("/writeReview")
    public String writeReview(@RequestParam Long orderId, @RequestParam Long productId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        ProductDto product = productRepository.findById(productId).get();
        model.addAttribute("product", product);
        model.addAttribute("orderId", orderId);
        return "/writeReview";
    }

    @PostMapping("/addReview")
    public String addReview(@ModelAttribute ReviewForm reviewForm, HttpServletRequest request, RedirectAttributes rttr) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        reviewForm.setMemberId(member.getId());
        String msg = reviewRepository.save(reviewForm);
        rttr.addFlashAttribute("msg", msg);
        return "redirect:/myreview";
    }

    @GetMapping("/review")
    public String review(@RequestParam Long productId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        List<ReviewDto> reviews = reviewRepository.findByProductId(productId);
        List<ReviewWithDetailDto> reviewList = reviews.stream().map(r -> ReviewWithDetailDto.from(r, productRepository.findById(orderRepository.findById(r.getOrderId()).get().getProductId()).get())).collect(Collectors.toList());
        model.addAttribute("reviewList", reviewList);
        return "/review";
    }

    @GetMapping("/myreview")
    public String myreview(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<ReviewDto> reviews = reviewRepository.findByUserId(member.getId());
        List<ReviewWithDetailDto> reviewList = reviews.stream().map(r -> ReviewWithDetailDto.from(r, productRepository.findById(orderRepository.findById(r.getOrderId()).get().getProductId()).get())).collect(Collectors.toList());
        model.addAttribute("reviewList", reviewList);
        return "/myreview";
    }

    @PostMapping("/deleteReview")
    public String deleteReview(HttpServletRequest request, RedirectAttributes rttr, @RequestParam Long reviewId) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = reviewRepository.delete(member.getId(), reviewId);
        rttr.addFlashAttribute("deleteMsg", msg);
        return "redirect:/myreview";
    }

    @PostMapping("/deleteAllReview")
    public String deleteAllReview(HttpServletRequest request, RedirectAttributes rttr) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = reviewRepository.deleteAll(member.getId());
        rttr.addFlashAttribute("deleteAllMsg", msg);
        return "redirect:/myreview";
    }
}

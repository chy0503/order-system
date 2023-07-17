package com.example.ordersystem.controller;

import com.example.ordersystem.controller.form.OrderForm;
import com.example.ordersystem.dto.*;
import com.example.ordersystem.repository.*;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/order")
    public String order(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<OrderDto> orders = orderRepository.findAllOrder(member.getId());
        List<OrderWithDetailDto> orderList = orders.stream().map(o -> OrderWithDetailDto.from(o, productRepository.findById(o.getProductId()).get(), addressRepository.findById(o.getAddressId()).get(), reviewRepository.isReviewedOrder(o.getId()))).collect(Collectors.toList());
        model.addAttribute("orderList", orderList);
        return "/order";
    }

    @GetMapping("/addOrder")
    public String addOrder(@RequestParam String productId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<AddressDto> addressList = addressRepository.findByUserId(member.getId());
        model.addAttribute("addressList", addressList);

        Optional<ProductDto> product = productRepository.findById(Long.valueOf(productId));
        OrderForm orderForm = OrderForm.builder()
                .memberId(member.getId())
                .productId(Long.valueOf(productId))
                .productName(product.get().getName())
                .productPrice(product.get().getPrice())
                .productNum(1)
                .inCart(0L)
                .build();
        model.addAttribute("orderForm", orderForm);
        return "/addOrder";
    }

    @GetMapping("/addCartOrder")
    public String addCartOrder(@RequestParam Long cartId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<AddressDto> addressList = addressRepository.findByUserId(member.getId());
        model.addAttribute("addressList", addressList);

        Optional<CartDto> cart = cartRepository.findById(cartId);
        CartWithProductDto cartDetail = CartWithProductDto.from(cart.get(), productRepository.findById(cart.get().getProductId()).get());
        OrderForm orderForm = OrderForm.builder()
                .memberId(member.getId())
                .productId(cartDetail.getProductId())
                .productName(cartDetail.getProductName())
                .productPrice(cartDetail.getProductPrice())
                .productNum(cartDetail.getNum())
                .inCart(cartId)
                .build();
        model.addAttribute("orderForm", orderForm);
        return "/addOrder";
    }

    @GetMapping("/addAllCartOrder")
    public String addAllCartOrder(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        List<AddressDto> addressList = addressRepository.findByUserId(member.getId());
        model.addAttribute("addressList", addressList);
        return "/addAllCartOrder";
    }

    @PostMapping("/addOrder")
    public String addOrder(@ModelAttribute OrderForm orderForm, RedirectAttributes rttr) {
        String msg = orderRepository.addOrder(orderForm);
        rttr.addFlashAttribute("orderMsg", msg);
        return "redirect:/main";
    }

    @PostMapping("/addAllCartOrder")
    public String addAllCartOrder(@RequestParam Long addressId, HttpServletRequest request, RedirectAttributes rttr) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = orderRepository.addAllCartOrder(member.getId(), addressId);
        rttr.addFlashAttribute("orderMsg", msg);
        return "redirect:/order";
    }

    @PostMapping("/confirmOrder")
    public String addOrder(@RequestParam Long orderId, HttpServletRequest request, RedirectAttributes rttr) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = orderRepository.confirmOrder(member.getId(), orderId);
        rttr.addFlashAttribute("msg", msg);
        return "redirect:/order";
    }
}

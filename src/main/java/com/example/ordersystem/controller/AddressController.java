package com.example.ordersystem.controller;

import com.example.ordersystem.controller.form.AddressForm;
import com.example.ordersystem.dto.AddressDto;
import com.example.ordersystem.dto.MemberDto;
import com.example.ordersystem.repository.AddressRepository;
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

@RequiredArgsConstructor
@Controller
public class AddressController {
    private final AddressRepository addressRepository;

    @GetMapping("/address")
    public String address(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        model.addAttribute("member", member);

        List<AddressDto> addressList = addressRepository.findByUserId(member.getId());
        model.addAttribute("addressList", addressList);
        return "/address";
    }

    @GetMapping("/addAddress")
    public String addAddress(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";
        return "/addAddress";
    }

    @PostMapping("/addAddress")
    public String addAddress(HttpServletRequest request, RedirectAttributes rttr, @ModelAttribute AddressForm addressForm) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = addressRepository.save(addressForm.getName(), member.getId(), addressForm.getAddress(), addressForm.getPhone());
        rttr.addFlashAttribute("msg", msg);
        return "redirect:/addAddress";
    }

    @PostMapping("/deleteAddress")
    public String deleteAddress(HttpServletRequest request, RedirectAttributes rttr, @RequestParam Long addressId) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return "redirect:/login";

        MemberDto member = (MemberDto) session.getAttribute("MEMBER");
        String msg = addressRepository.delete(member.getId(), addressId);
        rttr.addFlashAttribute("msg", msg);
        return "redirect:/address";
    }
}

package com.demo.cdmall1.web.controller.rest;

import java.security.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.product.entity.*;
import com.demo.cdmall1.domain.product.service.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class ProductMemberController {
	private final ProductMemberService productMemberService;
	private final ProductService productService;
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/product_member/good_or_bad")
	public ResponseEntity<?> goodOrBad(Integer pno, boolean isGood, Principal principal) {
		WishorNot state = productMemberService.wishOrNot(pno, isGood, principal.getName());
		Integer cnt = productService.goodOrBad(pno, state.ordinal());
		return ResponseEntity.ok(cnt);
	}	
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/product_member/is_exist")
	public ResponseEntity<?> isExist(Integer pno, String loginId) {
		boolean check = productMemberService.isExist(pno, loginId); 
		return ResponseEntity.ok(check);                                                                                                                                                                                                                                                                                                     
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/product_wish/wish")
	public ResponseEntity<?> findAllByUsername(Principal principal) {
		String username = principal.getName();
		List<ProductMember> wishList = productMemberService.getWishListByUsername(username);
		return ResponseEntity.ok(wishList);
	}
	
	
}

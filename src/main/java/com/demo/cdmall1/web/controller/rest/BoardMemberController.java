package com.demo.cdmall1.web.controller.rest;

import java.net.*;
import java.security.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.board.entity.WarnCheck;
import com.demo.cdmall1.domain.board.service.*;
import com.demo.cdmall1.domain.member.service.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class BoardMemberController {
	private final BoardMemberService service;
	private final RestTemplate restTemplate;
	@Value("${app.internal-base-url:http://127.0.0.1:8080}")
	private String appBaseUrl;
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/board_member/good_or_bad")
	public ResponseEntity<?> goodOrBad(Integer bno, boolean isGood, Principal principal) {
		GoodOrBad state = service.goodOrBad(bno, isGood, principal.getName());
		URI uri = UriComponentsBuilder.fromHttpUrl(appBaseUrl).path("/board/good_or_bad")
				.queryParam("bno", bno+"").queryParam("state", state.ordinal()+"").build().toUri();
		Integer cnt = restTemplate.getForObject(uri.toString(), Integer.class);
		return ResponseEntity.ok(cnt);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/board_member/warn")
	public ResponseEntity<?> warn(Integer bno, Principal principal){
		WarnCheck state = service.warncheck(bno, principal.getName());
		URI uri = UriComponentsBuilder.fromHttpUrl(appBaseUrl).path("/board/warn")
				.queryParam("bno", bno+"").queryParam("state", state.ordinal()+"").build().toUri();
		Integer cnt = restTemplate.getForObject(uri.toString(), Integer.class);
		return ResponseEntity.ok(cnt);
	}
}

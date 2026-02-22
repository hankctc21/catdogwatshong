package com.demo.cdmall1.web.controller.mvc;

import jakarta.servlet.http.*;

import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImageBoardMvcController {
	@GetMapping("/imageBoard/list")
	public void list() {
	}
	@GetMapping("/imageBoard/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/imageBoard/write")
	public void write() {
	}
	
	//사진게시판검색
	@GetMapping("/imageBoard/search")
	public void search() {
		
	}
		
	@PostMapping("/imageBoard/search")
	public void search(@RequestParam (defaultValue = "1") Integer pageno,String word, HttpSession session) {
		session.setAttribute("word", word);
	}
}

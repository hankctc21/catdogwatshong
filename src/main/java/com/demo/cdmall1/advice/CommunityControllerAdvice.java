package com.demo.cdmall1.advice;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.demo.cdmall1.domain.board.entity.BoardFail;

@RestControllerAdvice
public class CommunityControllerAdvice {
	@ExceptionHandler(BoardFail.BoardNotFoundException.class)
	public ResponseEntity<String> boardNotFoundException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다");
	}
	
	@ExceptionHandler(BoardFail.CommentNotFoundException.class)
	public ResponseEntity<String> commentNotFoundException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다");
	}
	
	@ExceptionHandler(BoardFail.AttachmentNotFoundException.class)
	public ResponseEntity<String> attachmentNotFoundException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("첨부파일을 찾을 수 없습니다");
	}
	
	@ExceptionHandler(BoardFail.IllegalJobException.class)
	public ResponseEntity<String> illegalJobException() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("요청을 처리할 수 없습니다");
	}
	
	@ExceptionHandler(BoardFail.AlreadyRecommendException.class)
	public ResponseEntity<String> alreadyRecommendException() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 처리한 작업입니다");
	}
}

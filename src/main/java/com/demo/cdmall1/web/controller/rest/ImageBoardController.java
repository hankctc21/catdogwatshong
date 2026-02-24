package com.demo.cdmall1.web.controller.rest;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

import jakarta.servlet.http.*;
import jakarta.validation.*;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.*;
import org.springframework.validation.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import com.demo.cdmall1.domain.imageboard.entity.*;
import com.demo.cdmall1.domain.imageboard.service.*;
import com.demo.cdmall1.util.*;
import com.demo.cdmall1.web.dto.*;

import lombok.*;

@RequiredArgsConstructor
@RestController
public class ImageBoardController {
	private final ImageBoardService imageService;
	@Value("${app.upload.image-dir:upload/image}")
	private String imageDir;
	// 이미지 첨부파일 보기
		@GetMapping(path={"/imageBoard/image", "/ibtemp/image"})
		public ResponseEntity<?> showImage(@RequestParam String imagename) throws IOException {
			File file = new File(imageDir, imagename);
			if(file.exists()==false) {
				file = new File(ZmallConstant.IMAGE_FOLDER + imagename);
			}
			HttpHeaders headers = new HttpHeaders();
			if(file.exists()==false) {
				String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='640' height='420'>"
						+ "<defs><linearGradient id='g' x1='0%' y1='0%' x2='100%' y2='100%'>"
						+ "<stop offset='0%' stop-color='#e2e8f0'/><stop offset='100%' stop-color='#cbd5e1'/>"
						+ "</linearGradient></defs>"
						+ "<rect width='100%' height='100%' fill='url(#g)'/>"
						+ "<text x='50%' y='48%' text-anchor='middle' dominant-baseline='middle' "
						+ "font-size='22' fill='#334155'>이미지 준비중</text>"
						+ "<text x='50%' y='56%' text-anchor='middle' dominant-baseline='middle' "
						+ "font-size='14' fill='#64748b'>" + imagename + "</text></svg>";
				headers.setContentType(MediaType.parseMediaType("image/svg+xml;charset=UTF-8"));
				return ResponseEntity.ok().headers(headers).body(svg.getBytes());
			}
			headers.setContentType(ZmallUtil.getMediaType(imagename));
			headers.add("Content-Disposition", "inline;filename="  + imagename);
			return ResponseEntity.ok().headers(headers).body(Files.readAllBytes(file.toPath()));
		}
		
		@PreAuthorize("isAuthenticated()")
		@PostMapping(path="/imageBoard/new", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> write(@Valid ImageBoardDto.Write dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			ImageBoard imageBoard = imageService.write(dto, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/imageBoard/read").queryParam("ibno", imageBoard.getIbno()).build().toUri();
			return ResponseEntity.created(uri).body(imageBoard);
		}
			
		//"/imageBoard/{ibno}"
		@GetMapping(path="/imageBoard/{ibno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> read(@PathVariable Integer ibno, Principal principal) {
			String username = (principal==null)? null : principal.getName();
			return ResponseEntity.ok(imageService.read(ibno, username));
		}
		
		@GetMapping(path="/imageBoard/all", produces=MediaType.APPLICATION_JSON_VALUE) 
		public ResponseEntity<?> list(@RequestParam(defaultValue="1") Integer pageno) {
			return ResponseEntity.ok(imageService.list(pageno)); 
		}
		 
		@PutMapping(path="/imageBoard/{ibno}", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> update(@Valid ImageBoardDto.Update dto, BindingResult bindingResult, Principal principal) throws BindException {
			if(bindingResult.hasErrors())
				throw new BindException(bindingResult);
			return ResponseEntity.ok(imageService.update(dto, principal.getName()));
		}
		
		@PostMapping("/imageBoard/ibcomments")
		public ResponseEntity<?> IBCommentCnt(@RequestParam Integer ibno) {
			Integer cnt = imageService.updateIBCommentCnt(ibno);
			return ResponseEntity.ok(cnt);
		}
		
		@GetMapping("/imageBoard/good_or_bad")
		public ResponseEntity<?> GoodOrBadCnt(@RequestParam Integer ibno, @RequestParam Integer state) {
			Integer cnt = imageService.goodOrBad(ibno, state);
			return ResponseEntity.ok(cnt);
		}
		
		@GetMapping("/imageBoard/report")
		public ResponseEntity<?> ReportCnt(@RequestParam Integer ibno, @RequestParam Integer state){
			Integer cnt = imageService.isreportCheck(ibno, state);
			return ResponseEntity.ok(cnt);
		}
		
		@DeleteMapping("/imageBoard/{ibno}")
		public ResponseEntity<?> delete(@PathVariable Integer ibno, Principal principal) {
			imageService.delete(ibno, principal.getName());
			URI uri = UriComponentsBuilder.newInstance().path("/").build().toUri();
			
			// 201일 때는 주소를 보내줘야 한다. ResponseEntity의 created메소드는 uri를 주면 Location이름으로 헤더에 추가해준다
			// 201이 아니면 백에서 수동으로 헤더에 Location을 추가해야 한다
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Location", uri.toString());
			
			// ResponseEntity에 header를 추가하려면 new 해야 한다
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
		}
		
		@GetMapping(path="/imageBoard/warnlist", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> warnlist(@RequestParam(defaultValue="1") Integer pageno, Integer warnCnt) {
			return ResponseEntity.ok(imageService.warnList(pageno, warnCnt));
		}
		
		@PostMapping("/imageBoard/inactive")
		public ResponseEntity<?> update_isActive(@RequestParam Integer ibno) {
			Boolean isActive = imageService.updateIsActive(ibno);
			return ResponseEntity.ok(isActive);
		}
		
		// 검색
		@PostMapping(path="/imageBoard/searchAll", produces=MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<?> search(@RequestParam(defaultValue = "1") Integer pageno, HttpSession session){
			Object wordObj = session.getAttribute("word");
			if(wordObj==null || wordObj.toString().isBlank())
				return ResponseEntity.badRequest().body("검색어가 없습니다");
			String word = wordObj.toString();
			URI uri = UriComponentsBuilder.newInstance().path("/imageBoard/search").queryParam("word", word).build().toUri();
			Map<String, Object> board = imageService.readSearchAll(pageno, word);
			return ResponseEntity.created(uri).body(board);
		}
	}


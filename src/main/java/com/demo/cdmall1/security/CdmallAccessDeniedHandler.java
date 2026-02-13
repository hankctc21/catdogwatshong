package com.demo.cdmall1.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.access.*;
import org.springframework.security.web.access.*;
import org.springframework.stereotype.*;

// 권한없음이 발생했을 때 /error/e403으로 유도할 AccessDenialHandler
@Component
public class CdmallAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String requestedWith = request.getHeader("X-Requested-With");
		String uri = request.getRequestURI();
		boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(requestedWith);
		boolean isApi = uri != null && uri.startsWith("/products/");
		if (isAjax || isApi) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"message\":\"권한이 없습니다\"}");
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute("msg", "잘못된 접근입니다");
		response.sendRedirect("/");
	}
}

package com.demo.cdmall1.domain.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.demo.cdmall1.domain.member.entity.Member;
import com.demo.cdmall1.domain.product.entity.Product;
import com.demo.cdmall1.domain.product.entity.Review;
import com.demo.cdmall1.domain.product.entity.ReviewDslRepository;
import com.demo.cdmall1.domain.product.entity.ReviewRepository;
import com.demo.cdmall1.domain.product.service.ReviewService;

@SpringBootTest
public class ReviewTest {
	@Autowired
	private ReviewDslRepository dslDao;
	@Autowired
	private ReviewRepository dao;
	@Autowired
	private ReviewService service;
	
	//@Test
	public void test1() {
		
		for(int i = 1; i < 23; i++) {
			int num = i%5;
			int num2 = i%3;
			if(num == 0) {
				num = 5;
			}
			
			if(num2==0) {
				num2 = 3;
			}
			
			Product product = Product.builder().pno(68).build();
			Member member = Member.builder().username("GUEST00"+num2).build(); 
					
			System.out.println("test1");
			Review review = new Review(product, null, member, num, "예시내용입니다"+i+"pno:"+69, "GUEST00"+num2, 3, 1,1,1, 2, 1, true, "gg.jpg",null, null);
			
			//review.addAttachment(new ReviewAttachment(null,  ));
			dao.save(review);
		}
	}
	
	//@Test
	public void test2() {
		int pageno = 1;
		Pageable pageable = PageRequest.of(pageno-1, 10);
		Product product = Product.builder().pno(71).build();
		dslDao.readAll(pageable, product);
	}
	
	//@Test
	public void test3() {
		Product product = Product.builder().pno(71).build();
		System.out.println("************test3: "+dslDao.countByPno(product));	
	}
	
	//@Test
	public void test4() {
		Product product = Product.builder().pno(71).build();
		dao.findByProduct(product);
		//System.out.println("************test3: "+dslDao.countByPno(product));	
	}
	
	//@Test
	public void test5() {
		Product product = Product.builder().pno(71).build();
		dslDao.readStarByPno(product);
	}
	
	//@Test
	public void test6() {
		System.out.println("*************sum of stars test 6: "+service.avgOfStars(71));
		
	}
	
	//@Test
	public void test7() {
		System.out.println("*************sum of stars test 7: "+service.avgOfStars(71));
		
	}
	
	@Test
	public void test8() {
		System.out.println("**************************findById: " + dao.findByRno(138));
		
		
	}
	
	
	
	
	
}

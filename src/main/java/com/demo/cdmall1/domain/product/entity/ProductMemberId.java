package com.demo.cdmall1.domain.product.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductMemberId implements Serializable {
	private String username;
	private Integer pno;
}

package com.demo.cdmall1.domain.product.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductAttachmentId implements Serializable {
	private Product product;
	private Integer ano;
} 

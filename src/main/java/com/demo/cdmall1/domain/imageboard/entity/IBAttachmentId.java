package com.demo.cdmall1.domain.imageboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class IBAttachmentId implements Serializable {
	private ImageBoard imageBoard;
	private Integer ano;
} 

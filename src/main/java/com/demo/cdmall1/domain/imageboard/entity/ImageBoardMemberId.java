package com.demo.cdmall1.domain.imageboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ImageBoardMemberId implements Serializable {
	private String username;
	private Integer ibno;
}

package com.demo.cdmall1.domain.board.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BoardMemberId implements Serializable {
	private String username;
	private Integer bno;
}

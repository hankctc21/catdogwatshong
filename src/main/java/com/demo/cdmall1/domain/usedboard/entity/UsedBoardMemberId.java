package com.demo.cdmall1.domain.usedboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsedBoardMemberId implements Serializable {
	private String username;
	private Integer ubno;
}

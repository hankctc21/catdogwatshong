package com.demo.cdmall1.domain.questionboard.entity;

import java.io.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class QSAttachmentId implements Serializable {
	private QuestionBoard questionBoard;
	private Integer ano;
}

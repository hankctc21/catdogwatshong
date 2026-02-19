package com.demo.cdmall1.domain.chatting.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain=true)
@Entity
@DynamicUpdate
public class ChattingHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chattinghistory_seq")
	@SequenceGenerator(name = "chattinghistory_seq", sequenceName = "chattinghistory_seq", allocationSize = 1)
	private Integer chathno;
	
	@Column(length=10)
	private String hostid;
	
	@Column(length=10)
	private String guestid;
	
	
	@JsonFormat(pattern="yyyy년MM월dd일 HH:mm:ss")
	@JsonProperty("created_time")
	private LocalDateTime createdTime;
	
	@Lob
	private String history;
}

package com.demo.cdmall1;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.*;

@Configuration
@EnableScheduling
public class ScheduledConfig {
	@Value("${app.scheduler.pool-size:2}")
	private int schedulerPoolSize;

	@Bean
	public TaskScheduler getTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(schedulerPoolSize);
		scheduler.setThreadNamePrefix("app-scheduler-");
		return scheduler;
	}
}

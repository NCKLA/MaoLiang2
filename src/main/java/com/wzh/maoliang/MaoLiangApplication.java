package com.wzh.maoliang;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class MaoLiangApplication {

	public static void main(String[] args) {
		/*
		 * SpringApplication springApplication = new
		 * SpringApplication(MaoLiangApplication.class);
		 * springApplication.addListeners(new ApplicationStartup());
		 */
		SpringApplication.run(MaoLiangApplication.class, args);
		
		
		 @EnableAsync
		 @Configuration
		 class TaskPoolConfig {
	        @Bean("ThreadExecutor")
	        public Executor taskExecutor() {
	            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	            executor.setCorePoolSize(10);
	            executor.setMaxPoolSize(20);
	            executor.setQueueCapacity(200);
	            executor.setKeepAliveSeconds(60);
	            executor.setThreadNamePrefix("ThreadExecutor-");
	            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
	            executor.setWaitForTasksToCompleteOnShutdown(true);
	            executor.setAwaitTerminationSeconds(60);
	            return executor;
	        }
	    }
	}
}

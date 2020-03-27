package com.shanshuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 * 
 * @author Yscredit
 *
 */
@EnableAsync
@SpringBootApplication
@MapperScan("com.shanshuan.mapper")
public class FkApplication {
	public static void main(String[] args) {
		SpringApplication.run(FkApplication.class, args);
	}
}
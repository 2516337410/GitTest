package com.cy.store;

import jakarta.servlet.MultipartConfigElement;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

@SpringBootApplication
@MapperScan("com.cy.store.mapper")
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Bean
	public MultipartConfigElement getMultipartConfigElement(){
		// 创建一个配置的工厂类对象
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//设置需要创建的对象的相关信息
		factory.setMaxFileSize(DataSize.of(10, DataUnit.MEGABYTES));
		factory.setMaxRequestSize(DataSize.of(15, DataUnit.MEGABYTES));
		// 通过工厂类来创建MultipartConfigElement对象
		return factory.createMultipartConfig();
	}
}

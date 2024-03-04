package com.cogamers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cogamers.common.FileManagerService;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	// 웹 이미지 path와 서버에 업로드 된 실제 이미지와 매핑 설정
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			
			registry
			.addResourceHandler("/images/**") // web image path >> http://localhost/images/aaaa_1705483526328/girl-8435329_640.png
			.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH);
			
			registry
	        .addResourceHandler("/img/**")
	        .addResourceLocations("classpath:/static/img/");
		}
}

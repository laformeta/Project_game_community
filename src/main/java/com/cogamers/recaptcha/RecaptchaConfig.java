package com.cogamers.recaptcha;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaConfig {

	private String site;
	private String secret;
	private String url;
	
}

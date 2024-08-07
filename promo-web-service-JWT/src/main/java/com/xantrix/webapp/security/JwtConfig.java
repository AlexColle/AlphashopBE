package com.xantrix.webapp.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties("sicurezza")
@Setter
@Getter
public class JwtConfig
{
	private String uri;
	private String refresh;
	private String header;
	private String prefix;
	private int expiration;
	private String secret;
}

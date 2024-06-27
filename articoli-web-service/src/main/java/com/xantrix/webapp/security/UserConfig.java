package com.xantrix.webapp.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gestuser")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserConfig {
    private String srvUrl;
    private String userId;
    private String password;
}

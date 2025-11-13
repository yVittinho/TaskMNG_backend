package com.taskmng.br.TaskMNG.security;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionCookieConfig {
    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        // Força o JSESSIONID a usar SameSite=None e permitir cookies cross-site
        return CookieSameSiteSupplier.ofNone();
    }
}

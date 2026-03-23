package com.um.springbootprojstructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

    @Bean
    public LdapContextSource contextSource(
            @Value("${app.ldap.url}") String url,
            @Value("${app.ldap.base-dn}") String baseDn,
            @Value("${app.ldap.user-dn}") String userDn,
            @Value("${app.ldap.password}") String password
    ) {
        LdapContextSource source = new LdapContextSource();
        source.setUrl(url);
        source.setBase(baseDn);
        source.setUserDn(userDn);
        source.setPassword(password);
        source.afterPropertiesSet();
        return source;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}

package com.girdharshukla.deliverymatch.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.uber.h3core.H3Core;

@Configuration
public class AppConfig {
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public H3Core h3initialization() throws IOException{
        return H3Core.newInstance();
    }
}

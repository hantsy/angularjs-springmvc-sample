package com.hantsylabs.restexample.springmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.hantsylabs.restexample.springmvc"})
public class DataJpaConfig {


}

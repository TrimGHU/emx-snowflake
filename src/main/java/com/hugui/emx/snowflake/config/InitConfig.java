package com.hugui.emx.snowflake.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hugui.emx.snowflake.interceptor.SecurityInterceptor;
import com.hugui.emx.snowflake.resolver.GlobalIdResolver;

/**
 * 
 * @author hugui
 *
 */

@Configuration
public class InitConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private GlobalIdResolver resolver;
	
	@Autowired
	private SecurityInterceptor interceptor;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(resolver);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
	}
}

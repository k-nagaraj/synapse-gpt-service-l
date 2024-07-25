package com.smartdocs.gpt.config;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import com.smartdocs.gpt.mongo.config.MultiTenantServletFilter;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Configuration
public class WebConfigurer implements ServletContextInitializer {

	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD,
				DispatcherType.ASYNC);
		initMetrics(servletContext, disps);
		log.info("Web application fully configured");
	}

	/**
	 * Initializes Metrics.
	 */
	private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
		log.debug("Initializing Metrics registries");

		FilterRegistration.Dynamic tenantFilter = servletContext.addFilter("tenantFilter",
				new MultiTenantServletFilter());

		tenantFilter.addMappingForUrlPatterns(disps, true, "/*");
	}

}

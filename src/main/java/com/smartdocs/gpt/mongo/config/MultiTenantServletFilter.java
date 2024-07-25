package com.smartdocs.gpt.mongo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import com.smartdocs.gpt.helper.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class MultiTenantServletFilter extends GenericFilterBean {

	@Value("${tenant.default}")
	String defaultTenant;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		TenantContext.setTenantId(extractSubdomain(request));

		chain.doFilter(request, response);
	}

	String extractSubdomain(ServletRequest req) {
		HttpServletRequest httpRequest = (HttpServletRequest) req;

		if (httpRequest.getHeader(TenantContext.TENANT_HEADER) != null) {

			return httpRequest.getHeader(TenantContext.TENANT_HEADER);
		}

		return defaultTenant;
	}

}

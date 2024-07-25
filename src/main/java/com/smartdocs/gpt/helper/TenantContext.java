package com.smartdocs.gpt.helper;



import org.springframework.stereotype.Component;


@Component
public class TenantContext {

    public static final String TENANT_HEADER = "X-Tenant";
    

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
       
        CONTEXT.set(tenantId);
    }

    public  static String getTenantId() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}


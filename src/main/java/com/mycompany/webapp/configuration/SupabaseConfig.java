package com.mycompany.webapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.url}")
    public String SUPABASE_URL;

    @Value("${supabase.service_role_key}")
    public String SUPABASE_SERVICE_ROLE_KEY;

    @Value("${supabase.bucket}")
    public String BUCKET_NAME;


}

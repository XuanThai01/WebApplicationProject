package com.mycompany.webapp.service;


import com.mycompany.webapp.configuration.SupabaseConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class SupabaseStorageService {

    private final SupabaseConfig config;

    public SupabaseStorageService(SupabaseConfig config) {
        this.config = config;
    }
    public String uploadFile(byte[] fileBytes, String filePath) {

        String uploadUrl = config.SUPABASE_URL + "/storage/v1/object/" +
                config.BUCKET_NAME + "/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", "Bearer " + config.SUPABASE_SERVICE_ROLE_KEY);
        headers.set("x-upsert", "true");

        HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

        return config.SUPABASE_URL +
                "/storage/v1/object/public/" +
                config.BUCKET_NAME + "/" + filePath;
    }
    public byte[] downloadPublicFile(String filePath) {
        try {
            String fileUrl = config.SUPABASE_URL +
                    "/storage/v1/object/public/" +  config.BUCKET_NAME + "/" + filePath;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(fileUrl, byte[].class);

            if (response.getStatusCode() != HttpStatus.OK)
                throw new RuntimeException("Failed to download file: " + filePath);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Download error: " + e.getMessage());
        }
    }
}
package Project.RedditAPI.Services.Auth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Auth {
    public static String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //Different login details as I had to re-create the app
        headers.setBasicAuth("5ZP5WPOVWBz9lPO9koPrtg", "_8P1yo1yll-KswVA5XYVkbGoZKChcw");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.put("User-Agent",
                Collections.singletonList("tomcat:com.e4developer.e4reddit-test:v1.0 (by /u/pranav_raj63)"));
        String body = "grant_type=password&username=pranav_raj63&password=pranav@1234";
        HttpEntity<String> request
                = new HttpEntity<>(body, headers);
        String authUrl = "https://www.reddit.com/api/v1/access_token";
        ResponseEntity<String> response = restTemplate.postForEntity(
                authUrl, request, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map.putAll(mapper
                    .readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
                    }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody());
        return String.valueOf(map.get("access_token"));
    }
}





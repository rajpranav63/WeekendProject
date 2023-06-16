package Project.RedditAPI.Services;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static Project.RedditAPI.Services.Auth2.Auth.getAuthToken;

public class RedditService {
    public  String readArticles(String subReddit) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String authToken = getAuthToken();
        headers.setBearerAuth(authToken);
        headers.put("User-Agent",
                Collections.singletonList("tomcat:com.e4developer.e4reddit-test:v1.0 (by /u/pranav_raj63)"));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
//        String url = "https://oauth.reddit.com/r/" + subReddit + "/hot";
       String url = "https://oauth.reddit.com/best";
        ResponseEntity<String> response
                = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
public static String postArticle(String subReddit, String title, String content) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    String authToken = getAuthToken(); //
    headers.setBearerAuth(authToken);
    headers.put("User-Agent",
            Collections.singletonList("tomcat:com.e4developer.e4reddit-test:v1.0 (by /u/pranav_raj63)"));
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Build the request body

    String REDDIT_API_URL = "https://oauth.reddit.com/api/submit";
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("kind", "self");
    requestBody.add("sr", subReddit);
    requestBody.add("title", title);
//    requestBody.add("url", url);
     requestBody.add("text", content);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
//    return response.getBody();

//    headers.set("User-Agent", "mac:redditbot:v0.1 (by /u/spandauistkacke)");
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    ResponseEntity<String> response = restTemplate.exchange(REDDIT_API_URL, HttpMethod.POST, entity, String.class);
    return response.getBody();
}


}

package com.jiju.services.urlshortner;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String generateRandomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

       return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Test
    public void shortAndLongUrlGeneration() throws Exception {
        String url="http://localhost:"+port;
        String urlForGettingShortUrl = url+"/url/short";
        String urlForGettingLongUrl = url+"/url/long";

        // acceptable media type
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);

        String longUrl="http://www."+generateRandomString()+".com";

        // body
        String body = "{\"url\":\""+longUrl+"\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        Map<String, Object> uriVariables = new HashMap<>();

        ResponseEntity<String> result = restTemplate.exchange(
                urlForGettingShortUrl,
                HttpMethod.GET, entity, String.class, uriVariables);
       assertThat(result!=null);
       assertThat(result.getStatusCode().is2xxSuccessful());
       assertThat(!result.getBody().isEmpty());

        body = "{\"url\":\""+result.getBody()+"\"}";
        entity = new HttpEntity<>(body, headers);
        result = restTemplate.exchange(
                urlForGettingLongUrl,
                HttpMethod.GET, entity, String.class, uriVariables);
        assertThat(result!=null);
        assertThat(result.getStatusCode().is2xxSuccessful());
        assertThat(!result.getBody().isEmpty());
        assertThat(result.getBody().equals(longUrl));
    }
}
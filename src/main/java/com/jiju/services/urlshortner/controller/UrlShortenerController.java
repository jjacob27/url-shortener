package com.jiju.services.urlshortner.controller;

import com.jiju.services.urlshortner.beans.Url;
import com.jiju.services.urlshortner.interfaces.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/url")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService shortnerService;

    @GetMapping(path="/short", consumes= MediaType.APPLICATION_JSON_VALUE
            ,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getShortUrl(HttpServletRequest request, @RequestBody Url longUrl) throws MalformedURLException {
        String protocol = new URL(request.getRequestURL().toString()).getProtocol();
        if(longUrl.getUrl()==null || longUrl.getUrl().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(protocol+"://"+shortnerService.getShortUrl(longUrl.getUrl())
                ,HttpStatus.OK);
    }

    @GetMapping(path="/long", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLongUrl(@RequestBody Url shortUrl){
        if(shortUrl.getUrl()==null || shortUrl.getUrl().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(shortnerService.getLongUrl(shortUrl.getUrl()),HttpStatus.OK);
    }
}

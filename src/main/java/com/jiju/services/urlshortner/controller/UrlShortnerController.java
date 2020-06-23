package com.jiju.services.urlshortner.controller;

import com.jiju.services.urlshortner.beans.Url;
import com.jiju.services.urlshortner.interfaces.UrlShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/url")
public class UrlShortnerController {

    @Autowired
    UrlShortnerService shortnerService;

    @GetMapping(path="/short", consumes= MediaType.APPLICATION_JSON_VALUE
            ,produces=MediaType.APPLICATION_JSON_VALUE)
    public String getShortUrl(HttpServletRequest request, @RequestBody Url longUrl) throws MalformedURLException {
        String protocol = new URL(request.getRequestURL().toString()).getProtocol();
        return protocol+"://"+shortnerService.getShortUrl(longUrl.getUrl());
    }

    @GetMapping(path="/long", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLongUrl(@RequestBody Url shortUrl){
        return shortnerService.getLongUrl(shortUrl.getUrl());
    }
}

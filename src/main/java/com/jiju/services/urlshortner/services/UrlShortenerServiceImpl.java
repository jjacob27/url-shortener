package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.interfaces.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@Getter
@Setter
public class UrlShortenerServiceImpl implements UrlShortenerService {
    private static final String FORWARD_SLASH = "/";

    @Value( "${short.domain.url}" )
    private String shortDomainUrl;

    @Autowired
    private Base62Service base62Service;

    @Autowired
    private CounterService counterService;

    @Autowired
    private UrlPersistenceService persistenceService;

    @Autowired
    private CacheService cacheService;

    @Override
    public String getShortUrl(String longUrl) {
        String shortUrl = persistenceService.getShortUrl(longUrl);
        if(shortUrl!=null && !shortUrl.isEmpty())
            return shortUrl;

        shortUrl = base62Service.getBase62String(counterService.getNextCount());
        persistenceService.storeUrls(shortUrl,longUrl);
        cacheService.storeInCache(shortUrl,longUrl);
        return shortDomainUrl + FORWARD_SLASH +shortUrl;
    }

    @Override
    public String getLongUrl(String shortUrl) {
        try {
            shortUrl = new URL(shortUrl).getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Url is improper");
        }
        if(shortUrl.startsWith(FORWARD_SLASH))
            shortUrl = shortUrl.substring(FORWARD_SLASH.length());

        String ret = cacheService.getLongUrlFromCache(shortUrl);
        if(ret==null){
            String longUrl= persistenceService.getLongUrl(shortUrl);
            cacheService.storeInCache(shortUrl,longUrl);
            ret = longUrl;
        }
        return ret;
    }
}

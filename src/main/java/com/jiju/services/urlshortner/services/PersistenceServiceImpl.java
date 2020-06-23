package com.jiju.services.urlshortner.services;

import com.jiju.services.urlshortner.beans.dao.UrlJpaEntity;
import com.jiju.services.urlshortner.interfaces.UrlPersistenceService;
import com.jiju.services.urlshortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistenceServiceImpl implements UrlPersistenceService {
    @Autowired
    UrlRepository repository;

    @Override
    public void storeUrls(String shortUrl, String longUrl) {
        repository.save(new UrlJpaEntity(shortUrl,longUrl));
    }

    @Override
    public String getLongUrl(String shortUrl) {
        UrlJpaEntity entity = repository.findByShortUrl(shortUrl);
        if(entity !=null)
            return entity.getLongUrl();
        else
            return "";
    }

    @Override
    public String getShortUrl(String longUrl) {
        UrlJpaEntity entity = repository.findByLongUrl(longUrl);
        if(entity !=null)
            return entity.getShortUrl();
        else
            return null;
    }
}

package com.jiju.services.urlshortner.repository;

import com.jiju.services.urlshortner.beans.dao.UrlJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CrudRepository<UrlJpaEntity,Long> {
    UrlJpaEntity save(UrlJpaEntity entity);
    UrlJpaEntity findByShortUrl(String shortUrl);
    UrlJpaEntity findByLongUrl(String longUrl);
}

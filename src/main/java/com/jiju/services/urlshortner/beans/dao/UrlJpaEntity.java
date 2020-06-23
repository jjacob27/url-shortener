package com.jiju.services.urlshortner.beans.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="URL_MAPPING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlJpaEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="SHORT_URL", nullable = false)
    String shortUrl;

    @Column(name="LONG_URL", nullable = false)
    String longUrl;

    public UrlJpaEntity(String shortUrl, String longUrl){
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }
}

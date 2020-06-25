package com.jiju.services.urlshortner;

import com.jiju.services.urlshortner.controller.UrlShortenerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UrlShortenerApplicationTests {

	@Autowired
	UrlShortenerController shortenerController;

	@Test
	void contextLoads() {
		assertThat(shortenerController).isNotNull();
	}

}

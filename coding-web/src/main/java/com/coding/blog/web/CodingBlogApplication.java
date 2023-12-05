package com.coding.blog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@Slf4j
@SpringBootApplication
public class CodingBlogApplication {
    public static void main(String[] args) {
        log.info("start coding-blog");
        log.warn("start coding-blog warn");
        log.error("start coding-blog error");
        log.debug("start coding-blog debug");
        SpringApplication.run(CodingBlogApplication.class, args);
    }
}

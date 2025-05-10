package com.example.persistence.examples.service;

import com.example.persistence.examples.model.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@DirtiesContext
@Sql("classpath:test-data.sql")
public class AuthorsServiceTest {

    @Autowired
    private AuthorsService authorsService;

    @Test
    public void findAllTest() {
        List<Author> authors = authorsService.getAuthors();
        authors.forEach(System.out::println);
    }
}

package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AuthorsRepository extends JpaRepository<AuthorEntity, Long> {

    @Transactional
    void deleteAuthorByName(String name);
}

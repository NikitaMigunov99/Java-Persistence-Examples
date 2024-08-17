package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.AuthorEntity;
import com.example.persistence.examples.model.db.AuthorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<AuthorEntity, Long> {

    @Transactional
    void deleteAuthorByName(String name);

    List<AuthorView> findByName(String name);
}

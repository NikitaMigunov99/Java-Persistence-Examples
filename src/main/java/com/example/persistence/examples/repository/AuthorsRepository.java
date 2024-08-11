package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsRepository extends JpaRepository<Author, Long> {

}

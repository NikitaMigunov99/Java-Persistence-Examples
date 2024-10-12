package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.AuthorEntity;
import com.example.persistence.examples.model.db.AuthorView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<AuthorEntity, Long> {

//    In this case you do not need @NamedEntityGraphs in AuthorEntity
//    @Override
//    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "jokes")
//    List<AuthorEntity> findAll();

    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "jokes-only")
    List<AuthorEntity> findAll();

    @Transactional
    void deleteAuthorByName(String name);

    List<AuthorView> findByName(String name);
}

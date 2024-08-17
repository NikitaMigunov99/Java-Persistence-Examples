package com.example.persistence.examples.mapper;

import com.example.persistence.examples.model.db.AuthorEntity;
import com.example.persistence.examples.model.domain.Author;
import com.example.persistence.examples.model.domain.JokeModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorEntityToDomainMapper {

    private final JokesEntityToDomainMapper jokesMapper;
    private final AddressEntityToDomainMapper addressMapper;

    public AuthorEntityToDomainMapper(
            JokesEntityToDomainMapper jokesMapper,
            AddressEntityToDomainMapper addressMapper) {
        this.jokesMapper = jokesMapper;
        this.addressMapper = addressMapper;
    }

    public List<Author> convert(List<AuthorEntity> authorEntities) {
        return authorEntities.stream().map(this::toModel).collect(Collectors.toList());
    }

    private Author toModel(AuthorEntity entity) {
        List<JokeModel> jokes = new ArrayList<>();
        if (entity.getJokes() != null) {
            List<JokeModel> jokesList = jokesMapper.convert(entity.getJokes());
            jokes.addAll(jokesList);
        }
        return new Author(
                entity.getId().toString(),
                entity.getName(),
                jokes,
                addressMapper.toDomain(entity.getAddress())
        );
    }
}

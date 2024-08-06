package com.example.persistence.examples.mapper;

import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.domain.JokeModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for {@link JokeEntity} and {@link JokeModel}
 */
@Component
public class JokesEntityToDomainMapper {

    public List<JokeModel> convert(List<JokeEntity> dbList) {
        return dbList.stream().map(this::toModel).collect(Collectors.toList());
    }

    private JokeModel toModel(JokeEntity entity) {
        return new JokeModel(
                entity.getId().toString(),
                entity.getType(),
                entity.getSetup(),
                entity.getPunchline()
        );
    }
}

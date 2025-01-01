package com.example.persistence.examples.service;

import com.example.persistence.examples.api.JokesClient;
import com.example.persistence.examples.mapper.AuthorEntityToDomainMapper;
import com.example.persistence.examples.model.db.AuthorEntity;
import com.example.persistence.examples.model.db.AuthorView;
import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.domain.Author;
import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.repository.AuthorsRepository;
import com.example.persistence.examples.repository.JokesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorsService {

    private final AuthorsRepository repository;
    private final AuthorEntityToDomainMapper mapper;
    private final JokesClient client;
    private final JokesRepository jokesRepository;

    @Autowired
    public AuthorsService(AuthorsRepository repository,
                          AuthorEntityToDomainMapper mapper,
                          JokesClient client,
                          JokesRepository jokesRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.client = client;
        this.jokesRepository = jokesRepository;
    }

    public List<Author> getAuthors() {
        return mapper.convert(repository.findAll());
    }

    public List<AuthorView> findByName(String name) {
        return repository.findByName(name);
    }

    public void deleteAuthor(String name) {
        repository.deleteAuthorByName(name);
    }

    public void createJokeAuthor(String name, Long id) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(name);
        JokeModel jokeModel = client.getJokeById(id.toString());
        JokeEntity jokeEntity = new JokeEntity(jokeModel.type(), jokeModel.setup(), jokeModel.punchline());
        List<JokeEntity> list = List.of(jokeEntity);
        list.forEach((entity -> entity.setAuthor(authorEntity)));
        authorEntity.getJokes().addAll(list);
        repository.save(authorEntity);
    }

    @Transactional
    public void addJokeToAuthor(Long authorId, Long id) {
        Optional<AuthorEntity> authorEntity = repository.findById(authorId);
        Optional<JokeEntity> jokeEntityOptional = jokesRepository.findById(id);
        if (authorEntity.isPresent() && jokeEntityOptional.isPresent()) {
            AuthorEntity entity = authorEntity.get();
            JokeEntity jokeEntity = jokeEntityOptional.get();
            jokeEntity.setAuthor(entity);
            //JokeModel jokeModel = client.getJokeById(id.toString());
            entity.getJokes().add(jokeEntity);
            repository.save(entity);
        } else {
            throw new NullPointerException("Could not find AuthorEntity with id=" + authorId);
        }
    }

    private JokeEntity getJokeEntity(JokeModel jokeModel, AuthorEntity entity) {
        JokeEntity jokeEntity = new JokeEntity(jokeModel.type(), jokeModel.setup(), jokeModel.punchline());
        jokeEntity.setAuthor(entity);
        return jokeEntity;
    }
}

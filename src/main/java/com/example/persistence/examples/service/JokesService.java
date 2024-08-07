package com.example.persistence.examples.service;

import com.example.persistence.examples.api.JokesClient;
import com.example.persistence.examples.mapper.JokesEntityToDomainMapper;
import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.db.JokeView;
import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import com.example.persistence.examples.repository.JokesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public final class JokesService {

    private final JokesClient client;
    private final JokesRepository repository;
    private final JokesEntityToDomainMapper mapper;

    @Autowired
    public JokesService(JokesClient client,
                        JokesRepository repository,
                        JokesEntityToDomainMapper mapper) {
        this.client = client;
        this.repository = repository;
        this.mapper = mapper;
    }

    public JokeModel getJoke() {
        return client.getJoke();
    }

    public JokeModel getJokeById(String id) {
        return client.getJokeById(id);
    }

    public List<JokeModel> getJokesList() {
        return client.getJokesList();
    }

    public List<JokeModel> getJokesDB() {
        List<JokeEntity> entities = repository.findAll();
        return mapper.convert(entities);
    }

    public void saveJoke(@RequestBody JokeSaveRequest request) {
        repository.save(new JokeEntity(request.type(), request.setup(), request.punchline()));
    }

    public void saveJokeById(@PathVariable String id) {
        JokeModel model = client.getJokeById(id);
        repository.save(new JokeEntity(model.type(), model.setup(), model.punchline()));
    }

    public int getJokesCount() {
        return repository.getJokesCount();
    }

    public List<JokeModel> findByPunchlineStartsWith(String word) {
        List<JokeEntity> entities = repository.findByPunchlineStartsWithIgnoreCase(word);
        return mapper.convert(entities);
    }

    public List<JokeView> getJokeByType(String type) {
        return repository.getJokeByType(type);
    }
}

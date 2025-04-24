package com.example.persistence.examples.service;

import com.example.persistence.examples.api.JokesClient;
import com.example.persistence.examples.mapper.JokesEntityToDomainMapper;
import com.example.persistence.examples.model.db.JokeEntity;
import com.example.persistence.examples.model.db.JokeView;
import com.example.persistence.examples.model.domain.JokeModel;
import com.example.persistence.examples.model.dto.JokeSaveRequest;
import com.example.persistence.examples.repository.JokesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JokesService {

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

    @Cacheable(cacheNames = "jokesList")
    public List<JokeModel> getJokesDB() {
        List<JokeEntity> entities = repository.findAll();
        return mapper.convert(entities);
    }

    @Cacheable(cacheNames = "jokes", key = "#id")
    public JokeModel getJokeByIdDB(Long id) {
        JokeEntity entity = repository.findById(id).orElseThrow();
        return mapper.toModel(entity);
    }

    public JokeModel getJokeByIdDBWithoutCaching(Long id) {
        JokeEntity entity = repository.findById(id).orElseThrow();
        return mapper.toModel(entity);
    }

    @CacheEvict(value = "jokesList", allEntries = true)
    public void saveJoke(JokeSaveRequest request) {
        repository.save(new JokeEntity(request.type(), request.setup(), request.punchline()));
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "jokesList", allEntries = true),
                    @CacheEvict(cacheNames = "jokes", key = "#id")
            }
    )
    public JokeModel updateJoke(Long id, JokeSaveRequest request) {
        JokeEntity entity = repository.findById(id).orElseThrow();
        entity.setType(request.type());
        entity.setSetup(request.setup());
        entity.setPunchline(request.punchline());
        repository.save(entity);
        return mapper.toModel(entity);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "jokesList", allEntries = true),
                    @CacheEvict(cacheNames = "jokes", allEntries = true)
            }
    )
    public void updateJokesType(String type) {
        repository.updateJokesType(type);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "jokesList", allEntries = true),
                    @CacheEvict(cacheNames = "jokes", key = "#id")
            }
    )
    public void removeJokeById(Long id) {
        repository.deleteById(id);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "jokesList", allEntries = true),
                    @CacheEvict(cacheNames = "jokes", allEntries = true)
            }
    )
    public void removeAll() {
        repository.truncateTable();
    }

    @CacheEvict(value = "jokesList", allEntries = true)
    public void saveJokeById(String id) {
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
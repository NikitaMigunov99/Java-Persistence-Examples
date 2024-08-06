package com.example.persistence.examples.domain.service;

import com.example.persistence.examples.api.JokesClient;
import com.example.persistence.examples.domain.model.JokeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class JokesService {

    private final JokesClient client;

    @Autowired
    public JokesService(JokesClient client) {
        this.client = client;
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
}

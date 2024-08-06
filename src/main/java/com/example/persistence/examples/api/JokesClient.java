package com.example.persistence.examples.api;

import com.example.persistence.examples.model.domain.JokeModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "jokes", url = "https://official-joke-api.appspot.com")
public interface JokesClient {

    @GetMapping(value = "/random_joke")
    JokeModel getJoke();

    @GetMapping(value = "/jokes/{id}")
    JokeModel getJokeById(@PathVariable("id") String id);

    @GetMapping(value = "/random_ten")
    List<JokeModel> getJokesList();
}

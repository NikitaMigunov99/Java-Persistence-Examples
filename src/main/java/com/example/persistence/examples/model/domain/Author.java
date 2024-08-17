package com.example.persistence.examples.model.domain;

import java.util.List;

public record Author(
        String id,
        String name,
        List<JokeModel> jokes,
        Address address
) {
}

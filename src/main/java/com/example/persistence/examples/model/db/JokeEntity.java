package com.example.persistence.examples.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DB Entity of joke
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JokeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String setup;

    @Column(nullable = false)
    private String punchline;

    public JokeEntity(String type, String setup, String punchline) {
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
    }
}


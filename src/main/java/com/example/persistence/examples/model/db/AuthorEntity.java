package com.example.persistence.examples.model.db;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "jokes-only",
                attributeNodes = {
                        @NamedAttributeNode("jokes")
                }),
        @NamedEntityGraph(name = "all-attributes",
                attributeNodes = {
                       @NamedAttributeNode("address"),
                        @NamedAttributeNode("jokes")
                })
})
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
    @SequenceGenerator(name = "author_seq", sequenceName = "author_seq", allocationSize = 1)
    private Long id;

    private String name;

    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL)
    private AddressEntity address;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    //@Fetch(FetchMode.SUBSELECT) //Can solve N+1 problem using IN operator
    private List<JokeEntity> jokes = new ArrayList<>();

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' + '}';
    }
}

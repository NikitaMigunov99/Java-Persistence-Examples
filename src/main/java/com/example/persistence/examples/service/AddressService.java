package com.example.persistence.examples.service;

import com.example.persistence.examples.mapper.AddressEntityToDomainMapper;
import com.example.persistence.examples.model.db.AddressEntity;
import com.example.persistence.examples.model.db.AuthorEntity;
import com.example.persistence.examples.model.domain.Address;
import com.example.persistence.examples.repository.AddressRepository;
import com.example.persistence.examples.repository.AuthorsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repository;
    private final AuthorsRepository authorRepository;
    private final AddressEntityToDomainMapper mapper;

    public AddressService(AddressRepository repository,
                          AuthorsRepository authorRepository,
                          AddressEntityToDomainMapper mapper) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    public void saveAddress(Long authorId, String city) {
        AuthorEntity authorEntity = authorRepository.getReferenceById(authorId);
        AddressEntity address = new AddressEntity();
        address.setCity(city);
        address.setAuthor(authorEntity);
        authorEntity.setAddress(address);
        repository.save(address);
    }

    @Transactional(readOnly = true)
    public List<Address> getAddresses() {
        return mapper.convert(repository.findAll());
    }
}

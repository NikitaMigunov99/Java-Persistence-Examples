package com.example.persistence.examples.mapper;

import com.example.persistence.examples.model.db.AddressEntity;
import com.example.persistence.examples.model.domain.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressEntityToDomainMapper {

    public List<Address> convert(List<AddressEntity> entities) {
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Address toDomain(AddressEntity entity) {
        return entity != null ? new Address(entity.getCity()) : null;
    }
}

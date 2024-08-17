package com.example.persistence.examples.repository;

import com.example.persistence.examples.model.db.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}

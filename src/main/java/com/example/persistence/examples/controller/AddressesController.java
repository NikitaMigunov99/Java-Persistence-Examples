package com.example.persistence.examples.controller;

import com.example.persistence.examples.model.domain.Address;
import com.example.persistence.examples.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressesController {

    private final AddressService service;

    @Autowired
    public AddressesController(AddressService service) {
        this.service = service;
    }

    @GetMapping("address")
    public ResponseEntity<List<Address>> getAll() {
        return ResponseEntity.ok(service.getAddresses());
    }

    @PostMapping("address")
    public ResponseEntity<String> save(Long authorId, String city) {
        service.saveAddress(authorId, city);
        return ResponseEntity.ok("Address is saved ");
    }
}

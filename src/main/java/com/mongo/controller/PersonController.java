package com.mongo.controller;

import com.mongo.collections.Person;
import com.mongo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/persons")
public class PersonController{

    @Autowired
    PersonService service;

    @PostMapping
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        log.info("Saving person {}", person);
        return ResponseEntity.ok(service.savePerson(person));
    }


    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Person> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id).orElse(null));
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<Person> findByName(@PathVariable String name) {
        return ResponseEntity.ok(service.findByName(name));
    }


    @GetMapping("/age/{age}")
    public ResponseEntity<List<Person>> findWithInAge(@PathVariable Integer age) {
        log.info("findWithInAge {}", age);
        return ResponseEntity.ok(service.findWithInAge(age));
    }


    @GetMapping("/search")
    public ResponseEntity<Page<Person>> search(@RequestParam(required = false) String name, @RequestParam(required = false, defaultValue =  "0") int age,@RequestParam(required = false) String city,
                                               @RequestParam(required = false, defaultValue = "0") int size, @RequestParam(required = false, defaultValue =  "5") int index) {
        log.info("findWithInAge {}", age);
        return ResponseEntity.ok(service.search(name, age, city, PageRequest.of(size, index)));
    }



    @GetMapping("/getOldestPersonByCity")
    public ResponseEntity<List<Person>> getOldestPersonByCity() {
        return ResponseEntity.ok(service.getOldestPersonByCity());
    }

    @GetMapping("/findByCities")
    public ResponseEntity<List<Person>> findByCities(@RequestParam List<String> cities) {
        return ResponseEntity.ok(service.findByCities(cities));
    }

}

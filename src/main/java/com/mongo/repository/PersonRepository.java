package com.mongo.repository;

import com.mongo.collections.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

    Person findByName(String name);


    @Query(value = "{age : {$gt :?0, $lte : ?1 }}", fields = "{address : 0}")
    List<Person> findWithInAge(int i, int age);
}

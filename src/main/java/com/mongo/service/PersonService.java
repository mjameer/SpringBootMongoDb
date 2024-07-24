package com.mongo.service;

import com.mongo.collections.Person;
import com.mongo.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public String savePerson(Person person) {
        return personRepository.save(person).getPersonID();
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByName(String name) {
        return personRepository.findByName(name);
    }


    public List<Person> findWithInAge(int age) {
        log.info("findWithInAge {}", age);
        return personRepository.findWithInAge(0, age);
    }

    @Autowired
    MongoTemplate mongoTemplate;

    public Page<Person> search(String name, int age, String city, PageRequest pageRequest) {

        Query query = new Query();
        if (age>0)
            query.addCriteria(Criteria.where("age").lte(age));

        if(city!=null)
            query.addCriteria(Criteria.where("city").is(city));

        if(name!=null)
            query.addCriteria(Criteria.where("name").is(name));

        query.with(pageRequest); // Apply pagination

        List<Person> personList = mongoTemplate.find(query, Person.class);
        long count = mongoTemplate.count(query, Person.class);

        log.info("Count of people List: " + personList.size());
        log.info("Count of people: " + count);

        return PageableExecutionUtils.getPage(personList, pageRequest, () -> count);
    }



    public List<Person> getOldestPersonByCity() {
        log.info("Getting the most oldest person");
        UnwindOperation unwind = Aggregation.unwind("address");

        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "age");

        GroupOperation groupByCity = Aggregation.group( "_id", "address.city");

        Aggregation aggregation = Aggregation.newAggregation(unwind, sort, groupByCity);

        return mongoTemplate.aggregate(aggregation, "Person", Person.class).getMappedResults();
    }
    public List<Person> findByCities(List<String> cities) {
        Query query = new Query(Criteria.where("address").elemMatch(Criteria.where("city").in(cities)));
        // Learning
        // query.fields().exclude("address");
        return mongoTemplate.find(query, Person.class);
    }

    public Optional<Person> findById(String id) {
        return personRepository.findById(id);
    }
}

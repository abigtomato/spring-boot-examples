package com.abigtomato.example.repository;

import com.abigtomato.example.pojo.UserPojo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends ElasticsearchRepository<UserPojo, Long> {

    List<UserPojo> findByAgeBetween(Integer age, Integer age2);

    List<UserPojo> findByNameLike(String name);
}

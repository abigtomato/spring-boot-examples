package com.abigtomato.example.controller;

import com.abigtomato.example.pojo.UserPojo;
import com.abigtomato.example.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/es")
@Slf4j
public class ElasticSearchController {

    private UserRepository userRepository;

    private ElasticsearchRestTemplate restTemplate;

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticSearchController(UserRepository userRepository,
                                   ElasticsearchRestTemplate restTemplate,
                                   RestHighLevelClient restHighLevelClient) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.restHighLevelClient = restHighLevelClient;
    }

    @PostMapping(value = "/create")
    public void create() {
        this.restTemplate.createIndex(UserPojo.class);
        this.restTemplate.putMapping(UserPojo.class);
    }

    @PostMapping(value = "/save")
    public void save() {
        this.userRepository.save(new UserPojo(1L, "albert", 20, "123456"));
    }

    @PostMapping(value = "/save/all")
    public void saveAll() {
        this.userRepository.saveAll(Arrays.asList(
                new UserPojo(2L, "java", 21, "123456"),
                new UserPojo(3L, "python", 22, "123456"),
                new UserPojo(4L, "go", 23, "123456"),
                new UserPojo(5L, "scala", 24, "123456"))
        );
    }

    @GetMapping(value = "/findAll")
    public void findAll() {
        this.userRepository.findAll().forEach(System.out::println);
    }

    @GetMapping(value = "/find/by")
    public void findBy() {
        this.userRepository.findByAgeBetween(20, 24).forEach(System.out::println);
        this.userRepository.findByNameLike("java").forEach(System.out::println);
    }

    @GetMapping(value = "/native/query")
    public void nativeQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "java"))
                .withPageable(PageRequest.of(0, 2))
                .withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC))
                .withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        Page<UserPojo> userPojoPage = this.userRepository.search(queryBuilder.build());

        System.out.println("userPojoPage.getTotalElements() = " + userPojoPage.getTotalElements());
        System.out.println("userPojoPage.getTotalPages() = " + userPojoPage.getTotalPages());

        userPojoPage.getContent().forEach(System.out::println);
    }
}

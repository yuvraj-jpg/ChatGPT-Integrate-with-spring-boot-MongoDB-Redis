package com.Task.ChatGPT;

import org.springframework.data.mongodb.repository.MongoRepository;
@org.springframework.stereotype.Repository
public interface mongoRepository extends MongoRepository<Store,String> {
}

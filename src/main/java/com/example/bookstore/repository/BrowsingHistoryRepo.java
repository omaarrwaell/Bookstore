package com.example.bookstore.repository;

import com.example.bookstore.domain.BrowsingHistory;
import com.example.bookstore.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrowsingHistoryRepo extends MongoRepository<BrowsingHistory,String> {
    BrowsingHistory getBrowsingHistoryByUserId(User userId);
}

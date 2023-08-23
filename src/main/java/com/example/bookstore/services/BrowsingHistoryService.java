package com.example.bookstore.services;

import com.example.bookstore.domain.BrowsingHistory;
import com.example.bookstore.domain.User;
import com.example.bookstore.repository.BrowsingHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrowsingHistoryService {
    private final BrowsingHistoryRepo browsingHistoryRepo;

    public BrowsingHistory save(BrowsingHistory browsingHistory){
        return browsingHistoryRepo.save(browsingHistory);
    }
    public BrowsingHistory getBrowsingHistoryByUserId(User userId){
        return browsingHistoryRepo.getBrowsingHistoryByUserId(userId);
    }

}

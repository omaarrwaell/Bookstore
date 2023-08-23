package com.example.bookstore.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;

@Document


@Getter
@Setter
@ToString
public class BrowsingHistory {

    @Id
    private String id;

    @DBRef
    private User userId;

    @Field
    private HashMap<String,Integer> categoriesFrequency = new HashMap<String, Integer>();


    public BrowsingHistory(){
        categoriesFrequency.put("drama", 0);
        categoriesFrequency.put("action", 0);
        categoriesFrequency.put("comedy", 0);
        categoriesFrequency.put("horror", 0);
        categoriesFrequency.put("thriller", 0);
        categoriesFrequency.put("romance", 0);
    }
    public String getMostFrequentCategory() {
        HashMap<String, Integer> categoriesFrequency = this.getCategoriesFrequency();

        String mostFrequentCategory = null;
        int maxFrequency = 0;

        for (String category : categoriesFrequency.keySet()) {
            int frequency = categoriesFrequency.get(category);
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentCategory = category;
            }
        }

        return mostFrequentCategory;
    }
}

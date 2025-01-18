package com.ctf.notekeeper.DataLoading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ctf.notekeeper.Note.Note;
import com.ctf.notekeeper.User.User;

import lombok.AllArgsConstructor;

// like the .env file, pretend this stuff doesn't exist
// this is a means to programmatically load data on start so you have something to play with
@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {
    private static final String FILEPATH = "classpath:data/SampleData.json";
    private static final String USER_COLL = "users";
    private static final String NOTE_COLL = "notes";

    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        try {
            Map<String, Object> data = readDataFromFile(FILEPATH);
            insertDataIntoMongoDB(data);
        } catch (Exception e) {
            System.err.println("Error during data loading process: " + e.getMessage());
        }
    }

    private Map<String, Object> readDataFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ResourceUtils.getFile(filePath)))) {
            ObjectMapper objectMapper = new ObjectMapper();
            MapType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
            return objectMapper.readValue(reader, mapType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading data from file");
        }
    }

    private void insertDataIntoMongoDB(Map<String, Object> data) {
        insertData(data.get(USER_COLL), User.class);
        insertData(data.get(NOTE_COLL), Note.class);
    }

    private <T> void insertData(Object data, Class<T> generic_class) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> items = objectMapper.convertValue(data, TypeFactory.defaultInstance().constructCollectionType(List.class, generic_class));
        mongoTemplate.insertAll(items);
    }
}

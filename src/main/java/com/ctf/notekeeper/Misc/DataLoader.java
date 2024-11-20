package com.ctf.notekeeper.Misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.ctf.notekeeper.Note.Note;
import com.ctf.notekeeper.Role.RoleEnum;
import com.ctf.notekeeper.User.User;

import io.github.cdimascio.dotenv.Dotenv;

// like the .env file, pretend this stuff doesn't exist
// this is a means to programmatically load data on start so you have something to play with
@Component
public class DataLoader implements CommandLineRunner {
    private final MongoTemplate mongoTemplate;
    private final Dotenv dotenv;
    private final Integer HASH_LEN = 62;

    public DataLoader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.dotenv = Dotenv.load();
    }

    @Override
    public void run (String... args) {
        try {
            List<RoleEnum> roles = new ArrayList<>();

            roles.add(RoleEnum.USER);
            mongoTemplate.insert(new User(2, dotenv.get("CTF_USERNAME_01"), dotenv.get("CTF_PASSWORD_01").substring(1, HASH_LEN - 1), roles));

            roles.add(RoleEnum.ADMIN);
            mongoTemplate.insert(new User(1, dotenv.get("CTF_USERNAME_00"), dotenv.get("CTF_PASSWORD_00").substring(1, HASH_LEN - 1), roles));

            List<Note> notes = Arrays.asList(
                new Note(1, "I am the all powerful", 1),
                new Note(2, "I am the alpha and the omega", 1),
                new Note(3, "I really need to make sure my password is decent. I am the admin after all.", 1),
                new Note(4, "Update on my last record, I pulled something from a repository of secure password lists. At least, that's how I'd interpret something named SecLists. There are a bunch of common credentials to pick from, so I just went with something from the best 1050. Feeling confident", 1),
                new Note(5, "I have a lot of features to implement", 2),
                new Note(6, "Day 1: This code base is terrible, but it is now my inherited mess", 2),
                new Note(7, "Day 2: Making this bowl of spaghetti code functional will be the challenge of my career", 2)
            );
            mongoTemplate.insertAll(notes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

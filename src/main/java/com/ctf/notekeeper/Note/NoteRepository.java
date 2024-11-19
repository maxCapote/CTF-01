package com.ctf.notekeeper.Note;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, Integer> {
    List<Note> findAllByUserId(Integer userId);
}

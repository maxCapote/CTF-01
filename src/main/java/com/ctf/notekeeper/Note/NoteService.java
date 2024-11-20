package com.ctf.notekeeper.Note;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ctf.notekeeper.User.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteService {
    private static final Pattern DESCRIPTION_REGEX = Pattern.compile("^.{1,512}$");

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    // standard to record a note
    // the note will be associated with the authenticated caller
    public Note recordNote(Note note) {
        validateDescription(note.getDescription());
        note.setId(generateNoteId());
        note.setUserId(getCurrentUserId());
        return noteRepository.insert(note);
    }

    // standard read as long as the id is valid
    public Note getNote(Integer id) {
        validateId(id);
        return noteRepository.findById(id).get();
    }

    // so much more work to update something
    // bleh
    public Note updateNote(Integer id, Note note) {
        validateId(id);
        validateDescription(note.getDescription());
        Note targetNote = noteRepository.findById(id).get();
        targetNote.setDescription(note.getDescription());
        return noteRepository.save(targetNote);
    }

    // blow it up
    public String deleteNote(Integer id) {
        validateId(id);
        noteRepository.deleteById(id);
        return "Deletion succeeded";
    }

    // ..
    public List<Note> listNotes() {
        return noteRepository.findAllByUserId(getCurrentUserId());
    }

    // nothing fancy for an id
    private synchronized Integer generateNoteId() {
        return noteRepository.findAll().size() + 1;
    }

    // this other stuff is probably just validation

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).getId();
    }

    private void validateDescription(String description) {
        if (description == null || !DESCRIPTION_REGEX.matcher(description).matches()) {
            throw new IllegalArgumentException("Invalid description");
        }
    }

    private void validateId (Integer id) {
        if (id < 1 || id > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid id");
        }

        noteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invalid id"));
    }
}

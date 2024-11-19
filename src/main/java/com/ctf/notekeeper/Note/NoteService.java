package com.ctf.notekeeper.Note;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.springframework.security.access.AccessDeniedException;
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

    public Note recordNote(Note note) {
        validateDescription(note.getDescription());
        note.setId(generateNoteId());
        note.setUserId(getCurrentUserId());
        return noteRepository.insert(note);
    }

    public Note getNote(Integer id) {
        validateId(id);
        return noteRepository.findById(id).get();
    }

    public Note updateNote(Integer id, Note note) {
        validateId(id);
        validateDescription(note.getDescription());
        Note targetNote = noteRepository.findById(id).get();
        targetNote.setDescription(note.getDescription());
        return noteRepository.save(targetNote);
    }

    public String deleteNote(Integer id) {
        validateId(id);
        noteRepository.deleteById(id);
        return "Deletion succeeded";
    }

    public List<Note> listNotes() {
        return noteRepository.findAllByUserId(getCurrentUserId());
    }

    private synchronized Integer generateNoteId() {
        return noteRepository.findAll().size() + 1;
    }

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

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Invalid id"));

        if (!getCurrentUserId().equals(note.getUserId())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }
}

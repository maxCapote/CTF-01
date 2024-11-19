package com.ctf.notekeeper.Note;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctf.notekeeper.Misc.CustomResponse;
import com.ctf.notekeeper.Misc.CustomResponseFactory;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/v1/notekeeper")
@AllArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @GetMapping("/test")
    public CustomResponse helloWorld() {
        return CustomResponseFactory.createResponse("message", "We're up");
    }

    @PostMapping("/record")
    public Note recordNote(@RequestBody Note note) {
        return noteService.recordNote(note);
    }

    @GetMapping("/{id}")
    public Note getNote(@PathVariable Integer id) {
        return noteService.getNote(id);
    }

    @PostMapping("/update/{id}")
    public Note updateNote(@PathVariable Integer id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/delete/{id}")
    public CustomResponse deleteNote(@PathVariable Integer id) {
        return CustomResponseFactory.createResponse("message", noteService.deleteNote(id));
    }

    @GetMapping("")
    public List<Note> listNotes() {
        return noteService.listNotes();
    }
}

package com.ctf.notekeeper.Note;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctf.notekeeper.ResponseHandling.CustomResponse;
import com.ctf.notekeeper.ResponseHandling.CustomResponseFactory;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/notekeeper")
@AllArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // throwing this in for a quick sanity check on start
    @GetMapping("/test")
    public CustomResponse helloWorld() {
        return CustomResponseFactory.createResponse("message", "We're up");
    }

    // this other stuff is the usual CRUDL

    @PostMapping("/record")
    public CustomResponse recordNote(@RequestBody Note note) {
        return CustomResponseFactory.createResponse("note", noteService.recordNote(note));
    }

    @GetMapping("/{id}")
    public CustomResponse getNote(@PathVariable Integer id) {
        return CustomResponseFactory.createResponse("note", noteService.getNote(id));
    }

    @PostMapping("/update/{id}")
    public CustomResponse updateNote(@PathVariable Integer id, @RequestBody Note note) {
        return CustomResponseFactory.createResponse("note", noteService.updateNote(id, note));
    }

    @DeleteMapping("/delete/{id}")
    public CustomResponse deleteNote(@PathVariable Integer id) {
        return CustomResponseFactory.createResponse("message", noteService.deleteNote(id));
    }

    @GetMapping("")
    public CustomResponse listNotes() {
        return CustomResponseFactory.createResponse("notes", noteService.listNotes());
    }
}

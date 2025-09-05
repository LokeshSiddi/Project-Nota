package com.lokesh.Project_Nota.controller;

import com.lokesh.Project_Nota.NoteDTO;
import com.lokesh.Project_Nota.model.Note;
import com.lokesh.Project_Nota.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO note) {
        return ResponseEntity.ok(noteService.createNote(note));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody NoteDTO note) {
        return ResponseEntity.ok(noteService.updateNote(id, note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.deleteNote(id));
    }

    @PostMapping("/share/{id}")
    public ResponseEntity<Map<String,String>> toggleShare(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.toggleShare(id, jwt));
    }

}

package com.lokesh.Project_Nota.controller;

import com.lokesh.Project_Nota.NoteDTO;
import com.lokesh.Project_Nota.model.Note;
import com.lokesh.Project_Nota.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "https://your-notes-frontend.vercel.app")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/all")
    public ResponseEntity<List<Note>> getAllNotes(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.getAllNotes(jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.getNoteById(id, jwt));
    }

    @PostMapping("/create")
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO note, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.createNote(note, jwt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody NoteDTO note, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.updateNote(id, note, jwt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.deleteNote(id, jwt));
    }

    @PostMapping("/share/{id}")
    public ResponseEntity<Map<String,String>> toggleShare(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(noteService.toggleShare(id, jwt));
    }

}

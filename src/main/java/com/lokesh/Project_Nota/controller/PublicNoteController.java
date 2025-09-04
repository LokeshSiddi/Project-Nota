package com.lokesh.Project_Nota.controller;

import com.lokesh.Project_Nota.model.Note;
import com.lokesh.Project_Nota.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/public/notes")
@CrossOrigin(origins = "*") // Allow requests from any origin
@RequiredArgsConstructor
public class PublicNoteController {
    // This controller can have endpoints that do not require authentication

    private final NoteService noteService;

    @GetMapping("{shareableId}")
    public ResponseEntity<Note> getNoteByShareableId(@PathVariable UUID shareableId) {
        return ResponseEntity.ok(noteService.getNoteByShareableId(shareableId));
    }

}

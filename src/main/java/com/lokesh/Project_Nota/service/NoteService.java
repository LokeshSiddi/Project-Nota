package com.lokesh.Project_Nota.service;

import com.lokesh.Project_Nota.NoteDTO;
import com.lokesh.Project_Nota.model.Note;
import com.lokesh.Project_Nota.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note createNote(NoteDTO note) {
        Note newNote = new Note();
        newNote.setTitle(note.getTitle());
        newNote.setContent(note.getContent());
        return noteRepository.save(newNote);
    }

    public Note getNoteById(long id) {
        return noteRepository.findById(id).get();
    }

    public Note updateNote(Long id, NoteDTO note) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        return noteRepository.save(existingNote);
    }

    public String deleteNote(Long id) {
        noteRepository.deleteById(id);
        if(noteRepository.existsById(id)) {
            return "Note deletion failed";
        }
        return "Note deleted successfully";
    }

    public Note getNoteByShareableId(UUID shareableId) {
        return noteRepository.findByShareableIdAndIsShared(shareableId, true)
                .orElseThrow(() -> new RuntimeException("Note not found or not shared with shareableId: " + shareableId));
    }

    public Map<String,String> toggleShare(Long id, Jwt jwt) {
        String userId = jwt.getSubject();
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));

        if(!existingNote.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to share this note");
        }

        existingNote.setShared(!existingNote.isShared());

        if(existingNote.isShared() && existingNote.getShareableId() == null) {
            existingNote.setShareableId(UUID.randomUUID());
        } else if (!existingNote.isShared()) {
            existingNote.setShareableId(null);
        }

        noteRepository.save(existingNote);

        Map<String,String> response = new HashMap<>();

        if(existingNote.isShared()) {
            response.put("message", "Note is now shared");
            response.put("shareableLink", "http://localhost:8080/api/public/notes/" + existingNote.getShareableId());
        } else {
            response.put("message", "Note is no longer shared");
            response.put("shareableLink", null);
        }

        return response;
    }
}

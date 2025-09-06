package com.lokesh.Project_Nota.service;

import com.lokesh.Project_Nota.NoteDTO;
import com.lokesh.Project_Nota.exception.NotFoundException;
import com.lokesh.Project_Nota.exception.UnauthorizedAccessException;
import com.lokesh.Project_Nota.model.Note;
import com.lokesh.Project_Nota.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public List<Note> getAllNotes(Jwt jwt) {
        return noteRepository.findAllByUserId(jwt.getSubject());
    }

    public Note createNote(NoteDTO note, Jwt jwt) {
        Note newNote = new Note();
        newNote.setTitle(note.getTitle());
        newNote.setContent(note.getContent());
        newNote.setUserId(jwt.getSubject());
        return noteRepository.save(newNote);
    }

    public Note getNoteById(long id, Jwt jwt) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        if (!note.getUserId().equals(jwt.getSubject())) {
            throw new UnauthorizedAccessException("You are not authorized to view this note");
        }

        return note;
    }


    public Note updateNote(Long id, NoteDTO note, Jwt jwt) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        if(!existingNote.getUserId().equals(jwt.getSubject())) {
            throw new UnauthorizedAccessException("You are not authorized to update this note");
        }

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        return noteRepository.save(existingNote);
    }

    public String deleteNote(Long id, Jwt jwt) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        if(!existingNote.getUserId().equals(jwt.getSubject())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this note");
        }

        noteRepository.deleteById(existingNote.getId());
        if(noteRepository.existsById(existingNote.getId())) {
            return "Note deletion failed";
        }
        return "Note deleted successfully";
    }

    public Note getNoteByShareableId(UUID shareableId) {
        return noteRepository.findByShareableIdAndIsShared(shareableId, true)
                .orElseThrow(() -> new NotFoundException("Note not found or not shared with shareableId: " + shareableId));
    }

    public Map<String,String> toggleShare(Long id, Jwt jwt) {
        String userId = jwt.getSubject();
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        if(!existingNote.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You are not authorized to share this note");
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
            response.put("shareableLink", frontendBaseUrl + "/api/public/notes/" + existingNote.getShareableId());
        } else {
            response.put("message", "Note is no longer shared");
            response.put("shareableLink", null);
        }

        return response;
    }
}

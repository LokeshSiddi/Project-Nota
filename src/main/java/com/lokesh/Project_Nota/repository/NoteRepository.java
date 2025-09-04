package com.lokesh.Project_Nota.repository;

import com.lokesh.Project_Nota.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByShareableId(UUID shareableId);

    Optional<Note> findByShareableIdAndIsShared(UUID shareableId, boolean isShared);
}

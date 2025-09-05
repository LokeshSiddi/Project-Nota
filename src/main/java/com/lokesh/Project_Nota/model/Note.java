package com.lokesh.Project_Nota.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

//    This is Keycloak User ID
    private String userId;

    @Column(unique = true)
    private UUID shareableId;

    private boolean isShared = false;
}

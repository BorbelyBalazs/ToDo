package com.borbely.appauthdemo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NonNull
    private String description;

    private LocalDate deadLine;

    private boolean done;

    @ManyToOne
    private AppUser owner;

    public ToDo(@NonNull String description, LocalDate deadLine, boolean done) {
        this.description = description;
        this.deadLine = deadLine;
        this.done = done;
    }
}



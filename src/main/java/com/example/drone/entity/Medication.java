package com.example.drone.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Medication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int weight;

    @Column(unique = true, nullable = false)
    private String code;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = true)
    @JsonBackReference
    private Drone drone;
}

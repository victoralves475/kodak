package br.edu.ifpb.kodak.model.security;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do papel, por exemplo: "USER" ou "ADMIN"
    @Column(nullable = false, unique = true)
    private String nome;

    public Role() {
    }

    public Role(String nome) {
        this.nome = nome;
    }
}

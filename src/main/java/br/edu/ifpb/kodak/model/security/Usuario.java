package br.edu.ifpb.kodak.model.security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que será usado como username (único) para a autenticação
    @Column(nullable = false, unique = true)
    private String email;

    // Senha criptografada do usuário
    @Column(nullable = false)
    private String password;

    // Relação com os papéis (roles) do usuário
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Construtor padrão para uso do JPA
    public Usuario() {
    }

    // Construtor com os atributos essenciais
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

package br.edu.ifpb.kodak.repository.security;

import br.edu.ifpb.kodak.model.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}

package br.edu.ifpb.kodak.service;

import br.edu.ifpb.kodak.model.DTO.LoginRequestDTO;
import br.edu.ifpb.kodak.model.security.Usuario;
import br.edu.ifpb.kodak.repository.security.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica o usuário com base no email e senha.
     *
     * @param loginRequest DTO contendo email e senha.
     * @return true se as credenciais forem válidas; false caso contrário.
     */
    public boolean authenticate(LoginRequestDTO loginRequest) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            return passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword());
        }
        return false;
    }

    /**
     * Autentica e retorna o objeto Usuario correspondente.
     *
     * @param loginRequest DTO contendo email e senha.
     * @return o objeto Usuario se as credenciais forem válidas; null caso contrário.
     */
    public Usuario authenticateAndGet(LoginRequestDTO loginRequest) {
        return usuarioRepository.findByEmail(loginRequest.getEmail())
                .filter(usuario -> passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword()))
                .orElse(null);
    }
}

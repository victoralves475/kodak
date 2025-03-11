package br.edu.ifpb.kodak.service.security;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.model.security.Role;
import br.edu.ifpb.kodak.model.security.Usuario;
import br.edu.ifpb.kodak.repository.security.RoleRepository;
import br.edu.ifpb.kodak.repository.security.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método existente para criação de usuário a partir do Photographer
    public Usuario createUsuarioFromPhotographer(Photographer photographer) {
        Usuario usuario = new Usuario();
        usuario.setEmail(photographer.getEmail());
        usuario.setPassword(passwordEncoder.encode(photographer.getPassword()));

        // Atribui a role "USER" por padrão
        String roleName = "USER";
        Role role = roleRepository.findByNome(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
        usuario.getRoles().add(role);

        return usuarioRepository.save(usuario);
    }

    // Método para promover o usuário a admin
    public void promoteToAdmin(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));

        // Busca ou cria a role ADMIN
        Role adminRole = roleRepository.findByNome("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

        // Adiciona a role ADMIN ao usuário, se ainda não estiver presente
        if (!usuario.getRoles().contains(adminRole)) {
            usuario.getRoles().add(adminRole);
            usuarioRepository.save(usuario);
        }
    }

    // Método para verificar se o usuário possui a role ADMIN
    public boolean isAdmin(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
        return usuario.getRoles().stream()
                .anyMatch(role -> role.getNome().equalsIgnoreCase("ADMIN"));
    }

    // Método para remover a role ADMIN do usuário
    public void demoteFromAdmin(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + email));
        // Remove a role ADMIN se presente
        usuario.getRoles().removeIf(role -> role.getNome().equalsIgnoreCase("ADMIN"));
        usuarioRepository.save(usuario);
    }
}

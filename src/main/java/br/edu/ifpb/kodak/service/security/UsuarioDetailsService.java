package br.edu.ifpb.kodak.service.security;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.model.security.Usuario;
import br.edu.ifpb.kodak.repository.security.UsuarioRepository;
import br.edu.ifpb.kodak.service.PhotographerService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PhotographerService photographerService;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository,
                                 PhotographerService photographerService) {
        this.usuarioRepository = usuarioRepository;
        this.photographerService = photographerService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        // Busca o fotógrafo correspondente para verificar se a conta está suspensa
        Photographer photographer = photographerService.getPhotographerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Fotógrafo não encontrado com email: " + email));

        if (photographer.isSuspended()) {
            throw new DisabledException("Conta suspensa");
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNome()))
                        .collect(Collectors.toList())
        );
    }
}

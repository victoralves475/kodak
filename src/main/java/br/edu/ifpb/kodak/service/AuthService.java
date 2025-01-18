package br.edu.ifpb.kodak.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.DTO.LoginRequestDTO;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotographerRepository;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PhotographerRepository photographerRepository;

    /**
     * Método para autenticar o usuário com base no email e senha.
     *
     * @param loginRequest DTO contendo email e senha.
     * @return true se as credenciais forem válidas; false caso contrário.
     */
    public boolean authenticate(LoginRequestDTO loginRequest) {
        // Busca o fotógrafo pelo email, retornando um Optional
        Optional<Photographer> optionalPhotographer = photographerRepository.findByEmail(loginRequest.getEmail());

        // Verifica se o fotógrafo existe e se a senha está correta
        if (optionalPhotographer.isPresent()) {
            Photographer photographer = optionalPhotographer.get();
            return loginRequest.getPassword().equals(photographer.getPassword());
        }

        // Retorna false se o fotógrafo não for encontrado
        return false;
    }
}

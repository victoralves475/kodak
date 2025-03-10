package br.edu.ifpb.kodak.configuration;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final PhotographerService photographerService;

    public CustomAuthenticationSuccessHandler(PhotographerService photographerService) {
        this.photographerService = photographerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Recupera o email (username) do usuário autenticado
        String email = authentication.getName();

        // Busca o fotógrafo correspondente
        Optional<Photographer> photographerOpt = photographerService.getPhotographerByEmail(email);
        if (photographerOpt.isPresent()) {
            Photographer photographer = photographerOpt.get();
            // Armazena o fotógrafo na sessão
            request.getSession().setAttribute("loggedPhotographer", photographer);
            // Redireciona para a página inicial do fotógrafo
            response.sendRedirect(request.getContextPath() + "/photographer/home?photographerId=" + photographer.getId());
        } else {
            // Se não encontrar o fotógrafo, redireciona para a página de login com mensagem de erro
            response.sendRedirect(request.getContextPath() + "/login/sign-in?error");
        }
    }
}

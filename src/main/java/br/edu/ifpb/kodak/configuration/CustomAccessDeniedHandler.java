package br.edu.ifpb.kodak.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final PhotographerService photographerService;

    public CustomAccessDeniedHandler(PhotographerService photographerService) {
        this.photographerService = photographerService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Cria um FlashMap e adiciona a mensagem de erro
        FlashMap flashMap = new FlashMap();
        flashMap.put("errorMessage", "Você não tem permissão para acessar esta página.");
        new SessionFlashMapManager().saveOutputFlashMap(flashMap, request, response);

        // Recupera o usuário autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Photographer photographer = photographerService.getPhotographerByEmail(email)
                .orElse(null);

        // Redireciona para a página inicial do fotógrafo com o parâmetro "photographerId"
        if (photographer != null) {
            response.sendRedirect(request.getContextPath() + "/photographer/home?photographerId=" + photographer.getId());
        } else {
            response.sendRedirect(request.getContextPath() + "/login/sign-in");
        }
    }
}

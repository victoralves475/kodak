package br.edu.ifpb.kodak.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        // Verifica se a exceção ou sua causa é uma DisabledException
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        FlashMap flashMap = new FlashMap();
        if (rootCause instanceof DisabledException) {
            flashMap.put("errorMessage", "Conta suspensa");
        } else {
            flashMap.put("errorMessage", "Email ou senha inválidos.");
        }
        new SessionFlashMapManager().saveOutputFlashMap(flashMap, request, response);

        super.onAuthenticationFailure(request, response, exception);
    }
}

package br.edu.ifpb.kodak.view;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;

@Component
public class CadastrarFotografo implements CommandLineRunner {

    @Autowired
    private PhotographerService fotografoService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Digite o nome do fotógrafo: ");
        String name = scanner.nextLine();
        
        System.out.print("Digite o email do fotógrafo: ");
        String email = scanner.nextLine();
        
        // Cria o fotógrafo
        Photographer fotografo = new Photographer();
        fotografo.setName(name);
        fotografo.setEmail(email);
        fotografo.setPassword("123456");
        
        // Salva o fotógrafo
        fotografoService.savePhotographer(fotografo);
        
        // Busca o fotógrafo cadastrado
        Photographer fotografoCadastrado = fotografoService.getPhotographerByEmail(email).orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado!"));
        
        System.out.println("Fotógrafo cadastrado com sucesso!");
        System.out.println("Nome: " + fotografoCadastrado.getName());
        System.out.println("Email: " + fotografoCadastrado.getEmail());
        
        scanner.close();
    }
}

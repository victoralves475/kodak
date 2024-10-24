package br.edu.ifpb.kodak.view;

import java.util.Scanner;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.service.PhotographerService;

public class CadastrarFotografo {
	
	public static void main(String[] args) {
		
		//Cria o Serviço
		PhotographerService fotografoService = new PhotographerService();
		
		//Inputs do console
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Digite o nome do fotografo: ");
		String name = scanner.nextLine();
		
		System.out.print("Digite o email do fotografo: ");
		String email = scanner.nextLine();
		
		//Cria o fotografo
		Photographer fotografo = new Photographer();
		fotografo.setName(name);
		fotografo.setEmail(email);
		
		//Salva o fotografo
		fotografoService.savePhotographer(fotografo);
		
		//Mostra o fotografo cadastrado
		Photographer fotografoCadastrado = fotografoService.getPhotographerByEmail(email).get();
		
		System.out.println("Fotografo cadastrado com sucesso!");
		System.out.println("Nome: " + fotografoCadastrado.getName());
		System.out.println("Email: " + fotografoCadastrado.getEmail());
		
		scanner.close();
		
		
	}
	

}

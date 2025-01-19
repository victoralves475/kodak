package br.edu.ifpb.kodak.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotographerRepository;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Service
public class PhotographerService {

	@Autowired
	private PhotographerRepository photographerRepository;
	
    @Autowired
    private PhotoService photoService;

	// CRUD

	// Create
	public Photographer savePhotographer(Photographer photographer) {
		return photographerRepository.save(photographer);
	}

	// Read

	public List<Photographer> getAllPhotographers() {
		return photographerRepository.findAll();
	}

//	public Optional<Photographer> getPhotographerById(int id) {
//		Optional<Photographer> photographer = photographerRepository.findById(id);
//		List<Photo> photos = photoService.getPhotosByPhotographerId(id);
//		System.out.println(photos);
//		photographer.get().setPhotos(new HashSet<>(photos));
//		return photographer;
//		
//	}
	
	public Optional<Photographer> getPhotographerById(int id) {
	    // Tenta encontrar o fotógrafo pelo ID
	    Optional<Photographer> photographerOpt = photographerRepository.findById(id);

	    if (photographerOpt.isPresent()) {
	        Photographer photographer = photographerOpt.get();

	        // Busca as fotos associadas ao fotógrafo
	        List<Photo> photos = photoService.getPhotosByPhotographerId(id);

	        // Adiciona as fotos ao fotógrafo
	        photos.forEach(photo -> photo.setPhotographer(photographer)); // Relacionamento bidirecional
	        photographer.setPhotos(new HashSet<>(photos));

	        return Optional.of(photographer);
	    }

	    return Optional.empty(); // Retorna vazio se o fotógrafo não foi encontrado
	}


	public Optional<Photographer> getPhotographerByEmail(String email) {
		return photographerRepository.findByEmail(email);
	}

	// Delete
	public void deletePhotographer(int id) {
		photographerRepository.deleteById(id);
	}

	public boolean existsPhotographerByEmail(String email) {
		return photographerRepository.existsPhotographerByEmail(email);
	}

}

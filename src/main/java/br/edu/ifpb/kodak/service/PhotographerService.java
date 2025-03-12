package br.edu.ifpb.kodak.service;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PhotographerService {

	@Autowired
	private PhotographerRepository photographerRepository;

	@Autowired
	private PhotoService photoService;

	// Create
	public Photographer savePhotographer(Photographer photographer) {
		return photographerRepository.save(photographer);
	}

	// Read

	public List<Photographer> getAllPhotographers() {
		return photographerRepository.findAll();
	}

	public Page<Photographer> getPhotographers(Pageable pageable) {
		return photographerRepository.findAll(pageable);
	}

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

	public List<Photographer> getPhotographerByName(String name) {
		return photographerRepository.findByNameContainingIgnoreCase(name);
	}

	public void changeLockStatus(int id) {
		Optional<Photographer> photographerOpt = photographerRepository.findById(id);
		if (photographerOpt.isPresent()) {
			Photographer photographer = photographerOpt.get();
			photographer.setLockedFollow(!photographer.isLockedFollow());
			photographerRepository.save(photographer);
		} else {
			throw new RuntimeException("Fotógrafo não encontrado: " + id);
		}
	}

	@Transactional
	public void likePhoto(int photoId, Photographer photographer) {
		Photo photo = photoService.getPhotoById(photoId)
				.orElseThrow(() -> new RuntimeException("Foto não encontrada"));

		Set<Photographer> likedPhotographers = photo.getLikedPhotographers();

		if (likedPhotographers.contains(photographer)) {
			likedPhotographers.remove(photographer);
			photographer.getLikedPhotos().remove(photo);
		} else {
			likedPhotographers.add(photographer);
			photographer.getLikedPhotos().add(photo);
		}

		photoService.savePhoto(photo);
		photographerRepository.save(photographer);
	}

	public List<Photographer> getPhotographersByIds(List<Integer> ids) {
		return photographerRepository.findAllById(ids);
	}

}

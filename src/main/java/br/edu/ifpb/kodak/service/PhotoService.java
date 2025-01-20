package br.edu.ifpb.kodak.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
	private final PhotoRepository photoRepository;

	public PhotoService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	// CRUD

	// Create
	public Photo savePhoto(Photo photo) {
		return photoRepository.save(photo);
	}

	// Read
	public List<Photo> getAllPhotos() {
		return photoRepository.findAll();
	}

	public Optional<Photo> getPhotoById(int id) {
		return photoRepository.findById(id);
	}

	public List<Photo> getPhotosByPhotographerId(int photographerId) {
		return photoRepository.findByPhotographerId(photographerId);
	}

	// Delete
	public void deletePhoto(int id) {
		photoRepository.deleteById(id);
	}

	public void likePhoto(int photoId, Photographer photographer) {
		Photo photo = photoRepository.findById(photoId).get();
		Set<Photographer> likedPhotographers = photo.getLikedPhotographers();

		likedPhotographers.add(photographer);
		
		photo.setLikedPhotographers(likedPhotographers);
		
		photoRepository.save(photo); // por algum motivo o like não tá persistindo

	}

}

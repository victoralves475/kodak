package br.edu.ifpb.kodak.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.PhotographerRepository;

@Service
public class PhotographerService {

	@Autowired
	private PhotographerRepository photographerRepository;

	// CRUD

	// Create
	public Photographer savePhotographer(Photographer photographer) {
		return photographerRepository.save(photographer);
	}

	// Read

	public List<Photographer> getAllPhotographers() {
		return photographerRepository.findAll();
	}

	public Optional<Photographer> getPhotographerById(int id) {
		return photographerRepository.findById(id);
	}

	public Optional<Photographer> getPhotographerByEmail(String email) {
		return photographerRepository.findByEmail(email);
	}

	// Delete
	public void deletePhotographer(int id) {
		photographerRepository.deleteById(id);
	}

	public Optional<Photographer> findByEmail(String email) { return photographerRepository.findByEmail(email); }

	public boolean validatePassword(Photographer photographer1, Photographer photographer2) {
		return photographer1.getPassword().equals(photographer2.getPassword());
	}

}

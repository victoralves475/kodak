package br.edu.ifpb.kodak.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.repository.HashtagRepository;

@Service
public class HashtagService {

	private HashtagRepository hashtagRepository;
	
	public HashtagService(HashtagRepository hastagRepository) {
		this.hashtagRepository = hastagRepository;
	}
	
	//CRUD
	
	//Create
	public Hashtag create(Hashtag hashtag) {
		return hashtagRepository.save(hashtag);
	}
	
	//Read
	public Optional<Hashtag> getHashtagById(int id) {
		return hashtagRepository.findById(id);
	}
	
	public Optional<Hashtag> getHashtagByTagName(String tagName) {
		return hashtagRepository.findByTagName(tagName);
	}
	
	//Update
	//Delete
	public void delete(Hashtag hashtag) {
		hashtagRepository.delete(hashtag);
	}
	
}

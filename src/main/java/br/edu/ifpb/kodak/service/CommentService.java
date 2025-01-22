package br.edu.ifpb.kodak.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	@Autowired
	private final CommentRepository commentRepository;

	// CRUD

	// Create
	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

	// Delete
	public void deleteComment(int id) {
		commentRepository.deleteById(id);
	}

	// Read
	public Optional<Comment> getCommentById(int id) {
		return commentRepository.findById(id);
	}

	public List<Comment> getCommentsByPhotoId(int photoId) {
		return commentRepository.findByPhotoId(photoId);
	}

	public void addCommentToPhoto(String commentText, Photo photo, Photographer photographer) {
		Comment comment = new Comment();
		
		comment.setCommentText(commentText);
        comment.setPhotographer(photographer);
		comment.setPhoto(photo);
		commentRepository.save(comment);
	}

}

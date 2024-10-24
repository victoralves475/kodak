package br.edu.ifpb.kodak.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

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

}

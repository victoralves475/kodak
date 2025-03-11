package br.edu.ifpb.kodak.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.model.Photographer;
import br.edu.ifpb.kodak.repository.CommentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	@Autowired
	private final CommentRepository commentRepository;

	// Cria e persiste um comentário, definindo o createdAt com a data/hora atual
	public Comment addCommentToPhoto(String commentText, Photo photo, Photographer photographer) {
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		comment.setPhotographer(photographer);
		comment.setPhoto(photo);
		comment.setCreatedAt(LocalDateTime.now()); // Define o timestamp de criação
		return commentRepository.save(comment);
	}

	// Delete
	public boolean deleteComment(int id, Photographer loggedPhotographer) {
		Optional<Comment> commentOpt = commentRepository.findById(id);
		if (commentOpt.isPresent()) {
			Comment comment = commentOpt.get();
			if (loggedPhotographer.isAdmin() || isCommentOwner(comment, loggedPhotographer)) {
				commentRepository.deleteCommentById(id);
				commentRepository.flush();
				return true;
			}
		}
		return false;
	}

	// Read
	public Optional<Comment> getCommentById(int id) {
		return commentRepository.findById(id);
	}

	public List<Comment> getCommentsByPhotoId(int photoId) {
		return commentRepository.findByPhotoId(photoId);
	}

	// Atualiza o comentário e retorna o objeto atualizado
	public Optional<Comment> updateComment(int id, Photographer photographer, String newCommentText) {
		Optional<Comment> commentOpt = commentRepository.findById(id);
		if (commentOpt.isPresent()) {
			Comment comment = commentOpt.get();
			if (isCommentOwner(comment, photographer)) {
				comment.setCommentText(newCommentText);
				Comment updatedComment = commentRepository.save(comment);
				return Optional.of(updatedComment);
			}
		}
		return Optional.empty();
	}

	private boolean isCommentOwner(Comment comment, Photographer loggedPhotographer) {
		return comment.getPhotographer().getId() == loggedPhotographer.getId();
	}
}

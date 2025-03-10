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
	public boolean deleteComment(int id, Photographer loggedPhotographer) {
		Optional<Comment> commentOpt = commentRepository.findById(id);

		if (commentOpt.isPresent()) {
			Comment comment = commentOpt.get();

			if (loggedPhotographer.isAdmin() || isCommentOwner(comment, loggedPhotographer)) {
				commentRepository.deleteById(id);
				return true;
			}
		}
		return false; // Comentário não encontrado
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

	public boolean updateComment(int id, Photographer photographer, String newCommentText) {
		Optional<Comment> commentOpt = commentRepository.findById(id);

		if (commentOpt.isPresent()) {
			Comment comment = commentOpt.get();

			if(isCommentOwner(comment, photographer)) {
				comment.setCommentText(newCommentText);
				commentRepository.save(comment);
				return true;
			}
		}
		return false;
	}

	private boolean isCommentOwner(Comment comment, Photographer loggedPhotographer) {
		return comment.getPhotographer().getId() == loggedPhotographer.getId();
	}



}

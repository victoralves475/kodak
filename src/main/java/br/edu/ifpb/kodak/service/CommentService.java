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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

	@Autowired
	private final CommentRepository commentRepository;

	// CRUD

	// Create
	public Comment saveComment(Comment comment) {
		return commentRepository.save(comment);
	}

	// Delete
	@Transactional
	public boolean deleteComment(int id, Photographer loggedPhotographer) {
		Optional<Comment> commentOpt = commentRepository.findById(id);

		if (commentOpt.isPresent()) {
			Comment comment = commentOpt.get();

			if (loggedPhotographer.isAdmin() || isCommentOwner(comment, loggedPhotographer)) {
				System.out.println("Entrou if de excluir - service: " + comment.getId());

				commentRepository.deleteCommentById(id); // 🔥 Alterado para excluir o objeto inteiro
				commentRepository.flush(); // 🔥 Força o JPA a executar a exclusão no banco imediatamente

				System.out.println("Comentário excluído com sucesso no banco.");
				return true;
			} else {
				System.out.println("Usuário não tem permissão para excluir este comentário.");
			}
		} else {
			System.out.println("Comentário não encontrado no banco.");
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

			System.out.println("🔹 Encontrou comentário ID: " + id);

			if (isCommentOwner(comment, photographer)) {
				System.out.println("✅ O fotógrafo é dono do comentário!");

				comment.setCommentText(newCommentText);
				commentRepository.save(comment);

				System.out.println("💾 Comentário atualizado: " + newCommentText);
				return true;
			} else {
				System.out.println("❌ O fotógrafo NÃO é dono do comentário!");
			}
		} else {
			System.out.println("❌ Comentário não encontrado no banco de dados!");
		}
		return false;
	}

	private boolean isCommentOwner(Comment comment, Photographer loggedPhotographer) {
		return comment.getPhotographer().getId() == loggedPhotographer.getId();
	}



}

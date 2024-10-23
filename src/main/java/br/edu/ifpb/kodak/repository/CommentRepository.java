package br.edu.ifpb.kodak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.kodak.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPhotoId(int photoId);

}

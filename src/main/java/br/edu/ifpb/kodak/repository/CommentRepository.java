package br.edu.ifpb.kodak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.kodak.model.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPhotoId(int photoId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :id")
    void deleteCommentById(@Param("id") Integer id);

}

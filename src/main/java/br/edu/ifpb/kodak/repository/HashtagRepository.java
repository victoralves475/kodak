package br.edu.ifpb.kodak.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.kodak.model.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findByTagNameContainingIgnoreCase(String tagName);
    Optional<Hashtag> findByTagNameIgnoreCase(String tagName);
    List<Hashtag> findAllByTagNameContainingIgnoreCase(String tagName);


}

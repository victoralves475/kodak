package br.edu.ifpb.kodak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.kodak.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findByPhotographerId(int photographerId);

}

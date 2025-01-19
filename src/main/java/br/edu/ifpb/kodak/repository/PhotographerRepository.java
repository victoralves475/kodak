package br.edu.ifpb.kodak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.kodak.model.Photographer;

public interface PhotographerRepository extends JpaRepository<Photographer, Integer> {

    Optional<Photographer> findByEmail(String email);

}


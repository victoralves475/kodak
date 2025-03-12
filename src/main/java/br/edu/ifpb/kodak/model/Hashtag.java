package br.edu.ifpb.kodak.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hashtag")
public class Hashtag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private int id;

	@Column(name = "tag_name", nullable = false)
	@Getter
	@Setter
	@NotNull(message = "O nome da hashtag não pode ser vazio")
	private String tagName;

	// RELACIONAMENTO COM FOTOS
	@ManyToMany(mappedBy = "tags")
	@Getter
	@Setter
	private Set<Photo> photos = new HashSet<>();

	public Hashtag() {
	}
	
	public Hashtag(String tagName) {
		this.tagName = tagName;
	}

}

package br.edu.ifpb.kodak.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photo")
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private int id;

	@Lob
	@Column(name = "image_data", nullable = false)
	@Getter
	@Setter
	private byte[] imageData;

	@ManyToOne
	@JoinColumn(name = "photographer_id", nullable = false)
	@Getter
	@Setter
	private Photographer photographer;

	@ManyToMany
	@JoinTable(name = "photo_hashtag", joinColumns = @JoinColumn(name = "photo_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	@Getter
	@Setter
	private Set<Hashtag> tags = new HashSet<>();

	@ManyToMany(mappedBy = "likedPhotos")
	@Getter
	@Setter
	private Set<Photographer> likedPhotographers  = new HashSet<>();

	@OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Getter
	@Setter
	private Set<Comment> comments = new HashSet<>();

	public Photo() {
	}

}

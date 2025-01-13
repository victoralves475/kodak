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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "photographer")
@Data
public class Photographer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false)
	@NotBlank(message = "O nome é obrigatório")
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	@NotBlank(message = "O email é obrigatório")
	@Email(message = "O email deve ser válido")
	private String email;
	
	@Column(name = "password", nullable = false)
	@NotBlank(message = "A senha é obrigatória")
	@Length(min = 6, max = 15, message = "A senha deve ter entre 6 e 15 caracteres")
	private String password;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "profilePic")
	private String profilePic;

	// RELACIONAMENTO COM FOTOS
	@OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Photo> photos = new HashSet<>();

	// RELACIONAMENTO COM COMENTÁRIOS
	@OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<>();

	// RELACIONAMENTO ENTRE FOTÓGRAFOS (SEGUINDO)
	@ManyToMany
	@JoinTable(name = "follow", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "followee_id"))
	private Set<Photographer> following = new HashSet<>();

	// RELACIONAMENTO ENTRE FOTÓGRAFOS (SEGUIDORES)
	@ManyToMany(mappedBy = "following")
	private Set<Photographer> followers = new HashSet<>();

	// RELACIONAMENTO COM LIKES EM FOTOS
	@ManyToMany
	@JoinTable(name = "photographer_photo_like", joinColumns = @JoinColumn(name = "photographer_id"), inverseJoinColumns = @JoinColumn(name = "photo_id"))
	private Set<Photo> likedPhotos = new HashSet<>();

	public Photographer() {
	}

}
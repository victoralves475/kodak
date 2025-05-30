package br.edu.ifpb.kodak.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "photographer")
@Data
@EqualsAndHashCode(exclude = { "photos", "comments", "following", "followers", "likedPhotos" })
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

	// Removido o campo "password", pois agora a senha é gerenciada pela entidade Usuario.

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "is_admin", nullable = false)
	private boolean isAdmin = false;

	/**
	 * Caminho da foto de perfil no sistema de arquivos.
	 */
	@Column(name = "profile_pic_path")
	private String profilePicPath;

	// Campo para indicar suspensão
	@Column(nullable = false)
	private boolean suspended = false;

	// Campo para indicar se o perfil poderá ser seguido
	@Column(nullable = false)
	private boolean lockedFollow = false;

	// Novo campo para controle de comentários (se necessário)
	@Column(name = "comment_suspend", nullable = false)
	private boolean commentSuspended = false;

	/**
	 * Relacionamento com fotos publicadas pelo fotógrafo.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Photo> photos = new HashSet<>();

	/**
	 * Relacionamento com comentários feitos pelo fotógrafo.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Comment> comments = new HashSet<>();

	/**
	 * Relacionamento com fotógrafos seguidos.
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "follow", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "followee_id"))
	private Set<Photographer> following = new HashSet<>();

	/**
	 * Relacionamento com seguidores.
	 */
	@JsonIgnore
	@ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
	private Set<Photographer> followers = new HashSet<>();

	/**
	 * Relacionamento com fotos que o fotógrafo curtiu.
	 */
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "photographer_photo_like", joinColumns = @JoinColumn(name = "photographer_id"), inverseJoinColumns = @JoinColumn(name = "photo_id"))
	private Set<Photo> likedPhotos = new HashSet<>();

	public Photographer() {
	}
}

package br.edu.ifpb.kodak.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comment")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private int id;

	@Column(name = "comment_text", length = 512)
	@Getter
	@Setter
	@NotBlank
	private String commentText;

	@Column(name = "created_at", nullable = false)
	@Getter
	@Setter
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "photographer_id", nullable = false)
	@Getter
	@Setter
	private Photographer photographer;

	@ManyToOne
	@JoinColumn(name = "photo_id", nullable = false)
	@Getter
	@Setter
	private Photo photo;

	public Comment() {
		this.createdAt = LocalDateTime.now();
	}

}

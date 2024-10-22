package br.edu.ifpb.kodak.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "photografers")
public class Photographer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String email;
	
	private String password;
	
	@ManyToMany
	@JoinTable(
			name = "followers_following",
			joinColumns = @JoinColumn(name = "photographer_id"),
			inverseJoinColumns = @JoinColumn(name = "follower_id")
			)
	private Set<Photographer> followers;
	
	

}

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "photo")
@Data
@EqualsAndHashCode(exclude = {"photographer"})
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Caminho do arquivo no sistema de arquivos
    @Column(name = "path", nullable = false)
    private String path;

    // Descrição opcional da foto
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToMany
    @JoinTable(name = "photo_hashtag", 
               joinColumns = @JoinColumn(name = "photo_id"), 
               inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "likedPhotos")
    private Set<Photographer> likedPhotographers = new HashSet<>();

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();
}

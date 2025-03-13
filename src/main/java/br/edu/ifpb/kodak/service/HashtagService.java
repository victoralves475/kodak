package br.edu.ifpb.kodak.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.model.Photo;
import br.edu.ifpb.kodak.repository.HashtagRepository;

@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PhotoService photoService;

    @Autowired
    public HashtagService(HashtagRepository hashtagRepository, PhotoService photoService) {
        this.hashtagRepository = hashtagRepository;
        this.photoService = photoService;
    }

    // CRUD

    // Create
//    public Hashtag create(Hashtag hashtag) {
//        String tagName = hashtag.getTagName().trim();
//        if (!tagName.startsWith("#")) {
//            tagName = "#" + tagName;
//        }
//        hashtag.setTagName(tagName);
//        return hashtagRepository.save(hashtag);
//    }

    public Hashtag create(Hashtag hashtag) {

        String tagName = hashtag.getTagName().trim();


        // Garante que a hashtag comece com '#'
        if (!tagName.startsWith("#")) {
            tagName = "#" + tagName;
        }

        // Remove espaços e substitui ocorrências de '#' no meio da hashtag
        tagName = tagName.replaceAll("\\s+", "").replaceAll("#+", "#");

        // Se ainda houver '#' no meio da string, corrige removendo e mantendo apenas o primeiro
        if (tagName.indexOf('#', 1) != -1) {
            tagName = "#" + tagName.substring(1).replaceAll("#", "");
        }

        // Verifica se a hashtag já existe no banco antes de persistir
        Optional<Hashtag> existingHashtag = hashtagRepository.findByTagNameIgnoreCase(tagName);
        if (existingHashtag.isPresent()) {
            return existingHashtag.get();
        }

        // Caso não exista, persiste a nova hashtag
        hashtag.setTagName(tagName);
        return hashtagRepository.save(hashtag);
    }



    // Read
    public Optional<Hashtag> getHashtagById(int id) {
        return hashtagRepository.findById(id);
    }

    public List<Hashtag> getHashtagByTagName(String tagName) {
        return hashtagRepository.findAllByTagNameContainingIgnoreCase(tagName);
    }

    // Find or Create
    public Hashtag findOrCreateByName(String tagName) {
        return hashtagRepository.findByTagNameIgnoreCase(tagName)
                .orElseGet(() -> create(new Hashtag(tagName)));
//         List<Hashtag> hashtags = hashtagRepository.findByTagNameContainingIgnoreCase(tagName);
//         // Se já existe pelo menos uma hashtag correspondente, retorna a primeira
//         if (!hashtags.isEmpty()) {
//             return hashtags.get(0);
//         }

//         // Caso contrário, cria uma nova
//         return create(new Hashtag(tagName));
    }

    // Delete
    public void delete(Hashtag hashtag) {
        hashtagRepository.delete(hashtag);
    }

    // Remove hashtag from photo
    @Transactional
    public void removeHashtagFromPhoto(Integer photoId, Integer tagId) {
        Photo photo = photoService.getPhotoById(photoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        Hashtag hashtag = hashtagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Hashtag não encontrada"));

        photo.getTags().remove(hashtag);

        photoService.savePhoto(photo);
    }

    public List<String> searchTagsByPartialName(String partialName) {
        return hashtagRepository.findAllByTagNameContainingIgnoreCase(partialName)
                .stream()
                .map(Hashtag::getTagName)
                .collect(Collectors.toList());
    }





}

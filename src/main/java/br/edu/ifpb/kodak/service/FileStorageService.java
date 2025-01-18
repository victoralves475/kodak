package br.edu.ifpb.kodak.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads/photographers/}")
    private String uploadDir;

    /**
     * Salva a foto de perfil de um fotógrafo.
     *
     * @param file MultipartFile enviado pelo fotógrafo.
     * @param photographerId ID do fotógrafo.
     * @return Caminho relativo do arquivo salvo.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public String saveProfilePic(MultipartFile file, Integer photographerId) throws IOException {
        validateFile(file);
        String subDir = "photographer_" + photographerId + "/";
        String fileName = "profile-pic.jpg"; // Nome fixo para foto de perfil
        return saveFile(file, subDir, fileName);
    }

    /**
     * Salva uma foto publicada por um fotógrafo.
     *
     * @param file MultipartFile enviado pelo fotógrafo.
     * @param photographerId ID do fotógrafo.
     * @return Caminho relativo do arquivo salvo.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    public String savePhoto(MultipartFile file, Integer photographerId) throws IOException {
        validateFile(file);
        String subDir = "photographer_" + photographerId + "/";
        String fileName = "photo_" + UUID.randomUUID() + "_" + sanitizeFileName(file.getOriginalFilename());
        return saveFile(file, subDir, fileName);
    }

    /**
     * Método genérico para salvar arquivos.
     *
     * @param file MultipartFile enviado pelo usuário.
     * @param subDir Subdiretório para armazenar o arquivo.
     * @param fileName Nome do arquivo a ser salvo.
     * @return Caminho relativo do arquivo salvo.
     * @throws IOException Se ocorrer um erro ao salvar o arquivo.
     */
    private String saveFile(MultipartFile file, String subDir, String fileName) throws IOException {
        // Cria o diretório se não existir
        Path uploadPath = Paths.get(uploadDir + subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Salva o arquivo no diretório
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Retorna o caminho relativo para uso no sistema
        return uploadDir + subDir + fileName;
    }

    /**
     * Valida o arquivo antes de salvar.
     *
     * @param file MultipartFile enviado pelo usuário.
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo está vazio.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            throw new IllegalArgumentException("Tipo de arquivo inválido. Somente imagens são permitidas.");
        }
    }

    /**
     * Sanitiza o nome do arquivo para evitar problemas de segurança.
     *
     * @param originalFileName Nome original do arquivo.
     * @return Nome sanitizado do arquivo.
     */
    private String sanitizeFileName(String originalFileName) {
        if (originalFileName == null) {
            throw new IllegalArgumentException("O nome do arquivo é nulo.");
        }
        return originalFileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    }
}

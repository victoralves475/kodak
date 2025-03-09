package br.edu.ifpb.kodak.service;

import br.edu.ifpb.kodak.model.Comment;
import br.edu.ifpb.kodak.model.Hashtag;
import br.edu.ifpb.kodak.model.Photo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    public ResponseEntity<byte[]> generatePdf(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        try {
            this.sortCommentsByDate(comments);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Definição de fontes para formatação
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Utiliza a foto associada ao primeiro comentário para extrair os dados
            Photo photo = comments.get(0).getPhoto();

            // Seção de Cabeçalho com informações da foto
            Paragraph title = new Paragraph("Detalhes da Foto", headerFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Linha em branco

            Paragraph photoDesc = new Paragraph("Descrição: " + photo.getDescription(), normalFont);
            document.add(photoDesc);

            Paragraph photographer = new Paragraph("Fotógrafo: " + photo.getPhotographer().getName(), normalFont);
            document.add(photographer);

            // Lista as hashtags, se houver
            if (photo.getTags() != null && !photo.getTags().isEmpty()) {
                StringBuilder tagsStr = new StringBuilder();
                for (Hashtag tag : photo.getTags()) {
                    tagsStr.append("#").append(tag.getTagName()).append(" ");
                }
                Paragraph tagsParagraph = new Paragraph("Hashtags: " + tagsStr.toString(), normalFont);
                document.add(tagsParagraph);
            }
            document.add(new Paragraph(" ")); // Linha em branco

            // Seção de Comentários
            Paragraph commentSection = new Paragraph("Comentários", subHeaderFont);
            document.add(commentSection);
            document.add(new Paragraph(" ")); // Linha em branco

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Percorre os comentários (assumindo que já estão em ordem cronológica)
            for (Comment comment : comments) {
                Paragraph commentParagraph = new Paragraph();
                commentParagraph.add(new Chunk("Data: " + comment.getCreatedAt().format(formatter) + "\n", normalFont));
                commentParagraph.add(new Chunk("Por: " + comment.getPhotographer().getName() + "\n", normalFont));
                commentParagraph.add(new Chunk(comment.getCommentText() + "\n", normalFont));
                document.add(commentParagraph);

                // Linha separadora para melhor visualização
                document.add(new Paragraph("--------------------------------------------------------------------------------", normalFont));
            }

            document.close();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());
        } catch (DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void sortCommentsByDate(List<Comment> comments) {
        comments.sort((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()));
    }
}

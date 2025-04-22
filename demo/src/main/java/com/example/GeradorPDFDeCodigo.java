package com.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeradorPDFDeCodigo {

    public static void main(String[] args) throws Exception {
        String caminhoBase = "C:/xampp/htdocs/testes/Meets";  // << troque aqui
        Path basePath = Paths.get(caminhoBase);
        String nomeDoPDF = "codigo_unificado.pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nomeDoPDF));
        document.open();

        // Fonte para títulos
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        // Fonte para o código
        Font codigoFont = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);

        try (Stream<Path> caminhos = Files.walk(Paths.get(caminhoBase))) {
            List<Path> arquivos = caminhos
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.toString().contains(".git"))
                    .collect(Collectors.toList());

            for (Path arquivo : arquivos) {
                String nomeArquivo = basePath.relativize(arquivo).toString();

                // Adiciona nome do arquivo como subtítulo
                Paragraph titulo = new Paragraph(nomeArquivo + "\n", tituloFont);
                document.add(titulo);

                // Lê conteúdo e adiciona como código
                List<String> linhas = Files.readAllLines(arquivo);
                String conteudo = String.join("\n", linhas);

                Paragraph codigo = new Paragraph(conteudo + "\n\n", codigoFont);
                document.add(codigo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();
        System.out.println("PDF gerado com sucesso!");
    }
}

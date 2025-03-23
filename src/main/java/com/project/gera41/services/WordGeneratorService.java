package com.project.gera41.services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordGeneratorService {

    public static void main(String[] args) {
        // Lista de rotinas simuladas para o teste
        List<String> rotinasContratadas = Arrays.asList("Rotina A - Processamento de Dados", "Rotina B - Emissão de Relatórios", "Rotina C - Controle de Estoque");

        // Caminho onde o documento será salvo
        String outputFilePath = "documentacao_teste.docx";

        // Criar o documento Word
        try {
            XWPFDocument document = new XWPFDocument();

            // Adicionar título
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Documentação das Rotinas Contratadas");
            titleRun.setBold(true);
            titleRun.setFontSize(20);

            // Adicionar as rotinas contratadas
            for (String rotina : rotinasContratadas) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(rotina);
                run.setFontSize(12);
            }

            // Salvar o documento em um arquivo
            try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
                document.write(out);
            } finally {
                document.close();
            }

            System.out.println("Documento gerado com sucesso em: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Erro ao gerar o documento: " + e.getMessage());
        }
    }
}

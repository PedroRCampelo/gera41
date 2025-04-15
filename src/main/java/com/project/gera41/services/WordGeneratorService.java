package com.project.gera41.services;

import com.project.gera41.models.Documento;
import com.project.gera41.models.DocumentoRotina;
import com.project.gera41.models.Rotina;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class WordGeneratorService {

    /**
     * Gera um documento Word com base nos dados do documento e rotinas selecionadas
     * @param documento Objeto contendo os dados do documento
     * @param rotinas Lista de rotinas selecionadas com suas ordens
     * @return Arquivo temporário contendo o documento gerado
     * @throws IOException Se ocorrer um erro na geração do arquivo
     */
    public File generateDocument(Documento documento, List<DocumentoRotina> rotinas) throws IOException {
        File tempFile = File.createTempFile("documento_", ".docx");

        try (XWPFDocument document = new XWPFDocument()) {
            // Adicionar título
            createTitle(document);

            // Adicionar cabeçalho (tabela com dados do cliente)
            createHeaderTable(document, documento);

            // Adicionar rotinas selecionadas
            addRoutines(document, rotinas);

            // Salvar o documento
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                document.write(out);
            }
        }

        return tempFile;
    }

    /**
     * Cria o título do documento
     */
    private void createTitle(XWPFDocument document) {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.LEFT);
        titleParagraph.setSpacingAfter(250); // Espaçamento após o título (em twips; 200 = ~10 pontos)
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setFontFamily("Tahoma");
        titleRun.setText("Ambientação");
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setColor("FEAC0E");
    }

    /**
     * Cria a tabela de cabeçalho com dados do documento
     */
    private void createHeaderTable(XWPFDocument document, Documento documento) {
        // Formatador de data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = documento.getDataCriacao() != null ?
                documento.getDataCriacao().format(formatter) : "";

        // Montar dados da tabela com as informações do documento
        String[][] dados = {
                {"Nome do Cliente: " + (documento.getNomeCliente() != null ? documento.getNomeCliente() : ""),
                        "Código do Cliente: " + (documento.getCodigoCliente() != null ? documento.getCodigoCliente() : "")},
                {"Nome do projeto: " + (documento.getNomeProjeto() != null ? documento.getNomeProjeto() : ""),
                        "Código do projeto: " + (documento.getCodigoProjeto() != null ? documento.getCodigoProjeto() : "")},
                {"Segmento cliente: " + (documento.getSegmentoCliente() != null ? documento.getSegmentoCliente() : ""),
                        "Unidade TOTVS: " + (documento.getUnidadeTotvs() != null ? documento.getUnidadeTotvs() : "")},
                {"Data: " + dataFormatada,
                        "Proposta comercial: " + (documento.getPropostaComercial() != null ? documento.getPropostaComercial() : "")},
                {"Gerente/Coordenador TOTVS: " + (documento.getGerenteTotvs() != null ? documento.getGerenteTotvs() : ""),
                        ""},
                {"Gerente/Coordenador cliente: " + (documento.getGerenteCliente() != null ? documento.getGerenteCliente() : ""),
                        ""}
        };

        // Criar a tabela
        XWPFTable table = document.createTable(6, 2);

        // Definir a largura fixa da tabela (em twips; 1 polegada = 1440 twips)
        table.setWidth(9000); // Aproximadamente 6,25 polegadas (5000 + 4000 = 9000 twips)
        table.getCTTbl().getTblPr().getTblW().setType(STTblWidth.DXA); // Unidade em twips
        table.getCTTbl().getTblPr().getTblW().setW(9000); // Largura fixa

        // Definir a largura fixa das colunas
        for (int col = 0; col < 2; col++) {
            table.getRow(0).getCell(col).getCTTc().addNewTcPr().addNewTcW().setW(col == 0 ? 5000 : 4000);
            table.getRow(0).getCell(col).getCTTc().addNewTcPr().addNewTcW().setType(STTblWidth.DXA);
        }

        // Definir margens internas (padding) nas células
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                XWPFTableCell cell = table.getRow(i).getCell(j);
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // Alinhamento vertical central
                // Definir margens internas (em twips)
                org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr tcPr = cell.getCTTc().addNewTcPr();
                org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar tcMar = tcPr.addNewTcMar();
                tcMar.addNewLeft().setW(60); // Margem esquerda
                tcMar.addNewRight().setW(60); // Margem direita
                tcMar.addNewTop().setW(60); // Margem superior
                tcMar.addNewBottom().setW(60); // Margem inferior
            }
        }

        // Definir a cor da borda da tabela
        table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda horizontal interna
        table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda vertical interna
        table.getCTTbl().getTblPr().getTblBorders().getTop().setColor("FFA500"); // Borda superior
        table.getCTTbl().getTblPr().getTblBorders().getBottom().setColor("FFA500"); // Borda inferior
        table.getCTTbl().getTblPr().getTblBorders().getLeft().setColor("FFA500"); // Borda esquerda
        table.getCTTbl().getTblPr().getTblBorders().getRight().setColor("FFA500"); // Borda direita

        // Preencher a tabela com os dados
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                XWPFTableCell cell = table.getRow(i).getCell(j);
                XWPFParagraph paragraph = cell.getParagraphs().getFirst();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                paragraph.setSpacingAfter(0);

                String cellText = dados[i][j];
                if (cellText.contains(":")) {
                    // Dividir o texto em duas partes: antes e depois do ":"
                    String[] parts = cellText.split(":", 2);
                    String beforeColon = parts[0] + ":";
                    String afterColon = parts.length > 1 ? parts[1] : "";

                    // Texto antes do ":" em negrito
                    XWPFRun runBefore = paragraph.createRun();
                    runBefore.setColor("434343");
                    runBefore.setFontFamily("Tahoma");
                    runBefore.setText(beforeColon);
                    runBefore.setFontSize(10);
                    runBefore.setBold(true);

                    // Texto depois do ":" sem negrito
                    XWPFRun runAfter = paragraph.createRun();
                    runAfter.setColor("7F7A7F");
                    runAfter.setFontFamily("Tahoma");
                    runAfter.setText(afterColon);
                    runAfter.setFontSize(10);
                    runAfter.setBold(false);
                } else {
                    // Se não houver ":", exibir o texto normalmente
                    XWPFRun run = paragraph.createRun();
                    run.setColor("7F7A7F");
                    run.setFontFamily("Tahoma");
                    run.setText(cellText);
                    run.setFontSize(10);
                    run.setBold(false);
                }
            }
        }

        // Adicionar espaçamento após a tabela
        XWPFParagraph spacingParagraph = document.createParagraph();
        spacingParagraph.setSpacingBefore(200);
    }

    /**
     * Adiciona as rotinas selecionadas ao documento
     */
    private void addRoutines(XWPFDocument document, List<DocumentoRotina> rotinas) {
        // Ordenar as rotinas pela ordem definida
        rotinas.sort(Comparator.comparing(DocumentoRotina::getOrdem));

        for (DocumentoRotina docRotina : rotinas) {
            Rotina rotina = docRotina.getRotina();

            // Criar título da rotina
            XWPFParagraph routineTitle = document.createParagraph();
            routineTitle.setAlignment(ParagraphAlignment.LEFT);
            routineTitle.setSpacingBefore(200);
            routineTitle.setSpacingAfter(100);

            XWPFRun titleRun = routineTitle.createRun();
            titleRun.setFontFamily("Tahoma");
            titleRun.setText(rotina.getCodigo() + " - " + rotina.getNome());
            titleRun.setBold(true);
            titleRun.setFontSize(12);
            titleRun.setColor("434343");

            // Criar descrição da rotina
            XWPFParagraph routineDesc = document.createParagraph();
            routineDesc.setAlignment(ParagraphAlignment.BOTH);
            routineDesc.setSpacingAfter(200);

            XWPFRun descRun = routineDesc.createRun();
            descRun.setFontFamily("Tahoma");
            descRun.setText(rotina.getDescricao());
            descRun.setFontSize(10);
            descRun.setColor("7F7A7F");
        }
    }
}
package com.project.gera41.services;

import com.project.gera41.models.Documento;
import com.project.gera41.models.DocumentoRotina;
import com.project.gera41.models.Rotina;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class WordGeneratorService {

    public File generateDocument(Documento documento, List<DocumentoRotina> documentoRotinas) throws IOException {
        XWPFDocument wordDocument = new XWPFDocument();

        // Texto "Ambientação"
        XWPFParagraph titleParagraph = wordDocument.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.LEFT);
        titleParagraph.setSpacingAfter(250); // Espaçamento após o título (em twips; 200 = ~10 pontos)
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setFontFamily("Tahoma");
        titleRun.setText("Ambientação");
        titleRun.setBold(true);
        titleRun.setFontSize(14);
        titleRun.setColor("FEAC0E");

        // Informações Gerais
//        addSectionTitle(wordDocument, "1. Informações Gerais");
//        addTableWithInfo(wordDocument, documento);

        // START HEADER
        LocalDate dataCriacao = documento.getDataCriacao() != null ? documento.getDataCriacao() : LocalDate.now();

        String[][] dados = {
                {"Nome do Cliente:" + documento.getNomeCliente(), "Código do Cliente:" + documento.getCodigoCliente()},
                {"Nome do projeto:" + documento.getNomeProjeto(), "Código do projeto:" + documento.getCodigoProjeto()},
                {"Segmento cliente:" + documento.getSegmentoCliente(), "Unidade TOTVS:" + documento.getUnidadeTotvs()},
                {"Data:" + dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Proposta comercial: "},
                {"Gerente/Coordenador TOTVS:" + documento.getGerenteTotvs(), ""},
                {"Gerente/Coordenador cliente:" + documento.getGerenteCliente(), ""},
        };

        // Criar objeto do cabeçalho
        XWPFTable table = wordDocument.createTable(6, 2);

        // Definir a largura fixa da tabela (em twips; 1 polegada = 1440 twips)
        table.setWidth(9000); // Aproximadamente 6,25 polegadas (5000 + 4000 = 9000 twips)
        table.getCTTbl().getTblPr().getTblW().setType(STTblWidth.DXA); // Unidade em twips
        table.getCTTbl().getTblPr().getTblW().setW(9000); // Largura fixa

        // Definir a largura fixa das colunas (mantendo os valores já definidos: 5000 e 4000 twips)
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
        // Preencher a tabela com os dados
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                XWPFTableCell cell = table.getRow(i).getCell(j); // Obtém a célula da linha i, coluna j
                XWPFParagraph paragraph = cell.getParagraphs().get(0); // Obtém o parágrafo dentro da célula
                paragraph.setAlignment(ParagraphAlignment.LEFT); // Alinhamento à esquerda
                paragraph.setSpacingAfter(0); // Retirando o espaçamento padrão do word (abaixo da linha)

                String cellText = dados[i][j];
                if (cellText.contains(":")) {
                    // Dividir o texto em duas partes: antes e depois do ":"
                    String[] parts = cellText.split(":", 2); // Divide no primeiro ":"
                    String beforeColon = parts[0] + ":"; // Parte antes do ":" (incluindo o ":")
                    String afterColon = parts.length > 1 ? parts[1] : ""; // Parte depois do ":", se existir

                    // Texto antes do ":" em negrito
                    XWPFRun runBefore = paragraph.createRun();
                    runBefore.setColor("434343");
                    runBefore.setFontFamily("Tahoma");
                    runBefore.setText(beforeColon);
                    runBefore.setFontSize(10);
                    runBefore.setBold(true); // Negrito

                    // Texto depois do ":" sem negrito
                    XWPFRun runAfter = paragraph.createRun();
                    runAfter.setColor("7F7A7F");
                    runAfter.setFontFamily("Tahoma");
                    runAfter.setText(afterColon);
                    runAfter.setFontSize(10);
                    runAfter.setBold(false); // Sem negrito
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
        XWPFParagraph spacingParagraph = wordDocument.createParagraph();
        spacingParagraph.setSpacingBefore(200); // Espaçamento antes do parágrafo (em twips; 200 = ~10 pontos)

        // Definir a cor da borda da tabela (laranja "FFA500", como já definido)
        table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda horizontal interna
        table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda vertical interna
        table.getCTTbl().getTblPr().getTblBorders().getTop().setColor("FFA500"); // Borda superior
        table.getCTTbl().getTblPr().getTblBorders().getBottom().setColor("FFA500"); // Borda inferior
        table.getCTTbl().getTblPr().getTblBorders().getLeft().setColor("FFA500"); // Borda esquerda
        table.getCTTbl().getTblPr().getTblBorders().getRight().setColor("FFA500"); // Borda direita
        // END HEADER
        
        // Rotinas
        if (!documentoRotinas.isEmpty()) {
            addSectionTitle(wordDocument, "2. Rotinas");
            addRotinas(wordDocument, documentoRotinas);
        }

        // Salvar o arquivo
        String fileName = "temp_" + documento.getId() + ".docx";
        File file = new File(fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            wordDocument.write(out);
        }
        return file;
    }

    private void addTitle(XWPFDocument document, String title) {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText(title);
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.setFontFamily("Arial");
        addEmptyLine(document, 1);
    }

    private void addSectionTitle(XWPFDocument document, String title) {
        XWPFParagraph sectionParagraph = document.createParagraph();
        XWPFRun sectionRun = sectionParagraph.createRun();
        sectionRun.setText(title);
        sectionRun.setBold(true);
        sectionRun.setFontSize(14);
        sectionRun.setFontFamily("Arial");
        addEmptyLine(document, 1);
    }

    private void addTableWithInfo(XWPFDocument document, Documento documento) {
        XWPFTable table = document.createTable(10, 2);
        table.setWidth("100%");

        // Estilo da tabela
        CTTblPr tblPr = table.getCTTbl().addNewTblPr();
        CTTblWidth tblWidth = tblPr.addNewTblW();
        tblWidth.setW(BigInteger.valueOf(5000));
        tblWidth.setType(STTblWidth.PCT);

        // Dados da tabela
        LocalDate dataCriacao = documento.getDataCriacao() != null ? documento.getDataCriacao() : LocalDate.now();
        setTableCell(table, 0, 0, "Data de Criação:");
        setTableCell(table, 0, 1, dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        setTableCell(table, 1, 0, "Nome do Cliente:");
        setTableCell(table, 1, 1, documento.getNomeCliente() != null ? documento.getNomeCliente() : "-");
        setTableCell(table, 2, 0, "Código do Cliente:");
        setTableCell(table, 2, 1, documento.getCodigoCliente() != null ? documento.getCodigoCliente() : "-");
        setTableCell(table, 3, 0, "Nome do Projeto:");
        setTableCell(table, 3, 1, documento.getNomeProjeto() != null ? documento.getNomeProjeto() : "-");
        setTableCell(table, 4, 0, "Código do Projeto:");
        setTableCell(table, 4, 1, documento.getCodigoProjeto() != null ? documento.getCodigoProjeto() : "-");
        setTableCell(table, 5, 0, "Segmento do Cliente:");
        setTableCell(table, 5, 1, documento.getSegmentoCliente() != null ? documento.getSegmentoCliente() : "-");
        setTableCell(table, 6, 0, "Unidade TOTVS:");
        setTableCell(table, 6, 1, documento.getUnidadeTotvs() != null ? documento.getUnidadeTotvs() : "-");
        setTableCell(table, 7, 0, "Proposta Comercial:");
        setTableCell(table, 7, 1, documento.getPropostaComercial() != null ? documento.getPropostaComercial() : "-");
        setTableCell(table, 8, 0, "Gerente TOTVS:");
        setTableCell(table, 8, 1, documento.getGerenteTotvs() != null ? documento.getGerenteTotvs() : "-");
        setTableCell(table, 9, 0, "Gerente Cliente:");
        setTableCell(table, 9, 1, documento.getGerenteCliente() != null ? documento.getGerenteCliente() : "-");

        addEmptyLine(document, 1);
    }

    private void setTableCell(XWPFTable table, int row, int col, String text) {
        XWPFTableCell cell = table.getRow(row).getCell(col);
        XWPFParagraph paragraph;
        List<XWPFParagraph> paragraphs = cell.getParagraphs();
        if (paragraphs.isEmpty()) {
            paragraph = cell.addParagraph();
        } else {
            paragraph = paragraphs.get(0); // Usar get(0) em vez de getFirst()
        }
        XWPFRun run = paragraph.createRun();
        run.setText(text);
        run.setFontFamily("Arial");
        run.setFontSize(12);
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private void addRotinas(XWPFDocument document, List<DocumentoRotina> documentoRotinas) {
        // Ordenar rotinas por ordem
        documentoRotinas.sort(Comparator.comparing(DocumentoRotina::getOrdem));

        for (DocumentoRotina docRotina : documentoRotinas) {
            Rotina rotina = docRotina.getRotina();
            XWPFParagraph rotinaParagraph = document.createParagraph();
            XWPFRun rotinaRun = rotinaParagraph.createRun();
            rotinaRun.setText("Rotina: " + rotina.getCodigo() + " - " + rotina.getNome());
            rotinaRun.setFontFamily("Arial");
            rotinaRun.setFontSize(12);
            rotinaRun.setBold(true);

            // Descrição da Rotina
            XWPFParagraph descParagraph = document.createParagraph();
            XWPFRun descRun = descParagraph.createRun();
            descRun.setText(rotina.getDescricao() != null ? rotina.getDescricao() : "Sem descrição");
            descRun.setFontFamily("Arial");
            descRun.setFontSize(12);

            addEmptyLine(document, 1);
        }
    }

    private void addEmptyLine(XWPFDocument document, int number) {
        for (int i = 0; i < number; i++) {
            XWPFParagraph empty = document.createParagraph();
            empty.createRun().setText("");
        }
    }
}
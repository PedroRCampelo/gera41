package com.project.gera41.services;

import com.project.gera41.models.Documento;
import com.project.gera41.models.DocumentoRotina;
import com.project.gera41.models.Rotina;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
public class WordGeneratorService_bkp {

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
        // addSectionTitle(wordDocument, "1. Informações Gerais");
        // addTableWithInfo(wordDocument, documento);

        // START HEADER
        LocalDate dataCriacao = documento.getDataCriacao() != null ? documento.getDataCriacao() : LocalDate.now();
        String[][] dados = {
                {"Nome do Cliente: " + documento.getNomeCliente(), "Código do Cliente: " + documento.getCodigoCliente()},
                {"Nome do projeto: " + documento.getNomeProjeto(), "Código do projeto: " + documento.getCodigoProjeto()},
                {"Segmento cliente: " + documento.getSegmentoCliente(), "Unidade TOTVS: " + documento.getUnidadeTotvs()},
                {"Data: " + dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Proposta comercial: " + documento.getPropostaComercial()},
                {"Gerente/Coordenador TOTVS: " + documento.getGerenteTotvs(), ""},
                {"Gerente/Coordenador cliente: " + documento.getGerenteCliente(), ""},
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
                CTTcPr tcPr = cell.getCTTc().addNewTcPr();
                CTTcMar tcMar = tcPr.addNewTcMar();
                tcMar.addNewLeft().setW(60); // Margem esquerda
                tcMar.addNewRight().setW(60); // Margem direita
                tcMar.addNewTop().setW(60); // Margem superior
                tcMar.addNewBottom().setW(60); // Margem inferior
            }
        }
        
        // Ajustes da tabela
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
        
        if (!documentoRotinas.isEmpty()) {
            addSectionTitle(wordDocument, "1. Processo: Faturamento"); // Incluir variável de módulo da MIT041
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

    // START ROTINAS    
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

    private void addRotinas(XWPFDocument document, List<DocumentoRotina> documentoRotinas) {
        // Ordenar rotinas por ordem
        documentoRotinas.sort(Comparator.comparing(DocumentoRotina::getOrdem));
        int i = 1;
        for (DocumentoRotina docRotina : documentoRotinas) {
            Rotina rotina = docRotina.getRotina();

            XWPFParagraph rotinaParagraph = document.createParagraph();
            XWPFRun rotinaRun = rotinaParagraph.createRun();
            rotinaRun.setText(i + ". " + rotina.getNome());
            i++;
            rotinaRun.setFontFamily("Tahoma");
            rotinaRun.setFontSize(14);
            rotinaRun.setColor("FEAC0E");
            rotinaRun.setBold(true);

            // Descrição da Rotina
            XWPFParagraph descParagraph = document.createParagraph();
            XWPFRun descRun = descParagraph.createRun();
            descRun.setText(rotina.getDescricao() != null ? rotina.getDescricao() : "Sem descrição");
            descRun.setFontFamily("Tahoma");
            descRun.setFontSize(10);

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
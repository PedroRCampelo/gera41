package com.project.gera41.services;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordGeneratorService {
    public static void main(String[] args) {
        // Caminho onde o documento será salvo
        String outputFilePath = "documentacao_teste.docx";

        try (XWPFDocument document = new XWPFDocument()) {
            // Título "Ambientação"
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.LEFT);
            titleParagraph.setSpacingAfter(250); // Espaçamento após o título (em twips; 200 = ~10 pontos)
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setFontFamily("Tahoma");
            titleRun.setText("Ambientação");
            titleRun.setBold(true);
            titleRun.setFontSize(14);
            titleRun.setColor("FEAC0E");

            // START HEADER
            String[][] dados = {
                    {"Nome do Cliente: EMPRESA TESTE CRIADA PARA TESTE COM TAMANHO REAL", "Código do Cliente: T2312T"},
                    {"Nome do projeto: EMPRESA TESTE - CRIADA PARA TESTE - TAMANHO", "Código do projeto: D000054821001"},
                    {"Segmento cliente: Manufatura", "Unidade TOTVS: Recife - PE"},
                    {"Data: 07/05/2024", "Proposta comercial: "},
                    {"Gerente/Coordenador TOTVS: Joao Campelo", ""},
                    {"Gerente/Coordenador cliente:", ""}
            };

            // Criar a tabela
            XWPFTable table = document.createTable(6, 2);

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

            // Definir a cor da borda da tabela (laranja "FFA500", como já definido)
            table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda horizontal interna
            table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "FFA500"); // Borda vertical interna
            table.getCTTbl().getTblPr().getTblBorders().getTop().setColor("FFA500"); // Borda superior
            table.getCTTbl().getTblPr().getTblBorders().getBottom().setColor("FFA500"); // Borda inferior
            table.getCTTbl().getTblPr().getTblBorders().getLeft().setColor("FFA500"); // Borda esquerda
            table.getCTTbl().getTblPr().getTblBorders().getRight().setColor("FFA500"); // Borda direita

            // Preencher a tabela com os dados
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 2; j++) {
                    XWPFTableCell cell = table.getRow(i).getCell(j); // Obtém a célula da linha i, coluna j
                    XWPFParagraph paragraph = cell.getParagraphs().getFirst(); // Obtém o parágrafo dentro da célula
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
            XWPFParagraph spacingParagraph = document.createParagraph();
            spacingParagraph.setSpacingBefore(200); // Espaçamento antes do parágrafo (em twips; 200 = ~10 pontos)
            // END HEADER


            // Salvar o documento
            try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
                document.write(out);
            }

            System.out.println("Documento gerado com sucesso em: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Erro ao gerar o documento: " + e.getMessage());
        }
    }
}
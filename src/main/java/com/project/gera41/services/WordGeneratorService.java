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

        // Título do Documento
        addTitle(wordDocument, "Especificação Técnica - " + documento.getNomeProjeto());

        // Informações Gerais
        addSectionTitle(wordDocument, "1. Informações Gerais");
        addTableWithInfo(wordDocument, documento);

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
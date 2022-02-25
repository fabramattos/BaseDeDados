package com.pml.bancodedados.excel;

import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import java.sql.Timestamp;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelEscrevePlanilhaCandle implements AcoesExcel{

    // ARQUIVOS PARA GRAVAÇÃO
    static XSSFWorkbook workbookGravacao;
    static XSSFSheet sheetGravacao;
    static CellStyle styleNumerico, styleDataCompleta;

    public void executa() {
        final String nomePlanilha = "Rel. Candles";
        workbookGravacao = new XSSFWorkbook();
        sheetGravacao = workbookGravacao.createSheet(nomePlanilha);
        
        Main.addTexto("\n\nPreparando planilha: " + nomePlanilha);
        // TITULO RELATORIO CANDLES
        int linha = 0;
        int coluna = 0;
        Row row = sheetGravacao.createRow(linha++);
        Cell cell = row.createCell(coluna++);
        cell.setCellValue("Data");
        cell = row.createCell(coluna++);
        cell.setCellValue("Abertura");
        cell = row.createCell(coluna++);
        cell.setCellValue("Maxima");
        cell = row.createCell(coluna++);
        cell.setCellValue("Minima");
        cell = row.createCell(coluna++);
        cell.setCellValue("Fechamento");
        cell = row.createCell(coluna++);
        cell.setCellValue("Indicador");

        
        Main.progressoAtualiza(Candle.listaCandleMinuto.size(), linha);
        for(Candle candle : Candle.listaCandleMinuto){
            row = sheetGravacao.createRow(linha++);
            coluna = 0;
            cell = row.createCell(coluna++);
            cell.setCellValue(Timestamp.valueOf(candle.getData())); cell.setCellStyle(styleDataCompleta);
            cell = row.createCell(coluna++);
            cell.setCellValue(candle.getAbertura()); cell.setCellStyle(styleNumerico);
            cell = row.createCell(coluna++);
            cell.setCellValue(candle.getMaxima()); cell.setCellStyle(styleNumerico);
            cell = row.createCell(coluna++);
            cell.setCellValue(candle.getMinima()); cell.setCellStyle(styleNumerico);
            cell = row.createCell(coluna++);
            cell.setCellValue(candle.getFechamento()); cell.setCellStyle(styleNumerico);
            cell = row.createCell(coluna++);
            cell.setCellValue(candle.getIndicadorExtra()); cell.setCellStyle(styleNumerico);
            cell = row.createCell(coluna++);
            Main.progressoAtualiza(linha);
        }
    }

    private void configuraFormatacao(HSSFWorkbook workbookGravacao) {
        CreationHelper createHelper = workbookGravacao.getCreationHelper();
        styleNumerico = workbookGravacao.createCellStyle();
        styleNumerico.setDataFormat(createHelper.createDataFormat().getFormat("0.00"));

        styleDataCompleta = workbookGravacao.createCellStyle();
        styleDataCompleta.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy HH:MM"));
    }
}

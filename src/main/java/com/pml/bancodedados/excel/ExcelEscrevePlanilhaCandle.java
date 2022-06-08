package com.pml.bancodedados.excel;

import com.pml.bancodedados.ArquivosTemporarios.ArquivoTemp;
import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import java.sql.Timestamp;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExcelEscrevePlanilhaCandle implements AcoesExcel{

    // ARQUIVOS PARA GRAVAÇÃO
    static SXSSFWorkbook workbookGravacao;
    static SXSSFSheet sheetGravacao;
    static CellStyle styleNumerico, styleDataCompleta;
    private final String NOME_PLANILHA = "Rel. Candles";


    public void executa() {
        workbookGravacao = new SXSSFWorkbook();
        sheetGravacao = workbookGravacao.createSheet(NOME_PLANILHA);
        preparaCabecalho();
        
        File file = (ArquivoTemp.getArquivoTemp()); 
        if(file == null)
            return;
        
        try(FileInputStream is = new FileInputStream(file)){
            ObjectInputStream ois = new ObjectInputStream(is);
            Candle candle = null;
            int linha = 1;
            while((candle = (Candle)ois.readObject()) != null){
                geraExcel(candle);
            }
        }catch (Exception ex) {
            System.out.println(ex.toString());
            Main.addTexto(ex.toString());
        }
    }
        

    private void geraExcel(Candle candle) {
        Row row = sheetGravacao.createRow(sheetGravacao.getLastRowNum() + 1);
        int coluna = 0;
        Cell cell = row.createCell(coluna++);
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
        Main.progressoIncrementa();
    }

    
    private void configuraFormatacao() {
        CreationHelper createHelper = workbookGravacao.getCreationHelper();
        styleNumerico = workbookGravacao.createCellStyle();
        styleNumerico.setDataFormat(createHelper.createDataFormat().getFormat("0.00"));

        styleDataCompleta = workbookGravacao.createCellStyle();
        styleDataCompleta.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy HH:MM"));
    }
    
    private void preparaCabecalho() {
        Main.addTexto("\n\nPreparando planilha: " + NOME_PLANILHA);
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
        
        configuraFormatacao();
    }
}

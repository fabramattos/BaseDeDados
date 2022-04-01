package com.pml.bancodedados.excel;

import com.pml.bancodedados.ArquivosTemporarios.ArquivoTemp;
import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import java.sql.Timestamp;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelEscrevePlanilhaCandle implements AcoesExcel{

    // ARQUIVOS PARA GRAVAÇÃO
    static XSSFWorkbook workbookGravacao;
    static XSSFSheet sheetGravacao;
    static CellStyle styleNumerico, styleDataCompleta;
    private String NOME_PLANILHA = "Rel. Candles";
    private boolean criouCabecalho = false;


    public void executa() {
        workbookGravacao = new XSSFWorkbook();
        sheetGravacao = workbookGravacao.createSheet(NOME_PLANILHA);
        
        File file = (ArquivoTemp.getArquivoTemp()); 
        if(file == null)
            return;
        
        try(InputStream is = new FileInputStream(file)){
            while(is.available() != 0){
                ObjectInputStream ois = new ObjectInputStream(is);
                List<Candle> listaCandle = (List<Candle>) ois.readObject();
                geraExcel(sheetGravacao, listaCandle);
            }
        }catch (IOException | ClassNotFoundException ex) {}
    }
        

    private void geraExcel(XSSFSheet sheetGravacao, List<Candle> listaCandle) {
        preparaCabecalho(sheetGravacao, listaCandle);
        
        int linha = sheetGravacao.getLastRowNum() + 1;
        int coluna = 0;
        Row row;
        Cell cell;
        Main.progressoAtualiza(listaCandle.size(), linha);
        for(Candle candle : listaCandle){
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
    
    private void preparaCabecalho(XSSFSheet sheet, List<Candle> listaDiario) {
        if(criouCabecalho)
            return;
        
        Main.addTexto("\n\nPreparando planilha: " + sheetGravacao.getSheetName());
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
        criouCabecalho = true;
    }
}

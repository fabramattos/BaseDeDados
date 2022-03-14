package com.pml.bancodedados.excel;

import com.monitorjbl.xlsx.StreamingReader;
import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelAbreArquivo implements AcoesExcel{

    public void executa(AcoesExcel classeAcao) throws NullPointerException {
        String origem = escolheOrigem();
        Main.addTexto("\nAbrindo arquivo: " + origem);
        Main.progressoIndeterminado();
        int linha = 0; //para erros
        try (FileInputStream is = new FileInputStream(new File(origem));
        Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            atualizaProgressBarComMaximoDeCelulas(origem);
            
            int celula = 0; //para barra de progresso
            for (Row row : sheet) {
                linha = row.getRowNum();
                Candle candle = new Candle();
                LocalDateTime data;
                String indicador = new String();
                for (Cell cell : row) {
                    if(cell.getStringCellValue() == null || cell.getStringCellValue() == ""
                    || cell.getStringCellValue().isEmpty() || cell.getStringCellValue().isEmpty()){
                        Main.progressoAtualiza(celula++);
                        continue;
                    }
                    if (row.getRowNum() == 0 && cell.getColumnIndex() == 5)
                        indicador = cell.getStringCellValue();
                    if (row.getRowNum() > 0) {
                        switch (cell.getColumnIndex()) {
                            case 0:
                                data = new Timestamp(cell.getDateCellValue().getTime()).toLocalDateTime();
                                candle.setData(data);
                                break;
                            case 1:
                                candle.setAbertura(cell.getNumericCellValue());
                                break;
                            case 2:
                                candle.setMaxima(cell.getNumericCellValue());
                                break;
                            case 3:
                                candle.setMinima(cell.getNumericCellValue());
                                break;

                            case 4:
                                candle.setFechamento(cell.getNumericCellValue());
                                break;

                            case 5:
                                candle.setIndicadorExtra(cell.getNumericCellValue());
                                candle.setIndicador(indicador);
                                break;
                        }
                    }
                    Main.progressoAtualiza(celula++);
                }// fim da leitura da celula
                gravaCandle(classeAcao, candle);
            }// fim da leitura da linha
        } catch(NullPointerException e) {
            e.printStackTrace();
            Main.addTexto("\nAbertura cancelada");
        } catch(FileNotFoundException e){
            Main.addTexto("\nArquivo Excel não encontrado!");
        } catch(NumberFormatException e){
            Main.addTexto("\nErro na linha: " + linha);
        } catch (IOException ex) {
            System.out.println("eita porra");
            
        }
        finally{
            Main.progressoConcluido();
        }
    }

    private void gravaCandle(AcoesExcel classeAcao, Candle candle) {
        if(candle.getData() == null)
            return;

        if(classeAcao instanceof ExcelPerguntaMinuto)
            Candle.hashCandleMinuto.add(candle);
        else
            Candle.hashCandleDiario.add(candle);
    }

    private String escolheOrigem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel '.xlsx'", "xlsx"));
        int result = fileChooser.showOpenDialog(null);
        String origem = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            origem = selectedFile.getAbsolutePath();
        } else {
            JOptionPane.showMessageDialog(null, "Operação abortada!");
        }
        return origem;
    }

    private void atualizaProgressBarComMaximoDeCelulas(String origemArquivo) throws FileNotFoundException, IOException {
       try (FileInputStream is = new FileInputStream(new File(origemArquivo));
        Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4096)
                    .open(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for(Row row : sheet)
                for(Cell cell : row)
                    i++;
            Main.progressoAtualizaMax(i);
        }
    }
}

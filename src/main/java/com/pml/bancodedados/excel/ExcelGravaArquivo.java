package com.pml.bancodedados.excel;

import static com.pml.bancodedados.excel.ExcelEscrevePlanilhaCandle.workbookGravacao;
import com.pml.bancodedados.interfaceGrafica.Main;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelGravaArquivo implements AcoesExcel{

    public void executa() {
        Main.progressoIndeterminado();
        String destino = escolheDestino();
        Main.addTexto("\n\nGravando o arquivo em: " + destino);
        
        try{
            FileOutputStream fos = new FileOutputStream(new File(destino));
            workbookGravacao.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Main.addTexto("\nArquivo Excel não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
            Main.addTexto("\nErro na edição do arquivo!");
        } catch(NullPointerException e) {
            e.printStackTrace();
            Main.addTexto("\nGravação abortada!");
        } finally{
            Main.progressoConcluido();
            Main.addTexto("\nGravação concluída com sucesso\n");
        }
    }

    private String escolheDestino() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel ''.xlsx' ", "xlsx");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showSaveDialog(null);
        String destino = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            destino = selectedFile.getAbsolutePath() + ".xlsx";
        } else {
            JOptionPane.showMessageDialog(null, "Operação abortada!");
        }
        return destino;
    }

}



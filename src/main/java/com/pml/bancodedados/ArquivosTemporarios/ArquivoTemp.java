/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pml.bancodedados.ArquivosTemporarios;

import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabra
 */
public class ArquivoTemp {
    
    private static final String FILENAME = "BancoDeDados";
    private static Path tmpFile;
    
    public static void gravaArqTemp(List<Candle> listaResumo){
        if(tmpFile != null){
            Main.addTexto("Deletando arquivo temporário\n");
            tmpFile.toFile().delete();
        }
        
        Main.addTexto("Criando arquivo temporário\n");
        try {
            tmpFile = Files.createTempFile(FILENAME, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try (FileOutputStream fos = new FileOutputStream(tmpFile.toFile(), true)){
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Main.addTexto("Escrevendo dados no arquivo temporário\n");
            for(Candle candle : listaResumo){
                oos.writeObject(candle);
            }
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(ArquivoTemp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static File getArquivoTemp(){
        return tmpFile.toFile();
    }
}

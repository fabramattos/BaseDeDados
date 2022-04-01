/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pml.bancodedados.ArquivosTemporarios;

import com.pml.bancodedados.candle.Candle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabra
 */
public class ArquivoTemp {
    
    private static final String FILENAME = "BancoDeDados.temp";
    private static File newTempDir = new File(System.getProperty("java.io.tmpdir"), "BancoDeDados");
    private static final String NEWFILE = newTempDir.getAbsolutePath() + File.separator + FILENAME;
    
    public static void gravaArqTemp(List<Candle> listaResumo){
        newTempDir.mkdir();
        newTempDir.deleteOnExit();
        //System.out.println("Pasta: " + newTempDir.toPath());
        
        File file = new File(NEWFILE);
        file.deleteOnExit();
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listaResumo);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(ArquivoTemp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static File getArquivoTemp(){
        return new File(NEWFILE);
    }
    
    public static void apagaArquivosTemp(){
        File file = new File(NEWFILE);
        if(file.exists()){
            file.delete();
            System.out.println("arquivo apagado");
        }
    }
    
}

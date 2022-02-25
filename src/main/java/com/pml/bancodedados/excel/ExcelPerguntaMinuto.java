package com.pml.bancodedados.excel;


import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;

import javax.swing.*;

/**
 * @author Felipe Mattos.
 *
 */
public class ExcelPerguntaMinuto implements AcoesExcel{

    public void executa() {
        Main.progressoIndeterminado();
        Candle.hashCandleMinuto.clear();
        new JOptionPane().showMessageDialog(null, "Selecione o arquivo para MINUTO");
        int escolha;
        ExcelAbreArquivo excel = new ExcelAbreArquivo();
        do{
            excel.executa(this);
            Main.progressoAtualiza(1, 1);
            escolha = JOptionPane.showConfirmDialog(null,
                    "Abrir mais arquivos MINUTO?",
                    "Pergunta:",
                    JOptionPane.YES_NO_OPTION,
                    0,
                    null);
        } while(escolha == JOptionPane.YES_OPTION);
        Main.addTexto("\nBanco de dados MINUTO carregado com sucesso.");
        Main.progressoAtualiza(1, 1);
    }
}



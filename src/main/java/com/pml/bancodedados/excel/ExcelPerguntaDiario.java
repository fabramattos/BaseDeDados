package com.pml.bancodedados.excel;

import com.pml.bancodedados.candle.Candle;
import com.pml.bancodedados.interfaceGrafica.Main;

import javax.swing.*;

public class ExcelPerguntaDiario implements AcoesExcel{

    public void executa() {
        Main.progressoIndeterminado();
        Candle.hashCandleDiario.clear();
        JOptionPane.showMessageDialog(null, "selecione banco de dados DIARIO!");
        int escolha;
        do{
            new ExcelAbreArquivo().executa(this);
            escolha = JOptionPane.showConfirmDialog(null,
                    "Abrir mais arquivos DIARIO?",
                    "Pergunta:",
                    JOptionPane.YES_NO_OPTION,
                    0,
                    null);
        } while(escolha == JOptionPane.YES_OPTION);
        Main.addTexto("\nBanco de dados DIARIO carregado com sucesso.");
        Main.progressoAtualiza(1, 1);
    }
}

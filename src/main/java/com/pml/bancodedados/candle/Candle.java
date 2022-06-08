package com.pml.bancodedados.candle;

import com.pml.bancodedados.ArquivosTemporarios.ArquivoTemp;
import com.pml.bancodedados.excel.ExcelEscrevePlanilhaCandle;
import com.pml.bancodedados.excel.ExcelGravaArquivo;
import com.pml.bancodedados.excel.ExcelPerguntaDiario;
import com.pml.bancodedados.excel.ExcelPerguntaMinuto;
import com.pml.bancodedados.interfaceGrafica.Main;
import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * @author Felipe Mattos
 */
public class Candle implements Comparable<Candle>, Serializable{

    // Atributos
    public static HashSet<Candle> hashCandleMinuto = new HashSet<>();
    public static HashSet<Candle> hashCandleDiario = new HashSet<>();
    public static List<Candle> listaCandleMinuto = new ArrayList<>();
    private static List<Candle> listaCandleDiario = new ArrayList<>();
    private LocalDateTime data;
    private double abertura, maxima, minima, fechamento, indicadorExtra;
    private String indicador;

    // Métodos Especiais


    public Candle(){
    }

    public Candle(Candle candle) {
        this.data = candle.getData();
        this.abertura = candle.getAbertura();
        this.maxima = candle.getMaxima();
        this.minima = candle.getMinima();
        this.fechamento = candle.getFechamento();
        this.indicador = candle.getIndicador();
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getIndicador()    {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getIndicadorExtra() {
        return indicadorExtra;
    }

    public void setIndicadorExtra(double indicadorExtra) {
        this.indicadorExtra = indicadorExtra;
    }

    public double getAbertura() {
        return abertura;
    }

    public void setAbertura(double abertura) {
        this.abertura = abertura;
    }

    public double getMaxima() {
        return maxima;
    }

    public void setMaxima(double maxima) {
        this.maxima = maxima;
    }

    public double getMinima() {
        return minima;
    }

    public void setMinima(double minima) {
        this.minima = minima;
    }

    public double getFechamento() {
        return fechamento;
    }

    public void setFechamento(double fechamento) {
        this.fechamento = fechamento;
    }

    /*
    @Override
    public String toString() {
        return "CANDLE: Data: " + data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear()
                + " | " + data.getHour() + ":" + data.getMinute()
                + " | abertura = " + abertura
                + " | maxima = " + maxima
                + " | minima = " + minima
                + " | fechamento = " + fechamento
                + " | indicador extra = " + indicadorExtra;
    }


     */
    @Override // Ordenação Natural
    public int compareTo(Candle c) {
        return data.compareTo(c.getData());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candle candle = (Candle) o;
        return data.equals(candle.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    private void geraCandleMinuto(){
        Main.addTexto("\nGerando banco de dados\n");
        listaCandleMinuto.clear();
        listaCandleMinuto.addAll(hashCandleMinuto);
        Main.addTexto("Ordenando candle minuto\n");
        listaCandleMinuto.sort(Comparator.comparing(Candle::getData));
    }

    private void geraCandlesIndicadores(){
        geraCandleMinuto();
        listaCandleDiario.clear();
        listaCandleDiario.addAll(hashCandleDiario);
        Main.addTexto("Ordenando candle diário\n");
        listaCandleDiario.sort(Comparator.comparing((Candle::getData)));
        
        Main.addTexto("\nAplicando indicador na base de dados\n");
        boolean tag = false;
        int i = 1;
        int k = 0;
        Main.progressoAtualiza(listaCandleMinuto.size(), i);
        for(Candle candleMin : listaCandleMinuto){
            for(int j = k; j < listaCandleDiario.size(); j++){
                if(verificaDia(candleMin, listaCandleDiario.get(j))){
                    candleMin.setIndicadorExtra(listaCandleDiario.get(j).getIndicadorExtra());
                    candleMin.setIndicador(listaCandleDiario.get(j).getIndicador());
                    k = j;
                    tag = true;
                    break;
                }
            }
            Main.progressoAtualiza(listaCandleMinuto.size(), i);
        }
        if(!tag)
            Main.addTexto("Não encontrou dados em comum! Abortando\n");
    }

    private boolean verificaDia(Candle candleMin, Candle candleDia) {
        return candleDia.getData().getYear() == candleMin.getData().getYear()
                && candleDia.getData().getDayOfYear() == candleMin.getData().getDayOfYear();
    }

    public void coletaDados_Minuto() {
        Main.apagaTexto();
        Main.addTexto("==================COLETANDO DADOS MINUTO=======================");
        new ExcelPerguntaMinuto().executa();
        geraCandleMinuto();
        
        geraArqTemp();
        
        new ExcelEscrevePlanilhaCandle().executa();
        new ExcelGravaArquivo().executa();
        Main.addTexto("\n==================FIM DAS OPERAÇÕES=======================");
    }
    
    public void coletaDados_IndicadorDiario(){
        Main.apagaTexto();
        Main.addTexto("==================COLETANDO DADOS MINUTO=======================");
        new ExcelPerguntaMinuto().executa();
        
        Main.addTexto("\n\n==================COLETANDO DADOS DIARIO=======================");
        new ExcelPerguntaDiario().executa();
        geraCandlesIndicadores();
        
        geraArqTemp();
        
        new ExcelEscrevePlanilhaCandle().executa();
        new ExcelGravaArquivo().executa();
        Main.addTexto("\n==================FIM DAS OPERAÇÕES=======================");
    }

    private void geraArqTemp() {
        ArquivoTemp.gravaArqTemp(listaCandleMinuto);
    }

}

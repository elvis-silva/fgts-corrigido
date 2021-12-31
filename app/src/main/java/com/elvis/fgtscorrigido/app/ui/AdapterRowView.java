package com.elvis.fgtscorrigido.app.ui;

/**
 * Created by elvis on 16/06/16.
 */

public class AdapterRowView {
    private int drawableRes;
    private String name, year, deposito, indiceTR, indiceINPC, correcaoTR, correcaoINPC, totalPerdas,
            appName, packageName;
    private TYPE type;

    public static enum TYPE {
        HEADER,
        INSERT,
        DETAIL
    }

    /**
     * Type HEADER
     * @param pName = Mês
     */
    public AdapterRowView(String pName) {
        type = TYPE.HEADER;
        name = pName;
        deposito = "";
    }

    /**
     * Type INSERT
     * @param pName = Mês
     * @param pDeposito = Valor depositado no mês
     */
    public AdapterRowView(String pName, String pDeposito) {
        type = TYPE.INSERT;
        name = pName;
        deposito = pDeposito;
    }

    /**
     * Type DETAIL
     * @param pName = Mês
     * @param pYear = Ano
     * @param pDeposito = Valor do depósito no mês
     * @param pIndiceTR = Índice TR do mês
     * @param pIndiceINPC = Índice INPC do mês
     * @param pCorrecaoTR = Valor do depósito atualizado pelo índice TR
     * @param pCorrecaoINPC = Valor do depósito atualizado pelo índice INPC
     * @param pTotalPerdas = Valor do valor atualizado pelo INPC subtraído o valor atualizado pela TR
     */
    public AdapterRowView(String pName, String pYear, String pDeposito, String pIndiceTR, String pIndiceINPC,
                          String pCorrecaoTR, String pCorrecaoINPC, String pTotalPerdas) {
        type = TYPE.DETAIL;
        name = pName;
        year = pYear;
        deposito = pDeposito;
        indiceTR = pIndiceTR;
        indiceINPC = pIndiceINPC;
        correcaoTR = pCorrecaoTR;
        correcaoINPC = pCorrecaoINPC;
        totalPerdas = pTotalPerdas;
    }

    public AdapterRowView(int pDrawableRes, String pAppName, String pPackageName) {
        drawableRes = pDrawableRes;
        appName = pAppName;
        packageName = pPackageName;
    }

    // getter
    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getDeposito() {
        return deposito;
    }

    public TYPE getType() {
        return type;
    }

    public String getIndiceTR() {
        return indiceTR;
    }

    public String getIndiceINPC() {
        return indiceINPC;
    }

    public String getCorrectTrValue() {
        return correcaoTR;
    }

    public String getCorrectInpcValue() {
        return correcaoINPC;
    }

    public String getTotalPerdas() {
        return totalPerdas;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    // setter
    public void setName(String pName) {
        name = pName;
    }

    public void setYear(String pYear) {
        year = pYear;
    }

    public void setDeposito(String pDeposito) {
        deposito = pDeposito;
    }

    public void setIndiceTR(String pIndiceTR) {
        indiceTR = pIndiceTR;
    }

    public void setIndiceINPC(String pIndiceINPC) {
        indiceINPC = pIndiceINPC;
    }

    public void setCorrectTrValue(String pCorrecaoTR) {
        correcaoTR = pCorrecaoTR;
    }

    public void setCorrectInpcValue(String pCorrecaoINPC) {
        correcaoINPC = pCorrecaoINPC;
    }

    public void setTotalPerdas(String pTotalPerdas) {
        totalPerdas = pTotalPerdas;
    }

    public void setAppName(String pAppName) {
        appName = pAppName;
    }

    public void setDrawableRes(int pDrawableRes) {
        drawableRes = pDrawableRes;
    }

    public void setPackageName(String pPackageName) {
        packageName = pPackageName;
    }
}

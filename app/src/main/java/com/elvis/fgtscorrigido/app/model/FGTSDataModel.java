package com.elvis.fgtscorrigido.app.model;

public class FGTSDataModel {
    private int id;
    private String name;
    private String month;
    private String year;
    private String deposito;
    private String tr;
    private String inpc;
    private String diference;

    public FGTSDataModel(){
    }

    public FGTSDataModel(String pName, String pMonth, String pYear, String pDeposito, String pTr, String pInpc, String pDiference) {
        name = pName;
        month = pMonth;
        year = pYear;
        deposito = pDeposito;
        tr = pTr;
        inpc = pInpc;
        diference = pDiference;
    }

    public void setId(int pId) {
        id = pId;
    }

    public void setName(String pName) {
        name = pName;
    }

    public void setMonth(String pMonth){
        month = pMonth;
    }

    public void setYear(String pYear){
        year = pYear;
    }

    public void setDeposito(String pDeposito){
        deposito = pDeposito;
    }

    public void setTr(String pTR){
        tr = pTR;
    }

    public void setInpc(String pINPC) {
        inpc = pINPC;
    }

    public void setDiference(String pDiference){
        diference = pDiference;
    }

    public long getId(){
        return id;
    }

    public String getName () {
        return name;
    }

    public String getMonth(){
        return month;
    }

    public String getYear(){
        return year;
    }

    public String getDeposito(){
        return deposito;
    }

    public String getTr(){
        return tr;
    }

    public String getInpc(){
        return inpc;
    }

    public String getDiference(){
        return diference;
    }
}

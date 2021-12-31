package com.elvis.fgtscorrigido.app.data;

import java.io.Serializable;

public class Detail implements Serializable {

    private String month, year, deposito, tr, inpc, diference;
    private int id;

    public Detail(){}

    public Detail(String pMonth, String pYear, String pDeposito, String pTr, String pInpc, String pDiference) {
        month = pMonth;
        year = pYear;
        deposito = pDeposito;
        tr = pTr;
        inpc = pInpc;
        diference = pDiference;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String pMonth) {
        month = pMonth;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String pYear) {
        year = pYear;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String pDeposito) {
        deposito = pDeposito;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String pTr) {
        tr = pTr;
    }

    public String getInpc() {
        return inpc;
    }

    public void setInpc(String pInpc) {
        inpc = pInpc;
    }

    public String getDiference() {
        return diference;
    }

    public void setDiference(String pDiference) {
        diference = pDiference;
    }

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        id = pId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object pObject) {
        if (this == pObject)
            return true;
        if (pObject == null)
            return false;
        if (getClass() != pObject.getClass())
            return false;
        Detail other = (Detail) pObject;
        return id == other.id;
    }
}

package com.elvis.fgtscorrigido.app.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Calcule {

    public static String firstMonthLabel = "Primeiro mês";
    public static String lastMonthLabel = "Último mês";
    public static String firstYearLabel = "Primeiro ano";
    public static String lastYearLabel = "Último ano";
    public static float firstYearFloat = 0;
    public static float lastYearFloat = 0;
    public static String firstMonth;
    public static String lastMonth;
    public static String firstYear;
    public static String lastYear;

    public static float getTotal (ArrayList<Indice> indicesList, float valueTotal, String indiceType) {
        float total;
        String startMonth = getFirstPeriod();
        String finalMonth = getLastPeriod();
        Indice firstIndice = null;
        Indice lastIndice = null;
        int i = 0;
        while ( i < indicesList.size()) {
            if (indicesList.get(i).type.equals(indiceType) &&
                    indicesList.get(i).name.equals(startMonth) &&
                    firstIndice == null) firstIndice = indicesList.get(i);
            if (indicesList.get(i).type.equals(indiceType) &&
                    indicesList.get(i).name.equals(finalMonth) &&
                    lastIndice == null) lastIndice = indicesList.get(i);
            i++;
        }

        List<Indice> subListIndice = indicesList.subList(indicesList.indexOf(firstIndice), indicesList.indexOf(lastIndice) + 1);
        int j = 0;
        float totalIndice = 0;
        float juros = 0;
        while (j < subListIndice.size()) {
            float indiceValue = Float.parseFloat(subListIndice.get(j).value);
            if (indiceValue <= 0) indiceValue = 0;
            if (j == subListIndice.size() - 1) {
                if (subListIndice.size() >= 2) {
                    totalIndice = totalIndice + (totalIndice * (indiceValue * 0.01f)) + indiceValue;
                } else {
                    totalIndice = indiceValue;
                }
            } else {
                if (j == 0) {
                    totalIndice = indiceValue;
                } else {
                    totalIndice = totalIndice + (totalIndice * (indiceValue * 0.01f)) + indiceValue;
                }
            }
            juros += 0.3f;
            j++;
        }
        total = valueTotal * (1 + (totalIndice * 0.01f)) * (1 + (juros * 0.01f));

        return total;
    }

    public static String getFirstPeriod () {
        return firstMonth + firstYear;
    }

    public static String getLastPeriod () {
        return lastMonth + lastYear;
    }

    public static String moneyFormat (float value) {
        DecimalFormat money = new DecimalFormat("0,000.00");

        String valueToString;
        if (money.format(value).startsWith("0") || money.format(value).startsWith("-0")) {
            money = new DecimalFormat("0.00");
            valueToString = money.format(value);
            return valueToString.replace(".",",");
        }
        valueToString = money.format(value).replace(",", ".");
        String firstPart = valueToString.substring(0, valueToString.length() - 3);
        String lastPart = valueToString.substring(valueToString.length() - 3, valueToString.length()).replace(".",",");

        return firstPart + lastPart;
    }

    public static boolean periodsOK (String firstMonthString, String firstYearString, String lastMonthString, String lastYearString) {
        int firstMonthIndex = 0;
        int lastMonthIndex = 0;
        int firstYear = Float.valueOf(firstYearString).intValue();
        int lastYear = Float.valueOf(lastYearString).intValue();
        if (firstYearString.equals(lastYearString)) {
            String[] monthsArray = {     "jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago",
                    "set", "out", "nov", "dez"     };

            String firstMonthMin = firstMonthString.substring(0, 3).toLowerCase();
            String lastMonthMin = lastMonthString.substring(0, 3).toLowerCase();
            int i = 0;
            while (i < monthsArray.length) {
                firstMonthIndex = monthsArray[i].equals(firstMonthMin) ? i : firstMonthIndex;
                lastMonthIndex = monthsArray[i].equals(lastMonthMin) ? i : lastMonthIndex;
                i++;
            }
        }
        return lastMonthIndex >= firstMonthIndex && lastYear >= firstYear;
    }

    public static float getFloat(String pDeposito) {
        return pDeposito.contains("Valor") ? 0 : Float.valueOf(pDeposito.replace(".","").replace(",","."));
    }
}
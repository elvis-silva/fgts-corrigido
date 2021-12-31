package com.elvis.fgtscorrigido.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.model.FGTSDataModel;
import com.elvis.fgtscorrigido.app.model.NameDataModel;
import com.elvis.fgtscorrigido.app.utils.GUI;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    public static final String FGTS_CORRIGIDO_TABLE = "FgtsCorrigidoTable";
    public static final String NAMES_TABLE          = "NamesTable";
    public static final String ID_NAMES             = "id_name";
    public static final String NAMES                = "names";
    public static final String ID                   = "_id";
    public static final String NAME                 = "name";
    public static final String MONTH                = "month";
    public static final String YEAR                 = "year";
    public static final String DEPOSITO             = "deposito";
    public static final String TR                   = "tr";
    public static final String INPC                 = "inpc";
    public static final String DIFERENCE            = "diference";
    public static final String [] COLUMNS           = {ID, NAME, MONTH, YEAR, DEPOSITO, TR, INPC, DIFERENCE};
    public static final String [] NAMES_COLUMNS     = {ID_NAMES, NAMES};

    public static final String DATABASE_NAME = "elvisfgtscorrigido.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE  = "CREATE TABLE " +
            FGTS_CORRIGIDO_TABLE                + "(" +
            ID                                  + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME                                + " TEXT," +
            MONTH                               + " TEXT," +
            YEAR                                + " TEXT," +
            DEPOSITO                            + " TEXT," +
            TR                                  + " TEXT," +
            INPC                                + " TEXT," +
            DIFERENCE                           + " TEXT);";

    public static final String NAMES_DB_CREATE  = "CREATE TABLE " +
            NAMES_TABLE                         + "(" +
            ID_NAMES                            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAMES                               + " TEXT);";

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DataHandler (Context pContext) {
        context = pContext;
        dbHelper = new DataBaseHelper(pContext);
    }

    public List<FGTSDataModel> getDataModel(String pDataName) {
        List<FGTSDataModel> modelList = new ArrayList<>();
        Cursor cursor = returnFGTSData();
        cursor.moveToFirst();
        int i = 0;
        while (i < cursor.getCount()) {
            FGTSDataModel fgtsDataModel = parseFGTSData(cursor);
            if(fgtsDataModel.getName().equals(pDataName)) {
                modelList.add(fgtsDataModel);
            }
            cursor.moveToNext();
            i++;
        }
        cursor.close();

        return modelList;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper (Context pContext) {
            super(pContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(NAMES_DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FGTS_CORRIGIDO_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + NAMES_DB_CREATE);
            onCreate(db);
        }
    }

    public DataHandler open () throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close () {
        db.close();
        dbHelper.close();
    }

    public Cursor returnFGTSData() {
        return db.query(FGTS_CORRIGIDO_TABLE, COLUMNS, ID, null, null, null, null);
    }

    public Cursor returnNamesData () {
        return db.query(NAMES_TABLE, NAMES_COLUMNS, ID_NAMES, null, null, null, null);
    }

    public FGTSDataModel addFGTSData (String pName, String pMonth, String pYear, String pDeposito, String pTR, String pINPC, String pDiference) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, pName);
        cv.put(MONTH, pMonth);
        cv.put(YEAR, pYear);
        cv.put(DEPOSITO, pDeposito);
        cv.put(TR, pTR);
        cv.put(INPC, pINPC);
        cv.put(DIFERENCE, pDiference);

        long rowID = db.insert(FGTS_CORRIGIDO_TABLE, null, cv);

        Cursor cursor = db.query(FGTS_CORRIGIDO_TABLE, COLUMNS, ID + " = " + rowID, null, null, null, null);
        cursor.moveToFirst();

        FGTSDataModel fgtsData = parseFGTSData(cursor);
        cursor.close();

        return fgtsData;
    }

    public void addNameData (String pName) {
        ContentValues cv = new ContentValues();
        cv.put(NAMES, pName);

        long rowID = db.insert(NAMES_TABLE, null, cv);

        Cursor cursor = db.query(NAMES_TABLE, NAMES_COLUMNS, ID_NAMES + " = " + rowID, null, null, null, null);
        cursor.close();
    }

    public FGTSDataModel parseFGTSData(Cursor pCursor) {
        FGTSDataModel fgtsData = new FGTSDataModel();
        fgtsData.setId((pCursor.getInt(0)));
        fgtsData.setName(pCursor.getString(1));
        fgtsData.setMonth(pCursor.getString(2));
        fgtsData.setYear(pCursor.getString(3));
        fgtsData.setDeposito(pCursor.getString(4));
        fgtsData.setTr(pCursor.getString(5));
        fgtsData.setInpc(pCursor.getString(6));
        fgtsData.setDiference(pCursor.getString(7));
        return fgtsData;
    }

    public NameDataModel parseNameData(Cursor pCursor) {
        NameDataModel nameDataModel = new NameDataModel();
        nameDataModel.setId(pCursor.getInt(0));
        nameDataModel.setName(pCursor.getString(1));
        return nameDataModel;
    }

    public String getNameData(Cursor pCursor) {
        return pCursor.getString(1);
    }

    public void clearData() {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = returnFGTSData();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if (rows > 0) {
            int i = 0;
            do {
                FGTSDataModel dataModel = parseFGTSData(cursor);
                db.delete(FGTS_CORRIGIDO_TABLE, ID + " = " + dataModel.getId(), null);
                cursor.moveToNext();
                i++;
            }
            while (i < rows);
        }
        cursor.close();
    }

    public void deleteNameData(String pName) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = returnNamesData();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if (rows > 0) {
            int i = 0;
            do {
                NameDataModel dataModel = parseNameData(cursor);
                if(dataModel.getName().equals(pName)) {
                    db.delete(NAMES_TABLE, ID_NAMES + " = " + dataModel.getId(), null);
                }
                cursor.moveToNext();
                i++;
            }
            while (i < rows);
        }
        cursor.close();
    }

    public void deleteFGTSData(String pName) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = returnFGTSData();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if (rows > 0) {
            int i = 0;
            do {
                FGTSDataModel dataModel = parseFGTSData(cursor);
                if(dataModel.getName().equals(pName)) {
                    db.delete(FGTS_CORRIGIDO_TABLE, ID + " = " + dataModel.getId(), null);
                }
                cursor.moveToNext();
                i++;
            }
            while (i < rows);
        }
        cursor.close();
    }

    public boolean containsName(String pName) {
        String newName = pName.trim();
        Cursor cursor = returnNamesData();
        cursor.moveToFirst();
        int rows = cursor.getCount();
        if (rows > 0) {
            int i = 0;
            do {
                NameDataModel dataModel = parseNameData(cursor);
                if(dataModel.getName().equals(newName)) {
                    return true;
                }
                cursor.moveToNext();
                i++;
            }
            while (i < rows);
        }
        cursor.close();

        return false;
    }
}
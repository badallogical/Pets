package com.passion.pets.data;

import android.content.Context;
import android.database.sqlite.*;

import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper{

    public static String DB_NAME="PetDB";
    public static int DB_VERSION=1;

    public PetDbHelper(@Nullable Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PetContract.CREATE_TABLE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(PetContract.DROP_TABLE_IF_EXIST);
        onCreate(sqLiteDatabase);
    }
}

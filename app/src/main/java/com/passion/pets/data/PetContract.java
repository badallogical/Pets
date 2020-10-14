package com.passion.pets.data;

import android.net.Uri;

public class PetContract {

    public static String TABLE_NAME="Pet";
    public static String PET_NAME="Name";
    public static String PET_ID ="_id";
    public static String PET_BREED="Breed";
    public static String PET_GENDER="Gender";
    public static String PET_WEIGHT="Weight";


    // Content Provide Constants
    public static final String CONTENT_AUTHORITY = "com.passion.pets";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_BASE_URI,TABLE_NAME);

    public static String CREATE_TABLE_ENTRY= "Create Table " + TABLE_NAME + " ( "
            + PET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + PET_NAME + " TEXT,"
            + PET_BREED + " TEXT,"
            + PET_GENDER + " INTEGER,"
            + PET_WEIGHT + " INTEGER )";

    public static String DROP_TABLE_IF_EXIST = "DROP TABLE IF EXISTS " + TABLE_NAME;
}

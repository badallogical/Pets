package com.passion.pets.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Pet.class}, version = 1 , exportSchema = false)
public abstract class PetDatabase extends RoomDatabase {

    // volatile shared database instance, only one at a application.
    private static volatile PetDatabase instance;

    // abstract dao return method that will be implemented by Room
    public abstract PetDao petDao();

    // singleton pattern
    static public PetDatabase getDatabase( Context context ){
        if( instance == null ){
            synchronized (PetDatabase.class){
                instance = Room.databaseBuilder( context.getApplicationContext(),
                        PetDatabase.class,
                        "pets_db").build();
            }
        }

        return instance;
    }



}

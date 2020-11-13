package com.passion.pets.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PetDao {

    /* SQL queries mapping to functions*/
    @Query("SELECT * FROM pets_table")
    LiveData<List<Pet>> getAllPets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPet(Pet p );

    @Delete
    void deletePet(Pet p);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updatePet(Pet p);

    @Query("DELETE FROM pets_table")
    void deleteAll();
}

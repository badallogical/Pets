package com.passion.pets.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets_table")
public class Pet {

    @PrimaryKey( autoGenerate = true)
    private int id;

    private String petName;
    private String petBreed;
    private int petGender;
    private float petWeight;

    /* constructors */
    public Pet( String petName, String petBreed, int petGender, float petWeight ){
        this.petName = petName;
        this.petBreed = petBreed;
        this.petGender = petGender;
        this.petWeight = petWeight;
    }

    @Ignore
    public Pet( String petName, int petGender, float petWeight ){
        this.petName = petName;
        this.petGender = petGender;
        this.petWeight = petWeight;
    }

    public void setId(int id) {
        this.id = id;
    }

    /* getters */
    public String getPetName() {
        return petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public int getPetGender() {
        return petGender;
    }

    public float getPetWeight() {
        return petWeight;
    }

    public int getId() {
        return id;
    }
}

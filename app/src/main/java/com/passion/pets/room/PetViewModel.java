package com.passion.pets.room;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class PetViewModel extends AndroidViewModel {

    private PetRepository petRepo;
    private LiveData<List<Pet>> allpets;

    public PetViewModel(@NonNull Application application) {
        super(application);
        petRepo = new PetRepository(application);
        allpets = petRepo.getAllPets();
    }

    public void insert(Pet pet){
        petRepo.insert(pet);
    }

    public void delete( Pet p ){
        petRepo.delete(p);
    }

    public void update(Pet pet ){
        petRepo.update(pet);
    }

    public void deleteAll(){
        petRepo.deleteAll();
    }

    public LiveData<List<Pet>> getAllpets(){
        return allpets;
    }

}

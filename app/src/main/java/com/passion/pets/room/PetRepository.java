package com.passion.pets.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PetRepository  {

    private LiveData<List<Pet>> allPets;
    private PetDao petDao;

    public PetRepository( Application application ){
        PetDatabase petDB = PetDatabase.getDatabase(application);
        petDao = petDB.petDao();
        allPets = petDao.getAllPets();
    }

    public void insert( Pet pet ){
        new AsyncInsert(petDao).execute(pet);
    }

    public void delete(Pet p){
        new AsyncDelete(petDao).execute( p );
    }

    public void deleteAll(){
        new AsyncDelete(petDao).execute((Pet) null);
    }

    public void update(Pet pet ){
        new AsyncUpdate(petDao).execute(pet);
    }

    public LiveData<List<Pet>> getAllPets(){
        return allPets;
    }

    /* Async Insert for Insert operation */
    private static class AsyncInsert extends AsyncTask< Pet, Void , Void >{
        private PetDao petDao;

        private AsyncInsert( PetDao petDao ){
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            petDao.insertPet(pets[0]);
            return null;
        }
    }

    /* Async Delete for Delete operation */
    private static class AsyncDelete extends AsyncTask<Pet, Void , Void >{
        private PetDao petDao;

        private AsyncDelete( PetDao petDao ){
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            if( pets[0] == null ){
                petDao.deleteAll();
            }else{
                petDao.deletePet( pets[0] );
            }

            return null;
        }
    }

    /* Async Update for Update operation */
    private static class AsyncUpdate extends AsyncTask< Pet, Void , Void >{
        private PetDao petDao;

        private AsyncUpdate( PetDao petDao ){
            this.petDao = petDao;
        }

        @Override
        protected Void doInBackground(Pet... pets) {
            if( pets[0] != null )
                petDao.updatePet( pets[0] );
            return null;
        }
    }


}

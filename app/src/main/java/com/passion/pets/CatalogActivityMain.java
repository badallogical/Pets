package com.passion.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.passion.pets.data.PetContract;
import com.passion.pets.data.PetCursorAdapter;
import com.passion.pets.data.PetDbHelper;

import java.net.URI;

public class CatalogActivityMain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static PetDbHelper mPetdbhelper;
    public ListView pet_list;

    // Cursor
    PetCursorAdapter cursorAdapter;

    // CursorLoader
    public static final int BASE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set mPetDbHelper
        mPetdbhelper = new PetDbHelper(this );

        // set petList
        pet_list = (ListView) findViewById(R.id.pet_list);
        pet_list.setEmptyView( findViewById(R.id.empty_view) );
        pet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivityMain.this, EditorActivity.class);
                intent.setData(ContentUris.withAppendedId(PetContract.CONTENT_URI, id));
                startActivity(intent);
            }
        });

        // floating action button for launching editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivityMain.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //setup adapter
        cursorAdapter = new PetCursorAdapter(this,null);
        pet_list.setAdapter(cursorAdapter);

        // initialize CursorLoader
        getSupportLoaderManager().initLoader(BASE_LOADER_ID,null,this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ){
        switch (item.getItemId()){
            case R.id.op1 :
                // insert dummy data
                insertDummyData();
                break;

                case R.id.op2 :
                    // Alert warning for deleting all pets
                    AlertDialog.Builder alertDeleteAll = new AlertDialog.Builder(this);
                    alertDeleteAll.setTitle(R.string.delete_all_pet);
                    alertDeleteAll.setPositiveButton(R.string.delete_all, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete all pets
                            deleteAllPets();
                        }
                    });


                    alertDeleteAll.setNegativeButton(R.string.cancel, new Dialog.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                             // cancel deleting all pet
                            if( dialogInterface != null ){
                                dialogInterface.dismiss();
                            }
                        }
                    });

                    // show Dialog Box
                    alertDeleteAll.create().show();
                    break;

            case R.id.info:
                // Dialog for App information Developer info
                AlertDialog.Builder info = new AlertDialog.Builder(this);
                info.setView( LayoutInflater.from(this).inflate(R.layout.info_layout,null) );
                info.setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( dialogInterface != null ){
                            dialogInterface.dismiss();
                        }
                    }
                } );

                // show
                info.create().show();
                break;
        }

        return true;
    }

    private void deleteAllPets() {
        // get ContentResolver
        final int rowDeleted = getContentResolver().delete(PetContract.CONTENT_URI, null,null);

        // notify user
        Toast toast = Toast.makeText(this,rowDeleted + " Pet Deleted", Toast.LENGTH_LONG);
        toast.show();
    }

    private void insertDummyData() {
        // set dummy pet data
        ContentValues dummyPet = new ContentValues();
        dummyPet.put(PetContract.PET_NAME, "Toto");
        dummyPet.put(PetContract.PET_BREED, "German");
        dummyPet.put(PetContract.PET_GENDER, 1);
        dummyPet.put(PetContract.PET_WEIGHT,4);

        // insert with ContentResolver
        final Uri uri = getContentResolver().insert(PetContract.CONTENT_URI, dummyPet);
        String msg = null;
        if( uri == Uri.parse(String.valueOf(R.string.INVALID_DATA))){
            msg = "Invalid Data";
        }
        else{
            msg = "Pet Saved";
        }

        // show message
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       // perform operation based on Loader id
        switch( id ){
            case BASE_LOADER_ID :
                return new CursorLoader(
                        this,
                        PetContract.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

            default: return null;
        }
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {
          cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}

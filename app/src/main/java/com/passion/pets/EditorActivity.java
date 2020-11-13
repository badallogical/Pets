package com.passion.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.passion.pets.room.Pet;
import com.passion.pets.room.PetViewModel;

import java.util.List;


public class EditorActivity extends AppCompatActivity {

    // components
    TextView mPetName;
    TextView mPetBreed;
    Spinner mPetGender;
    TextView mPetWeight;

    //Intent constant
    public static final String intentType = "INTENT_TYPE";
    public static final String viewModel = "VIEW_MODEL";

    // Intent
    Intent intent;

    boolean editMode = false;

    PetViewModel petViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // get components
        mPetName = findViewById(R.id.edit_pet_name);
        mPetBreed = findViewById(R.id.edit_pet_breed);
        mPetGender = findViewById(R.id.edit_gender);
        mPetWeight = findViewById(R.id.edit_weight);

        intent = getIntent();
        petViewModel = ViewModelProviders.of(this).get(PetViewModel.class);
        if( intent.getExtras().get(intentType).toString().equals("ADD") ){
            getSupportActionBar().setTitle("Add a pet");
            editMode = false;
        }
        else{
            getSupportActionBar().setTitle("Edit your pet");
            Pet p = (Pet) intent.getExtras().get("PET");
            mPetName.setText(p.getPetName() );
            mPetBreed.setText(p.getPetBreed());
            mPetGender.setSelection(p.getPetGender());
            mPetWeight.setText( String.valueOf(p.getPetWeight()));
            editMode = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        if( editMode == false ) {
            MenuItem etcOpt = menu.getItem(0);
            etcOpt.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.delete :
                Pet p = getPet();
                if( p == null ){
                    Toast.makeText(this, "Invalid Entry", Toast.LENGTH_LONG).show();
                }
                else{
                    petViewModel.delete(p);
                    Toast.makeText(this, "Deleted Entry", Toast.LENGTH_LONG).show();
                }
                finish();
                break;

            case R.id.save :
                Pet pC = getCurrentPet();
                if( pC == null ){
                    Toast.makeText(this, "Invalid Entry", Toast.LENGTH_LONG).show();
                }
                else if( editMode == false ){
                    petViewModel.insert(pC);
                    Toast.makeText(this, "Inserted Entry", Toast.LENGTH_LONG).show();
                }
                else{
                    Pet existingPet = getPet();
                    pC.setId(existingPet.getId());
                    petViewModel.update(pC);
                    Toast.makeText(this, "Updated Entry", Toast.LENGTH_LONG).show();
                }
                finish();
                break;

        }

        return true;
    }

    private Pet getPet() {
        Pet p = null;
        if( editMode == true )
            p = (Pet) intent.getExtras().get("PET");

        return p;
    }

    private Pet getCurrentPet(){
        Pet p = null;
        if( validate() )
            p = (Pet) new Pet(mPetName.getText().toString(), mPetBreed.getText().toString(), mPetGender.getSelectedItemPosition(), Float.parseFloat( mPetWeight.getText().toString()) );

        return p;
    }

    private boolean validate() {
        if(TextUtils.isEmpty(mPetName.getText()) || TextUtils.isEmpty(mPetBreed.getText()) || TextUtils.isEmpty( Integer.toString(mPetGender.getSelectedItemPosition()) ) || TextUtils.isEmpty(mPetWeight.getText()) ){
            return false;
        }
        return true;
    }

}

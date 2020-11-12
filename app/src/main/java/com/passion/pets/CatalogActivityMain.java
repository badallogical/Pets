package com.passion.pets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.passion.pets.data.PetListAdapter;
import com.passion.pets.room.Pet;
import com.passion.pets.room.PetViewModel;

import java.util.List;


public class CatalogActivityMain extends AppCompatActivity {

    PetViewModel petViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* recycler view */
        RecyclerView recyclerView = findViewById(R.id.pet_list);
        PetListAdapter petListAdapter = new PetListAdapter();
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        recyclerView.setAdapter(petListAdapter);

        RelativeLayout emptyView =  findViewById(R.id.empty_view);

        /* View Model */
        // get the viewModel by view model provider
        petViewModel = ViewModelProviders.of(this).get(PetViewModel.class);


        // add a observer to live data
        petViewModel.getAllpets().observe( this, (pets) -> {
            // change pet list
            petListAdapter.setPetList(pets);

            // configure empty view
            if( pets.isEmpty()){
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else{
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            Toast.makeText(this, "Dataset Changed", Toast.LENGTH_LONG ).show();
        });

        /* Floating Action Button */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent pushPet = new Intent( CatalogActivityMain.this , EditorActivity.class );
            pushPet.putExtra(EditorActivity.intentType, "ADD");
            startActivity(pushPet);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        switch (item.getItemId()){
            case R.id.op1 :
                // insert dummy data
                insertDummyData();
                break;

                case R.id.op2 :
                    // Alert warning for deleting all pets
                    AlertDialog.Builder alertDeleteAll = new AlertDialog.Builder(this);
                    alertDeleteAll.setTitle(R.string.delete_all_pet);
                    alertDeleteAll.setPositiveButton(R.string.delete_all, (dialogInterface, i) -> {
                        // delete all pets
                        deleteAllPets();
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
        petViewModel.deleteAll();
    }

    private void insertDummyData() {
        Pet p = new Pet("Toto", "Bulldog", 0, 2f );
        petViewModel.insert(p);
    }



}

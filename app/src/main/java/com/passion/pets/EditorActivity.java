package com.passion.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.passion.pets.data.PetContract;
import com.passion.pets.data.PetDbHelper;

public class EditorActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    TextView name;
    TextView breed;
    Spinner genderSpinner;
    TextView measurement;

    int gender = 0;

    // Editor Loader
    public static final int EDITOR_LOADER = 2;
    Uri item_uri = null;

    // Pet changed
    private boolean mPetChanged = false;

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        //get components
        name = (TextView) findViewById(R.id.edit_pet_name);
        breed = (TextView) findViewById(R.id.edit_pet_breed);
        genderSpinner = (Spinner) findViewById(R.id.edit_gender);
        measurement = (TextView) findViewById(R.id.edit_weight);

        // set Touch listener
        name.setOnTouchListener(touchListener);
        breed.setOnTouchListener(touchListener);
        genderSpinner.setOnTouchListener(touchListener);
        measurement.setOnTouchListener(touchListener);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = 0;
            }
        });

        // get Intent and check for editor mode
        Intent intent = getIntent();
        String editor_mode = "Add a Pet";
        item_uri = intent.getData();
        if( item_uri != null ){
            // editor mode
            editor_mode = "Edit Pet";

            // initial CursorLoader
            getSupportLoaderManager().initLoader(EDITOR_LOADER, null, this);
        }


        // set action up
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(editor_mode);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.editor_menu, menu);
            return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if( item_uri == null ) {
            MenuItem delete_item = menu.findItem(R.id.delete);
            delete_item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                // save the pets data to db
                ContentValues content = new ContentValues();
                content.put(PetContract.PET_NAME, name.getText().toString());
                content.put(PetContract.PET_BREED, breed.getText().toString());
                content.put(PetContract.PET_GENDER, gender);
                content.put(PetContract.PET_WEIGHT, measurement.getText().toString());

                // insert / Update using ContentResolver, get the URI in back ( inserted row URI path )
                Uri insertedRow = Uri.parse("Updated");
                if (item_uri == null)
                    // Normal Insertion
                    insertedRow = getContentResolver().insert(PetContract.CONTENT_URI, content);
                else {
                    // Update
                    String selection = PetContract.PET_ID + "=?";
                    String selectionArgs[] = new String[]{
                            String.valueOf(ContentUris.parseId(item_uri))
                    };
                    getContentResolver().update(item_uri, content, selection, selectionArgs);
                }

                // close this activity
                finish();

                if (insertedRow.equals(Uri.parse(String.valueOf(R.string.INVALID_DATA)))) {
                    // Notify with Toast
                    Toast t = Toast.makeText(this, "Invalid data", Toast.LENGTH_LONG);
                    t.show();
                } else if (insertedRow.equals(Uri.parse("Updated"))) {
                    // Notify with Toast
                    Toast t = Toast.makeText(this, "Pet Updated ", Toast.LENGTH_LONG);
                    t.show();
                } else {
                    // Notify with Toast
                    Toast t = Toast.makeText(this, "Pet Added at " + ContentUris.parseId(insertedRow), Toast.LENGTH_LONG);
                    t.show();
                }
                break;

            case R.id.delete:

                // AlertBox to confirm delete
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(this);
                alertDelete.setTitle(R.string.delete_this_pet);
                alertDelete.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePet();
                    }
                });

                alertDelete.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close dialog box , cancel deletion
                        if( dialogInterface != null ){
                            dialogInterface.dismiss();
                        }
                    }
                });

                // show dialog box
                alertDelete.create().show();
                break;

            case android.R.id.home:
                if( !mPetChanged ) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                else{
                    // show a Confirmation Dialog
                    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // back to parent
                            NavUtils.navigateUpFromSameTask(EditorActivity.this);

                        }
                    };
                    showWarningUnsavedChanges(listener);
                }
                break;
        }
        return true;
    }

    private void showWarningUnsavedChanges(DialogInterface.OnClickListener discardlistener) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setMessage(R.string.unsaved_changes);
        alertBox.setPositiveButton(R.string.keep_editing, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // close the pop up box
                if( dialogInterface != null ){
                    dialogInterface.dismiss();
                }
            }
        });

        alertBox.setNegativeButton(R.string.discard, discardlistener );
        alertBox.create().show();
    }

    public void setGender(View v) {
        gender = (int) genderSpinner.getSelectedItem();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // if editor is to add new pet just skip
        switch ( id ) {
            case EDITOR_LOADER:
                return new CursorLoader(
                        this,
                        item_uri,
                        null,
                        null,
                        null,
                        null
                );

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // populate fields with data
        if (data.moveToFirst()) {
            name.setText(data.getString(data.getColumnIndexOrThrow(PetContract.PET_NAME)));
            breed.setText(data.getString(data.getColumnIndexOrThrow(PetContract.PET_BREED)));
            gender = data.getInt(data.getColumnIndexOrThrow(PetContract.PET_GENDER));
            genderSpinner.setSelection(gender);
            measurement.setText(String.valueOf(data.getInt(data.getColumnIndexOrThrow(PetContract.PET_WEIGHT))));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // reset fields
        name.setText("");
        breed.setText("");
        genderSpinner.setSelection(0);
        measurement.setText("");
    }

    @Override
    public void onBackPressed() {

        // set discard option
        if( !mPetChanged ){
            super.onBackPressed();
        }
        else{
            // perpare discard option
            DialogInterface.OnClickListener listner = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // close that activity
                    finish();
                }
            };

            // pass to show and provide positive button too
            showWarningUnsavedChanges(listner);
        }
    }

    private void deletePet(){
        // reset the fields
        String selection = PetContract.PET_ID + "=?";
        String selectionArgs[] = new String[] {
                String.valueOf(ContentUris.parseId(item_uri))
        };

        getContentResolver().delete(item_uri,selection, selectionArgs);

        // close activity
        finish();

        // show pop up
        Toast t = Toast.makeText(this, "Pet Deleted", Toast.LENGTH_LONG);
        t.show();
    }
}

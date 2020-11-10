//package com.passion.pets.data;
//
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.passion.pets.CatalogActivityMain;
//import com.passion.pets.R;
//
//public class PetProvider extends ContentProvider {
//
//    private static final int PETS = 100;
//    private static final int PET_ID = 101;
//
//    // create UriMatcher
//    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//    static {
//        // define UriMatcher , add mapping to uri patterns to unique Integer Code
//        mUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.TABLE_NAME, PETS);
//        mUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.TABLE_NAME + "/#", PET_ID);
//    }
//
//    @Override
//    public boolean onCreate() {
//        return true;
//    }
//
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//
//        // get Readable Database
//        SQLiteDatabase db = CatalogActivityMain.mPetdbhelper.getReadableDatabase();
//
//        // get Cursor
//        Cursor cursor;
//
//        int match = mUriMatcher.match(uri);
//        switch (match) {
//            case PETS:
//                // TODO:
//                cursor = db.query(PetContract.TABLE_NAME, null, null, null, null, null, sortOrder);
//                break;
//            case PET_ID:
//                // TODO:
//                selection = PetContract.PET_ID + "=?";
//                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
//                cursor = db.query(PetContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknow Uri sir");
//        }
//
//        // set notifier for cursor adapter
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//
//        return cursor;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//
//    @Override
//    public Uri insert(Uri uri, ContentValues contentValues) {
//
//        //check for empty breed and give default
//        if(  TextUtils.isEmpty(contentValues.getAsString( PetContract.PET_BREED).trim() ) ){
//            contentValues.put(PetContract.PET_BREED, "Unknown Breed");
//        }
//        // get Writable Database
//        SQLiteDatabase db = CatalogActivityMain.mPetdbhelper.getWritableDatabase();
//
//        // sanity check
//        if (!sanityCheck(contentValues)) {
//            return Uri.parse(String.valueOf(R.string.INVALID_DATA));
//        }
//
//        // UriMatcher
//        int match = mUriMatcher.match(uri);
//        long id;
//
//        switch (match) {
//            case PETS:
//                // insert data
//                id = db.insert(PetContract.TABLE_NAME, null, contentValues);
//                break;
//            default:
//                throw new IllegalArgumentException("Insertion address is invalid (check the URI");
//        }
//
//        // notify cursor adapter
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        return ContentUris.withAppendedId(PetContract.CONTENT_URI, id);
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        // get writeable database
//        SQLiteDatabase db = CatalogActivityMain.mPetdbhelper.getWritableDatabase();
//
//        int rowDeleted = 0;
//
//        // UriMatcher
//        final int match = mUriMatcher.match(uri);
//
//        // map to operation
//        switch (match) {
//            case PETS:
//                // TODO: remove multiple rows
//                rowDeleted = db.delete(PetContract.TABLE_NAME, selection, selectionArgs);
//
//                break;
//            case PET_ID:
//                // fetch the ID prepare condition
//                selection = PetContract.PET_ID + "=?";
//                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
//                rowDeleted = db.delete(PetContract.TABLE_NAME, selection, selectionArgs);
//
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid URI for delete Operation");
//        }
//
//        // Manage SQLITE_SEQUENCE KEY
//        // prepare sql
//        final String RESET_KEY_VALUE = "UPDATE SQLITE_SEQUENCE SET SEQ = ( SELECT COUNT(*) FROM " + PetContract.TABLE_NAME + ") WHERE NAME=" + "\"" + PetContract.TABLE_NAME + "\"";
//        // reset the key generation
//        db.execSQL(RESET_KEY_VALUE);
//
//        // notify cursorAdapter
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        return rowDeleted;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
//
//        //check for empty breed and give default
//        if(  TextUtils.isEmpty(contentValues.getAsString( PetContract.PET_BREED).trim() ) ){
//            contentValues.put(PetContract.PET_BREED, "Unknown Breed");
//        }
//
//        // get the Readable database
//        SQLiteDatabase db = CatalogActivityMain.mPetdbhelper.getWritableDatabase();
//
//        int rowAffected = 0;
//
//        // UriMatcher
//        int match = mUriMatcher.match(uri);
//
//        // sanity check
//        if (!sanityCheck(contentValues)) {
//            // return the ERROR code
//            return R.string.INVALID_DATA;
//        }
//
//        // Perform operation based on query
//        switch (match) {
//            case PETS:
//                // TODO: update multiple data
//                rowAffected = db.update(PetContract.TABLE_NAME, contentValues, selection, selectionArgs);
//                break;
//            case PET_ID:
//                // TODO: update a single row, given by ID in the
//                selection = PetContract.PET_ID + "=?";
//                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
//                rowAffected = db.update(PetContract.TABLE_NAME, contentValues, selection, selectionArgs);
//                break;
//
//            default:
//                throw new IllegalArgumentException("Invalid Update URI");
//        }
//
//        // notify cursorAdapter
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        return rowAffected;
//    }
//
//    private boolean sanityCheck(ContentValues contentValues) {
//        if (contentValues.size() == 0) {
//            return false;
//        }
//
//        Toast t = Toast.makeText(getContext(), "Sanity Checking", Toast.LENGTH_LONG);
//        t.show();
//        Log.v("Logging", "Name = " + TextUtils.isEmpty(contentValues.getAsString(PetContract.PET_NAME)));
//
//        if ((contentValues.getAsString(PetContract.PET_NAME)).trim().isEmpty()) {
//            return false;
//        }
//
//        if (!(contentValues.getAsInteger(PetContract.PET_GENDER) == 0 || contentValues.getAsInteger(PetContract.PET_GENDER) == 1 || contentValues.getAsInteger(PetContract.PET_GENDER) == 2)) {
//            return false;
//        }
//
//        if (!((contentValues.getAsInteger(PetContract.PET_WEIGHT) != null ) && (contentValues.getAsInteger(PetContract.PET_WEIGHT) > 0))) {
//            return false;
//        }
//
//        return true;
//    }
//}

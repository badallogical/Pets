package com.passion.pets.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.passion.pets.R;

public class PetCursorAdapter extends CursorAdapter {

    Context context;

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // TODO: inflate the item layout
        return LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: populate the item with data
        TextView pet_name = (TextView) view.findViewById(R.id.pet_name);
        pet_name.setText( cursor.getString( cursor.getColumnIndexOrThrow(PetContract.PET_NAME)));

        TextView pet_breed = (TextView) view.findViewById(R.id.pet_breed);
        pet_breed.setText( cursor.getString( cursor.getColumnIndexOrThrow(PetContract.PET_BREED)));
    }
}

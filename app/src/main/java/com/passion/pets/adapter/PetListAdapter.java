package com.passion.pets.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.passion.pets.room.Pet;

public class PetListAdapter extends ArrayAdapter<Pet> {

    public PetListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


}

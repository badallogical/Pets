package com.passion.pets.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.passion.pets.R;
import com.passion.pets.room.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetListHolder> {

    private List<Pet> petList = new ArrayList<>();


    @Override
    public PetListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_layout, parent, false );
        return new PetListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetListHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.petName.setText(pet.getPetName());
        holder.petBreed.setText(pet.getPetBreed());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public void setPetList( List<Pet> pets ){
        this.petList = pets;
        notifyDataSetChanged();
    }

    class PetListHolder extends RecyclerView.ViewHolder{

        private TextView petName;
        private TextView petBreed;

        public PetListHolder( View itemView ) {
            super(itemView);
            petName = itemView.findViewById(R.id.pet_name);
            petBreed = itemView.findViewById(R.id.pet_breed);
        }

    }

}

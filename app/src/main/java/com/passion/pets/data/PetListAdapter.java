package com.passion.pets.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.passion.pets.R;
import com.passion.pets.room.Pet;

import org.w3c.dom.Text;

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
        holder.petGender.setText( ( getGender(pet.getPetGender())) );
        holder.petWeight.setText( String.valueOf(pet.getPetWeight()) + " Kg");
    }

    private String getGender(int petBreed) {
        switch (petBreed){
            case 1: return "Male";
            case 2: return "Female";
            default: return "Unknown";
        }
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
        private TextView petGender;
        private TextView petWeight;

        public PetListHolder( View itemView ) {
            super(itemView);
            petName = itemView.findViewById(R.id.pet_name);
            petBreed = itemView.findViewById(R.id.pet_breed);
            petGender = itemView.findViewById(R.id.pet_gender);
            petWeight = itemView.findViewById(R.id.pet_weight);
        }

    }

}

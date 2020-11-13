package com.passion.pets.data;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.passion.pets.EditorActivity;
import com.passion.pets.R;
import com.passion.pets.room.Pet;
import com.passion.pets.room.PetViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetListHolder> {

    private List<Pet> petList = new ArrayList<>();
    private Context context;
    private PetViewModel viewModel;

    public PetListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PetListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView)LayoutInflater.from(context).inflate( R.layout.item_layout, parent, false );
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_LONG);
                    Intent intent = new Intent( context, EditorActivity.class );
                    intent.putExtra(EditorActivity.intentType, "EDIT");
                    intent.putExtra("PET",  petList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            petName = itemView.findViewById(R.id.pet_name);
            petBreed = itemView.findViewById(R.id.pet_breed);
            petGender = itemView.findViewById(R.id.pet_gender);
            petWeight = itemView.findViewById(R.id.pet_weight);
        }

    }

}

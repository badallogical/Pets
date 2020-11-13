package com.passion.pets.room;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets_table")
public class Pet implements Parcelable {

    @NonNull
    @PrimaryKey( autoGenerate = true )
    private long id = 0;

    private String petName;
    private String petBreed;
    private int petGender;
    private float petWeight;

    /* constructors */
    public Pet( String petName, String petBreed, int petGender, float petWeight ){
        this.petName = petName;
        this.petBreed = petBreed;
        this.petGender = petGender;
        this.petWeight = petWeight;
    }


    protected Pet(Parcel in) {
        id = in.readLong();
        petName = in.readString();
        petBreed = in.readString();
        petGender = in.readInt();
        petWeight = in.readFloat();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    /* getters */
    public String getPetName() {
        return petName;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public int getPetGender() {
        return petGender;
    }

    public float getPetWeight() {
        return petWeight;
    }

    public long getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(petName);
        dest.writeString(petBreed);
        dest.writeInt(petGender);
        dest.writeFloat(petWeight);
    }
}

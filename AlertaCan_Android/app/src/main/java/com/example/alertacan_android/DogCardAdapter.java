package com.example.alertacan_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class DogCardAdapter extends FirestoreRecyclerAdapter<Dog, DogCardAdapter.DogCardViewHolder> {

    public DogCardAdapter(@NonNull FirestoreRecyclerOptions<Dog> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DogCardViewHolder holder, int position, @NonNull Dog model) {
        holder.dogNameTextView.setText(model.getName());
        holder.dogRaceTextView.setText(model.getRace());

        Picasso.get()
                .load(model.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.dogImageView);
    }

    @NonNull
    @Override
    public DogCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog_card,
                parent,false);

        return new DogCardViewHolder(v);
    }

    class DogCardViewHolder extends RecyclerView.ViewHolder {

        public TextView dogNameTextView;
        public TextView dogRaceTextView;
        public ImageView dogImageView;


        public DogCardViewHolder(@NonNull View itemView) {
            super(itemView);

            dogNameTextView = itemView.findViewById(R.id.dogNameTextView);
            dogRaceTextView = itemView.findViewById(R.id.dogRaceTextView);
            dogImageView = itemView.findViewById(R.id.dogImageView);


        }
    }
}

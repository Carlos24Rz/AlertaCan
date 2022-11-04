package com.example.alertacan_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertacan_android.R;
import com.example.alertacan_android.interfaces.RecyclerViewInterface;
import com.example.alertacan_android.models.Dog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DogCardAdapter extends RecyclerView.Adapter<DogCardAdapter.DogCardViewHolder> {
    private final RecyclerViewInterface mRecyclerViewInterface;
    private Context mContext;
    private List<Dog> mDogs;

    public DogCardAdapter(Context context, List<Dog> dogs, RecyclerViewInterface recyclerViewInterface){
        mContext = context;
        mDogs = dogs;
        mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public DogCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_dog_card, parent, false);
        return new DogCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DogCardViewHolder holder, int position) {
        Dog currentDog = mDogs.get(position);
        holder.dogNameTextView.setText(currentDog.getName());
        holder.dogRaceTextView.setText(currentDog.getRace());

        Picasso.get()
                .load(currentDog.getImageUrl())
                .fit()
                .centerCrop()
                .placeholder( R.drawable.progress_animation )
                .into(holder.dogImageView);

    }

    @Override
    public int getItemCount() {
        return mDogs.size();
    }

    public class DogCardViewHolder extends RecyclerView.ViewHolder {

        public TextView dogNameTextView;
        public TextView dogRaceTextView;
        public ImageView dogImageView;

        public DogCardViewHolder(@NonNull View itemView) {
            super(itemView);

            dogNameTextView = itemView.findViewById(R.id.dogNameTextView);
            dogRaceTextView = itemView.findViewById(R.id.dogRaceTextView);
            dogImageView = itemView.findViewById(R.id.dogImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mRecyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos!= RecyclerView.NO_POSITION) mRecyclerViewInterface.onItemClick(pos);
                    }
                }
            });

        }
    }
}
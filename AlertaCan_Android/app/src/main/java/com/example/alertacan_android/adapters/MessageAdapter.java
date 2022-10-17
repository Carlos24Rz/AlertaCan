package com.example.alertacan_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertacan_android.R;
import com.example.alertacan_android.interfaces.RecyclerViewInterface;
import com.example.alertacan_android.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final RecyclerViewInterface mRecyclerViewInterface;
    private Context mContext;
    private List<Message> mMessages;

    public MessageAdapter(Context context,List<Message> messages,RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        mMessages = messages;
        mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message currentMessage = mMessages.get(position);
        holder.phoneTextView.setText(currentMessage.getPhone());
        holder.addressTextView.setText(currentMessage.getAddress());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView phoneTextView;
        public TextView addressTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRecyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) mRecyclerViewInterface.onItemClick(pos);
                    }
                }
            });

        }
    }
}

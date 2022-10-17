package com.example.alertacan_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alertacan_android.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class AddressPredictionArrayAdapter extends ArrayAdapter<AutocompletePrediction> {

    private List<AutocompletePrediction> addressesAll;

    public AddressPredictionArrayAdapter(@NonNull Context context, @NonNull List<AutocompletePrediction> addresses) {
        super(context, 0, addresses);
        addressesAll = new ArrayList<>(addresses);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return addressFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.address_prediction_item,parent,false
            );
        }

        TextView primaryText = convertView.findViewById(R.id.addressPrimaryText);
        TextView secondaryText = convertView.findViewById(R.id.addressSecondaryText);

        AutocompletePrediction prediction = getItem(position);

        if(prediction != null){
            primaryText.setText(prediction.getPrimaryText(null).toString());
            secondaryText.setText(prediction.getSecondaryText(null).toString());
        }
        return convertView;
    }

    private Filter addressFilter = new Filter(){
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AutocompletePrediction) resultValue).getFullText(null).toString();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<AutocompletePrediction> predictions = new ArrayList<>(addressesAll);

            results.values = predictions;
            results.count = predictions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}

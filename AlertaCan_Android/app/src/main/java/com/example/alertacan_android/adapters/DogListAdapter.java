package com.example.alertacan_android.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.alertacan_android.fragments.DogListFragment;

public class DogListAdapter extends FragmentStateAdapter {

    private final String mEmail;

    public DogListAdapter(@NonNull FragmentActivity fragmentActivity, String email) {
        super(fragmentActivity);
        mEmail = email;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new DogListFragment();
        Bundle args = new Bundle();
        //set query arguments to fragment
        args.putInt("menuVal",position);
        args.putString("userEmail",mEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

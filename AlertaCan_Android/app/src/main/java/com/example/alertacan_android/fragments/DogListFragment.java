package com.example.alertacan_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.alertacan_android.R;
import com.example.alertacan_android.activities.dogInfo.DogInfoActivity;
import com.example.alertacan_android.adapters.DogCardAdapter;
import com.example.alertacan_android.interfaces.RecyclerViewInterface;
import com.example.alertacan_android.models.Dog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DogListFragment extends Fragment implements RecyclerViewInterface, FilterDogsFabFragment.BottomSheetListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("dogs");

    private DogCardAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton filterFAB;
    private FloatingActionButton clearFAB;
    private List<Dog> prev;
    private List<Dog> mDogs;

    private int fragmentValue;
    private String email;

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dog_list, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentValue = getArguments().getInt("menuVal");
        email = getArguments().getString("userEmail");


        swipeRefreshLayout = view.findViewById(R.id.refreshDogList);
        recyclerView = view.findViewById(R.id.dogListRecylerView);
        filterFAB = view.findViewById(R.id.floatingActionButton);
        clearFAB = view.findViewById(R.id.clearFloatingActionButton);
        clearFAB.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));

        loadDogs();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDogs();
            }
        });

        filterFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDogsFabFragment bottomSheet = new FilterDogsFabFragment();
                Bundle args = new Bundle();
                args.putString("user",email);
                args.putInt("fragmentVal",fragmentValue);
                bottomSheet.setArguments(args);
                bottomSheet.show(getChildFragmentManager(), "filterBottomSheet");
            }
        });

        clearFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setEnabled(true);
                filterFAB.setVisibility(View.VISIBLE);
                clearFAB.setVisibility(View.INVISIBLE);
                mDogs.clear();
                mDogs.addAll(prev);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadDogs(){
        mDogs = new ArrayList<>();

        Query query;

        switch(fragmentValue) {
            case 0:
                //get "encontrados" dogs
                query = notebookRef
                        .whereEqualTo("state","Encontrado")
                        .whereNotEqualTo("user", email);
                break;
            case 1:
                //get "perdidos" dogs
                query = notebookRef
                        .whereEqualTo("state","Perdido")
                        .whereNotEqualTo("user", email);
                break;
            default:
                //get user dogs
                query = notebookRef
                        .whereEqualTo("user", email);
                break;
        }

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Dog dog = new Dog(document.getId()
                                        , document.getString("name")
                                        , document.getString("breed")
                                        , document.getString("imageUrl")
                                );

                                mDogs.add(dog);
                            }

                            adapter = new DogCardAdapter(mView.getContext(),mDogs,DogListFragment.this);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.e("PopulateDogFragment", "Error getting documents: ", task.getException());
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mView.getContext(), DogInfoActivity.class);
        intent.putExtra("dogId", mDogs.get(position).getId());
        intent.putExtra("fragmentValue", Integer.toString(fragmentValue));
        startActivity(intent);
    }

    @Override
    public void onFilteredClicked(List<Dog> dogs) {
        swipeRefreshLayout.setEnabled(false);
        filterFAB.setVisibility(View.INVISIBLE);
        clearFAB.setVisibility(View.VISIBLE);
        prev = new ArrayList<>();
        prev.addAll(mDogs);
        mDogs.clear();
        mDogs.addAll(dogs);
        adapter.notifyDataSetChanged();
    }
}
package com.example.pcwh.fragments;

import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pcwh.R;
import com.example.pcwh.models.HackRisk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HackRisksFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView lvRisks;
    List<HackRisk> list = new ArrayList<>();
    ArrayAdapter<HackRisk> adapter;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_hack_risks, container, false);

        // Initialize
        swipeRefreshLayout = fragment.findViewById(R.id.swipe_refresh_layout);
        lvRisks = fragment.findViewById(R.id.lv_risks);

        // Initialize database
        db = FirebaseFirestore.getInstance();

        db.collection("hackings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                list.clear();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    HackRisk risk = document.toObject(HackRisk.class);
                    list.add(risk);
                }

                if (list.size() > 0) {
                    // Set list adapter
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
                    lvRisks.setAdapter(adapter);
                }
            }
        });

        // Pull to refresh list
        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        return fragment;
    }

    private void loadData() {
        db.collection("hackings").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                list.clear();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    HackRisk risk = document.toObject(HackRisk.class);
                    list.add(risk);
                }

                if (list.size() > 0) {
                    // Set list adapter
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
                    lvRisks.setAdapter(adapter);
                }
            }

            swipeRefreshLayout.setRefreshing(false);
        });
    }
}
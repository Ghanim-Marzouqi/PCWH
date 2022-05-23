package com.example.pcwh.fragments;

import android.hardware.Camera;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pcwh.R;
import com.example.pcwh.models.HackRisk;

import java.util.ArrayList;
import java.util.List;

public class HackRisksFragment extends Fragment {

    ListView lvRisks;
    List<HackRisk> list = new ArrayList<>();
    ArrayAdapter<HackRisk> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_hack_risks, container, false);

        // Initialize
        lvRisks = fragment.findViewById(R.id.lv_risks);

        // Set list adapter
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        lvRisks.setAdapter(adapter);

        return fragment;
    }
}
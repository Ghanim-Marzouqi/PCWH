package com.example.pcwh.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcwh.R;
import com.example.pcwh.models.Info;
import com.example.pcwh.models.User;
import com.example.pcwh.models.UserCredentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class AboutUsFragment extends Fragment {

    // Declare
    TextView tvAboutUs;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_about_us, container, false);

        // Initialize
        tvAboutUs = fragment.findViewById(R.id.tv_about_us);

        // Initialize Firebase Database
        db = FirebaseFirestore.getInstance();

        // Load about us info
        DocumentReference docRef = db.collection("informations").document("about_us");
        docRef.get().addOnCompleteListener(dbTask -> {
            if (dbTask.isSuccessful()) {
                DocumentSnapshot document = dbTask.getResult();
                assert document != null;
                if (document.exists()) {
                    Info info = document.toObject(Info.class);
                    assert info != null;
                    tvAboutUs.setText(HtmlCompat.fromHtml(info.getText(), 0));
                } else {
                    Toast.makeText(getContext(), "About us info doesn't exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Cannot get about us info", Toast.LENGTH_SHORT).show();
            }
        });

        return fragment;
    }
}
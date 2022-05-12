package com.example.pcwh.fragments;

import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcwh.R;
import com.example.pcwh.models.Info;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CameraHackInfoFragment extends Fragment {

    // Declare
    TextView tvCameraHack;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_camera_hack_info, container, false);

        // Initialize
        tvCameraHack = fragment.findViewById(R.id.tv_camera_hack);

        // Initialize Firebase Database
        db = FirebaseFirestore.getInstance();

        // Load about us info
        DocumentReference docRef = db.collection("informations").document("camera_hacking");
        docRef.get().addOnCompleteListener(dbTask -> {
            if (dbTask.isSuccessful()) {
                DocumentSnapshot document = dbTask.getResult();
                assert document != null;
                if (document.exists()) {
                    Info info = document.toObject(Info.class);
                    assert info != null;
                    tvCameraHack.setText(HtmlCompat.fromHtml(info.getText(), 0));
                } else {
                    Toast.makeText(getContext(), "Camera hacking info doesn't exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Cannot get camera hacking info", Toast.LENGTH_SHORT).show();
            }
        });

        return fragment;
    }
}
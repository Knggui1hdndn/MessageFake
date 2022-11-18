package com.example.messagefake.frag_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.messagefake.R;

public class frag_authenticateWith extends Fragment {
    private ImageView avatar;
    private TextView name;
    private Button sendEmail, sendPhoneNumber, continue_authenticate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authenticate_with, container, false);
        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        sendEmail = view.findViewById(R.id.sendEmail);
        sendPhoneNumber = view.findViewById(R.id.sendPhoneNumber);
        continue_authenticate = view.findViewById(R.id.continue_authenticate);
        return view;
    }
}

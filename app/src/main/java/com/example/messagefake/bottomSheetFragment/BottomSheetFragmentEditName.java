package com.example.messagefake.bottomSheetFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.messagefake.I.ArchivedMessages;
import com.example.messagefake.I.EditName;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragmentEditName extends BottomSheetDialogFragment {
    Context context;
    ArchivedMessages mainActivity;

    public BottomSheetFragmentEditName(Context context) {
        this.context = context;
    }

    private Button btnEdit ;
    private DAO_sqlite dao_sqlite;
    private Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_name, container, false);
        dao_sqlite = new DAO_sqlite(context);
        btnEdit = view.findViewById(R.id.btnEdit);
         args = getArguments();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(getActivity(), EditName.class));

            }
        });
        return view;
    }


}
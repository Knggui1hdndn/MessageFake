package com.example.messagefake.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.messagefake.I.ArchivedMessages;
import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetFragmentStorage extends BottomSheetDialogFragment {
    Context context;
    ArchivedMessages mainActivity;

    public BottomSheetFragmentStorage(Context context) {
        this.context = context;
    }

    private Button btndelete, btnBo;
    private DAO_sqlite dao_sqlite;
    private Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_archied, container, false);
        dao_sqlite = new DAO_sqlite(context);
        btndelete = view.findViewById(R.id.btndelete);
        btnBo = view.findViewById(R.id.btnBo);
        args = getArguments();
        mainActivity = (ArchivedMessages) getActivity();
        deleteUser();
        storageUser();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        onViewCreatedđược gọi ngay sau onCreateView(phương thức bạn khởi tạo và tạo tất cả các đố
//                i tượng của mình, bao gồm cả của bạn TextView)
        super.onViewCreated(view, savedInstanceState);
        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

    }

    private void storageUser() {
        btnBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao_sqlite.deleteStorage(args.getString("email"));
                mainActivity.loadUserBeforeStorega(args.getInt("index"));
                 DAO_Friebase.addArchivedMessage((ArrayList<String>) dao_sqlite.getStorage());
                dismiss();
            }
        });
    }

    private void deleteUser() {
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.status) {
                            dao_sqlite.deleteStorage(args.getString("email"));
                            mainActivity.loadUserBeforeStorega(args.getInt("index1"));
                            ArrayList<String> strings = args.getStringArrayList("index");
                            dao_sqlite.deleteUser(args.getString("user"));
                            dao_sqlite.deleteChatUser(args.getString("email"));
                            strings.remove(args.getString("email"));
                            DAO_Friebase.addArchivedMessage(strings);
                        } else {
                            dialog1.setTitle("Lỗi")
                                    .setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_wifi_tethering_error_24))
                                    .setMessage("Vui lòng kiểm tra lại kết nối mạng.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).setNegativeButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                        }
                    }
                }).setTitle("Xóa cuộc trò chuyện này?").setMessage("Bạn không thể hoàn tác sau khi xóa bản sao của cuộc trò chuyện này.").show();

            }
        });
    }
}
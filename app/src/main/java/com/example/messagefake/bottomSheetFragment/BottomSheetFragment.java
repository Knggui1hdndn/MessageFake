package com.example.messagefake.bottomSheetFragment;

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

import com.example.messagefake.MainActivity;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    Context context;
    MainActivity mainActivity;

    public BottomSheetFragment(Context context) {
        this.context = context;
    }

    private Button btndelete, btnSave, btnTatThongBao, btnDanhDau, btnChan;
    private DAO_sqlite dao_sqlite;
    private Bundle args;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        onViewCreatedđược gọi ngay sau onCreateView(phương thức bạn khởi tạo và tạo tất cả các đố
//                i tượng của mình, bao gồm cả của bạn TextView)
        super.onViewCreated(view, savedInstanceState);
        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_buttom_sheet, container, false);

        dao_sqlite = new DAO_sqlite(context);
        btndelete = view.findViewById(R.id.btndelete);
        btnSave = view.findViewById(R.id.btnSave);
        btnTatThongBao = view.findViewById(R.id.btnTatThongBao);
        btnDanhDau = view.findViewById(R.id.btnDanhDau);
        btnChan = view.findViewById(R.id.btnChan);
        args = getArguments();
        mainActivity = (MainActivity) getActivity();
        deleteMessage();
        storageUser();
        blockUser();
        return view;
    }

    private void blockUser() {
        btnChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()).setPositiveButton("Chặn", (dialog, which) -> {
                    if (MainActivity.status) {
                        dao_sqlite.addBlock(args.getString("user"));
                        DAO_Friebase.addBlock((ArrayList<String>) dao_sqlite.getListBlock());

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
                }).setTitle("Thông báo!").setMessage("Xác nhận chặn người dùng này .").show();


                dismiss();

            }
        });
    }

    private void storageUser() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao_sqlite.addStorage(args.getString("user"));
                mainActivity.loadUserBeforeStorega(args.getInt("index1"));
                DAO_Friebase.addArchivedMessage((ArrayList<String>) dao_sqlite.getStorage());
                dismiss();
            }
        });
    }

    private void deleteMessage() {
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss()).setPositiveButton("Xóa", (dialog, which) -> {
                    if (MainActivity.status) {
                        mainActivity.loadUserBeforeStorega(args.getInt("index1"));
                        dao_sqlite.deleteUser(args.getString("user"));
                        dao_sqlite.deleteChatUser(args.getString("user"));
                         DAO_Friebase.deleteMessage(dao_sqlite.getKeyMessage(args.getString("user")));
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
                }).setTitle("Xóa cuộc trò chuyện này?").setMessage("Bạn không thể hoàn tác sau khi xóa bản sao của cuộc trò chuyện này.").show();

            }
        });
    }
}
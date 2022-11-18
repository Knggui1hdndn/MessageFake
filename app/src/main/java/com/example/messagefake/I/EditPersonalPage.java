package com.example.messagefake.I;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagefake.Adapter.AdapterVertiEditInfo;
import com.example.messagefake.Adapter.openProfile;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.detail;
import com.example.messagefake.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditPersonalPage extends AppCompatActivity {
    Button btnEdit, btnAddCoverImage, btnAddStory,btnAddStory2, btnEditInformation, btnAddInterests;
    RoundedImageView imgAvatar;
    RecyclerView rcy;
    RoundedImageView imgCoverImage;
    static int PICK_IMAGE_REQUEST = 123;
    Dialog dialog;
    Uri filePath1;
    Uri filePath2;
    boolean aBoolean = true;
    DAO_sqlite dao_sqlite;
    SharedPreferences preferences;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    AdapterVertiEditInfo adapterVertiEditInfo;
    List<detail> details;
    String emailAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_page);
        dao_sqlite = new DAO_sqlite(this);
        initView();
        setAdapter();
        setToolBar();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean = true;
                openFolder();
            }
        });
         btnAddCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean = false;

                openFolder();

            }
        });
        btnAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPersonalPage.this,EditStory.class));
            }
        });
    }

    private void setAdapter() {
        user user = dao_sqlite.getUser(emailAuth).get(0);
        String getHabitat = user.getHabitat();
        String getAddress = user.getAddress();
        List<String> getEducation = user.getEducation();
        List<String> getWork = user.getWork();
        String getRelationship = user.getRelationship();
        details = new ArrayList<>();
        details.add(new detail(R.drawable.ic_baseline_home_241, "Sống tại " + getHabitat));
        details.add(new detail(R.drawable.ic_baseline_fmd_good_241, "Đến từ " + getAddress));
        if (getEducation != null) {
            for (String s : getEducation) {
                details.add(new detail(R.drawable.education, "Học vấn " + s));
            }
        } else {
            details.add(new detail(R.drawable.education, "Học vấnnull"));
        }
        if (getWork != null) {
            for (String s : getWork) {
                details.add(new detail(R.drawable.ic_baseline_home_repair_service_24, "Nơi làm việc " + s));
            }
        } else {
            details.add(new detail(R.drawable.ic_baseline_home_repair_service_24, "Nơi làm việcnull"));
        }
        details.add(new detail(R.drawable.heart, "Tình trạng mối quan hệ " + getRelationship));
        adapterVertiEditInfo = new AdapterVertiEditInfo(new openProfile() {
            @Override
            public void openProfile(user user) {

            }
        });
        adapterVertiEditInfo.setData(details);
        rcy.setLayoutManager(new LinearLayoutManager(EditPersonalPage.this, RecyclerView.VERTICAL, false));
        rcy.setAdapter(adapterVertiEditInfo);
    }

    private void openFolder() {
        dialog = createDialog();
        ImageView imageView = dialog.findViewById(R.id.img);
        ImageView imageView1 = dialog.findViewById(R.id.img1);
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EditPersonalPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);

            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EditPersonalPage.this, new String[]{Manifest.permission.CAMERA}, 1);

            }
        });
    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog(EditPersonalPage.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_img);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions.length > 0) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
                dialog.cancel();
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
                dialog.cancel();
            }
        }
    }

    public void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);//set icon tren toolbar
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_245);//set icon menu
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
        ) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Bitmap bitmap = null;
                try {
                    if (aBoolean) {
                        filePath1 = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);

                    } else {
                        filePath2 = data.getData();

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);

                    }
                    if (aBoolean) {
                        imgAvatar.setImageBitmap(bitmap);

                    } else {
                        imgCoverImage.setImageBitmap(bitmap);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                if (aBoolean) {
                    imgAvatar.setImageBitmap(bp);

                } else {
                    imgCoverImage.setImageBitmap(bp);
                }
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/");
                file.mkdir();//check xem thư mục được tạo chưa
                String s = System.currentTimeMillis() + ".jpg";
                File file1 = new File(file, s);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                    bp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (aBoolean) {
                    filePath1 = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + s));
                } else {
                    filePath2 = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + s));
                }
            }
        }

    }

    private void uploadImage(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + DAO_Friebase.getEmailUserSend() + "/avatar/" + FirebaseAuth.getInstance().getUid());
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(EditPersonalPage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        FirebaseStorage.getInstance().getReference().child("images/" + DAO_Friebase.getEmailUserSend() + "/avatar/" + FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference("user/" + DAO_Friebase.getEmailUserSend() + "/avata").setValue(uri.toString());
                                new Thread(() -> dao_sqlite.updateImg(uid, uri.toString())).start();
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("onActivityResult", "onActivityResult: " + e.getMessage());
                    Toast.makeText(EditPersonalPage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
        });
    }

    private void initView() {
        rcy = findViewById(R.id.rcy);
        btnEdit = findViewById(R.id.btnEdit);
        btnAddCoverImage = findViewById(R.id.btnAddCoverImage);
        btnAddStory = findViewById(R.id.btnAddStory);
        btnAddStory2 = findViewById(R.id.btnAddStory2);
        btnEditInformation = findViewById(R.id.btnEditInformation);
        btnAddInterests = findViewById(R.id.btnAddInterests);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgCoverImage = findViewById(R.id.imgCoverImage);
        imgAvatar.setImageBitmap(BitmapFactory.decodeByteArray(dao_sqlite.getImg(uid), 0, dao_sqlite.getImg(uid).length));
        imgCoverImage.setImageBitmap(BitmapFactory.decodeByteArray(dao_sqlite.getImg(uid), 0, dao_sqlite.getImg(uid).length));

    }
}
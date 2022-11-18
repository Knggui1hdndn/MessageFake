package com.example.messagefake.I;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.messagefake.Adapter.AdapterVertiFriend;
import com.example.messagefake.R;
import com.example.messagefake.dataFirebase.DAO_Friebase;
import com.example.messagefake.dataFirebase.DAO_sqlite;
import com.example.messagefake.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonalPage extends AppCompatActivity {
    private Uri filePath;
    RoundedImageView img;
    Button btnCam, btnEditProfile, btnHabitat, btnAddress, btnIntroductoryInformation, btnEditPublicDetails, btnSeeAllFriends;
    TextView txtNumberOfFriends, txtFindFriend, txtName;
    private final int PICK_IMAGE_REQUEST = 71;
    DAO_sqlite dao_sqlite;
    String uid;
    int x = 0;
    SharedPreferences preferences;
    Dialog dialog;
    RecyclerView rcy;
    AdapterVertiFriend adapterVertiFriend;
LottieAnimationView a;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);
        setToolBar();
        dao_sqlite = new DAO_sqlite(this);
        initview();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalPage.this,EditPersonalPage.class));
            }
        });
        btnEditPublicDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalPage.this,EditPersonalPage.class));

            }
        });
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            img.setImageBitmap(BitmapFactory.decodeByteArray(dao_sqlite.getImg(uid), 0, dao_sqlite.getImg(uid).length));

        } catch (Exception ignored) {
        } finally {
            openCamera();
        }
        setAdapter();
    }

    private void setAdapter() {
        List<user> users = new ArrayList<>();
        adapterVertiFriend = new AdapterVertiFriend(user -> {
            Intent intent = new Intent(PersonalPage.this, FriendInformation.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
        adapterVertiFriend.setData(users);
        rcy.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rcy.setAdapter(adapterVertiFriend);
        FirebaseDatabase.getInstance().getReference("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (String s : dao_sqlite.getFriends()) {
                        if (s.equals(Objects.requireNonNull(dataSnapshot.getValue(user.class)).getUID()) && !s.equals(uid)) {
                            users.add(dataSnapshot.getValue(user.class));
                        }
                    }
                }
                if (users.size() > 0) {
                     adapterVertiFriend.setData(users);
                    a.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openCamera() {
        btnCam.setOnClickListener(v -> {
            dialog = createDialog();
            ImageView imageView = dialog.findViewById(R.id.img);
            ImageView imageView1 = dialog.findViewById(R.id.img1);
            dialog.show();
            imageView.setOnClickListener(v12 -> ActivityCompat.requestPermissions(PersonalPage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST));
            imageView1.setOnClickListener(v1 -> {
                ActivityCompat.requestPermissions(PersonalPage.this, new String[]{Manifest.permission.CAMERA}, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        //todo when permission is granted
                    } else {
                        //request for the permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                   }
                }
           });
        });
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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        super.onBackPressed();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.batdau2, R.anim.ketthucfrag);
        }
        return true;
    }

    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables"})
    private Dialog createDialog() {
        Dialog dialog = new Dialog(PersonalPage.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_img);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initview() {
        a = findViewById(R.id.a);
        img = findViewById(R.id.img);
        btnCam = findViewById(R.id.btnCam);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnHabitat = findViewById(R.id.btnHabitat);
        btnAddress = findViewById(R.id.btnAddress);
        btnIntroductoryInformation = findViewById(R.id.btnIntroductoryInformation);
        btnEditPublicDetails = findViewById(R.id.btnEditPublicDetails);
        btnSeeAllFriends = findViewById(R.id.btnSeeAllFriends);
        txtNumberOfFriends = findViewById(R.id.txtNumberOfFriends);
        txtFindFriend = findViewById(R.id.txtFindFriend);
        rcy = findViewById(R.id.rcy);

        txtName = findViewById(R.id.txtName);
        txtName.setText(dao_sqlite.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).get(0).getName());
        txtNumberOfFriends.setText(dao_sqlite.getFriends().size() + " Người bạn");
        try {
            btnHabitat.setText(setTextSpannable("Sống tại " + dao_sqlite.getUser(uid).get(0).getHabitat()));
        } catch (Exception e) {
            btnHabitat.setVisibility(View.GONE);
        } finally {
            try {
                btnAddress.setText(setTextSpannable("Đến từ " + dao_sqlite.getUser(uid).get(0).getAddress()));
            } catch (Exception e) {
                btnAddress.setVisibility(View.GONE);
            }
        }
        ;


    }

    private SpannableString setTextSpannable(String s) {
        SpannableString string = new SpannableString(s);
        string.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), TextUtils.indexOf(s, 'i'), s.length(), 0);
        return string;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
        ) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filePath = data.getData();
                Log.d("onActivityResult", "onActivityResult: " + filePath.getPath());
                uploadImage();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    img.setImageBitmap(bitmap);

                } catch (IOException e) {
                    Log.d("onActivityResult", "onActivityResult: " + e.getMessage());
                }
            } else {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bp);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/");
                file.mkdir();
                String s = System.currentTimeMillis() + ".jpg";
                File file1 = new File(file, s);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                    bp.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filePath = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + s));
                Log.d("onActivityResult", "onActivityResult: " + filePath);
                uploadImage();
            }

        }
    }

    private void download() {
        FirebaseStorage.getInstance().getReference().child("images/" + FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" +DAO_Friebase.getEmailUserSend()+"/avatar/"+ FirebaseAuth.getInstance().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonalPage.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            FirebaseStorage.getInstance().getReference().child("images/" +DAO_Friebase.getEmailUserSend()+"/avatar/"+ FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase.getInstance().getReference("user/" + DAO_Friebase.getEmailUserSend() + "/avata").setValue(uri.toString());
                                    new Thread(() -> dao_sqlite.updateImg(uid, uri.toString())).start();

                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.d("onActivityResult", "onActivityResult: " + e.getMessage());

                            Toast.makeText(PersonalPage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
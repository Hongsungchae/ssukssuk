package com.example.ssukssuk;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;

import android.os.Bundle;

import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ssukssuk.Board.BoardVO;
import com.example.ssukssuk.Diary.DiaryVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaryWrite extends AppCompatActivity {
    Button btn_photo;
    private final int GALLER_CODE = 10;
    ImageView photo;
    private FirebaseStorage storage;
    EditText edt_title,edt_content;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Diary");
    ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_write);

        btn_back = findViewById(R.id.btn_DWA_Back);
        findViewById(R.id.iv_DWA_Photo).setOnClickListener(onClickListener);
        photo = findViewById(R.id.iv_DWA_Photo);
        storage = FirebaseStorage.getInstance();
        edt_content = findViewById(R.id.edt_DWA_Content);
        edt_title = findViewById(R.id.edt_DWA_Title);
        String date;
        btn_photo = findViewById(R.id.btn_DWA_Write);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        int a = 0;
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            DataSnapshot snapshot = task.getResult();

                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String indate = sdf.format(cal.getTime());
                            for (DataSnapshot data : snapshot.getChildren()) {
                                DiaryVO vo = data.getValue(DiaryVO.class);

                                if ((edt_title.getText().toString().equals(vo.getTitle()))) {
                                    Log.d("dfdfdf1", vo.getTitle());
                                    a++;
                                }

                            }
                            Log.d("aaaaaaa", String.valueOf(a));
                            if (a ==0) {
                                String writer = DiaryWrite.this.getSharedPreferences("mySPF", Context.MODE_PRIVATE).
                                        getString("user_login_id1", null);
                                myRef.push().setValue(new BoardVO(
                                        indate,
                                        edt_content.getText().toString(),
                                        writer,
                                        edt_title.getText().toString()
                                ));
                                Intent intent = new Intent(DiaryWrite.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DiaryWrite.this, "동일한 제목이 있습니다.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(DiaryWrite.this, "제목을 수정해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_DWA_Photo:
                    loadAlbum();
                    break;
            }
        }
    };

    private void loadAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLER_CODE) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String indate = sdf.format(cal.getTime());
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            StorageReference riverRef = storageRef.child("images/" + indate + ".jpg");
            UploadTask uploadTask = riverRef.putFile(file);
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                photo.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(DiaryWrite.this, "사진이 정상적으로 업로드 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(DiaryWrite.this, "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                    ImageView img_test = findViewById(R.id.iv_DWA_Photo);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String indate = sdf.format(cal.getTime());
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://ssukssuk-af5d6.appspot.com/");
                    StorageReference storageRef = storage.getReference();
                    storageRef.child("images/" + indate + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //이미지 로드 성공시
                            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(img_test);

                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //이미지 로드 실패시
                    Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

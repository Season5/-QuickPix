package com.scurrae.chris.quickpix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ImageView imageView;
    private DatabaseHandler db;
    static final int CAM_REQUEST = 1;
    private GridView gridView;
    private RecyclerView recyclerView;
    private List<Images> bmpls = new ArrayList<>();
    private List<Bitmap> nk = new ArrayList<>();
    private DatabaseAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button = (Button)findViewById(R.id.open_cam);
//        imageView = (ImageView)findViewById(R.id.grid);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        Firebase.setAndroidContext(this);
        getFire();
        dba = new DatabaseAdapter(this, bmpls);
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setAdapter(dba);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }
    private void openCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Firebase myFirebaseRef = new Firebase("https://shortshotie.firebaseio.com/");
        final Firebase imageRef = myFirebaseRef.child("images");
        // Display image
        Images i = new Images();
        if(resultCode == RESULT_OK){
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bmp);
            String imagefile = Base64.encodeToString(i.getBArray(bmp), Base64.DEFAULT);
            imageRef.push().setValue(imagefile);

//            db.addImage(new Images(bmp));
        }


        dba = new DatabaseAdapter(this, bmpls);

        getFire();

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setAdapter(dba);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    public void getFire(){
        Firebase myFirebaseRef = new Firebase("https://shortshotie.firebaseio.com/");
        final Firebase imageRef = myFirebaseRef.child("images");
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bmpls.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()) {
                    byte[] imageAsBytes = Base64.decode(d.getValue(String.class), Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                    bmpls.add(new Images(bmp));


                    dba.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}

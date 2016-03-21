package com.scurrae.chris.quickpix;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button = (Button)findViewById(R.id.open_cam);
        imageView = (ImageView)findViewById(R.id.grid);
//        gridView = (GridView)findViewById(R.id.gridview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open camera app
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Accessing the image file
                File file = getFile();
                // Returning the image from camera
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                // Begin camera activity
                startActivityForResult(camera_intent, CAM_REQUEST);

            }
        });
    }
    private File getFile(){
        // Instanciate file
        File folder = new File("sdcard/camera_app");
        boolean success = false;
        if(!folder.exists()){
            // Create folder if it doesn't exist
            success = folder.mkdir();
        }
        if(!success){
            Log.d("MAIN", "Folder not Created");
        } else {
            Log.d("MAIN", "Folder  Created");
        }
        // Create image file
        File image_file = new File(folder, "cam_image.jpg");

        return image_file;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Display image
        String path = "sdcard/camera_app/cam_image.jpg";
        Drawable path1 = Drawable.createFromPath(path);
        imageView.setImageDrawable(path1);
        SQLiteDatabase sdb = this.openOrCreateDatabase("image.db", Context.MODE_PRIVATE, null);
        try {
            FileInputStream dis = new FileInputStream(path);
            byte[] ima = new byte[dis.available()];
            dis.read(ima);

            ContentValues values = new ContentValues();
            values.put("a", ima);
            sdb.insert("tb", null, values);
        } catch (IOException e){
            e.printStackTrace();
        }
//        // Convert to byte array
//        Bitmap bmp = BitmapFactory.decodeFile(path);
//        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
//        bmp.recycle();
//        byte[] bytes = bYtE.toByteArray();
//        String imageFile = Base64.encodeToString(bytes, Base64.DEFAULT);
//        db.addImage(new Images(imageFile));
//        // convert image string to jpg
//        List<Images> img = db.getAllImages();
//
//
//        Log.d("Main", "Error");
//        db = new DatabaseHandler(this);
//
//        DatabaseAdapter dba = new DatabaseAdapter(this, img);
////        gridView.setAdapter(dba);
//        recyclerView = (RecyclerView)findViewById(R.id.recycler);
//        recyclerView.setAdapter(dba);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }
}

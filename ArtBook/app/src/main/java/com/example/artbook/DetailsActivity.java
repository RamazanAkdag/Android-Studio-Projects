package com.example.artbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.artbook.databinding.ActivityDetailsBinding;
import com.example.artbook.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding activityDetailsBinding;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        activityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = activityDetailsBinding.getRoot();
        setContentView(view);

        registerLauncher();

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if(info.equals("new")){
            activityDetailsBinding.nameText.setText(" ");
            activityDetailsBinding.ArtistNameText.setText(" ");
            activityDetailsBinding.YearText.setText(" ");

            activityDetailsBinding.button.setVisibility(View.VISIBLE);

            activityDetailsBinding.imageView.setImageResource(R.drawable.selectimageimg);

        }else{
            int artId = intent.getIntExtra("artId",0);
            activityDetailsBinding.button.setVisibility(View.INVISIBLE);
            db = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            try {

                Cursor cursor = db.rawQuery("select * from arts where id = ?", new String[] {String.valueOf(artId)});
                int artnameIx = cursor.getColumnIndex("artname");
                int painternameIx = cursor.getColumnIndex("paintername");
                int yearIx = cursor.getColumnIndex("year");
                int imageIx = cursor.getColumnIndex("image");


                while (cursor.moveToNext()){
                    activityDetailsBinding.nameText.setText(cursor.getString(artnameIx));
                    activityDetailsBinding.ArtistNameText.setText(cursor.getString(painternameIx));
                    activityDetailsBinding.YearText.setText(cursor.getString(yearIx));
                    //activityDetailsBinding.nameText.setText(cursor.getBlob(imageIx));

                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                    activityDetailsBinding.imageView.setImageBitmap(bitmap);

                    System.out.println("activityDetails : " + activityDetailsBinding.nameText.getText());
                }
                cursor.close();

            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }



    public Bitmap makeSmallerImage(Bitmap image,int maximumSize){
        int width =  image.getWidth();
        int height = image.getHeight();

        double bitmapRatio = (double)width/(double) height;

        if(bitmapRatio > 1){
            //yatay resim
            width = maximumSize;
            height = (int)(width / bitmapRatio);


        }else {
            //dikey resim
            height= maximumSize;
            width = (int)(width / bitmapRatio);

        }
        return image.createScaledBitmap(image,width,height,true);
    }

    public void save(View view){

        String name = activityDetailsBinding.nameText.getText().toString();
        String artist = activityDetailsBinding.ArtistNameText.getText().toString();
        String year = activityDetailsBinding.YearText.getText().toString();

        Bitmap smallImage = makeSmallerImage(selectedImage,300);

        //Byte dizisine dönüştürüp sqlitea kaydedeceğiz
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();


        try {
            db = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            db.execSQL("create table if not exists arts(id INTEGER PRIMARY KEY,artname VARCHAR , paintername VARCHAR ,year VARCHAR, image BLOB) ");

            String sql = "insert into arts (artname,paintername,year,image) values (?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sql);
            sqLiteStatement.bindString(1,name);
            sqLiteStatement.bindString(2,artist);
            sqLiteStatement.bindString(3,year);
            sqLiteStatement.bindBlob(4,byteArray);
            sqLiteStatement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
        //Önceki açık aktiviteleri kapat
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void selectImage(View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Android 33 ve üzeri
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                //izin isteme/request permission
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    //izin açıklaması yapmak zorunlu mu ?
                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                        }
                    }).show();

                }else{
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);

                }

            }else {
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }


        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //izin isteme/request permission
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //izin açıklaması yapmak zorunlu mu ?
                    Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                        }
                    }).show();

                }else{
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                }

            }else {
                //gallery
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);
            }
        }



    }

    private void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult =  result.getData();
                    if(intentFromResult != null){
                        Uri imageUri = intentFromResult.getData();
                        //activityDetailsBinding.imageView.setImageURI(imageUri);

                        //resmi bitmapa çevir
                        try {
                            if(Build.VERSION.SDK_INT >= 28){//android sürümüyle alakalı
                                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageUri);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                activityDetailsBinding.imageView.setImageBitmap(selectedImage);
                            }else{
                                selectedImage = MediaStore.Images.Media.getBitmap(DetailsActivity.this.getContentResolver(),imageUri);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //permission granted
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    //permission denied
                    Toast.makeText(DetailsActivity.this, "Permission needed! ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
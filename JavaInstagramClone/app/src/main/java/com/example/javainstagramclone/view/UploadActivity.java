package com.example.javainstagramclone.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.javainstagramclone.R;
import com.example.javainstagramclone.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    ActivityUploadBinding binding;
    ActivityResultLauncher<String> permissionLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher;

    Bitmap selectedImage;

    Uri imageUri;
    private SharedPreferences prefs;
    private boolean permissionDenied;

    private FirebaseStorage fStorage;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fFireStore;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fStorage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fFireStore = FirebaseFirestore.getInstance();
        storageReference = fStorage.getReference();//firebasenin storage kısmındaki boş sayfayı tutuyor bu değişken
        //oraya istediğimiz zaman storageReference.child("images") şeklinde klasör açabiliriz




        registerLaunchers();
    }

    public void uploadButtonClick(View view){
        if(imageUri != null){
            UUID uuid = UUID.randomUUID();
            String imageName = "images/"+ uuid +".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(UploadActivity.this,"Succeess",Toast.LENGTH_SHORT).show();
                    StorageReference newReference = fStorage.getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            String comment = binding.commentText.getText().toString();
                            FirebaseUser user = fAuth.getCurrentUser();
                            String email = user.getEmail();

                            HashMap<String,Object> postData = new HashMap<>();
                            postData.put("userEmail",email);
                            postData.put("downloadUrl",downloadUri);
                            postData.put("comment",comment);
                            postData.put("date", FieldValue.serverTimestamp());

                            fFireStore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(UploadActivity.this,FeedActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(UploadActivity.this,"resim null",Toast.LENGTH_SHORT).show();
        }

    }

    public void selectImage(View view){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(UploadActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_MEDIA_IMAGES)) {
                    Snackbar.make(view,"need to permission for search gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();

                }else{
                    permissionLauncher.launch(
                            Manifest.permission.READ_MEDIA_IMAGES);
                }





            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);


            }


        }else{
            if (ContextCompat.checkSelfPermission(
                    UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view,"need to permission for search gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();

                }else{
                    permissionLauncher.launch(
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }



            } else {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);


            }

        }

    }

    private void registerLaunchers(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == RESULT_OK){
                    Intent intentFromResult = o.getData();

                    if(intentFromResult != null){
                        imageUri = intentFromResult.getData();
                        binding.imageView.setImageURI(imageUri);
                       /* try {
                            if(Build.VERSION.SDK_INT >= 28){
                                ImageDecoder.Source source=  ImageDecoder.createSource(getContentResolver(),imageUri);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);


                            }else{
                                selectedImage = MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(),imageUri);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }*/
                    }
                }else{

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
                        Toast.makeText(UploadActivity.this, "Permission needed", Toast.LENGTH_SHORT).show();



                    }
            }
        });
    }

}
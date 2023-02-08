package com.fazil.labca1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText mResultEt;
    ImageView mPreviewIv;

    private static final int CAMERA_REQUEST_CODE=200;
    private static final int STORAGE_REQUEST_CODE=400;
    private static final int IMAGE_PICK_GALLERY_CODE=1000;
    private static final int IMAGE_PICK_CAMERA_CODE=1001;

    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    Button buttonOpenCamera, buttonOpenGallery, buttonToCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LightTheme);
        setContentView(R.layout.activity_main);

        // REMOVE DARK MODE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // ACTION BAR
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.gradient));
        getSupportActionBar().setElevation(0);
        // ACTION BAR

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                //Intent is used to switch from one activity to another.

                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Remove multiple opening of activity

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, 1500);

//        mResultEt = findViewById(R.id.resultEt);
//        mPreviewIv = findViewById(R.id.imageIv);
//
//        mResultEt.setText("Click on the Camera or Gallery button to convert image to HTML.");
//
//        //Camera permission
//        cameraPermission = new String[]{Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        //Storage permission
//        storagePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        // OPEN CAMERA
//        buttonOpenCamera = findViewById(R.id.button_open_camera);
//        buttonOpenCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //camera option clicked
//                if(!checkCameraPermission()){
//                    //camera permission not allowed, request it
//                    requestCameraPermission();
//                }
//                else{
//                    //Permission allowed, take picture
//                    pickCamera();
//                }
//            }
//        });
//
//        // OPEN GALLERY
//        buttonOpenGallery = findViewById(R.id.button_open_gallery);
//        buttonOpenGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //gallery option clicked
//                if(!checkStoragePermission()){
//                    //storage permission not allowed, request it
//                    requestStoragePermission();
//                }
//                else{
//                    //Permission allowed, take picture
//                    pickGallery();
//                }
//            }
//        });
//
//        // COPY THE TEXT
//        buttonToCopy = findViewById(R.id.button_to_copy);
//        buttonToCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("textToCopy", mResultEt.getText().toString());
//                clipboard.setPrimaryClip(clip);
//                Toast.makeText(MainActivity.this, "Text Copied Successfully!!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//    //actionbar menu
//
//
//    private void pickGallery() {
//        //intent to pick image from gallery
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        //set intent type to image
//        intent.setType("image/*");
//        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
//    }
//
//    private void pickCamera() {
//        //intent to take image from camera, it will also be save to storage to get high quality image
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "NewPic"); //Title of the picture
//        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text"); // Description
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
//        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
//    }
//
//    private void requestStoragePermission() {
//        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE);
//    }
//
//    private boolean checkStoragePermission() {
//        boolean result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//
//        return result;
//    }
//
//    private void requestCameraPermission() {
//        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE);
//    }
//
//    private boolean checkCameraPermission() {
//        boolean result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result && result1;
//    }
//
//    //handle permission result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case CAMERA_REQUEST_CODE:
//                if (grantResults.length > 0) {
//                    boolean cameraAccepted = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageAccepted = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && writeStorageAccepted) {
//                        pickCamera();
//                    } else {
//                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//
//            case STORAGE_REQUEST_CODE:
//                if (grantResults.length > 0) {
//                    boolean writeStorageAccepted = grantResults[0] ==
//                            PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageAccepted) {
//                        pickGallery();
//                    } else {
//                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//    }
//
//    //handle image result
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode==RESULT_OK){
//            if(requestCode==IMAGE_PICK_GALLERY_CODE){
//                //got image from gallery now crop it
//                CropImage.activity(data.getData())
//                        .setGuidelines(CropImageView.Guidelines.ON) // enable image guidelines
//                        .start(this);
//
//            }
//            if(requestCode==IMAGE_PICK_CAMERA_CODE){
//                //got image from camera now crop it
//                CropImage.activity(image_uri)
//                        .setGuidelines(CropImageView.Guidelines.ON) // enable image guidelines
//                        .start(this);
//
//            }
//        }
//
//        //get cropped image
//        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result= CropImage.getActivityResult(data);
//            if(resultCode==RESULT_OK){
//                Uri resultUri=result.getUri(); //get image uri
//                //set image to image view
//                mPreviewIv.setImageURI(resultUri);
//
//                //get drawable bitmap for text recognition
//                BitmapDrawable bitmapDrawable = (BitmapDrawable)mPreviewIv.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
//
//                TextRecognizer recognizer= new TextRecognizer.Builder(getApplicationContext()).build();
//                if(!recognizer.isOperational()){
//                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                    SparseArray<TextBlock> items = recognizer.detect(frame);
//                    StringBuilder sb = new StringBuilder();
//                    //get text from sb until there is no text
//                    for(int i = 0; i<items.size(); i++){
//                        TextBlock myItem=items.valueAt(i);
//                        sb.append(myItem.getValue());
//                        sb.append("\n");
//                    }
//                    //set text to edit text
//                    mResultEt.setText(sb.toString());
//                }
//
//            }
//            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                //if there is any error show it
//                Exception error = result.getError();
//                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
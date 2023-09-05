package com.example.roomates.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.roomates.R;
import com.example.roomates.databinding.ActivitySignUpBinding;
import com.example.roomates.utilities.Constants;
import com.example.roomates.utilities.PreferenceManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();
    }
    private void setListener(){
        binding.textSignIn.setOnClickListener(v->onBackPressed());
        binding.buttonSignUp.setOnClickListener(v->{
            if(isValidSignUpDetails()){
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);


        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                        loading(false);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                        preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
                        preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                        preferenceManager.putString(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("id",documentReference.getId());
                        hashMap.put("userName",preferenceManager.getString(Constants.KEY_NAME));
                        hashMap.put("imageUrl",preferenceManager.getString(Constants.KEY_IMAGE));
                        hashMap.put("email",preferenceManager.getString(Constants.KEY_EMAIL));
                        reference.child(documentReference.getId()).setValue(hashMap);
                        Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                })
                .addOnFailureListener(exception ->{
                        loading(false);
                        showToast(exception.getMessage());
                });

    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight= bitmap.getHeight() * previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewHeight,previewWidth,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        }
                        catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if(encodedImage==null) {
            showToast("Select profile image");
            return false;
        }
        else if(binding.inputName.getText().toString().trim().isEmpty()){
        showToast("Enter name");
        return false;
        }
        else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Enter email");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter valid image");
            return false;
        }
        else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Confirm your password");
            return false;
        }
        else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password and Confirm Password must be same");
            return false;
        }
        return true;
    }

    private void loading(Boolean isLoading){
        if(isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
                binding.buttonSignUp.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

package doan.cuoiki.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

import doan.cuoiki.R;

public class EditProfile extends AppCompatActivity {

    private EditText editName, editEmail, ediPhonenumber, editPassword;
    private Button saveButton;

    ImageButton imageButton;

    FirebaseAuth auth;

    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();





        editName = findViewById(R.id.editName);
//        editEmail = findViewById(R.id.editEmail);
        //ediPhonenumber = findViewById(R.id.ediPhonenumber);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);


        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    //String PhoneNumber = ediPhonenumber.getText().toString().trim();
                    String newPass = editPassword.getText().toString().trim();
                    String newName = editName.getText().toString();
                    if(newPass.isEmpty() || newName.isEmpty())
                    {
                        Toast.makeText(EditProfile.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        updatePassword(newPass);
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newName)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Thay đổi tên thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Thay đổi tên thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Xử lý trường hợp người dùng là null
                    Toast.makeText(EditProfile.this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void updateMail(String email) {
       user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()) {
                   Toast.makeText(EditProfile.this, "Thay đổi email thành công", Toast.LENGTH_SHORT).show();
               } else {
                   Exception e = task.getException();
                   Toast.makeText(EditProfile.this, "Thay đổi email thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });

    }


    private void updateName(String newName)
    {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build();
        user.updateProfile(profileUpdates)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thay đổi tên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Thay đổi tên thất bại", Toast.LENGTH_SHORT).show();
                }
            });
    }


    private void updatePassword(String newPass) {
        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfile.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
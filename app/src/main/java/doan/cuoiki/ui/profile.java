package doan.cuoiki.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import doan.cuoiki.R;

public class profile extends AppCompatActivity {

    private TextView textView_show_welcome, textView_show_full_name,textView_show_dob,textView_show_email,textView_show_mobile, textView_show_gender;
    private ProgressBar progressBar;
    private String fullName, email, doB,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    Button editButton;

    BottomNavigationView bottomNavigationView;

    FloatingActionButton btn_camera;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView_show_welcome = findViewById(R.id.titleName);
        //textView_show_gender = findViewById(R.id.textView_show_number);
        textView_show_email = findViewById(R.id.textView_show_email);
        textView_show_dob = findViewById(R.id.textView_show_dob);
        textView_show_full_name = findViewById(R.id.textView_show_full_name);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.home_item);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Thay thế switch-case cũ bằng switch-case mới
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
                // Xử lý khi itemId là R.id.home_item
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.list_item) {
                // Xử lý khi itemId là R.id.list_item
                startActivity(new Intent(getApplicationContext(), ListFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.search_bar) {
                // Xử lý khi itemId là R.id.search_bar
                startActivity(new Intent(getApplicationContext(), SearchFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.user) {
                // Xử lý khi itemId là R.id.search_bar

                return true;
            }else {
                return false;
            }


        });



        imageView = (ImageView) findViewById(R.id.profileImg);
        btn_camera =   (FloatingActionButton) findViewById(R.id.float_btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(profile.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        editButton = (Button) findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi nút được click
                // Ví dụ: Chuyển sang màn hình chỉnh sửa hồ sơ
                Intent intent = new Intent(profile.this, EditProfile.class);
                startActivity(intent);
            }
        });


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        showUserProfile(firebaseUser);

        if (firebaseUser == null){
            Toast.makeText(profile.this,"some thing went wrong! User's details are not available at the moment ",
                    Toast.LENGTH_SHORT ).show();

        } else {
            //ProgressBar progressBar = findViewById(R.id.progressBar);
//            if (progressBar != null) {
//                progressBar.setVisibility(View.VISIBLE); // Hoặc View.GONE, View.INVISIBLE tùy thuộc vào yêu cầu của bạn
//            }
            //showUserProfile(firebaseUser);
            //textView_show_email.setText(firebaseUser.getEmail());
            //Fname.setText("Mật khẩu: Đã ẩn vì lý do bảo mật");
            showUserProfile(firebaseUser);

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imageView.setImageURI(uri);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        textView_show_welcome.setText("Welcome, " + firebaseUser.getDisplayName() + "!");
        textView_show_full_name.setText(firebaseUser.getDisplayName());
        textView_show_email.setText(firebaseUser.getEmail());

        ///Extracting user refernce from databasr for "dk users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User's");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    fullName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.moblie;

                    textView_show_welcome.setText("Welcome, " + fullName + "!");
                    textView_show_full_name.setText(fullName);
                    textView_show_dob.setText(doB);
                    textView_show_email.setText(email);
                    //textView_show_email.setText(firebaseUser.getEmail().toString());
                    textView_show_gender.setText(gender);
                    textView_show_mobile.setText(mobile);
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this,"Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
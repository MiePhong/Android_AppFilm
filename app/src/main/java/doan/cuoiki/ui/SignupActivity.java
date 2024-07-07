package doan.cuoiki.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import doan.cuoiki.R;

public class SignupActivity extends AppCompatActivity {

    EditText siginup_email, siginup_password1, siginup_password2;
    Button signup_button;
    TextView LoginText;
    String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z]+\\.[a-zA-Z]+$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        addEvents();
        addControls();
    }


    private void addEvents(){
        siginup_email= (EditText) findViewById(R.id.siginup_email);
        siginup_password1=(EditText) findViewById(R.id.siginup_password1);
        siginup_password2= (EditText) findViewById(R.id.siginup_password2);
        signup_button=(Button) findViewById(R.id.signup_button);
        //LoginText=(TextView) findViewById(R.id.LoginText);
        progressDialog = new ProgressDialog(this);
    }
    private  void addControls() {
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });
    }
    private  void PerforAuth() {
        String email = siginup_email.getText().toString();
        String password = siginup_password1.getText().toString();
        String changespassword = siginup_password2.getText().toString();

        if (!email.matches(emailPattern)){
            siginup_email.setError("Nhập email");
        }
        else if (password.isEmpty()|| password.length()<6){
            siginup_password1.setError("Nhập đúng mật khẩu");
        }
        else if (!password.equals(changespassword)){
            siginup_password2.setError("Mật khẩu không khớp");
        }
        else{
            progressDialog.setMessage("Vui lòng chờ vài giây. Đăng kí đang tiến hành...");
            progressDialog.setTitle("Đăng kí");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }

                private void sendUserToNextActivity() {
                    Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            });
        }
    }
}
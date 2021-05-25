package com.example.firebaselab8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText email, name, password, repassword;
    private Button btnRegister;
    private TextView textSignin;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getSupportActionBar().hide();

        email = findViewById(R.id.RegisterEmail);
        name = findViewById(R.id.RegisterName);
        password = findViewById(R.id.RegisterPassword);
        repassword = findViewById(R.id.RegisterPassword2);
        btnRegister = findViewById(R.id.btnDK);
        textSignin = findViewById(R.id.textLogin);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    String emailValue = email.getText().toString().trim();
                    String nameValue = name.getText().toString().trim();
                    String passwordValue = password.getText().toString().trim();

                    auth.createUserWithEmailAndPassword(emailValue, passwordValue)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        firebaseDatabase.getReference("app_title").setValue("boring_database");
                                        String uid = auth.getCurrentUser().getUid();
                                        User user = new User(uid, nameValue, emailValue);
                                        databaseReference = firebaseDatabase.getReference("users");
                                        databaseReference.child(uid).setValue(user);
                                        startActivity(new Intent(Register.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "Đăng ký tài khoản không thành công: "
                                                + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
        textSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Signin.class));
                finish();
            }
        });
    }
        private boolean valid() {
            String emailValue = email.getText().toString().trim();
            String nameValue = name.getText().toString().trim();
            String passwordValue = password.getText().toString().trim();
            String rePasswordValue = repassword.getText().toString().trim();

            if (nameValue.equals("")) {
                name.setError("Tên không được để trống");
                return false;
            }

            if (emailValue.equals("")) {
                email.setError("Email không được để trống");
                return false;
            }

            if (passwordValue.equals("")) {
                password.setError("Mật khẩu không được để trống");
                return false;
            }
            if (rePasswordValue.equals("")) {
                repassword.setError("Xác nhận lại mật khẩu");
                return false;
            }
            if (!passwordValue.equals(rePasswordValue)) {
                repassword.setError("Mật khẩu không khớp nhau");
                return false;
            }
            return true;

    }
}

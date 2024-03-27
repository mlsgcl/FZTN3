package com.app.fztn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
public class MainActivity extends AppCompatActivity {

    private EditText yeniKullaniciMail, yeniKullanicisifre, yeniKullaniciİsim;
    private String txtEmail, txtSifre, txtİsim;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private HashMap<String, Object> mData;
    private FirebaseFirestore mFirestore;
    private DocumentReference docRef; //tek bir kullanıcı verisi okumak için

    TextView forgotPassword, createAccount;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, KayitActivity2.class);
                startActivity(register);
            }
        });







        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnSifirla).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail = emailBox.getText().toString();
                        if (!TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(MainActivity.this, "Email id giriniz", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "E mailinizi kontrol edin", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Gönderim yapılamadı,failed", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                    }
                });
                dialogView.findViewById(R.id.btnIptal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                }
                dialog.show();
            }
        });


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


    }

    private void Login() {
        txtEmail = yeniKullaniciMail.getText().toString();
        txtSifre = yeniKullanicisifre.getText().toString();

        if (TextUtils.isEmpty(txtEmail)) {
            yeniKullaniciMail.setError("E-posta adresiniz boş olamaz!");
            return;
        } else if (TextUtils.isEmpty(txtSifre)) {
            yeniKullanicisifre.setError("Parolanız boş olamaz");
            return;
        } else {
            mAuth.signInWithEmailAndPassword(txtEmail, txtSifre).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent giris = new Intent(getApplicationContext(), GirisActivity2.class);
                    startActivity(giris);
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // reload();
        }
    }
    public void init() {
        createAccount = findViewById(R.id.kayit_button);
        forgotPassword = findViewById(R.id.sifremi_unuttum);
        yeniKullaniciMail = findViewById(R.id.kullanici_mail);
        yeniKullanicisifre = findViewById(R.id.kullanici_parola);
        yeniKullaniciİsim = findViewById(R.id.kayit_kulad);
        loginButton = findViewById(R.id.giris_btn);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

    }
}
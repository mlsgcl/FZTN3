package com.app.fztn;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NeyimVarActivity extends AppCompatActivity {
    private DbAdapter dbAdapter;
    private Spinner spinnerAgrıBolgesi, spinnerAgrıDerece, spinnerAgrıSekli, spinnerAgrıSüresi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neyim_var);

        dbAdapter = new DbAdapter(this) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        //dbAdapter.open();

        // Spinner bileşenlerini tanımlayın ve değer ataması yapın
        spinnerAgrıBolgesi = findViewById(R.id.spinnerAgrıBolgesi);
        spinnerAgrıDerece = findViewById(R.id.spinnerAgrıDerece);
        spinnerAgrıSekli = findViewById(R.id.spinnerAgrıŞekli);
        spinnerAgrıSüresi = findViewById(R.id.spinnerAgrıSüresi);

        Button myButton = findViewById(R.id.buttonSave);







        // Button'a tıklama dinleyicisi ekleyin
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kaydet();
            }
        });
    }

    private String getFirebaseUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            return currentUser.getUid();
        }

        // Kullanıcı oturum açmamışsa veya bir hata oluştuysa null veya başka bir değer döndürebilirsiniz.
        return null;
    }



    private void kaydet() {
        String userId = getFirebaseUserId();
        String agriBolgesi = spinnerAgrıBolgesi.getSelectedItem().toString();
        String agriDerece = spinnerAgrıDerece.getSelectedItem().toString();
        String agriSekli = spinnerAgrıSekli.getSelectedItem().toString();
        String agriSuresi = spinnerAgrıSüresi.getSelectedItem().toString();

        // Veritabanına bilgileri ekle
        long result = dbAdapter.insertAgrilar(userId, agriBolgesi, agriDerece, agriSekli, agriSuresi);
        if (result != -1) {
            // Başarıyla eklendi
           // Toast.makeText(this, "Bilgiler başarıyla eklendi", Toast.LENGTH_SHORT).show();
        } else {
            // Hata oluştu
            Toast.makeText(this, "Bilgiler eklenirken bir hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }


}

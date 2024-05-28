package com.app.fztn;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NeyimVarActivity extends AppCompatActivity {
    private DbAdapter dbAdapter;
    private Spinner spinnerAgrıBolgesi, spinnerSemptomlar, spinnerAgrıDerece, spinnerAgrıSekli, spinnerAgrıSüresi;


    public NeyimVarActivity() {}

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
        dbAdapter.open();

        // Spinner bileşenlerini tanımlayın ve değer ataması yapın
        spinnerAgrıBolgesi = findViewById(R.id.spinnerAgrıBolgesi);
        spinnerSemptomlar = findViewById(R.id.spinnerSemptomlar);
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String agriBolgesi = spinnerAgrıBolgesi.getSelectedItem().toString();
        String semptomlar = spinnerSemptomlar.getSelectedItem().toString();
        String agriDerece = spinnerAgrıDerece.getSelectedItem().toString();
        String agriSekli = spinnerAgrıSekli.getSelectedItem().toString();
        String agriSuresi = spinnerAgrıSüresi.getSelectedItem().toString();
        long result = dbAdapter.insertAgrilar(userId, agriBolgesi, semptomlar, agriDerece, agriSekli, agriSuresi);

        // Önerilen video URL'sini belirleyin
        String recommendedVideoUrl = "";

        if (result != -1) {
            // Başarıyla eklendi
            Toast.makeText(this, "Bilgiler başarıyla eklendi", Toast.LENGTH_SHORT).show();


            // Kullanıcı seçimlerine bağlı olarak URL'yi belirleyin
            if (agriBolgesi.equals("Omuz") && semptomlar.equals("uyuşma") && Integer.parseInt(agriDerece) >= 5 && agriSekli.equalsIgnoreCase("Batıcı") && agriSuresi.equals("Kronik")) {
                recommendedVideoUrl = "https://youtube.com/shorts/hTfMSrMHtq8?si=sC9sHPgvhwomPJMy";
            } else if ((agriBolgesi.equals("Baş") || agriBolgesi.equals("Boyun")) && semptomlar.equals("güçsüzlük") && Integer.parseInt(agriDerece) >= 5 && agriSekli.equalsIgnoreCase("iğneleyici") && agriSuresi.equals("Kronik")) {
                recommendedVideoUrl = "https://youtube.com/shorts/vMvPXYiXvFU?si=VmysZEZNGZQCvwjG";
            } else if (agriBolgesi.equals("Bel") && semptomlar.equals("güçsüzlük") && Integer.parseInt(agriDerece) >= 5 && agriSekli.equalsIgnoreCase("Batıcı") && agriSuresi.equals("Kronik")) {
                recommendedVideoUrl = "https://www.youtube.com/shorts/RgyrfYbYvxY";
            } else if (agriBolgesi.equals("Bacak") && semptomlar.equals("uyuşma") && Integer.parseInt(agriDerece) >= 5 && agriSekli.equalsIgnoreCase("yanıcı") && agriSuresi.equals("Kronik")) {
                recommendedVideoUrl = "https://www.youtube.com/shorts/v7pZO4VpzJU";
            } else if (agriBolgesi.equals("El") && semptomlar.equals("uyuşma") && Integer.parseInt(agriDerece) >= 5 && agriSekli.equalsIgnoreCase("batıcı") && agriSuresi.equals("Kronik")) {
                recommendedVideoUrl = "https://www.youtube.com/shorts/cyDYR05Rw3A";
            }

            Log.d("", "URL: " + recommendedVideoUrl);

            }else {
                // Hata oluştu
            }

        long result2 = dbAdapter.insertRecommendedVideo(userId, recommendedVideoUrl);
        if (result2 != -1) {
            Log.d("", "URL: " + recommendedVideoUrl + " başarıyla veritabanına kaydedildi.");
        } else {
            Log.d("", "URL: " + recommendedVideoUrl + " veritabanına kaydedilirken bir hata oluştu.");
        }
        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}

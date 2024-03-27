package com.app.fztn;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RandevuFragment extends Fragment {

    private DatePicker datePicker;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private Button buttonRandevuAl;
    private SharedPreferences sharedPreferences;
    private DbAdapter dbHelper;
    private FirebaseAuth firebaseAuth;

    public RandevuFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_randevu, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        hourPicker = view.findViewById(R.id.hourPicker);
        minutePicker = view.findViewById(R.id.minutePicker);
        buttonRandevuAl = view.findViewById(R.id.buttonRandevuAl);

        // Saat aralığını ayarlayabilirsiniz (örneğin, 0-23 saat)
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        // Dakika aralığını ayarlayabilirsiniz (örneğin, 0-59 dakika)
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        sharedPreferences = getActivity().getSharedPreferences("RandevuSharedPreferences", Context.MODE_PRIVATE);
        dbHelper = new DbAdapter(getContext()) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        firebaseAuth = FirebaseAuth.getInstance();

        buttonRandevuAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.open();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                int hour = hourPicker.getValue();
                int minute = minutePicker.getValue();

                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, day, hour, minute);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());


                String selectedDateStr = dateFormat.format(selectedDateTime.getTime());
                String selectedTimeStr = timeFormat.format(selectedDateTime.getTime());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("savedDateTime", selectedDateStr + " " + selectedTimeStr);
                editor.apply();
                // Kontrol: Seçilen saatte başka bir randevu var mı?
                if (dbHelper.isHourOccupied(selectedDateStr, selectedTimeStr)) {
                    Toast.makeText(getContext(), "Seçilen saatte başka bir randevu bulunmaktadır. Lütfen başka bir saat seçiniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kontrol: Seçilen tarih ve saat aynı mı?
                if (dbHelper.isSameDateTimeOccupied(selectedDateStr, selectedTimeStr)) {
                    Toast.makeText(getContext(), "Seçilen tarih ve saat başka bir randevuda kullanılmaktadır. Lütfen başka bir tarih veya saat seçiniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "Randevu Alındı: " + selectedDateStr + " " + selectedTimeStr, Toast.LENGTH_SHORT).show();


                //Toast.makeText(getContext(), "Randevu Alındı: " + selectedDateStr + " " + selectedTimeStr, Toast.LENGTH_SHORT).show();

                FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();






                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userId = user.getUid();

// Kullanıcı ID'sine ait satırın var olup olmadığını kontrol et
                if (dbHelper.userExists(userId)) {
                    // Satır varsa güncelle
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("randevu_tarih", selectedDateStr);
                    updateValues.put("randevu_saat", selectedTimeStr);

                    boolean updatedRows = dbHelper.updateUser("RANDEVU", updateValues, "user_id=?", new String[]{userId});

                    if (updatedRows ==true) {
                        Toast.makeText(getContext(), "Tarih ve saat güncellendi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Tarih ve saat güncellenemedi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Satır yoksa yeni bir satır ekle
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("user_id", userId);
                    values.put("randevu_tarih", selectedDateStr);
                    values.put("randevu_saat", selectedTimeStr);

                    long newRowId = db.insert("RANDEVU", null, values);

                    if (newRowId != -1) {
                        Toast.makeText(getContext(), "Tarih ve saat kaydedildi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Tarih ve saat kaydedilemedi", Toast.LENGTH_SHORT).show();
                    }


                    dbHelper.close();




                }
            }

        });
        return view;
    }}

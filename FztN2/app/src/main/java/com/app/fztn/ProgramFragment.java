package com.app.fztn;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProgramFragment extends Fragment {
    private DbAdapter dbAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_program, container, false);

        dbAdapter = new DbAdapter(getActivity()) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        dbAdapter.open();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        Button buttonVideo = view.findViewById(R.id.Video);
        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String recommendedVideoUrl = dbAdapter.getRecommendedVideoUrl(userId);

                // Başlık ve URL'yi göstermek için gerekli bileşenleri bulun
                WebView webViewVideoPlayer = view.findViewById(R.id.webViewVideoPlayer);


                webViewVideoPlayer.loadUrl(recommendedVideoUrl);



            }
        });



        return view;
    }}




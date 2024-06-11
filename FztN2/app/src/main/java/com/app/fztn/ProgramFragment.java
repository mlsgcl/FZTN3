package com.app.fztn;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
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



        LinearLayout linearLayout1 = view.findViewById(R.id.linearlayout1);
        WebView webViewVideoPlayer = view.findViewById(R.id.webViewVideoPlayer);

        // JavaScript'i etkinleştir
        WebSettings webSettings = webViewVideoPlayer.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // WebViewClient ve WebChromeClient ayarla
        webViewVideoPlayer.setWebViewClient(new WebViewClient());
        webViewVideoPlayer.setWebChromeClient(new WebChromeClient());

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kullanıcı ID'sini burada alabilirsiniz
                String recommendedVideoUrl = dbAdapter.getRecommendedVideoUrl(userId);
                // WebView'i görünür yap ve URL'yi yükle
                webViewVideoPlayer.setVisibility(View.VISIBLE);
                webViewVideoPlayer.loadUrl(recommendedVideoUrl);
            }
        });



        return view;
    }}




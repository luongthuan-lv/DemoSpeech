package com.example.demospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private Spinner spn;
    private MySpinnerAdapter mySpinnerAdapter;
    private Language language;
    private String languageCode, voiceName;
    private List<Language> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.btn).setOnClickListener(this);
        spn = findViewById(R.id.spn);

        addLanguage();
        setAdapter();
        setSpinner();
    }

    private void addLanguage() {
        languageList.add(new Language("Tiếng Việt", "vi-VN", "vi-VN-Wavenet-C"));
        languageList.add(new Language("Tiếng Anh", "en-GB", "en-GB-Standard-A"));
        languageList.add(new Language("Tiếng Nhật", "ja-JP", "ja-JP-Wavenet-B"));
        languageList.add(new Language("Tiếng Trung", "yue-HK", "yue-HK-Standard-C"));
        languageList.add(new Language("Tiếng Hàn", "ko-KR", "ko-KR-Wavenet-A"));
        languageList.add(new Language("Tiếng Pháp", "fr-FR", "fr-FR-Standard-C"));
        languageList.add(new Language("Tiếng Đức", "de-DE", "de-DE-Standard-F"));
        languageList.add(new Language("Tiếng Indonesia", "id-ID", "id-ID-Standard-A"));
        languageList.add(new Language("Tiếng Nga", "ru-RU", "ru-RU-Standard-A"));
    }

    private void setAdapter() {
        mySpinnerAdapter = new MySpinnerAdapter(this, languageList);
    }

    private void setSpinner() {
        spn.setAdapter(mySpinnerAdapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // nguoi dung click va chon 1 hang trong danh sach
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                language = languageList.get(position);
                languageCode = language.getLanguageCode();
                voiceName = language.getVoiceName();
            }


            // neu nguoi dung mo spinner ma ko chon gi
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                Intent intent = new Intent(this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("languageCode",languageCode);
                bundle.putString("voiceName", voiceName);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
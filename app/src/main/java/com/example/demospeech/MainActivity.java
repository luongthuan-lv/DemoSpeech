package com.example.demospeech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements MainContract.IView {
    private static final int TEXT_TO_SPEECH_CODE = 0x100;

    private MainContract.IPresenter mPresenter;
    private EditText mEditText;
    private Button mButtonSpeak;
    private Button mButtonSpeakPause;
    private Button mButtonSpeakStop;
    private String languageCode, voiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBundle();
        buildViews();
        mPresenter = new MainPresenter(MainActivity.this, languageCode, voiceName);
        mPresenter.onCreate();
        initAndroidTTS();
    }

    private void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        languageCode = bundle.getString("languageCode");
        voiceName = bundle.getString("voiceName");

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private void buildViews() {




        mEditText = findViewById(R.id.ettIdText);
        mButtonSpeak = findViewById(R.id.btnIdSpeak);
        mButtonSpeakPause = findViewById(R.id.btnIdPauseAndResume);
        mButtonSpeakStop = findViewById(R.id.btnIdStop);

        mButtonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startSpeak(mEditText.getText().toString());
            }
        });


        mButtonSpeakPause.setOnClickListener((view) -> {
            if (mButtonSpeakPause.getText().toString().compareTo(getResources().getString(R.string.btnPtSpeechPause)) == 0) {
                mPresenter.pauseSpeak();
                mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechResume));
            } else {
                mPresenter.resumeSpeak();
                mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechPause));
            }
        });

        mButtonSpeakStop.setOnClickListener((view) -> mPresenter.stopSpeak());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // init android tts
        if (requestCode == TEXT_TO_SPEECH_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mPresenter.initAndroidTTS();
                return;
            }
            Intent installIntent = new Intent();
            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installIntent);
        }
    }


    @Override
    public int getProgressPitch() {
        return 1500;
    }

    @Override
    public int getProgressSpeakRate() {
        return 75;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.pauseSpeak();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.resumeSpeak();
    }

    @Override
    public void invoke(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    @Override
    public void setPresenter(MainContract.IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initAndroidTTS() {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TEXT_TO_SPEECH_CODE);
    }
}
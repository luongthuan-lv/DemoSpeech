package com.example.demospeech.modol;

import android.content.Context;

import com.example.demospeech.googlecloudtts.GoogleCloudAPIConfig;
import com.example.demospeech.googlecloudtts.GoogleCloudTTS;

import java.util.ArrayList;
import java.util.List;
/**
 * Author: Changemyminds.
 * Date: 2018/6/25.
 * Description:
 * Reference:
 */
public class GoogleCloudTTSAdapter extends GoogleCloudTTS implements ISpeech, GoogleCloudTTS.ISpeakListener {
    private List<ISpeechListener> mSpeechListeners = new ArrayList<>();

    // lấy context từ bên presenter rồi truyền qua bên GoogleCloudTTS
    public GoogleCloudTTSAdapter(GoogleCloudAPIConfig apiConfig, Context context) {
        super(context,apiConfig);
        addSpeakListener(this);
    }

    @Override
    public void start(String text) {
        super.start(text);
    }

    @Override
    public void resume() {
        super.resumeAudio();
    }

    @Override
    public void pause() {
        super.pauseAudio();
    }

    @Override
    public void stop() {
        super.stopAudio();
    }

    @Override
    public void exit() {
        super.exit();
        removeSpeakListener(this);
        mSpeechListeners.clear();
    }

    @Override
    public void addSpeechListener(ISpeechListener speechListener) {
        mSpeechListeners.add(speechListener);
    }

    @Override
    public void removeSpeechListener(ISpeechListener speechListener) {
        mSpeechListeners.remove(speechListener);
    }

    @Override
    public void onSuccess(String message) {
        for (ISpeechListener speechListener : mSpeechListeners) {
            speechListener.onSuccess(message);
        }
    }

    @Override
    public void onFailure(String message, Exception e) {
        for (ISpeechListener speechListener : mSpeechListeners) {
            speechListener.onFailure(message, e);
        }
    }
}

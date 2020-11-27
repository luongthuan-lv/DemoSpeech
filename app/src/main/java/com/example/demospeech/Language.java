package com.example.demospeech;

public class Language {
    private String languageName;
    private String languageCode;
    private String voiceName;

    public Language(String languageName, String languageCode, String voiceName) {
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.voiceName = voiceName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }
}

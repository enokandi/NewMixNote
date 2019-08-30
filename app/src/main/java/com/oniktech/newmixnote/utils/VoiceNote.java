package com.oniktech.newmixnote.utils;

public class VoiceNote {
    private int id;
    private int noteId;
    private String voiceName;
    private String voiceAddress;

    public VoiceNote(int id, int noteId, String voiceName, String voiceAddress) {
        this.id = id;
        this.noteId = noteId;
        this.voiceName = voiceName;
        this.voiceAddress = voiceAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getVoiceAddress() {
        return voiceAddress;
    }

    public void setVoiceAddress(String voiceAddress) {
        this.voiceAddress = voiceAddress;
    }
}

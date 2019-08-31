package com.oniktech.newmixnote.utils;

public class TextNote {
    private int id;
    private int noteId;
    private String text;

    public TextNote(int id, int noteId, String text) {
        this.id = id;
        this.noteId = noteId;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

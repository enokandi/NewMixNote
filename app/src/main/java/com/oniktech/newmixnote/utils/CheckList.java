package com.oniktech.newmixnote.utils;

public class CheckList {
    private int id;
    private int noteId;
    private String checkName;
    private String checked;

    public CheckList(int id, int noteId, String checkName, String checked) {
        this.id = id;
        this.noteId = noteId;
        this.checkName = checkName;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getCheckName() {
        return checkName;
    }

    public String isChecked() {
        return checked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}

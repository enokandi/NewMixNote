package com.oniktech.newmixnote.utils;

public class PictureNote {
    private int id;
    private int noteId;
    private String pictureName;
    private String pictureAddress;

    public PictureNote(int id, int noteId, String pictureName, String pictureAddress) {
        this.id = id;
        this.noteId = noteId;
        this.pictureName = pictureName;
        this.pictureAddress = pictureAddress;
    }

    public int getId() {
        return id;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getPictureAddress() {
        return pictureAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public void setPictureAddress(String pictureAddress) {
        this.pictureAddress = pictureAddress;
    }
}

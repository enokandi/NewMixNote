package com.oniktech.newmixnote.utils;

public class Note {
    private int id;
    private String title;
    private String type;
    private String rememberTime;
    private boolean haveReminder;

    Note(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.setHaveReminder(false);
        this.setRememberTime(" ");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRememberTime() {
        return rememberTime;
    }

    public void setRememberTime(String rememberTime) {
        this.rememberTime = rememberTime;
    }

    public boolean isHaveReminder() {
        return haveReminder;
    }

    public void setHaveReminder(boolean haveReminder) {
        this.haveReminder = haveReminder;
    }
}

package com.oniktech.newmixnote.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.oniktech.newmixnote.R;

public class VoiceNoteActivity extends AppCompatActivity {
    ImageView create_Button;
    EditText title;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_note);
        create_Button = findViewById(R.id.voiceNote_create);
        title = findViewById(R.id.voiceNote_Title);
        recyclerView = findViewById(R.id.voiceNote_Recycle);
    }
}

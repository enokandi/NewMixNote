package com.oniktech.newmixnote.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.oniktech.newmixnote.R;
import com.oniktech.newmixnote.utils.DatabaseHelper;

public class TextNoteActivity extends AppCompatActivity {
    ImageView create_Button;
    EditText title,text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);
        create_Button = findViewById(R.id.textNote_create);
        title = findViewById(R.id.textNote_Title);
        text = findViewById(R.id.textNote_Text);

        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        Animation anim_create= AnimationUtils.loadAnimation(TextNoteActivity.this,R.anim.scale2);

        create_Button.setOnClickListener(view -> {

            if(!( (title.getText().toString().equals("")) && (text.getText().toString().equals("")) ))
            {
                create_Button.startAnimation(anim_create);
                databaseHelper.insertTextNote(title.getText().toString(), text.getText().toString());
            }else
            {
                finish();
            }
        });

        anim_create.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            //...
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }
}

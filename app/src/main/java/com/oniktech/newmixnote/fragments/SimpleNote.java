package com.oniktech.newmixnote.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oniktech.newmixnote.R;
import com.oniktech.newmixnote.activity.CheckListActivity;
import com.oniktech.newmixnote.activity.PictureNoteActivity;
import com.oniktech.newmixnote.activity.TextNoteActivity;
import com.oniktech.newmixnote.activity.VoiceNoteActivity;
import com.oniktech.newmixnote.utils.DatabaseHelper;
import com.oniktech.newmixnote.utils.Note;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class SimpleNote extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Typeface typeface;
    private String mParam1;
    private String mParam2;
    private boolean flag;
    public static SimpleNote newInstance(String param1, String param2) {
        SimpleNote fragment = new SimpleNote();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_simple_note, container, false);
        //for change font programically
        typeface = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.timesnewroman);
        //<initialize views------------------------------------------------
        ImageView new_text = view.findViewById(R.id.simple_new_text);
        ImageView new_checklist = view.findViewById(R.id.simple_new_checklist);
        ImageView new_pic = view.findViewById(R.id.simple_new_pic);
        ImageView new_voice = view.findViewById(R.id.simple_new_voice);
        ImageView fab = view.findViewById(R.id.fab);
        ImageView createFastNote = view.findViewById(R.id.simple_createNote);
        EditText fastNote = view.findViewById(R.id.simple_fastNote);
        fastNote.setTypeface(typeface);
        LinearLayout menu = view.findViewById(R.id.simple_menu);
        recyclerView=view.findViewById(R.id.Simple_content_recycle);
        databaseHelper=new DatabaseHelper(getContext());
        //---------------------------------------------------------------------->//

        //<---define animations------------------------------------------------------
        Animation anim_menu= AnimationUtils.loadAnimation(getContext(),R.anim.scale);
        Animation anim_item= AnimationUtils.loadAnimation(getContext(),R.anim.scale_rotate);
        Animation anim_create= AnimationUtils.loadAnimation(getContext(),R.anim.scale2);
        Animation anim_revrerse_menu= AnimationUtils.loadAnimation(getContext(),R.anim.reverse_scale);
        Animation anim_revrerse_item= AnimationUtils.loadAnimation(getContext(),R.anim.reverse_scale_rotate);
        //---------------------------------------------------------------------------->//

        //<----click listeners--------------------------------------------------------------------------------------------------------
        new_text.setOnClickListener(view1 -> {
            Intent myIntent = new Intent(getActivity(), TextNoteActivity.class);
            //-1 for a new Note
            myIntent.putExtra("id", -1);
            startActivity(myIntent);
        });
        new_checklist.setOnClickListener(view12 -> {
            Intent myIntent = new Intent(getActivity(), CheckListActivity.class);
            //-1 for a new Note
            myIntent.putExtra("id", -1);
            startActivity(myIntent);
        });
        new_pic.setOnClickListener(view13 -> startActivity(new Intent(getActivity(), PictureNoteActivity.class).putExtra("id", -1) ));

        new_voice.setOnClickListener(view14 -> startActivity(new Intent(getActivity(), VoiceNoteActivity.class).putExtra("id", -1)));

        createFastNote.setOnClickListener(view15 -> {
            //checking the fastnote is not empty
            if(!fastNote.getText().toString().equals("") ) {
                //animation for saving note
                createFastNote.startAnimation(anim_create);
                String note=fastNote.getText().toString();
                //if exist '/n' character ,befor '/n' is title. condition:'/n' is not last character
                if( note.contains("\n") && (note.length()-1 > note.indexOf("\n")) ){
                    databaseHelper.insertTextNote( note.substring(0, note.indexOf("\n")) ,note.substring(note.indexOf("\n")+1) );
                }
                //if number of character is grater than 30, first 30 character is title
                else if(fastNote.getText().toString().length()>30) {
                    databaseHelper.insertTextNote(note.substring(0, 30), note.toString());
                }
                //set fast note for title and text of note
                else {
                    databaseHelper.insertTextNote(note, note);
                }
                //for update recycler view content
                onResume();
            }
        });


        //flag for check menu , is expanded or no
        flag=true;
        fab.setOnClickListener(view16 -> {
            menu.setVisibility(View.VISIBLE);
            if(flag) {
                flag=false;
                menu.startAnimation(anim_menu);
                new_text.startAnimation(anim_item);
                new_checklist.startAnimation(anim_item);
                new_pic.startAnimation(anim_item);
                new_voice.startAnimation(anim_item);
            }else{
                flag=true;
                menu.startAnimation(anim_revrerse_menu);
                new_text.startAnimation(anim_revrerse_item);
                new_checklist.startAnimation(anim_revrerse_item);
                new_pic.startAnimation(anim_revrerse_item);
                new_voice.startAnimation(anim_revrerse_item);
            }
        });
        //---------------------------------------------------------------------------------------------------------------------------------------------->//

        //<for clear fast note  when animation finished---------------------------------------------
        anim_create.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //...
                fastNote.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        //---------------------------------------------------------------------------------->//

        //set data to recycler view
        getData();

        return view;
    }

    //set recycler view data
    private void getData(){

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler view content
        ArrayList<Note> mainres= databaseHelper.getData();
        //if no data disappear recycle
        if(mainres.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new SimpleNote.adapter(mainres));
        }

    }
//calling by every change
    @Override
    public  void onResume() {
        getData();
        super.onResume();
    }
    //<recycler view adapter class-------------------------------------------------------------------------------------------------
    public class adapter extends RecyclerView.Adapter<adapter.myViewHolder>{

        private ArrayList<Note> res;

        adapter(ArrayList<Note> res) {
            this.res = res;
        }

        class myViewHolder extends RecyclerView.ViewHolder{

            TextView title,text;
            ImageView lineLeft,image;
            myViewHolder(View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.recycle_title);
                text=itemView.findViewById(R.id.recycle_text);
                lineLeft=itemView.findViewById(R.id.recycle_line_left);
                image=itemView.findViewById(R.id.recycle_image);
            }

        }
        @NotNull
        @Override
        public adapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View item= inflater.inflate(R.layout.item_simple_note_list,parent,false);
            return new adapter.myViewHolder(item);
        }

        @Override
        public void onBindViewHolder(final SimpleNote.adapter.myViewHolder holder, int position) {

            holder.title.setTypeface(typeface);
            holder.text.setTypeface(typeface);

            //show space rather than enter in title
            holder.title.setText(res.get(position).getTitle().replace("\n"," "));

            //if type is text note show summary
            if( res.get(position).getType()!=null && res.get(position).getType().equals("TextNote"))
            {
                holder.text.setText(databaseHelper.getTextSummary(res.get(position).getId()).replace("\n"," "));
            }
            else if(!(res.get(position).getType()==null) && res.get(position).getType().equals("CheckList"))
            {
                holder.text.setText("");
            }
            else
            {
                holder.text.setText("");
            }


        }
        @Override
        public int getItemCount() {
            return res.size();
        }

    }
    //--------------------------------------------------------------------------------------------------------------------------->//

}

package com.oniktech.newmixnote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oniktech.newmixnote.R;
import com.oniktech.newmixnote.utils.CheckList;
import com.oniktech.newmixnote.utils.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckListActivity extends AppCompatActivity {
    ImageView create_Button;
    EditText title;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<CheckList> mainres;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        create_Button = findViewById(R.id.checklist_create);
        title = findViewById(R.id.checklist_Title);
        recyclerView = findViewById(R.id.checklist_Recycle);
        //get id of note . if exists, id is grater tan 0 and for new note is -1
        Intent mIntent = getIntent();
        id = mIntent.getIntExtra("id", -1);

        databaseHelper=new DatabaseHelper(CheckListActivity.this);

        //animation for save note
        Animation anim_create= AnimationUtils.loadAnimation(CheckListActivity.this,R.anim.scale2);

        //content of recycler view
        ArrayList<CheckList> mainres=new ArrayList<CheckList>();
//<------------click listeners---------------------------------------------------
        create_Button.setOnClickListener(view -> {

            if(!( title.getText().toString().equals("") ))
            {
                ;
               // create_Button.startAnimation(anim_create);
                     //   databaseHelper.updateChecklistItem(mainres.get(mainres.size()).getId(),
                       //                                     mainres.get(mainres.size()).getCheckName(),
                        //                                    mainres.get(mainres.size()).isChecked(),id,title.getText().toString());
                //databaseHelper.insertChecklist(title.getText().toString(), title.getText().toString());

            }else
            {
                finish();
            }
        });
//----------------------------------------------------------------------------->//

        //-------------end activity after animation finished
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
        //------------------------------------------------------->//

        //get checklist data if exists
        getChecklistData(mainres);
    }


    private void getChecklistData(ArrayList<CheckList> mainres){

        recyclerView.setLayoutManager(new LinearLayoutManager(CheckListActivity.this));

        //if checklist created before
        if(id>0)
            mainres= databaseHelper.getCheckListData(id);
        //an empty check list to shown and edit new note
        mainres.add(new CheckList(-1,id,"","false"));

        if(mainres.isEmpty())
        {
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new CheckListActivity.adapter(mainres));
        }

    }


    public class adapter extends RecyclerView.Adapter<adapter.myViewHolder>{

        private ArrayList<CheckList> res;

        adapter(ArrayList<CheckList> res) {
            this.res = res;
        }

        class myViewHolder extends RecyclerView.ViewHolder{
            ImageView create_checklist;
            CheckBox checkBox;
            EditText text;
            myViewHolder(View itemView) {
                super(itemView);
                create_checklist=itemView.findViewById(R.id.item_checklist_create);
                text=itemView.findViewById(R.id.item_checklist_EditText);
                checkBox=itemView.findViewById(R.id.item_checklist_checkbox);
            }
        }

        @NotNull
        @Override
        public adapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View item= inflater.inflate(R.layout.item_checklist,parent,false);
            return new adapter.myViewHolder(item);
        }

        @Override
        public void onBindViewHolder(final CheckListActivity.adapter.myViewHolder holder, int position) {

            ;
            holder.text.setText(res.get(position).getCheckName());


            if(res.get(position).isChecked().equals("true"))
                holder.checkBox.setChecked(true);
            else
                holder.checkBox.setChecked(false);

            //***********************************
            if(res.get(position).getId()<0)
                holder.text.setEnabled(true);

            //*****************************************
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        holder.text.setEnabled(true);
                }
            });
            //for save checklist items
            holder.create_checklist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //insert note and checklistitem
                    if(res.get(position).getId()<0 && id<0) {
                        if(!(holder.text.getText().toString().equals(""))) {
                            holder.text.setEnabled(false);
                            String checked="false";
                            if(holder.checkBox.isChecked())
                                checked="true";
                            CheckList c =databaseHelper.insertChecklistNote(title.getText().toString(), holder.text.getText().toString(),checked);
                            res.add(res.size() - 1, c);
                            id=c.getNoteId();
                        }

                    }//just insert to checklist item
                    else if(res.get(position).getId()<0){
                        if(!(holder.text.getText().toString().equals(""))) {
                            holder.text.setEnabled(false);
                            String checked="false";
                            if(holder.checkBox.isChecked())
                                checked="true";
                            res.add(res.size() - 1, databaseHelper.insertChecklistItem(id, holder.text.getText().toString(),checked,title.getText().toString()));
                        }
                    }
                    //update checklist
                    else{
                        holder.text.setEnabled(false);
                        String checked = "false";
                        if (holder.checkBox.isChecked())
                            checked = "true";
                        databaseHelper.updateChecklistItem(res.get(position).getId(), holder.text.getText().toString(), checked,id,title.getText().toString());
                        res.set(position,new CheckList( res.get(position).getId(),id, holder.text.getText().toString(),checked) );

                    }

                }
            });



        }
        @Override
        public int getItemCount() {
            return res.size();
        }
    }


}

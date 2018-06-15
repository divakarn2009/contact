package com.example.divakar_4416.testapp.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.divakar_4416.testapp.Db.DatabaseHelper;
import com.example.divakar_4416.testapp.R;

import java.util.ArrayList;

public class ContactDetails extends AppCompatActivity {
    ImageView back_button;
    ScrollView scrollView;
    LinearLayout scrollcontent;
    TextView title;
    ArrayList <String> sal = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);
        Bundle extras= getIntent().getExtras();
        String phone=extras.getString("phone");
        String name = extras.getString("name");
        back_button=(ImageView)findViewById(R.id.back_button);
        scrollView=(ScrollView)findViewById(R.id.scroll_notes);
        title=(TextView)findViewById(R.id.title_details);
        scrollcontent=(LinearLayout)findViewById(R.id.scroll_content);
        title.setText(name);




        DatabaseHelper db= new DatabaseHelper(getApplicationContext());
        sal=db.getNotes(phone);
        for(int i=0;i<sal.size();i++){
            String temp=sal.get(i);
            TextView tv1 = new TextView(this);
            tv1.setText(temp);
            scrollcontent.addView(tv1);
            View line = new View(this);
            line.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    5
            ));
            line.setBackgroundColor(Color.parseColor("#B3B3B3"));
            scrollcontent.addView(line);
        }



        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}

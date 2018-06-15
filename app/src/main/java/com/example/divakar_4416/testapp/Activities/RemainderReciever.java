package com.example.divakar_4416.testapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.divakar_4416.testapp.R;

public class RemainderReciever extends Activity {

    TextView phone_view,name_view;
    EditText msg_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle extras= getIntent().getExtras();
        String phone=extras.getString("phone");
        String msg=extras.getString("msg");
        String name = extras.getString("name");
        setContentView(R.layout.show_remainder);
        phone_view=(TextView)findViewById(R.id.phone_value);
        name_view=(TextView)findViewById(R.id.name_value);
        msg_view=(EditText)findViewById(R.id.remainder_msg);
        phone_view.setText(phone);
        name_view.setText(name);
        msg_view.setText(msg);


    }
}

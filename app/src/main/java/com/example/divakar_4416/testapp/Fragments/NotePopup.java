package com.example.divakar_4416.testapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.divakar_4416.testapp.R;
import com.example.divakar_4416.testapp.Db.DatabaseHelper;

public class NotePopup extends DialogFragment {

    EditText notes;
    Button save;
    String phone,name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notes_popup, container,
                false);
        Bundle bundle = this.getArguments();
        phone = bundle.getString("phone");
        name= bundle.getString("name");

        notes=(EditText)rootView.findViewById(R.id.notes);
        save=(Button)rootView.findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note= String.valueOf(notes.getText());
                long addfailure;
                DatabaseHelper databaseHelper=new DatabaseHelper(getActivity().getApplicationContext());
                addfailure=databaseHelper.addNotes(phone,name,note);
                Toast.makeText(getActivity().getApplicationContext(),"Notes added successfully", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });


        return rootView;
    }
}

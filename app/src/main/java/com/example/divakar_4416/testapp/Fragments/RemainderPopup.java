package com.example.divakar_4416.testapp.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.divakar_4416.testapp.Activities.RemainderReciever;
import com.example.divakar_4416.testapp.AlarmReciever;
import com.example.divakar_4416.testapp.R;

import java.util.Calendar;

public class RemainderPopup extends DialogFragment {

    Button set_remainder;
    EditText month_value,day_value,hour_value,minute_value,msg_value;
    int month,day,hour,minute;
    String msg;
    String phone,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remainder_popup, container,
                false);
        Bundle bundle = this.getArguments();
        phone = bundle.getString("phone");
        name = bundle.getString("name");
        set_remainder =(Button)rootView.findViewById(R.id.set_remainder);
        month_value = (EditText) rootView.findViewById(R.id.month_value);
        day_value = (EditText) rootView.findViewById(R.id.day_value);
        hour_value = (EditText) rootView.findViewById(R.id.hour_value);
        minute_value = (EditText) rootView.findViewById(R.id.minute_value);
        msg_value = (EditText) rootView.findViewById(R.id.message_value);

        set_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month=Integer.parseInt(String.valueOf(month_value.getText()));
                day=Integer.parseInt(String.valueOf(day_value.getText()));
                hour=Integer.parseInt(String.valueOf(hour_value.getText()));
                minute=Integer.parseInt(String.valueOf(minute_value.getText()));
                msg=String.valueOf(msg_value.getText());

                Intent intent = new Intent(getActivity(), RemainderReciever.class);
                intent.putExtra("msg",msg);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0,
                        intent, PendingIntent.FLAG_ONE_SHOT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(2018, month, day, hour, minute, 5);

                Toast.makeText(getActivity().getApplicationContext(),"Remainder set at"+String.valueOf(calendar.getTime()), Toast.LENGTH_LONG).show();

                AlarmManager alarm = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                getDialog().dismiss();
            }
        });
        return rootView;
    }
}

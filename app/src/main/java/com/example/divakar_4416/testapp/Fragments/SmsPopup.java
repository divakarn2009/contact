package com.example.divakar_4416.testapp.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.util.Log;
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

public class SmsPopup extends DialogFragment {
    Button send_now,send_later;
    EditText month_value,day_value,hour_value,minute_value,msg_value;
    int month,day,hour,minute;
    String msg;
    String phone,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sms_popup, container,
                false);
        Bundle bundle = this.getArguments();
        phone = bundle.getString("phone");
        name = bundle.getString("name");
        send_now =(Button)rootView.findViewById(R.id.send_now);
        send_later = (Button) rootView.findViewById(R.id.send_later);
        month_value = (EditText) rootView.findViewById(R.id.month_value);
        day_value = (EditText) rootView.findViewById(R.id.day_value);
        hour_value = (EditText) rootView.findViewById(R.id.hour_value);
        minute_value = (EditText) rootView.findViewById(R.id.minute_value);
        msg_value = (EditText) rootView.findViewById(R.id.message_value);


        send_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg=String.valueOf(msg_value.getText());
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null,msg, null, null);
                    Toast.makeText(getActivity().getApplicationContext(), "SMS sent Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(getActivity().getApplicationContext(), "SMS sending Failed", Toast.LENGTH_SHORT).show();
                }
                getDialog().dismiss();
            }
        });

        send_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month=Integer.parseInt(String.valueOf(month_value.getText()));
                day=Integer.parseInt(String.valueOf(day_value.getText()));
                hour=Integer.parseInt(String.valueOf(hour_value.getText()));
                minute=Integer.parseInt(String.valueOf(minute_value.getText()));
                msg=String.valueOf(msg_value.getText());

                Intent intent = new Intent(getActivity(), AlarmReciever.class);
                intent.putExtra("msg",msg);
                intent.putExtra("phone",phone);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 123, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(2018, month, day, hour, minute, 5);

                Toast.makeText(getActivity().getApplicationContext(),"sms scheduled at"+String.valueOf(calendar.getTime()), Toast.LENGTH_LONG).show();

                AlarmManager alarm = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                getDialog().dismiss();
            }
        });


        return rootView;
    }
}

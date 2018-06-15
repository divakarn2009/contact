package com.example.divakar_4416.testapp.Fragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divakar_4416.testapp.Activities.ContactDetails;
import com.example.divakar_4416.testapp.Activities.MainActivity;
import com.example.divakar_4416.testapp.AlarmReciever;
import com.example.divakar_4416.testapp.Contact;
import com.example.divakar_4416.testapp.Db.DatabaseHelper;
import com.example.divakar_4416.testapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ContactList extends Fragment {

    private RecyclerView recyclerView;
    private List<Contact> contactsList = new ArrayList<>();
    private ContactsAdapter cAdapter;
    MainActivity mainActivity;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int previousExpandedPosition = -1;
    private int mExpandedPosition = -1;
    private DatabaseHelper db;
    private FrameLayout container;
    private String global_name,global_phone;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_main, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycle);
        mainActivity=(MainActivity)getActivity();

        cAdapter = new ContactsAdapter(contactsList);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(cLayoutManager);

//        /*Recycler view Listener*/
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ClickListener() {
//
//
//            @Override
//            public void onClick(View view, int position, MotionEvent event) {
//
//            }
//        }));

//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(cAdapter);

        showContacts();
        return view;
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            getContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContactList() {
        final String[] PROJECTION = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String selectionFields =  ContactsContract.RawContacts.ACCOUNT_TYPE + " = ?";
        String[] selectionArgs = new String[]{"com.google"};

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, selectionFields,selectionArgs, ContactsContract.Contacts.DISPLAY_NAME);
        if (cursor != null) {
            try {
                final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                Contact contact;
                String name, number;
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex);
                    number = cursor.getString(numberIndex);
                    contact = new Contact(name,number);
                    contactsList.add(contact);
                }
            } finally {
                cursor.close();
                cAdapter.notifyDataSetChanged();
            }
        }
    }


    public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

        private List<Contact> contactsList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name,phone;
            public LinearLayout expand_menu;
            public ImageView sms,notes,remainder,details;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.name);
                phone = (TextView) view.findViewById(R.id.phone);
                expand_menu = (LinearLayout) view.findViewById(R.id.menu);
                sms = (ImageView) view.findViewById(R.id.imageView6);
                notes = (ImageView) view.findViewById(R.id.imageView4);
                remainder  = (ImageView) view.findViewById(R.id.imageView5);
                details  = (ImageView) view.findViewById(R.id.imageView3);
                sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        SmsPopup dFragment = new SmsPopup();
                        Bundle bundle=new Bundle();
                        bundle.putString("phone",global_phone);
                        bundle.putString("name",global_name);
                        dFragment.setArguments(bundle);
                        dFragment.show(fm, "Dialog Fragment");
                    }
                });
                notes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        NotePopup dFragment = new NotePopup();
                        Bundle bundle=new Bundle();
                        bundle.putString("phone",global_phone);
                        bundle.putString("name",global_name);
                        dFragment.setArguments(bundle);
                        dFragment.show(fm, "Dialog Fragment");
                    }
                });
                remainder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        RemainderPopup dFragment = new RemainderPopup();
                        Bundle bundle=new Bundle();
                        bundle.putString("phone",global_phone);
                        bundle.putString("name",global_name);
                        dFragment.setArguments(bundle);
                        dFragment.show(fm, "Dialog Fragment");
                    }
                });
                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailspage = new Intent(getActivity(),ContactDetails.class);
                        detailspage.putExtra("phone",global_phone);
                        detailspage.putExtra("name",global_name);
                        startActivity(detailspage);
                    }
                });

            }
        }


        public ContactsAdapter(List<Contact> contactsList) {
            this.contactsList = contactsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_list_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Contact contact = contactsList.get(position);
            holder.name.setText(contact.getName());
            holder.phone.setText(contact.getPhone());

            final boolean isExpanded = position==mExpandedPosition;
            holder.expand_menu.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.itemView.setActivated(isExpanded);

            if (isExpanded)
                previousExpandedPosition = position;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView name,phone;
                    name=(TextView)v.findViewById(R.id.name);
                    phone=(TextView)v.findViewById(R.id.phone);
                    global_name= String.valueOf(name.getText());
                    global_phone= String.valueOf(phone.getText());
                    mExpandedPosition = isExpanded ? -1:position;
                    notifyItemChanged(previousExpandedPosition);
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }
    }

}

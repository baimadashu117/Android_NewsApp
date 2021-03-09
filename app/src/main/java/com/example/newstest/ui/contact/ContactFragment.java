package com.example.newstest.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newstest.R;

public class ContactFragment extends Fragment {

    private ContactViewModel contactViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                ViewModelProviders.of(this).get(ContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        final TextView textView = root.findViewById(R.id.contact_email);
        final TextView textView2 = root.findViewById(R.id.contact_message);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data= new  Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse( "mailto:way.ping.li@gmail.com" ));
                data.putExtra(Intent.EXTRA_SUBJECT,  "User Feedback" );
                data.putExtra(Intent.EXTRA_TEXT,  "(Please write your feedback here ;) )" );
                startActivity(data);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int phoneNumber = 123457;
                String message = "Please write your feedback here ;) ";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        });
        return root;
    }

}
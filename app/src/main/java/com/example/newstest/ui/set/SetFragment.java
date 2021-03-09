package com.example.newstest.ui.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.newstest.Drawer_V1;
import com.example.newstest.LoginActivity;
import com.example.newstest.MyApplication;
import com.example.newstest.R;

public class SetFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_set, container, false);
        Button btlogout = root.findViewById(R.id.button2);
        final TextView textView2 = root.findViewById(R.id.textView15);
        final TextView setMode = root.findViewById(R.id.set_night_mode);

        setMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNightMode();
            }
        });


        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_contact);
            }
        });

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return root;
    }

    public void setNightMode()
    {
        MyApplication application = (MyApplication)this.getActivity().getApplication();
        boolean currentMode = application.getMode();
        if(!currentMode)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        application.setMode();
    }
}
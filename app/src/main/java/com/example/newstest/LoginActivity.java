package com.example.newstest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText e1, e2;
    Button bt,bt2;
    TextView txt;
    UserOpenHelper helper;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.name);
        e2 = findViewById(R.id.pswd);
        bt = findViewById(R.id.login);
        bt2 = findViewById(R.id.button2);
        txt = findViewById(R.id.sign);

        helper = new UserOpenHelper(this);
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String isChecked = sp.getString("isChecked", "no");
        if (isChecked.equals("no")) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("isChecked", "yes");
            editor.commit();
            e1.setText(sp.getString("name", ""));
            e2.setText(sp.getString("pass", ""));
        } else {
/*
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();*/

        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", e1.getText().toString());
                editor.putString("pass", e2.getText().toString());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name", e1.getText().toString());
                editor.putString("pass", e2.getText().toString());
                editor.putString("isCheck", "no");
                editor.commit();
                String user_value = e1.getText().toString();
                String pswd_value = e2.getText().toString();
                if (user_value.length() == 0 || pswd_value.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter your username/password", Toast.LENGTH_SHORT).show();
                } else {
//                    try{
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String sql = String.format("select * from users where _NAME='%s' and _PSWD='%s';", user_value, pswd_value);
                    Cursor cursor = db.rawQuery(sql, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            int id = cursor.getInt(0);
                            editor.putInt("id", id);
                            editor.commit();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Toast.makeText(LoginActivity.this, cursor + "=======" + user_value + "!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User name/password error", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

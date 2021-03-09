package com.example.newstest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
ImageButton ib;
EditText name ,pswd,repswd;
Button sign;
UserOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ib=findViewById(R.id.back);
        sign=findViewById(R.id.sign);
        SharedPreferences sp= getSharedPreferences("data", Context.MODE_PRIVATE);
        name=findViewById(R.id.name);
        pswd=findViewById(R.id.pswd);
        repswd=findViewById(R.id.repswd);
        helper=new UserOpenHelper(this);
        name.setText(sp.getString("name",""));
        pswd.setText(sp.getString("pass",""));
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("isChecked","no");
        editor.commit();
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_value=name.getText().toString();
                String pswd_value=pswd.getText().toString();
                String repswd_value=repswd.getText().toString();
                if (name_value.length()==0 || pswd_value.length()==0){
                    Toast.makeText(SignUpActivity.this,"Please enter your username/password", Toast.LENGTH_SHORT).show();
                }else
                if(pswd_value.equals(repswd_value)){
                    //链接服务器，添加数据
                    SQLiteDatabase db=helper.getWritableDatabase();
                    //判断是否注册过
                    String find= String.format("select _NAME from users where _NAME='%s';",name_value);


                    String sql= String.format("insert into users(_NAME,_PSWD) values('%s','%s');"
                            ,name_value,pswd_value);
                    Cursor cursor=db.rawQuery(find,null);
                    if (cursor!=null && cursor.getCount()>0){
                        Toast.makeText(SignUpActivity.this,"The user has been registered", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            db.execSQL(sql);
                            Toast.makeText(SignUpActivity.this, "Registration successful, welcome to demo", Toast.LENGTH_SHORT).show();
                            db.close();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.putString("name",name_value);
                            editor.putString("pass",pswd_value);
                            editor.commit();
                            startActivity(intent);
                            SignUpActivity.this.finish();

                        } catch (Exception e) {
                            Toast.makeText(SignUpActivity.this, "Failed to add, please check the input", Toast.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    Toast.makeText(SignUpActivity.this,"Repeated input is inconsistent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

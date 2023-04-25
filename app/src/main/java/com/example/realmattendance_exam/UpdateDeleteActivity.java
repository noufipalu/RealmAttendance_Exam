package com.example.realmattendance_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class UpdateDeleteActivity extends AppCompatActivity {

    EditText id, name, status;
    Button update, back;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        name = findViewById(R.id.sname);
        status = findViewById(R.id.sStatus);
        update = findViewById(R.id.updatebtn);
        back = findViewById(R.id.backbtn);
        realm = Realm.getDefaultInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateDeleteActivity.this, MainActivity.class));
            }
        });
    }
}
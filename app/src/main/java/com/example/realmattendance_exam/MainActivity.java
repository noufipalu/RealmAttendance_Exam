package com.example.realmattendance_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    EditText name, status;
    Button save, show;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.studentName);
        status = findViewById(R.id.attStatus);
        save = findViewById(R.id.save);
        show = findViewById(R.id.show);

        realm = Realm.getDefaultInstance();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sname = name.getText().toString();
                String sAttStatus = status.getText().toString();
                
                if (TextUtils.isEmpty(sname)){
                    name.setError("Field cannot be empty");
                    name.requestFocus();
                } else if (TextUtils.isEmpty(sAttStatus)){
                    status.setError("Enter attendance status");
                    status.requestFocus();
                } else {
                    addToDatabase(sname, sAttStatus);
                    name.setText("");
                    status.setText("");
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowActivity.class));
            }
        });
    }

    private void addToDatabase(String sname, String sAttStatus) {
        DataModel dataModel = new DataModel();
        Number id = realm.where(DataModel.class).max("id");

        long nextId;

        if (id == null){
            nextId = 1;
        }else {
            nextId =id.intValue()+1;
        }

        dataModel.setId(nextId);
        dataModel.setName(sname);
        dataModel.setStatus(sAttStatus);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(dataModel);
            }
        });

        Toast.makeText(this, "Value inserted Successfully", Toast.LENGTH_SHORT).show();
    }

}
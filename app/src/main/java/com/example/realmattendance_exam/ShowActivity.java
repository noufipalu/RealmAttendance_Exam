package com.example.realmattendance_exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;

public class ShowActivity extends AppCompatActivity {

    TextView list;
    Realm realm;
    List<DataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        list = findViewById(R.id.list);
        realm = Realm.getDefaultInstance();

        readData();
    }

    private void readData() {
        dataModels = realm.where(DataModel.class).findAll();
        list.setText("");
        for (int i=0; i<dataModels.size(); i++){
            list.append("\n Student ID: "+dataModels.get(i).getId() +
                        "\n Student Name: "+dataModels.get(i).getName() +
                        "\n Status: "+dataModels.get(i).getStatus() + "\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.options_menu_back:
                startActivity(new Intent(ShowActivity.this, MainActivity.class));
                break;
            case R.id.options_menu_delete:
                deleteData();
                break;
            case R.id.options_menu_edit:
                updateDataDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteData() {
        AlertDialog.Builder al = new AlertDialog.Builder(ShowActivity.this);
        View view =getLayoutInflater().inflate(R.layout.delete_update, null);
        al.setView(view);

        EditText id_ = view.findViewById(R.id.studId);
        Button delete = view.findViewById(R.id.delete);

        AlertDialog alertDialog = al.show();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = Long.parseLong(id_.getText().toString());
                DataModel dataModel = realm.where(DataModel.class).equalTo("id", id).findFirst();

                if (dataModel.getId() == 0){
                    Toast.makeText(ShowActivity.this, "", Toast.LENGTH_SHORT).show();
                }
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        alertDialog.dismiss();
                        dataModel.deleteFromRealm();
                        Toast.makeText(ShowActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateDataDialog() {
        AlertDialog.Builder al = new AlertDialog.Builder(ShowActivity.this);
        View view = getLayoutInflater().inflate(R.layout.delete_update, null);
        al.setView(view);

        EditText id_ = view.findViewById(R.id.studId);
        Button update = view.findViewById(R.id.update);
        AlertDialog alertDialog = al.show();

        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                long id = Long.parseLong(id_.getText().toString());
                DataModel dataModel = realm.where(DataModel.class).equalTo("id", id).findFirst();
                ShowUpdateDialog(dataModel);
            }
        });
    }

    private void ShowUpdateDialog(DataModel dataModel) {
        AlertDialog.Builder al = new AlertDialog.Builder(ShowActivity.this);
        View view = getLayoutInflater().inflate(R.layout.activity_update_delete, null);
        al.setView(view);

        EditText name = view.findViewById(R.id.sname);
        EditText status = view.findViewById(R.id.sStatus);
        Button update = view.findViewById(R.id.updatebtn);
        AlertDialog alertDialog = al.show();

        name.setText(dataModel.getName());
        status.setText(dataModel.getStatus());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        dataModel.setName(name.getText().toString());
                        dataModel.setStatus(status.getText().toString());

                        realm.copyToRealmOrUpdate(dataModel);
                        Toast.makeText(ShowActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
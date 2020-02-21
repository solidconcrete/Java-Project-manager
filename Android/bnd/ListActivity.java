package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.bnd.helpers.TinkloKontroleris;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas);
        Intent dabar = this.getIntent();
        GetUserList prisijungti = new GetUserList();
        prisijungti.execute(dabar.getStringExtra("token"));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    private final class GetUserList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String url ="http://192.168.0.103:8080/kursinisWeb/gp_" + params[0] + ".htm";
            System.out.println("Sent token: " + params[0]);
            try {
                return TinkloKontroleris.sendGet(url);
            } catch (Exception e) {
                e.printStackTrace();
                return "nepavyko gauti duomenu is web";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("GAUTA: " + result);
            if (result != null) {
                try {

                    String[] projects = result.split(",");
                    final ArrayList<String> projectList = new ArrayList<>();
                    ArrayList<String> projectCompany = new ArrayList<>();
                    String url;
                    for (int i = 0; i < projects.length; i++)
                    {
                        projectList.add(projects[i]);
                        url = "http://192.168.0.103:8080/kursinisWeb/gc_" + projects[i] + ".htm";
                        projectCompany.add(TinkloKontroleris.sendGet(url));
                    }
                    ListView sar = findViewById(R.id.sarasui);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (ListActivity.this, android.R.layout.simple_list_item_1, projectList);
                    sar.setAdapter(arrayAdapter);
                    sar.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            System.out.println(projectList.get(position));
                            Intent intent = new Intent(ListActivity.this, TaskActivity.class);
                            intent.putExtra("projectName", projectList.get(position));
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(ListActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
            }
        }
    }


}

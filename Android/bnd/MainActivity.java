package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bnd.ds.User;
import com.example.bnd.helpers.TinkloKontroleris;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void prisijungi(View v) {
        EditText log = findViewById(R.id.l_login);
        EditText pas = findViewById(R.id.l_pass);
        String login = log.getText().toString();
        String pass = pas.getText().toString();
        String siuntimui = "{\"login\":\"" + login + "\", \"password\":\"" + pass + "\"}";
        User loggedIn = new User();
        loggedIn.setLogin(login);
        UserLogin prisijungti = new UserLogin();
        prisijungti.execute(siuntimui);
    }

    private final class UserLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Tikrinami prisijungimo duomenys", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://192.168.0.103:8080/kursinisWeb/login.htm";
            String postDataParams = params[0];
            System.out.println("ISSIUSTA: " + postDataParams);
            try {
                return TinkloKontroleris.sendPost(url, postDataParams);
            } catch (Exception e) {
                e.printStackTrace();
                return "couldn't get data from web";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("GAUTA: " + result);
            if (!result.contains("wrong login or password")) {
                Gson parseris = new Gson();
                try {
//                    User gautas = parseris.fromJson(result, User.class);
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra("token", result);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "wrong login or password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "wrong login or password", Toast.LENGTH_LONG).show();
            }
        }
    }
}





package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;

public class createTask extends AppCompatActivity {

    public void addTask(View v)
    {
        EditText name = findViewById(R.id.name);
        String taskName = name.getText().toString();
        Intent project = this.getIntent();
        String projectName = project.getStringExtra("projectName");
        System.out.println("Project: " + projectName + " Task: " + taskName);
        String url = "http://192.168.0.103:8080/kursinisWeb/addTask_"+ projectName + "_" + taskName + ".htm";

        try
        {
            String status = TinkloKontroleris.sendGet(url);
            if(status.equals("Task created"))
            {
                Toast.makeText(createTask.this, status, Toast.LENGTH_LONG).show();

                finish();
            }
            else
                Toast.makeText(createTask.this, "not created", Toast.LENGTH_LONG).show();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int w = dm.widthPixels;
        int h = dm.heightPixels;

        getWindow().setLayout((int) (w*0.8), (int) (h*0.6));
        RelativeLayout layout = findViewById(R.id.popup);
        layout.setBackgroundColor(Color.parseColor("#EC4849"));

    }
}

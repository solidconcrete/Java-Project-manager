package com.example.bnd;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bnd.helpers.TinkloKontroleris;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    public void addTask(View v)
    {
        Intent intent = new Intent(TaskActivity.this, createTask.class);
        Intent project = this.getIntent();
        String projectName = project.getStringExtra("projectName");
        intent.putExtra("projectName", projectName);
        startActivity(intent);
    }

    public void refreshPage(View v)
    {
        Intent intent = new Intent(TaskActivity.this, TaskActivity.class);
        Intent project = this.getIntent();
        String projectName = project.getStringExtra("projectName");
        intent.putExtra("projectName", projectName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Intent intent = this.getIntent();
        String projectName = intent.getStringExtra("projectName");
        Toast.makeText(TaskActivity.this, "selected" + projectName, Toast.LENGTH_LONG).show();
        TextView projectNameField = (TextView) findViewById(R.id.projectName);
        projectNameField.setText(projectName);
        getProjectTasks getTasks = new getProjectTasks();
        getTasks.execute(projectName);

    }

    private final class getProjectTasks extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params)
        {
            System.out.println("Sent: " + params[0]);
            String url = "http://192.168.0.103:8080/kursinisWeb/getTasks_" + params[0] + ".htm";
            try
            {

                return TinkloKontroleris.sendGet(url);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "couldn't get tasks";
            }
        }
        @Override
        protected void onPostExecute (String result)
        {
            super.onPostExecute(result);
            System.out.println("Received " + result);
            if (result != null) {
                try {

                    final String[] tasks = result.split(",");
                    final ArrayList<String> projectList = new ArrayList<>();
                    ArrayList<String> projectCompany = new ArrayList<>();
                    String url;
                    for (int i = 0; i < tasks.length; i++) {
                        projectList.add(tasks[i]);
                    }
                    ListView sar = findViewById(R.id.tasks);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (TaskActivity.this, android.R.layout.simple_list_item_1, projectList);
                    sar.setAdapter(arrayAdapter);
                    sar.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            System.out.println(projectList.get(position));

                            checkTask(projectList.get(position));

                        }

                    });
                    sar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                TinkloKontroleris.sendGet("http://192.168.0.103:8080/kursinisWeb/completeTask_" + projectList.get(position) + "_" + "mobile user" + ".htm");
                                Toast.makeText(TaskActivity.this, "task completed", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        return true;
                        }
                    });
                }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(TaskActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TaskActivity.this, "Neteisingi duomenys", Toast.LENGTH_LONG).show();
                }
        }

    }

    public void checkTask(String taskName)
    {
        try {
            String status = TinkloKontroleris.sendGet("http://192.168.0.103:8080/kursinisWeb/checkTask_"+ taskName + ".htm");
            Toast.makeText(TaskActivity.this, status, Toast.LENGTH_LONG).show();
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }

    }

}

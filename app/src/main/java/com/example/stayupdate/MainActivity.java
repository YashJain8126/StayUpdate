package com.example.stayupdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        findAllViews();

        fillUpTheList("all");

    }

    public void findAllViews(){
        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setUpToolbar(){
        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        TextView search_button=toolbar.findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchDialog();
            }
        });
    }

    private void startSearchDialog() {
        final AlertDialog dialogBuilder= new AlertDialog.Builder(this).create();
        LayoutInflater inflater=this.getLayoutInflater();
        View dialogView=inflater.inflate(R.layout.search_tab,null);
        EditText editText=dialogView.findViewById(R.id.editText);
        CardView search=dialogView.findViewById(R.id.sb);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=editText.getText().toString().trim();
                fillUpTheList(s);
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void fillUpTheList(String search) {
        OkHttpClient client=new OkHttpClient();
        String url="https://newsapi.org/v2/everything?q="+search+"&apiKey=db3f194f09864a5797d6bcde71f12f14";
        Request request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object=new JSONObject(response.body().string());
                    JSONArray news=object.getJSONArray("articles");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter adap=new adapter(MainActivity.this,news);
                            list.setAdapter(adap);
                            adap.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
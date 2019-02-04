package com.example.ghifa.pendataanbarang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.ghifa.pendataanbarang.Adapter.AdapterData;
import com.example.ghifa.pendataanbarang.Model.ModelData;
import com.example.ghifa.pendataanbarang.Util.AppController;
import com.example.ghifa.pendataanbarang.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    RecyclerView mRecyclerview;
    List<ModelData> mItems;
    ProgressBar loading;
    FloatingActionButton add;
    SearchView sv;
    String searchSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview = findViewById(R.id.recyclerviewTemp);
        loading = findViewById(R.id.loading_main);
        add = findViewById(R.id.add_float);
        mItems = new ArrayList<>();
        loadJson();

        mManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterData(MainActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);
        sv = findViewById(R.id.search_data);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InsertData.class));
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchSubmit = query;
                searchData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void searchData()
    {
        JsonArrayRequest reqCari = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_SEARCH, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        loading.setVisibility(View.GONE);

                        Log.d("volley", "response : " + response.toString());

                        for(int i = 0; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setKode(data.getString("kode"));
                                md.setNama(data.getString("nama"));
                                md.setHarga(data.getString("harga"));
                                md.setDate(data.getString("date"));
                                md.setImg(data.getString("img"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> mp = new HashMap<>();
                mp.put("nama", searchSubmit);

                return mp;
            }
        };

        AppController.getInstance().addToRequestQueue(reqCari);
    }

    private void loadJson()
    {

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_DATA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        loading.setVisibility(View.GONE);

                        Log.d("volley", "response : " + response.toString());

                        for(int i = 0; i < response.length(); i++)
                        {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setKode(data.getString("kode"));
                                md.setNama(data.getString("nama"));
                                md.setHarga(data.getString("harga"));
                                md.setDate(data.getString("date"));
                                md.setImg(data.getString("img"));
                                mItems.add(md);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : " + error.getMessage());

                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);

    }
}

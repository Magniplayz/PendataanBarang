package com.example.ghifa.pendataanbarang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ghifa.pendataanbarang.Util.AppController;
import com.example.ghifa.pendataanbarang.Util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InsertData extends AppCompatActivity {

    EditText kode, nama, harga;
    ImageView mBack, mGambar;
    Button btnSimpan, btnDelete;
    ProgressBar loadingSave;
    private final int IMG_REQUEST = 1;
    Bitmap bitmap;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);
        String intent_kode = data.getStringExtra("kode");
        final String intent_nama = data.getStringExtra("nama");
        String intent_harga = data.getStringExtra("harga");
        String intent_img = data.getStringExtra("img");

        kode = findViewById(R.id.inp_kode);
        nama = findViewById(R.id.inp_nama);
        harga = findViewById(R.id.inp_harga);
        mBack = findViewById(R.id.btn_back_from_insert);
        btnSimpan = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        loadingSave = findViewById(R.id.loading_insert);
        mGambar = findViewById(R.id.img_edit);
        header = findViewById(R.id.head_insert);

        if(update == 1)
        {
            header.setText("Update Data");
            btnSimpan.setText("UPDATE");
            kode.setText(intent_kode);
            kode.setVisibility(View.GONE);
            nama.setText(intent_nama);
            harga.setText(intent_harga);
            //load image to update
            Glide.with(getApplicationContext()).load(intent_img).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mGambar);
        }
        else if(update == 0)
        {
            btnDelete.setVisibility(View.GONE);
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(update == 1)
                {
                    if(nama.getText().toString().isEmpty())
                    {
                        nama.setError("Harap isi Nama Barang!");
                    }
                    else if(harga.getText().toString().isEmpty())
                    {
                        harga.setError("Harap isi Harga Barang!");
                    }
                    else
                    {
                        updateData();
                    }
                }
                else
                {
                    if(kode.getText().toString().isEmpty() || kode.getText().toString().length() > 5 ||
                            kode.getText().toString().length() < 5)
                    {
                        kode.setError("Harap isi kode dengan benar!");
                    }
                    else if(nama.getText().toString().isEmpty())
                    {
                        nama.setError("Harap isi Nama Barang!");
                    }
                    else if(harga.getText().toString().isEmpty())
                    {
                        harga.setError("Harap isi Harga Barang!");
                    }
                    else
                    {
                        simpanData();
                    }
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InsertData.this, MainActivity.class));
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(InsertData.this);

                deleteDialog.setCancelable(true);
                deleteDialog.setTitle("Hapus " + intent_nama);
                deleteDialog.setMessage("Apakah anda yakin akan menghapus " + intent_nama + " secara permanen?");

                deleteDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                deleteDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData();
                    }
                });
                deleteDialog.show();
            }
        });

        mGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                mGambar.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void updateData()
    {
        loadingSave.setVisibility(View.VISIBLE);

        StringRequest updateData = new StringRequest(Request.Method.POST, ServerAPI.URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingSave.setVisibility(View.GONE);

                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "Berhasil mengupdate barang", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InsertData.this, MainActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingSave.setVisibility(View.GONE);
                        Toast.makeText(InsertData.this, "Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("kode",kode.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("harga",harga.getText().toString());
                map.put("img", imageToString(bitmap));

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(updateData);
    }

    private void simpanData()
    {

        loadingSave.setVisibility(View.VISIBLE);

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingSave.setVisibility(View.GONE);

                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertData.this, "Berhasil menginputkan barang", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InsertData.this, MainActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingSave.setVisibility(View.GONE);
                        Toast.makeText(InsertData.this, "Gagal Insert Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("kode",kode.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("harga",harga.getText().toString());
                map.put("img", imageToString(bitmap));

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);

    }

    private void deleteData()
    {
        loadingSave.setVisibility(View.VISIBLE);

        StringRequest delReq = new StringRequest(Request.Method.POST, ServerAPI.URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingSave.setVisibility(View.GONE);
                        Toast.makeText(InsertData.this, "Data telah dihapus", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(InsertData.this, MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startActivity(new Intent(InsertData.this, MainActivity.class));
                        finish();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("kode", kode.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(delReq);
    }

}

package com.example.ghifa.pendataanbarang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;

public class Detail extends AppCompatActivity {

    ImageView mGambar, backToMain;
    TextView dHeader, dNama, dKode, dHarga, dTanggal;
    Button toUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);
        String intent_kode = data.getStringExtra("kode");
        final String intent_nama = data.getStringExtra("nama");
        String intent_harga = data.getStringExtra("harga");
        final String intent_img = data.getStringExtra("img");
        String intent_tanggal = data.getStringExtra("date");

        mGambar = findViewById(R.id.gambarBarang);
        backToMain = findViewById(R.id.btn_back_from_detail);
        dHeader = findViewById(R.id.head_barang);
        dNama = findViewById(R.id.detail_nama);
        dKode = findViewById(R.id.detail_kode);
        dHarga = findViewById(R.id.detail_harga);
        dTanggal = findViewById(R.id.detail_tanggal);
        toUpdate = findViewById(R.id.to_update);

        dHeader.setText("Detail " + intent_nama);
        dNama.setText(intent_nama);
        dKode.setText(intent_kode);
        dHarga.setText(intent_harga);
        dTanggal.setText(intent_tanggal);

        Glide.with(getApplicationContext()).load(intent_img).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mGambar);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Detail.this, MainActivity.class));
                finish();
            }
        });

        toUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent up = new Intent(getApplicationContext(), InsertData.class);
                up.putExtra("update", 1);
                up.putExtra("kode", dKode.getText().toString());
                up.putExtra("nama", dNama.getText().toString());
                up.putExtra("harga", dHarga.getText().toString());
                up.putExtra("img", intent_img);

                startActivity(up);
            }
        });
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }
}

package com.example.ghifa.pendataanbarang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ghifa.pendataanbarang.Detail;
import com.example.ghifa.pendataanbarang.InsertData;
import com.example.ghifa.pendataanbarang.Model.ModelData;
import com.example.ghifa.pendataanbarang.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {

    private List<ModelData> mItems;
    private Context context;

    public AdapterData(Context context, List<ModelData> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_row, viewGroup, false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holderData, int i) {
        ModelData md = mItems.get(i);
        holderData.tvkode.setText(md.getKode());
        holderData.tvnama.setText(md.getNama());
        holderData.tvharga.setText(md.getHarga());
        holderData.tvtanggal.setText(md.getDate());
        //image
        Glide.with(context).load(md.getImg()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holderData.thumbnail);

        holderData.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder
    {
        ImageView thumbnail;
        TextView tvkode, tvnama, tvharga, tvtanggal;
        ModelData md;

        public HolderData (View view)
        {
            super(view);

            tvkode = view.findViewById(R.id.kode);
            tvnama = view.findViewById(R.id.nama);
            tvharga = view.findViewById(R.id.harga);
            tvtanggal = view.findViewById(R.id.tanggal);
            thumbnail = view.findViewById(R.id.thumb);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent update = new Intent(context, Detail.class);
                    update.putExtra("update", 1);
                    update.putExtra("kode", md.getKode());
                    update.putExtra("nama", md.getNama());
                    update.putExtra("harga", md.getHarga());
                    update.putExtra("img", md.getImg());
                    update.putExtra("date", md.getDate());

                    context.startActivity(update);
                }
            });

        }
    }

}

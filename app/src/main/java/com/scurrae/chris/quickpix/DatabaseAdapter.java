package com.scurrae.chris.quickpix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/21/16.
 */
public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<Images> data = new ArrayList<>();

    public DatabaseAdapter(Context context, List<Images> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = inflater.inflate(R.layout.images, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Images current = data.get(position);
        byte[] decode = Base64.decode(current.get_image(), Base64.DEFAULT);
        Bitmap decodedbit = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        holder.imageView.setImageBitmap(decodedbit);
        holder.imageView.setImageDrawable(current.get_image());


    }

    @Override
    public int getItemCount() {
        return data.size();

    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imagevi);

        }
    }
}
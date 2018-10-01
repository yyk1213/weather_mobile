package com.example.yeon1213.myapplication.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yeon1213.myapplication.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<String> mliving_dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView index;
        public MyViewHolder(LinearLayout v){
            super(v);
            index = v.findViewById(R.id.living_text);
        }
    }

    public MainAdapter(List<String> myDataset){
        this.mliving_dataset =myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.main_data_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String weatherData = mliving_dataset.get(position);
        holder.index.setText(weatherData);
    }

    @Override
    public int getItemCount() {
        return mliving_dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

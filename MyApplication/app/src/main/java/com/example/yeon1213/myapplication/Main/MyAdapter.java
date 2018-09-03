package com.example.yeon1213.myapplication.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeon1213.myapplication.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> mliving_dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView index;
        public MyViewHolder(TextView v){
            super(v);
            index =v.findViewById(R.id.living_text);
        }
    }

    public MyAdapter(List<String> myDataset){
        this.mliving_dataset =myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.data_row, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String weatherData = mliving_dataset.get(position);
        holder.index.setText("열 지수:"+weatherData);//우선 이 값을 넣어보기
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

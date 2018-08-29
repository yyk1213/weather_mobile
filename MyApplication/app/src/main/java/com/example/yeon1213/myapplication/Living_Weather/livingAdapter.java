package com.example.yeon1213.myapplication.Living_Weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yeon1213.myapplication.R;

import java.util.List;

public class livingAdapter extends RecyclerView.Adapter<livingAdapter.living_ViewHolder>{
    private List<Living_data> living_Dataset;

    public static class living_ViewHolder extends RecyclerView.ViewHolder{

        //text 말고 checkbox도 넣어야 함
        public TextView living_name,living_expla;
        public living_ViewHolder(View v){
            super(v);
            living_name =v.findViewById(R.id.living_name);
            living_expla=v.findViewById(R.id.living_expla);
        }
    }

    public livingAdapter(List<Living_data> myDataset){
        living_Dataset =myDataset;
    }

    @Override
    public living_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.living_row,parent,false);
      return new living_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(living_ViewHolder living_viewHolder, int position) {
        Living_data LD=living_Dataset.get(position);
        living_viewHolder.living_name.setText(LD.getLiving_name());
        living_viewHolder.living_expla.setText(LD.getLiving_explanation());
    }

    @Override
    public int getItemCount() {
        return living_Dataset.size();
    }
}

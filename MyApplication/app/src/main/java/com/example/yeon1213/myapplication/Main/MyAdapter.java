//package com.example.yeon1213.myapplication.Main;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.example.yeon1213.myapplication.R;
//
//import java.util.List;
//
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//
//    private List<WeatherData> mDataset;
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView temp_text;
//        public MyViewHolder(TextView v){
//            super(v);
//            temp_text =v.findViewById(R.id.temp);
//        }
//    }
//
//    public MyAdapter(List<WeatherData> myDataset){
//        mDataset =myDataset;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        TextView v = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_row, parent, false);
//
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        WeatherData weatherData =mDataset.get(position);
//        holder.temp_text.setText(weatherData.getWeather());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//}

package com.example.yeon1213.myapplication.Life_Radius;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.R;

import java.util.Calendar;
import java.util.List;

public class LifeRadiusActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView mRadiusRecyclerView;
    private RadiusAdapter mAdapter;
    private RecyclerView.LayoutManager radius_LayoutManager;
    private LocationDatabase database;
    public FloatingActionButton fab;
    public static final String EXTRA_DATA="com.example.yeon1213.myapplication.Life_Radius.location_data";

    public static Intent newIntent(Context context,LocationData locationData){
        Intent dataIntent=new Intent(context, LifeRadiusActivity.class);
//        dataIntent.putExtra(EXTRA_DATA, locationData);
        return dataIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_radius);

        getSupportActionBar().setTitle("생활반경 설정");

        mRadiusRecyclerView=findViewById(R.id.radius_recycler_view);
        mRadiusRecyclerView.setHasFixedSize(true);

        radius_LayoutManager=new LinearLayoutManager(getApplicationContext());
        mRadiusRecyclerView.setLayoutManager(radius_LayoutManager);

        fab=findViewById(R.id.radius_plus_btn);
        fab.setOnClickListener(this);

        database=LocationDatabase.getDataBase(this);

        updateUI();
    }

    @Override
    public void onClick(View v) {
        //플로팅 버튼 누를 때
        Intent settingRadiusIntent=new Intent(LifeRadiusActivity.this,SettingLifeRadiusActivity.class);
        startActivity(settingRadiusIntent);
    }

    private void updateUI(){

        List<LocationData> radius=database.getLocationDAO().getLocation();

        mAdapter=new RadiusAdapter(this, radius);

        mRadiusRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class RadiusHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SwitchCompat.OnCheckedChangeListener{

        public TextView mLocationTextView;
        public TextView mTimeTextView;
        public TextView mDayOfWeekTextView;
        public SwitchCompat mAlarmSwitch;
        public View mView;

        public RadiusHolder(View itemView) {
            super(itemView);

            mLocationTextView=itemView.findViewById(R.id.radius_location);
            mTimeTextView=itemView.findViewById(R.id.radius_time);
            mDayOfWeekTextView=itemView.findViewById(R.id.radius_day_of_week);
            mAlarmSwitch =itemView.findViewById(R.id.radius_check);

            mView=itemView;
            mView.setOnClickListener(this);

            mAlarmSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            //편집하는 내용이 나와야함
                //db에서 저장된 데이터를 가져와서 그 내용의 값을 그 내용을 화면에 보이게 해야한다.
            int position=(int)mView.getTag();
            if(database.getLocationDAO().getLocation().get(position).getMAlarmCheck()){

                Intent settingRadiusIntent=new Intent(LifeRadiusActivity.this,SettingLifeRadiusActivity.class);
                settingRadiusIntent.putExtra(SettingLifeRadiusActivity.EXTRA_KEY,"Hello");
                startActivityForResult(settingRadiusIntent,0);
            }else{
                itemView.setEnabled(false);
            }
        }



        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if(isChecked){
                //체크가 되면 뷰항목을 누를 수 있다.--뷰항목 활성화
                int position=(int)mView.getTag();
                Log.d("스위치 체크",""+position+" "+isChecked);
                LocationData locationData=database.getLocationDAO().getLocation().get(position);

//                Intent i=LifeRadiusActivity.newIntent(SettingLifeRadiusActivity, locationData);

                locationData.setMAlarmCheck(isChecked);

                database.getLocationDAO().update(locationData);
//            }else{
                //누를 수 없다.--비활성화
//            }
        }
    }

    private class RadiusAdapter extends RecyclerView.Adapter<RadiusHolder>{
        private List<LocationData> mRadius;
        private Context context;

        public RadiusAdapter(Context context, List<LocationData> mRadius) {
            this.mRadius = mRadius;
            this.context=context;
        }

        @Override
        public RadiusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            View view=layoutInflater.inflate(R.layout.radius_row,parent,false);

            return new RadiusHolder(view);
        }

        @Override
        public void onBindViewHolder(RadiusHolder holder, int position) {
            holder.mView.setTag(position);

            LocationData radius=mRadius.get(position);
            holder.mLocationTextView.setText(radius.getMLocation_name());
            holder.mTimeTextView.setText(radius.getMTime());
            holder.mDayOfWeekTextView.setText(getDayOfWeek(radius.getMDay_of_week()));
            holder.mAlarmSwitch.setChecked(radius.getMAlarmCheck());

            Log.d("요일",""+getDayOfWeek(radius.getMDay_of_week()));
        }

        @Override
        public int getItemCount() {
            return mRadius.size();
        }

        private String getDayOfWeek(int dayOfWeek){

            String mDayOfWeek="";

            for(int day=0; day<7; day++) {
                switch (day) {
                    case 0:
                        if (((dayOfWeek >> day) & 1) == 1) {//월요일
                            mDayOfWeek +="월";
                        }
                        break;
                    case 1:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek +="화";
                        }
                        break;
                    case 2:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek +="수";
                        }
                        break;
                    case 3:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek +="목";
                        }
                        break;
                    case 4:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek +="금";
                        }
                        break;
                    case 5:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek +="토";
                        }
                        break;
                    case 6:
                        if (((dayOfWeek >> day) & 1) == 1) {//일요일
                            mDayOfWeek +="일";
                        }
                        break;
                }
            }
            return mDayOfWeek;
        }
    }
}

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

import com.example.yeon1213.myapplication.Alarm.Alarm;
import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.Main.MainActivity;
import com.example.yeon1213.myapplication.R;

import java.util.List;

public class LifeRadiusActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRadiusRecyclerView;
    private RadiusAdapter mAdapter;
    private RecyclerView.LayoutManager radius_LayoutManager;
    private LocationDatabase database;
    public FloatingActionButton fab;
    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_radius);

        getSupportActionBar().setTitle("생활반경 설정");

        mRadiusRecyclerView = findViewById(R.id.radius_recycler_view);
        mRadiusRecyclerView.setHasFixedSize(true);

        radius_LayoutManager = new LinearLayoutManager(getApplicationContext());
        mRadiusRecyclerView.setLayoutManager(radius_LayoutManager);

        fab = findViewById(R.id.radius_plus_btn);
        fab.setOnClickListener(this);

        database = LocationDatabase.getDataBase(this);
        alarm = new Alarm();

        updateUI();
    }

    @Override
    public void onClick(View v) {
        //플로팅 버튼 누를 때
        Intent settingRadiusIntent = new Intent(LifeRadiusActivity.this, SettingLifeRadiusActivity.class);
        startActivityForResult(settingRadiusIntent,0);
    }

    private void updateUI() {

        List<LocationData> radius = database.getLocationDAO().getLocation();

        mAdapter = new RadiusAdapter(this, radius);

        mRadiusRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK){
            updateUI();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private class RadiusHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SwitchCompat.OnCheckedChangeListener {

        public TextView mLocationNameTextView;
        public TextView mTimeTextView;
        public TextView mDayOfWeekTextView;
        public SwitchCompat mAlarmSwitch;
        public View mView;

        public RadiusHolder(View itemView) {
            super(itemView);

            mLocationNameTextView = itemView.findViewById(R.id.radius_location);
            mTimeTextView = itemView.findViewById(R.id.radius_time);
            mDayOfWeekTextView = itemView.findViewById(R.id.radius_day_of_week);
            mAlarmSwitch = itemView.findViewById(R.id.radius_check);

            mView = itemView;
            mView.setOnClickListener(this);

            mAlarmSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = (int) mView.getTag();
            //알람이 활성화 돼 있으면 db 내용을 편잡하게 한다.
//            if (database.getLocationDAO().getLocation().get(position).getMAlarmCheck()) {

                int id = database.getLocationDAO().getLocation().get(position).getMId();

                Intent settingRadiusIntent = new Intent(LifeRadiusActivity.this, SettingLifeRadiusActivity.class);
                settingRadiusIntent.putExtra(SettingLifeRadiusActivity.EXTRA_DATA_ID, id);
                settingRadiusIntent.putExtra(SettingLifeRadiusActivity.EXTRA_DATA_POSITION, position);

                startActivityForResult(settingRadiusIntent, 0);

//            } else {
//                itemView.setEnabled(false);
//            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            int position = (int) mView.getTag();
            LocationData locationData = database.getLocationDAO().getLocation().get(position);

            if (isChecked) {
                //체크가 되면 뷰항목을 누를 수 있고 알람이 설정된다.
                itemView.setEnabled(true);
                alarm.setAlarm(getApplicationContext(), locationData);
                locationData.setMAlarmCheck(true);

            } else {
                //뷰항목이 비활성화 되고 알람이 취소된다.
                itemView.setEnabled(false);
                alarm.removeAlarm(getApplicationContext(), locationData);
                locationData.setMAlarmCheck(false);
            }

            database.getLocationDAO().update(locationData);
            //mAdapter.notifyDataSetChanged();
        }
    }

    private class RadiusAdapter extends RecyclerView.Adapter<RadiusHolder> {
        private List<LocationData> mRadius;
        private Context context;
        private boolean onBind;

        public RadiusAdapter(Context context, List<LocationData> mRadius) {
            this.mRadius = mRadius;
            this.context = context;
        }

        @Override
        public RadiusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.radius_row, parent, false);

            return new RadiusHolder(view);
        }

        @Override
        public void onBindViewHolder(RadiusHolder holder, int position) {
            holder.mView.setTag(position);

            LocationData radius = mRadius.get(position);
            holder.mLocationNameTextView.setText(radius.getMLocation_name());
            holder.mTimeTextView.setText(radius.getMTime());
            holder.mDayOfWeekTextView.setText(getDayOfWeek(radius.getMDay_of_week()) + "요일");
            holder.mAlarmSwitch.setChecked(radius.getMAlarmCheck());

            Log.d("요일", "" + getDayOfWeek(radius.getMDay_of_week()));
        }

        @Override
        public int getItemCount() {
            return mRadius.size();
        }

        private String getDayOfWeek(int dayOfWeek) {

            String mDayOfWeek = "";

            for (int day = 0; day < 7; day++) {
                switch (day) {
                    case 0:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "일";
                        }
                        break;
                    case 1:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "월";
                        }
                        break;
                    case 2:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "화";
                        }
                        break;
                    case 3:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "수";
                        }
                        break;
                    case 4:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "목";
                        }
                        break;
                    case 5:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "금";
                        }
                        break;
                    case 6:
                        if (((dayOfWeek >> day) & 1) == 1) {
                            mDayOfWeek += "토";
                        }
                        break;
                }
            }
            return mDayOfWeek;
        }
    }
}

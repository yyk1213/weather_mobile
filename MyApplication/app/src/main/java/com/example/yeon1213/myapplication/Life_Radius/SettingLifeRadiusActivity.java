package com.example.yeon1213.myapplication.Life_Radius;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.yeon1213.myapplication.Alarm.AlarmReceiver;
import com.example.yeon1213.myapplication.DataBase.LocationDAO;
import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.Main.MainActivity;
import com.example.yeon1213.myapplication.R;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;

public class SettingLifeRadiusActivity extends AppCompatActivity implements View.OnClickListener,ToggleButton.OnCheckedChangeListener{

    protected GeoDataClient mGeoDataClient;
    private AutoCompleteTextView mSearchPlace;
    private PlaceAutocompleteAdapter mAdapter;
    private TextView mPlaceDetailsAttribution;
    private TextView mPlaceDetailsText;
    private TextView mStartTime;
    private Button mTimeBtn;
    private Button mSaveBtn;
    private Button mClearBtn;

    private LocationDatabase database;
    //저장할 데이터
    private LocationData locationData=new LocationData();
    private String mLocation_name;
    private int mDayOfWeek =0;
    private int mAlarm_id;

    public static final String EXTRA_KEY="KEY";

    //Day of week buttons
    ToggleButton tSun;
    ToggleButton tMon;
    ToggleButton tTue;
    ToggleButton tWed;
    ToggleButton tThur;
    ToggleButton tFri;
    ToggleButton tSat;

    private static final LatLngBounds BOUNDS_GRATER_KOREA=new LatLngBounds(new LatLng(35.9078, 127.7669),new LatLng(35.9078, 127.7669));
    private static final String EXTRA_ALARM_ID="com.example.yeon1213.myapplication.Life_Radius.alarm_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_life_radius);
        getSupportActionBar().setTitle("생활반경 설정");

//        String a=String.valueOf(getIntent().getExtras().get(EXTRA_KEY));
//        Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();

        //저장db가져오기
        database=LocationDatabase.getDataBase(this);

        //places api 클라이언트
        mGeoDataClient= Places.getGeoDataClient(this,null);

        InitView();

        mAdapter=new PlaceAutocompleteAdapter(this,mGeoDataClient,BOUNDS_GRATER_KOREA,null);
        mSearchPlace.setAdapter(mAdapter);

        mSearchPlace.setOnItemClickListener(mAutocompleteClickListener);
        mTimeBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timeBtn:
                Calendar now=Calendar.getInstance();
                int hour=now.get(Calendar.HOUR_OF_DAY);
                int minute=now.get(Calendar.MINUTE);
                boolean is24Hour=true;

                TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //시간 저장
                        int hour=view.getCurrentHour();
                        //DB에 시간 저장
                        locationData.setMTime(hour+":"+minute);

                        mStartTime.setText(hour+":"+minute);
                        Log.d("시간",""+hour+" "+minute);
                    }
                };

                TimePickerDialog timePickerDialog=new TimePickerDialog(SettingLifeRadiusActivity.this,onTimeSetListener,hour,minute,is24Hour);
                timePickerDialog.show();
                break;

            case R.id.clear_btn:
                mSearchPlace.setText("");
                break;

            case R.id.saveBtn:
                //설정이 완료되지 않은 경우
                if(locationData.getMTime()==null || locationData.getMLocation_name()==null || locationData.getMDay_of_week()==0) {
                    Toast.makeText(this, "설정을 마무리해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                //DB에 저장
                locationData.setMAlarmCheck(true);

                LocationDAO locationDAO=database.getLocationDAO();
                locationDAO.insert(locationData);
               //알람매니저로 알람 등록
                setAlarm();

                setResult(RESULT_OK);
                finish();

                //알람 리스트 화면으로 돌아가기
//                Intent radiusIntent=new Intent(this,LifeRadiusActivity.class);
//                startActivityForResult(radiusIntent);
                break;
        }
    }

    private void setAlarm(){
        String mTime=locationData.getMTime();

        int mHour, mMin;
        int mAlarmId=locationData.getMId();
        //시간 받아오기
        String[] time=mTime.split(":");
        mHour=Integer.parseInt(time[0].trim());
        mMin=Integer.parseInt(time[1].trim());

        Log.d("시간알기", ""+mHour+":"+mMin);

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.HOUR_OF_DAY,mHour);//1시간 전에 설정하기
        calendar.set(calendar.MINUTE,mMin);

        //요일 받아오기
        int mDayOfWeek=locationData.getMDay_of_week();

        getDayOfWeek(mDayOfWeek,calendar);
        Log.d("캘린더",""+calendar.toString());
        long currentTime=System.currentTimeMillis();
        long setTime=calendar.getTimeInMillis();
        //하루 시간
        long oneDay=1000*60*60*24;
        //지난 알림 다음날 울리기
        while(currentTime>setTime){
            setTime +=oneDay;
        }

        Intent receiverIntent=new Intent(this,AlarmReceiver.class);
        //알람매니저 등록
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        //알람 설정 시각에 발생하는 인텐트
        PendingIntent alarmIntent= PendingIntent.getBroadcast(this,mAlarmId,receiverIntent,0);
        //알람 설정
        alarmManager.set(AlarmManager.RTC_WAKEUP,setTime,alarmIntent);
    }

    private void InitView(){

        mSearchPlace =findViewById(R.id.place_autocomplete_powered_by_google);
        mPlaceDetailsText=findViewById(R.id.place_address);
        mPlaceDetailsAttribution = findViewById(R.id.place_attribution);
        mClearBtn=findViewById(R.id.clear_btn);
        mTimeBtn=findViewById(R.id.timeBtn);
        mStartTime=findViewById(R.id.start_time);
        mSaveBtn=findViewById(R.id.saveBtn);

        //요일
        tSun = findViewById(R.id.tSun);
        tMon = findViewById(R.id.tMon);
        tTue = findViewById(R.id.tTue);
        tWed = findViewById(R.id.tWed);
        tThur = findViewById(R.id.tThur);
        tFri = findViewById(R.id.tFri);
        tSat = findViewById(R.id.tSat);

        tSun.setOnCheckedChangeListener(this);
        tMon.setOnCheckedChangeListener(this);
        tTue.setOnCheckedChangeListener(this);
        tWed.setOnCheckedChangeListener(this);
        tThur.setOnCheckedChangeListener(this);
        tFri.setOnCheckedChangeListener(this);
        tSat.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.tSun:
               setDayOfWeek(6,isChecked);
                break;
            case R.id.tMon:
                setDayOfWeek(0,isChecked);
                break;
            case R.id.tTue:
                setDayOfWeek(1,isChecked);
                break;
            case R.id.tWed:
                setDayOfWeek(2,isChecked);
                break;
            case R.id.tThur:
                setDayOfWeek(3,isChecked);
                break;
            case R.id.tFri:
                setDayOfWeek(4,isChecked);
                break;
            case R.id.tSat:
                setDayOfWeek(5,isChecked);
                break;
        }

        Log.d("요일 선택",""+ mDayOfWeek);
        locationData.setMDay_of_week(mDayOfWeek);
    }
    //선택된 요일 bit계산
    private void setDayOfWeek(int day, boolean checked){
        if(checked)
            mDayOfWeek |=(1<<day);
        else
            mDayOfWeek &=~(1<<day);
    }
    //중복된 코드가 많다
    private void getDayOfWeek(int dayOfWeek, Calendar calendar){
        for(int day=0; day<7; day++) {//0이 월요일, 6이 일요일
            switch (day) {
                case 0:
                    if (((dayOfWeek >> day) & 1) == 1) {//월요일
                        calendar.add(Calendar.DAY_OF_WEEK,2);
                    }
                    break;
                case 1:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        calendar.add(Calendar.DAY_OF_WEEK,3);
                    }
                    break;
                case 2:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        calendar.add(Calendar.DAY_OF_WEEK,4);
                    }
                    break;
                case 3:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        calendar.add(Calendar.DAY_OF_WEEK,5);
                    }
                    break;
                case 4:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        calendar.add(Calendar.DAY_OF_WEEK,6);
                    }
                    break;
                case 5:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        calendar.add(Calendar.DAY_OF_WEEK,7);
                    }
                    break;
                case 6:
                    if (((dayOfWeek >> day) & 1) == 1) {//일요일
                        calendar.add(Calendar.DAY_OF_WEEK,1);
                    }
                    break;
            }
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener=
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final AutocompletePrediction item=mAdapter.getItem(position);
                    final String placeId=item.getPlaceId();
                    final CharSequence primaryText=item.getPrimaryText(null);

                    Log.i("선택", "Autocomplete item selected: " + primaryText);

                    Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
                    placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

                    Log.i("선택", "Called getPlaceById to get Place details for " + placeId);
                }
            };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                LatLng latLng=place.getLatLng();

                locationData.setMLatitude(latLng.latitude);
                locationData.setMLongitude(latLng.longitude);

                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),place.getAddress()));
                mLocation_name=formatPlaceDetails(getResources(), place.getName(),place.getAddress()).toString();
                //장소 저장
                locationData.setMLocation_name(mLocation_name);

                Log.d("결과",""+formatPlaceDetails(getResources(), place.getName(),place.getAddress()));

                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(
                            Html.fromHtml(thirdPartyAttribution.toString()));
                }

                Log.i("TAG", "Place details received: " + place.getName());

                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e("오류", "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res,CharSequence name, CharSequence address) {

        return Html.fromHtml(res.getString(R.string.place_details,name,address));
    }

}

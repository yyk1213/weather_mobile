package com.example.yeon1213.myapplication.Life_Radius;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.style.CharacterStyle;
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

import com.example.yeon1213.myapplication.Alarm.Alarm;
import com.example.yeon1213.myapplication.DataBase.LocationDAO;
import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
import com.example.yeon1213.myapplication.R;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
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

public class SettingLifeRadiusActivity extends AppCompatActivity implements View.OnClickListener, ToggleButton.OnCheckedChangeListener {

    protected GeoDataClient mGeoDataClient;
    private AutoCompleteTextView mSearchPlace;
    private CustomAutoCompleteAdapter mAdapter;
    private TextView mPlaceDetailsAddress;
    private TextView mPlaceDetailsText;
    private TextView mStartTime;
    private Button mTimeBtn;
    private Button mSaveBtn;
    private Button mClearBtn;
    private Button mRemoveBtn;

    private LocationDatabase database;
    private Alarm alarm;
    //저장할 데이터
    private LocationData locationData = new LocationData();
    private String data_Location_name;
    private String data_Location_address;
    private int mDayOfWeek = 0;

    private int item_id;
    private int position;

    //Day of week buttons
    ToggleButton tSun;
    ToggleButton tMon;
    ToggleButton tTue;
    ToggleButton tWed;
    ToggleButton tThur;
    ToggleButton tFri;
    ToggleButton tSat;

    private static final LatLngBounds BOUNDS_GRATER_KOREA = new LatLngBounds(new LatLng(35.9078, 127.7669), new LatLng(35.9078, 127.7669));
    public static final String EXTRA_DATA_ID = "com.example.yeon1213.myapplication.Life_Radius.location_data_id";
    public static final String EXTRA_DATA_POSITION = "com.example.yeon1213.myapplication.Life_Radius.location_data_position";

    public static Intent newIntent(Context context, int locationData_id, int data_position) {
        Intent dataIntent = new Intent(context, SettingLifeRadiusActivity.class);
        dataIntent.putExtra(EXTRA_DATA_ID, locationData_id);
        dataIntent.putExtra(EXTRA_DATA_POSITION, data_position);

        return dataIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_life_radius);
        getSupportActionBar().setTitle("생활반경 설정");

        //저장db가져오기
        database = LocationDatabase.getDataBase(this);
        alarm = new Alarm();

        //places api 클라이언트
        mGeoDataClient = Places.getGeoDataClient(this, null);

        InitView();

        position = getIntent().getIntExtra(EXTRA_DATA_POSITION, 0);
        item_id = getIntent().getIntExtra(EXTRA_DATA_ID, 0);

        if (item_id != 0) {
            locationData = database.getLocationDAO().getData(item_id);

            int dayofWeek = locationData.getMDay_of_week();

            mPlaceDetailsText.setText(locationData.getMLocation_name());
            mStartTime.setText(locationData.getMTime());
            mPlaceDetailsAddress.setText(locationData.getMLocation_address());
            //요일도 가져오기
            checkDayOfWeek(dayofWeek);
            //저장을 수정버튼으로 바꾸고 db update
            mSaveBtn.setText("수정");
            mRemoveBtn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeBtn:
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);
                boolean is24Hour = true;

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //시간 저장
                        int hour = view.getCurrentHour();
                        //DB에 시간 저장
                        locationData.setMTime(hour + ":" + minute);

                        mStartTime.setText(hour + ":" + minute);
                        Log.d("시간", "" + hour + " " + minute);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingLifeRadiusActivity.this, onTimeSetListener, hour, minute, is24Hour);
                timePickerDialog.show();
                break;

            case R.id.clear_btn:
                mSearchPlace.setText("");
                break;

            case R.id.saveBtn:
                //설정이 완료되지 않은 경우
                if (locationData.getMTime() == null || locationData.getMLocation_name() == null || locationData.getMDay_of_week() == 0) {
                    Toast.makeText(this, "설정을 마무리해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }

                locationData.setMAlarmCheck(true);

                LocationDAO locationDAO = database.getLocationDAO();

                if (mSaveBtn.getText() == "수정") {

                    locationDAO.update(locationData);
                } else {
                    locationDAO.insert(locationData);
                }

                //locationData를 다시 가져오기 알람 id를 가져오기 위해
                database=LocationDatabase.getDataBase(this);
                int max=database.getLocationDAO().getLocation().size()-1;

                int max_id=database.getLocationDAO().getLocation().get(max).getMId();

                locationData.setMId(max_id);

                //알람매니저 등록
                alarm.setAlarm(this, locationData);

                setResult(RESULT_OK);
                finish();

                break;
            case R.id.removeBtn:
                LocationData removeData = database.getLocationDAO().getLocation().get(position);
                database.getLocationDAO().delete(removeData);//db항목 삭제
                //알람 지우기---오류
                alarm.removeAlarm(this, removeData);

                setResult(RESULT_OK);
                finish();

        }
    }

    private void InitView() {

        mSearchPlace = findViewById(R.id.place_autocomplete_powered_by_google);
        mPlaceDetailsText = findViewById(R.id.place_name);
        mPlaceDetailsAddress = findViewById(R.id.place_attribution);
        mClearBtn = findViewById(R.id.clear_btn);
        mTimeBtn = findViewById(R.id.timeBtn);
        mStartTime = findViewById(R.id.start_time);
        mSaveBtn = findViewById(R.id.saveBtn);
        mRemoveBtn = findViewById(R.id.removeBtn);

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

        mAdapter = new CustomAutoCompleteAdapter(this, mGeoDataClient, BOUNDS_GRATER_KOREA, null);//어댑터 안에 필터 있음
        mSearchPlace.setAdapter(mAdapter);

        mSearchPlace.setOnItemClickListener(mAutocompleteClickListener);
        mTimeBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mRemoveBtn.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tSun:
                setDayOfWeek(0, isChecked);
                break;
            case R.id.tMon:
                setDayOfWeek(1, isChecked);
                break;
            case R.id.tTue:
                setDayOfWeek(2, isChecked);
                break;
            case R.id.tWed:
                setDayOfWeek(3, isChecked);
                break;
            case R.id.tThur:
                setDayOfWeek(4, isChecked);
                break;
            case R.id.tFri:
                setDayOfWeek(5, isChecked);
                break;
            case R.id.tSat:
                setDayOfWeek(6, isChecked);
                break;
        }

        Log.d("요일 선택", "" + mDayOfWeek);
        locationData.setMDay_of_week(mDayOfWeek);

    }

    //선택된 요일 bit계산
    private void setDayOfWeek(int day, boolean checked) {
        if (checked)
            mDayOfWeek |= (1 << day);
        else
            mDayOfWeek &= ~(1 << day);
    }

    private void checkDayOfWeek(int dayOfWeek) {
        for (int day = 0; day < 7; day++) {
            switch (day) {
                case 0:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tSun.setChecked(true);
                    }
                    break;
                case 1:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tMon.setChecked(true);
                    }
                    break;
                case 2:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tTue.setChecked(true);
                    }
                    break;
                case 3:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tWed.setChecked(true);
                    }
                    break;
                case 4:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tThur.setChecked(true);
                    }
                    break;
                case 5:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tFri.setChecked(true);
                    }
                    break;
                case 6:
                    if (((dayOfWeek >> day) & 1) == 1) {
                        tSat.setChecked(true);
                    }
                    break;
            }
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final AutocompletePrediction item = mAdapter.getPrediction_item(position);
                    final String placeId = item.getPlaceId();
                    //final CharSequence primaryText=item.getPrimaryText(null);

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

                LatLng latLng = place.getLatLng();

                locationData.setMLatitude(latLng.latitude);
                locationData.setMLongitude(latLng.longitude);

                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName()));
                mPlaceDetailsAddress.setText(formatPlaceDetails(getResources(), place.getAddress()));

                data_Location_name = formatPlaceDetails(getResources(), place.getName()).toString();
                data_Location_address = formatPlaceDetails(getResources(), place.getAddress()).toString();

                //장소 저장
                locationData.setMLocation_name(data_Location_name);
                locationData.setMLocation_address(data_Location_address);

                // Display the third party attributions if set.
//                final CharSequence thirdPartyAttribution = places.getAttributions();
//                if (thirdPartyAttribution == null) {
//                    mPlaceDetailsAddress.setVisibility(View.GONE);
//                } else {
//                    mPlaceDetailsAddress.setVisibility(View.VISIBLE);
//                    mPlaceDetailsAddress.setText(
//                            Html.fromHtml(thirdPartyAttribution.toString()));
//                }

                Log.i("TAG", "Place details received: " + place.getName());
                places.release();

            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e("오류", "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name) {

        return Html.fromHtml(res.getString(R.string.place_details, name));
    }
}

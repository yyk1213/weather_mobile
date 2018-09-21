package com.example.yeon1213.myapplication.Life_Radius;

import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yeon1213.myapplication.DataBase.LocationData;
import com.example.yeon1213.myapplication.DataBase.LocationDatabase;
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

public class SettingLifeRadiusActivity extends AppCompatActivity{

    protected GeoDataClient mGeoDataClient;
    private AutoCompleteTextView mSearchPlace;
    private PlaceAutocompleteAdapter mAdapter;
    private TextView mPlaceDetailsAttribution;
    private TextView mPlaceDetailsText;
    private Button mTimeBtn;

    private LocationDatabase database;
    //저장할 데이터
    private LocationData locationData=new LocationData();
    private String mLocation_name;
    private String mTime;
    private String mDayOfWeek;

    private static final LatLngBounds BOUNDS_GRATER_KOREA=new LatLngBounds(new LatLng(35.9078, 127.7669),new LatLng(35.9078, 127.7669));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_life_radius);
        getSupportActionBar().setTitle("생활반경 설정");
        //저장db가져오기
        database=LocationDatabase.getDataBase(this,0);

        //places api 클라이언트
        mGeoDataClient= Places.getGeoDataClient(this,null);

        mSearchPlace =findViewById(R.id.place_autocomplete_powered_by_google);
        mPlaceDetailsText=findViewById(R.id.place_address);
        mPlaceDetailsAttribution = findViewById(R.id.place_attribution);
        mTimeBtn=findViewById(R.id.timeBtn);

        mAdapter=new PlaceAutocompleteAdapter(this,mGeoDataClient,BOUNDS_GRATER_KOREA,null);
        mSearchPlace.setAdapter(mAdapter);

        mSearchPlace.setOnItemClickListener(mAutocompleteClickListener);

        Button clearBtn=findViewById(R.id.clear_btn);

        clearBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mSearchPlace.setText("");
            }
        });
        //시간 설정하고 DB에 저장
        mTimeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                };

                Calendar now=Calendar.getInstance();
                int hour=now.get(Calendar.HOUR_OF_DAY);
                int minute=now.get(Calendar.MINUTE);

                locationData.setMTime(hour+":"+minute);
                Log.d("시간",""+hour+" "+minute);
                //24시간 포맷에 있는지 아닌지 확인
                boolean is24Hour=true;

                TimePickerDialog timePickerDialog=new TimePickerDialog(SettingLifeRadiusActivity.this,onTimeSetListener,hour,minute,is24Hour);
                timePickerDialog.show();
            }
        });

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

                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),place.getAddress()));
                mLocation_name=formatPlaceDetails(getResources(), place.getName(),place.getAddress()).toString();

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

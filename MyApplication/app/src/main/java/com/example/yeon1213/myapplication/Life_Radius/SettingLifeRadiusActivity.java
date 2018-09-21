package com.example.yeon1213.myapplication.Life_Radius;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
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
import android.widget.Toast;

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

public class SettingLifeRadiusActivity extends AppCompatActivity{

    protected GeoDataClient mGeoDataClient;

    private AutoCompleteTextView mSearchPlace;
    private PlaceAutocompleteAdapter mAdapter;
    private TextView mPlaceDetailsAttribution;
    private TextView mPlaceDetailsText;

    private static final LatLngBounds BOUNDS_GRATER_KOREA=new LatLngBounds(new LatLng(35.9078, 127.7669),new LatLng(35.9078, 127.7669));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_life_radius);
        //places api 클라이언트
        mGeoDataClient= Places.getGeoDataClient(this,null);

        mSearchPlace =findViewById(R.id.place_autocomplete_powered_by_google);
        mPlaceDetailsText=findViewById(R.id.place_address);
        mPlaceDetailsAttribution = findViewById(R.id.place_attribution);

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

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener=
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //디비에 넣는 코드 작성
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

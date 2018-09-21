package com.example.yeon1213.myapplication.Life_Radius;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceAutocompleteAdapter extends ArrayAdapter<AutocompletePrediction> {

    public static final String TAG = "PlaceAutoAdapter";

    private GeoDataClient mGeoDataClient;
    private List<Place> mResultList; //검색 추천 list
    private LatLngBounds mBounds;
    private PlaceAutocompleteAdapter.CustomAutoCompleteFilter mListFilter =
            new PlaceAutocompleteAdapter.CustomAutoCompleteFilter();

    public PlaceAutocompleteAdapter(@NonNull Context context, GeoDataClient mGeoDataClient, LatLngBounds bounds) {
        super(context,android.R.layout.simple_expandable_list_item_2,android.R.id.text1);
        this.mGeoDataClient = mGeoDataClient;
        this.mBounds=bounds;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        View row = super.getView(position, view, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        Place item = getItem(position);

        TextView textView1 = row.findViewById(android.R.id.text1);
        TextView textView2 = row.findViewById(android.R.id.text2);

        return row;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mListFilter;
    }

    public class CustomAutoCompleteFilter extends Filter {
            private Object lock = new Object();
            private Object lockTwo = new Object();
            private boolean placeResults = false;

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();//필터링해서 결과 받는 것
                placeResults = false;

                final List<Place> placesList = new ArrayList<>();//필터링 결과값이 담기는 리스트
                //결과 값이 없을 경우
                if (prefix !=null) {

                    Task<AutocompletePredictionBufferResponse> task
                            = getAutoCompletePlaces(prefix);

                    task.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Auto complete prediction successful");
                                AutocompletePredictionBufferResponse predictions = task.getResult();
                                Place autoPlace;
                                for (AutocompletePrediction prediction : predictions) {
                                    autoPlace = new Place();
                                    autoPlace.setPlaceId(prediction.getPlaceId());
                                    autoPlace.setPlaceText(prediction.getFullText(null).toString());
                                    placesList.add(autoPlace);
                                }
                                predictions.release();
                                Log.d(TAG, "Auto complete predictions size " + placesList.size());
                            } else {
                                Log.d(TAG, "Auto complete prediction unsuccessful");
                            }
                            //inform waiting thread about api call completion
                            placeResults = true;
                            synchronized (lockTwo) {
                                lockTwo.notifyAll();
                            }
                        }
                    });

                    //wait for the results from asynchronous API call
                    while (!placeResults) {
                        synchronized (lockTwo) {
                            try {
                                lockTwo.wait();
                            } catch (InterruptedException e) {

                            }
                        }
                    }
                    results.values = placesList;
                    results.count = placesList.size();
                    Log.d(TAG, "Autocomplete predictions size after wait" + results.count);
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    mResultList = (ArrayList<Place>) results.values;
                } else {
                    mResultList = null;
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            private Task<AutocompletePredictionBufferResponse> getAutoCompletePlaces(CharSequence query) {
                //create autocomplete filter using data from filter Views
                AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();

                Task<AutocompletePredictionBufferResponse> results =
                        mGeoDataClient.getAutocompletePredictions(query.toString(), null,
                                filterBuilder.build());
                return results;
            }
        }

//    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
//        Log.i(TAG, "Starting autocomplete query for: " + constraint);
//
//        // Submit the query to the autocomplete API and retrieve a PendingResult that will
//        // contain the results when the query completes.
//        Task<AutocompletePredictionBufferResponse> results =
//                mGeoDataClient.getAutocompletePredictions(constraint.toString(), mBounds,
//                        mPlaceFilter);
//
//        // This method should have been called off the main UI thread. Block and wait for at most
//        // 60s for a result from the API.
//        try {
//            Tasks.await(results, 60, TimeUnit.SECONDS);
//        } catch (ExecutionException | InterruptedException | TimeoutException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            AutocompletePredictionBufferResponse autocompletePredictions = results.getResult();
//
//            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
//                    + " predictions.");
//
//            // Freeze the results immutable representation that can be stored safely.
//            return DataBufferUtils.freezeAndClose(autocompletePredictions);
//        } catch (RuntimeExecutionException e) {
//            // If the query did not complete successfully return null
//            Toast.makeText(getContext(), "Error contacting API: " + e.toString(),
//                    Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Error getting autocomplete prediction API call", e);
//            return null;
//        }
//    }
}
package com.example.yeon1213.myapplication.Living_Weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.yeon1213.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LivingWeather extends AppCompatActivity {
    private RecyclerView living_RecyclerView;
    private RecyclerView.Adapter living_Adapter;
    private RecyclerView.LayoutManager living_LayoutManager;
    private List<Index_data> living_weatherList =new ArrayList<>();
    private int item_id;
    private SharedPreferences index_pref;
    private  SharedPreferences.Editor editor;

    public static final String EXTRA_ACTIVITY_POSITION="com.example.yeon1213.myapplication.Main.MenuItem_tag";

    public static Intent newIntent(Context context,int position){
        Intent activityIntent=new Intent(context, LivingWeather.class);
        activityIntent.putExtra(EXTRA_ACTIVITY_POSITION,position);

        return activityIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_weather);

        index_pref=getSharedPreferences("index_setting", Activity.MODE_PRIVATE);
        editor=index_pref.edit();

        living_RecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        living_RecyclerView.setHasFixedSize(true);

        living_LayoutManager=new LinearLayoutManager(getApplicationContext());
        living_RecyclerView.setLayoutManager(living_LayoutManager);

        living_Adapter=new livingAdapter(living_weatherList);
        living_RecyclerView.setAdapter(living_Adapter);

        item_id=getIntent().getIntExtra(EXTRA_ACTIVITY_POSITION,0);

        if(item_id==R.id.life){
            getSupportActionBar().setTitle("생활기상 지수 선택");
            LivingData();
        }
        else if(item_id==R.id.health){
            getSupportActionBar().setTitle("보건기상 지수 선택");
            HealthData();
        }
    }

    private void LivingData(){
        Index_data index_data =new Index_data("더위 체감지수(서비스 기간: 5월~ 9월)", "기온,습도 햇볕 등을 고려해 인체가 느끼는 더위를 지수로 환산");
        living_weatherList.add(index_data);

        index_data =new Index_data("자외선 지수(서비스 기간: 3월~ 11월)", "태양고도가 최대인 남중시간 때 지표에 도달하는 자외선의 복사랑을 지수로 환산");
        living_weatherList.add(index_data);

        index_data =new Index_data("식중독 지수(서비스 기간: 연중)", "최근 5년('10년~14년)동안의 세균성, 바이러스성 식중독 발생 유무를 기반으로 기상에 따른 식중독 발생 가능성을 나타내는 것");
        living_weatherList.add(index_data);

        index_data =new Index_data("불쾌 지수(서비스 기간: 6월~ 9월)", "기온과 습도의 조합으로 사람이 느끼는 온도를 표현한 것으로 온습도지수(THI)라고도 함");
        living_weatherList.add(index_data);

        index_data =new Index_data("열 지수(서비스 기간: 6월~ 9월)", "기온과 습도에 따른 사람이 실제로 느끼는 더위를 지수화한 것");
        living_weatherList.add(index_data);

        index_data =new Index_data("체감온도(서비스 기간: 11월~ 3월)", "외부에 있는 사람이나 동물이 바람과 한기에 노출된 피부로부터 열을 빼앗길 때 느끼는 추운 정도를 나타내는 지수");
        living_weatherList.add(index_data);

        index_data =new Index_data("동파가능 지수(서비스 기간: 12월~ 2월)", "기온과 일최저기온을 이용하여, 겨울철 한파로 인해 발생되는 수도관 및 계량기의 동파발생가능성을 나타낸 지수");
        living_weatherList.add(index_data);

        index_data =new Index_data("대기 확산 지수(서비스 기간: 11월~ 5월)", "오염물질이 대기 중에 유입되어 존재할 경우, 대기상태(소산과 관련된 기상요소)에 의해 변화될 수 있는 가능성 예보를 의미");
        living_weatherList.add(index_data);

        index_data =new Index_data("세차 지수(서비스 기간: 연중)", "날씨에 따른 세차하기 좋은 정도");
        living_weatherList.add(index_data);

        living_Adapter.notifyDataSetChanged();
    }

    private void HealthData(){
        Index_data index_data =new Index_data("천식폐질환가능지수(서비스 기간: 연중)", "기상조건(최저기온, 일교차, 현지기압, 상대습도)에 따른 천식·폐질환 발생 가능정도를 지수화");
        living_weatherList.add(index_data);

        index_data =new Index_data("뇌졸중가능지수(서비스 기간: 3월~ 11월)", "기상조건(최저기온, 일교차, 현지기압, 상대습도)에 따른 뇌졸중 발생 가능정도를 지수화");
        living_weatherList.add(index_data);

        index_data =new Index_data("피부질환가능지수(서비스 기간: 연중)", "기상조건(최고기온, 상대습도)에 따른 피부질환 발생 가능정도를 지수화");
        living_weatherList.add(index_data);

        index_data =new Index_data("감기가능지수(서비스 기간: 9월~4월)", "기상조건(최저기온, 일교차, 현지기압, 상대습도)에 따른 감기 발생 가능정도를 지수화");
        living_weatherList.add(index_data);

        index_data =new Index_data("꽃가루농도위험지수(서비스 기간: 4월~5월(참나무, 소나무),9월~10월(잡초류))", "기상조건(최고기온, 최저기온, 강수량, 평균풍속 등)에 따른 꽃가루 알레르기 발생 가능정도를 지수화");
        living_weatherList.add(index_data);

        living_Adapter.notifyDataSetChanged();
    }

    class livingAdapter extends RecyclerView.Adapter<livingAdapter.living_ViewHolder>{
        private List<Index_data> index_Dataset;

        public class living_ViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener{

            //index 말고 checkbox도 넣어야 함
            public TextView living_name,living_expla;
            public CheckBox index_checkBox;

            public living_ViewHolder(View v){
                super(v);

                living_name =v.findViewById(R.id.living_name);
                living_expla=v.findViewById(R.id.living_expla);
                index_checkBox=v.findViewById(R.id.index_checkbox);

                index_checkBox.setOnCheckedChangeListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //tag값을 받아와서 그 받아온 값들을 프리퍼런스에 저장,그 값의 id값을 부여, 보여주는 리스트 값을 다르게 설정해야 한다.
                int item_ID=(int) itemView.getTag();
                if(isChecked){
                    editor.putBoolean(living_weatherList.get(item_ID).getLiving_name(),true);
                }else{
                    editor.putBoolean(living_weatherList.get(item_ID).getLiving_name(),false);
                }
                editor.commit();
            }
        }


        public livingAdapter(List<Index_data> myDataset){
            index_Dataset =myDataset;
        }

        @Override
        public living_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.living_row,parent,false);
            return new living_ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(living_ViewHolder living_viewHolder, int position) {
            living_viewHolder.itemView.setTag(position);

            Index_data LD= index_Dataset.get(position);
            living_viewHolder.living_name.setText(LD.getLiving_name());
            living_viewHolder.living_expla.setText("- " + LD.getLiving_explanation());
        }

        @Override
        public int getItemCount() {
            return index_Dataset.size();
        }
    }
}

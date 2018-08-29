package com.example.yeon1213.myapplication.Living_Weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.yeon1213.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LivingWeather extends AppCompatActivity {
    private RecyclerView living_RecyclerView;
    private RecyclerView.Adapter living_Adapter;
    private RecyclerView.LayoutManager living_LayoutManager;
    private List<Living_data> living_weatherList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_weather);

        getSupportActionBar().setTitle("생활기상 지수 선택");

        living_RecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        living_RecyclerView.setHasFixedSize(true);

        living_LayoutManager=new LinearLayoutManager(getApplicationContext());
        living_RecyclerView.setLayoutManager(living_LayoutManager);

        living_Adapter=new livingAdapter(living_weatherList);
        living_RecyclerView.setAdapter(living_Adapter);

        LivingData();
    }

    private void LivingData(){
        Living_data living_data=new Living_data("더위 체감지수(서비스 기간: 5월~ 9월)", "기온,습도 햇볕 등을 고려해 인체가 느끼는 더위를 지수로 환산");
        living_weatherList.add(living_data);

        living_data=new Living_data("자외선 지수(서비스 기간: 3월~ 11월)", "태양고도가 최대인 남중시간 때 지표에 도달하는 자외선의 복사랑을 지수로 환산");
        living_weatherList.add(living_data);

        living_data=new Living_data("식중독 지수(서비스 기간: 연중)", "최근 5년('10년~14년)동안의 세균성, 바이러스성 식중독 발생 유무를 기반으로 기상에 따른 식중독 발생 가능성을 나타내는 것");
        living_weatherList.add(living_data);

        living_data=new Living_data("불쾌 지수(서비스 기간: 6월~ 9월)", "기온과 습도의 조합으로 사람이 느끼는 온도를 표현한 것으로 온습도지수(THI)라고도 함");
        living_weatherList.add(living_data);

        living_data=new Living_data("열 지수(서비스 기간: 6월~ 9월)", "기온과 습도에 따른 사람이 실제로 느끼는 더위를 지수화한 것");
        living_weatherList.add(living_data);

        living_data=new Living_data("체감온도(서비스 기간: 11월~ 3월)", "외부에 있는 사람이나 동물이 바람과 한기에 노출된 피부로부터 열을 빼앗길 때 느끼는 추운 정도를 나타내는 지수");
        living_weatherList.add(living_data);

        living_data=new Living_data("동파가능 지수(서비스 기간: 12월~ 2월)", "기온과 일최저기온을 이용하여, 겨울철 한파로 인해 발생되는 수도관 및 계량기의 동파발생가능성을 나타낸 지수");
        living_weatherList.add(living_data);

        living_data=new Living_data("대기 확산 지수(서비스 기간: 11월~ 5월)", "오염물질이 대기 중에 유입되어 존재할 경우, 대기상태(소산과 관련된 기상요소)에 의해 변화될 수 있는 가능성 예보를 의미");
        living_weatherList.add(living_data);

        living_Adapter.notifyDataSetChanged();
    }

}

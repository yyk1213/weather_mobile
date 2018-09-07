package com.example.yeon1213.myapplication.Main.Weather.FineDust;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FineDust {
    @SerializedName("list")
    @Expose
    private java.util.List<com.example.yeon1213.myapplication.Main.Weather.FineDust.List> list = null;
    @SerializedName("parm")
    @Expose
    private Parm parm;
    @SerializedName("ArpltnInforInqireSvcVo")
    @Expose
    private ArpltnInforInqireSvcVo arpltnInforInqireSvcVo;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public java.util.List<com.example.yeon1213.myapplication.Main.Weather.FineDust.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.yeon1213.myapplication.Main.Weather.FineDust.List> list) {
        this.list = list;
    }

    public Parm getParm() {
        return parm;
    }

    public void setParm(Parm parm) {
        this.parm = parm;
    }

    public ArpltnInforInqireSvcVo getArpltnInforInqireSvcVo() {
        return arpltnInforInqireSvcVo;
    }

    public void setArpltnInforInqireSvcVo(ArpltnInforInqireSvcVo arpltnInforInqireSvcVo) {
        this.arpltnInforInqireSvcVo = arpltnInforInqireSvcVo;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}

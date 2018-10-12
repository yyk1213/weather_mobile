package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Minutely {
    @SerializedName("station")
    @Expose
    private Station station;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("precipitation")
    @Expose
    private Precipitation precipitation;
    @SerializedName("sky")
    @Expose
    private Sky sky;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("temperature")
    @Expose
    private Temperature temperature;
    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("pressure")
    @Expose
    private Pressure pressure;
    @SerializedName("lightning")
    @Expose
    private String lightning;
    @SerializedName("timeObservation")
    @Expose
    private String timeObservation;

    @SerializedName("NearValue")
    @Expose
    private NearValue nearValue;

    public NearValue getNearValue() {
        return nearValue;
    }

    public void setNearValue(NearValue nearValue) {
        this.nearValue = nearValue;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Sky getSky() {
        return sky;
    }

    public void setSky(Sky sky) {
        this.sky = sky;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public String getLightning() {
        return lightning;
    }

    public void setLightning(String lightning) {
        this.lightning = lightning;
    }

    public String getTimeObservation() {
        return timeObservation;
    }

    public void setTimeObservation(String timeObservation) {
        this.timeObservation = timeObservation;
    }

    class Pressure {

        @SerializedName("surface")
        @Expose
        private String surface;
        @SerializedName("seaLevel")
        @Expose
        private String seaLevel;

        public String getSurface() {
            return surface;
        }

        public void setSurface(String surface) {
            this.surface = surface;
        }

        public String getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(String seaLevel) {
            this.seaLevel = seaLevel;
        }
    }
    class Rain {

        @SerializedName("sinceMidnight")
        @Expose
        private String sinceMidnight;
        @SerializedName("last10min")
        @Expose
        private String last10min;
        @SerializedName("last15min")
        @Expose
        private String last15min;
        @SerializedName("last30min")
        @Expose
        private String last30min;
        @SerializedName("last1hour")
        @Expose
        private String last1hour;
        @SerializedName("last6hour")
        @Expose
        private String last6hour;
        @SerializedName("last12hour")
        @Expose
        private String last12hour;
        @SerializedName("sinceOntime")
        @Expose
        private String sinceOntime;
        @SerializedName("last24hour")
        @Expose
        private String last24hour;

        public String getSinceMidnight() {
            return sinceMidnight;
        }

        public void setSinceMidnight(String sinceMidnight) {
            this.sinceMidnight = sinceMidnight;
        }

        public String getLast10min() {
            return last10min;
        }

        public void setLast10min(String last10min) {
            this.last10min = last10min;
        }

        public String getLast15min() {
            return last15min;
        }

        public void setLast15min(String last15min) {
            this.last15min = last15min;
        }

        public String getLast30min() {
            return last30min;
        }

        public void setLast30min(String last30min) {
            this.last30min = last30min;
        }

        public String getLast1hour() {
            return last1hour;
        }

        public void setLast1hour(String last1hour) {
            this.last1hour = last1hour;
        }

        public String getLast6hour() {
            return last6hour;
        }

        public void setLast6hour(String last6hour) {
            this.last6hour = last6hour;
        }

        public String getLast12hour() {
            return last12hour;
        }

        public void setLast12hour(String last12hour) {
            this.last12hour = last12hour;
        }

        public String getSinceOntime() {
            return sinceOntime;
        }

        public void setSinceOntime(String sinceOntime) {
            this.sinceOntime = sinceOntime;
        }

        public String getLast24hour() {
            return last24hour;
        }

        public void setLast24hour(String last24hour) {
            this.last24hour = last24hour;
        }
    }
}

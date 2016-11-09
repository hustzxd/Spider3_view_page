package com.example.hustzxd.spider3_view_page.javaBean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 记录位置信息和
 * Created by Administrator on 2016/5/27.
 */
public class Location extends BmobObject {
    private String Location;
    private List<String> BSSIDList;
    private List<Integer> RSSIDList;

    @Override
    public String toString() {
        return "Location{" +
                "Location='" + Location + '\'' +
                ", BSSIDList=" + BSSIDList +
                ", RSSIDList=" + RSSIDList +
                '}';
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public List<String> getBSSIDList() {
        return BSSIDList;
    }

    public void setBSSIDList(List<String> BSSIDList) {
        this.BSSIDList = BSSIDList;
    }

    public List<Integer> getRSSIDList() {
        return RSSIDList;
    }

    public void setRSSIDList(List<Integer> RSSIDList) {
        this.RSSIDList = RSSIDList;
    }
}

package com.example.hustzxd.spider3_view_page;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustzxd.spider3_view_page.javaBean.Location;
import com.example.hustzxd.spider3_view_page.mapview.MapViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Button mUploadBtn;
    private static Button mLocateBtn;
    private static ProgressBar mProgressBarUpload;
    private static TextView mProgressUpload;
    private static EditText mLocationEt;
    private static WifiManager mWifiManagerUpload;
    private static WifiManager mWifiManagerLocate;
    private static ProgressBar mProgressBarLocate;
    private static TextView mProgressLocate;
    private static TextView mMyLocationEt;
    private static String startWith = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "490fc9fadc396031ddc7ccf4a4c05b8b");
        mWifiManagerUpload = (WifiManager) getSystemService(WIFI_SERVICE);
        mWifiManagerLocate = (WifiManager) getSystemService(WIFI_SERVICE);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.readme, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                mLocateBtn = (Button) rootView.findViewById(R.id.locate_btn);
                mMyLocationEt = (TextView) rootView.findViewById(R.id.location_tv);
                mProgressBarLocate = (ProgressBar) rootView.findViewById(R.id.progressBar);
                mProgressLocate = (TextView) rootView.findViewById(R.id.progress);
                mLocateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mMyLocationEt.setText("China! right? ~ ~");
                        if (!mWifiManagerLocate.isWifiEnabled()) {
                            mWifiManagerLocate.setWifiEnabled(true);
                            return;
                        }
                        Toast.makeText(getContext(), "locating!", Toast.LENGTH_LONG).show();
                        new MyTask2().execute();
                    }
                });
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_upload, container, false);
                mUploadBtn = (Button) rootView.findViewById(R.id.upload_btn);
                mProgressBarUpload = (ProgressBar) rootView.findViewById(R.id.progressBar);
                mProgressUpload = (TextView) rootView.findViewById(R.id.progress);
                mLocationEt = (EditText) rootView.findViewById(R.id.location_et);
                mUploadBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mWifiManagerUpload.isWifiEnabled()) {
                            mWifiManagerUpload.setWifiEnabled(true);
                            return;
                        }
                        String location = mLocationEt.getText().toString();
                        if (TextUtils.isEmpty(location)) {
                            mLocationEt.setError("where are you?");
                            return;
                        }
                        Toast.makeText(getContext(), "uploading!", Toast.LENGTH_LONG).show();
                        new MyTask().execute(location);
                    }
                });
            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.fragment_third, container, false);
                Button testMapVeiwBtn = (Button) rootView.findViewById(R.id.test_map_view_btn);
                testMapVeiwBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), MapViewActivity.class);
                        startActivityForResult(intent, 0);
                    }
                });
            }
            return rootView;
        }

        /**
         * 采集wifi信息，并执行云端代码，返回位置信息
         */
        private class MyTask2 extends AsyncTask<Void, Integer, JSONObject> {

            private int number = 1;//采集wifi次数
            private int interval = 1500;//采集间隔时间，单位ms

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLocateBtn.setClickable(false);
                mProgressBarLocate.setVisibility(View.VISIBLE);
                mProgressLocate.setText("%0");
//                mProgressLocate.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mProgressLocate.setText("%" + values[0]);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                mLocateBtn.setClickable(true);
                mProgressLocate.setVisibility(View.GONE);

                Log.i("sss", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("BSSID");
                    if (jsonArray.length() == 0) {
                        Toast.makeText(getContext(), "There is no wifi starts with " + startWith,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                    //第一个参数是上下文对象，第二个参数是云端逻辑的方法名称，
                    // 第三个参数是上传到云端逻辑的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
                    String cloudCodeName = "locate_test";
                    ace.callEndpoint(getContext(), cloudCodeName, jsonObject, new CloudCodeListener() {
                        @Override
                        public void onSuccess(Object o) {
                            mMyLocationEt.setText(o.toString());
                            mProgressBarLocate.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            mMyLocationEt.setText(s);
                            mProgressBarLocate.setVisibility(View.GONE);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(Void... params) {
                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i < number; i++) {
                    mWifiManagerLocate.startScan();
                    try {
                        Thread.sleep(interval);
                        publishProgress(100 / number * (i + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<ScanResult> scanResults = mWifiManagerLocate.getScanResults();
                    for (ScanResult result : scanResults) {
                        result.level += 100;
                        if (result.SSID.startsWith(startWith)) {
                            if (map.containsKey(result.BSSID)) {
                                map.put(result.BSSID, result.level + map.get(result.BSSID));
                            } else {
                                map.put(result.BSSID, result.level);
                            }
                        }
                    }
                }
                List<String> BSSIDs = new ArrayList<>();
                List<Integer> RSSIs = new ArrayList<>();
                for (String key : map.keySet()) {
//                    if (map.get(key) / number >= 20) {
                    BSSIDs.add(key);
                    RSSIs.add(map.get(key) / number);
                    Log.i("sss", "key" + key + "level" + map.get(key) / number);
//                    }
                }
                JSONArray jsonArray = new JSONArray(BSSIDs);
                JSONArray jsonArray1 = new JSONArray(RSSIs);
                Map map1 = new HashMap();
                map1.put("BSSID", jsonArray);
                map1.put("RSSI", jsonArray1);
                JSONObject jsonObject = new JSONObject(map1);
                return jsonObject;
            }
        }

        /**
         * 获取wifi信息的任务
         * 第一个参数是传入参数
         * 第二是Progress
         * 第三个是返回参数
         * Created by Administrator on 2016/5/27.
         */
        private class MyTask extends AsyncTask<String, Integer, Location> {
            private int number = 3;//采集wifi次数
            private int interval = 1500;//采集间隔时间，单位ms

            /**
             * 任务执行前，UI线程更新
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //更新按钮
                mUploadBtn.setClickable(false);
                mProgressBarUpload.setVisibility(View.VISIBLE);
//                mProgressUpload.setText("%0");
//                mProgressUpload.setVisibility(View.VISIBLE);
            }

            /**
             * 任务执行完
             *
             * @param location
             */
            @Override
            protected void onPostExecute(Location location) {
                super.onPostExecute(location);
                mUploadBtn.setClickable(true);

                mProgressUpload.setVisibility(View.GONE);
                if (location.getBSSIDList().size() == 0) {
//                    Toast.makeText(getContext(), "There is no wifi starts with " + startWith,
//                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "There is no wifi" + startWith,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                location.save(getContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "upload successful!", Toast.LENGTH_SHORT).show();
                        mProgressBarUpload.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getContext(), "upload failed!\n error message:" + s,
                                Toast.LENGTH_SHORT).show();
                        mProgressBarUpload.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mProgressUpload.setText("%" + values[0]);
            }

            @Override
            protected Location doInBackground(String... params) {
                Location location = new Location();
                List<String> BSSIDs = new ArrayList<>();
                List<Integer> RSSIs = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();
                for (int i = 0; i < number; i++) {
                    mWifiManagerUpload.startScan();
                    try {
                        Thread.sleep(interval);
                        publishProgress(100 / number * (i + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<ScanResult> scanResults = mWifiManagerUpload.getScanResults();
                    for (ScanResult scanResult : scanResults) {
                        scanResult.level += 100;
                        if (scanResult.SSID.startsWith(startWith)) {
                            if (!map.containsKey(scanResult.BSSID)) {
                                map.put(scanResult.BSSID, scanResult.level);
                            } else {
                                map.put(scanResult.BSSID, scanResult.level + map.get(scanResult.BSSID));
                            }
                        }
                    }
                }
                for (String s : map.keySet()) {
                    String key;
                    int value;
                    key = s;
                    value = map.get(key);
//                    if (value / number >= 20) {
                    BSSIDs.add(key);
                    RSSIs.add(value / number);
                    Log.i("sss", key + "--" + value / number);
//                    }
                }
                location.setLocation(params[0]);
                location.setBSSIDList(BSSIDs);
                location.setRSSIDList(RSSIs);
                return location;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

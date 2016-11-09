package com.example.hustzxd.spider3_view_page.mapview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hustzxd.spider3_view_page.R;
import com.onlylemi.mapview.library.MapView;
import com.onlylemi.mapview.library.MapViewListener;

import java.io.IOException;

public class MapLayerTestActivity extends AppCompatActivity {
    private static final String TAG = "MapLayerTestActivity";

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_layer_test);
        mapView = (MapView) findViewById(R.id.mapview);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getAssets().open("map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.loadMap(bitmap);
        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onMapLoadSuccess() {
                Log.i(TAG, "onMapLoadSuccess");
                //mapView.setCurrentRotateDegrees(60);
            }

            @Override
            public void onMapLoadFail() {
                Log.i(TAG, "onMapLoadFail");
            }

        });
    }
}

package com.thimat.sockettelkarnet.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.getyourmap.glmap.GLMapBBox;
import com.getyourmap.glmap.GLMapDownloadTask;
import com.getyourmap.glmap.GLMapDrawable;
import com.getyourmap.glmap.GLMapImageGroup;
import com.getyourmap.glmap.GLMapImageGroupCallback;
import com.getyourmap.glmap.GLMapInfo;
import com.getyourmap.glmap.GLMapLocaleSettings;
import com.getyourmap.glmap.GLMapManager;
import com.getyourmap.glmap.GLMapMarkerLayer;
import com.getyourmap.glmap.GLMapTrack;
import com.getyourmap.glmap.GLMapTrackData;
import com.getyourmap.glmap.GLMapVectorObjectList;
import com.getyourmap.glmap.GLMapView;
import com.getyourmap.glmap.GLMapView.GLMapPlacement;
import com.getyourmap.glmap.GLMapView.GLMapTileState;
import com.getyourmap.glmap.GLMapView.GLUnitSystem;
import com.getyourmap.glmap.ImageManager;
import com.getyourmap.glmap.MapPoint;
import com.thimat.sockettelkarnet.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;

public class MapViewActivity extends Activity
        implements GLMapView.ScreenCaptureCallback, GLMapManager.StateListener {

    private static class Pin {
        MapPoint pos;
        int imageVariant;
    }

    private static class Pins implements GLMapImageGroupCallback {
        private final ReentrantLock lock;
        private final Bitmap[] images;
        private final List<Pin> pins;

        Pins(ImageManager imageManager) {
            lock = new ReentrantLock();
            images = new Bitmap[3];
            pins = new ArrayList<>();
            images[0] = imageManager.open("1.svgpb", 1, 0xFFFF0000);
            images[1] = imageManager.open("2.svgpb", 1, 0xFF00FF00);
            images[2] = imageManager.open("3.svgpb", 1, 0xFF0000FF);
        }

        @Override
        public int getImagesCount() {
            return pins.size();
        }

        @Override
        public int getImageIndex(int i) {
            return pins.get(i).imageVariant;
        }

        @Override
        public MapPoint getImagePos(int i) {
            return pins.get(i).pos;
        }

        @Override
        public void updateStarted() {
            Log.i("GLMapImageGroupCallback", "Update started");
            lock.lock();
        }

        @Override
        public void updateFinished() {
            Log.i("GLMapImageGroupCallback", "Update finished");
            lock.unlock();
        }

        @Override
        public int getImageVariantsCount() {
            return images.length;
        }

        @Override
        public Bitmap getImageVariantBitmap(int i) {
            return images[i];
        }

        @Override
        public MapPoint getImageVariantOffset(int i) {
            return new MapPoint(images[i].getWidth() / 2.0, 0);
        }

        int size() {
            int rv;
            lock.lock();
            rv = pins.size();
            lock.unlock();
            return rv;
        }

        void add(Pin pin) {
            lock.lock();
            pins.add(pin);
            lock.unlock();
        }

        void remove(Pin pin) {
            lock.lock();
            pins.remove(pin);
            lock.unlock();
        }

        Pin findPin(GLMapView mapView, float touchX, float touchY) {
            Pin rv = null;
            lock.lock();
            for (int i = 0; i < pins.size(); ++i) {
                Pin pin = pins.get(i);

                MapPoint screenPos = mapView.convertInternalToDisplay(new MapPoint(pin.pos));
                Rect rt = new Rect(-40, -40, 40, 40);
                rt.offset((int) screenPos.x, (int) screenPos.y);
                if (rt.contains((int) touchX, (int) touchY)) {
                    rv = pin;
                    break;
                }
            }
            lock.unlock();
            return rv;
        }
    }

    private GLMapDrawable image;
    private GLMapImageGroup imageGroup;
    private Pins pins;
    private GestureDetector gestureDetector;
    protected GLMapView mapView;
    private GLMapInfo mapToDownload;
    private Button btnDownloadMap;

    private GLMapMarkerLayer markerLayer;
    private GLMapLocaleSettings localeSettings;
    private CurLocationHelper curLocationHelper;

    private int trackPointIndex;
    private GLMapTrack track;
    private GLMapTrackData trackData;
    private Handler handler;
    private Runnable trackRecordRunnable;
    double offlinelat=0;
    double offlinelot=0;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        mapView = this.findViewById(R.id.map_view);
        //------------------------------------------------
         offlinelat = getIntent().getDoubleExtra("lat", 0);
         offlinelot = getIntent().getDoubleExtra("lon", 0);
        // Map list is updated, because download button depends on available map list and during first
        // launch this list is empty
        GLMapManager.UpdateMapList(null);

        btnDownloadMap = this.findViewById(R.id.button_dl_map);
        btnDownloadMap.setOnClickListener(v -> {
            if (mapToDownload != null) {
                GLMapDownloadTask task = GLMapManager.getDownloadTask(mapToDownload);
                if (task != null) {
                    task.cancel();
                } else {
                    GLMapManager.DownloadDataSets(
                            mapToDownload, GLMapInfo.DataSetMask.ALL, MapViewActivity.this);
                }
                updateMapDownloadButtonText();
            } else {
                Intent i = new Intent(v.getContext(), DownloadActivity.class);

                MapPoint pt = mapView.getMapCenter();
                i.putExtra("cx", pt.x);
                i.putExtra("cy", pt.y);
                v.getContext().startActivity(i);
            }
        });

        GLMapManager.addStateListener(this);

        localeSettings = new GLMapLocaleSettings();
        mapView.setLocaleSettings(localeSettings);
        mapView.loadStyle(getAssets(), "DefaultStyle.bundle");
        checkAndRequestLocationPermission();

        mapView.setScaleRulerStyle(
                GLUnitSystem.International, GLMapPlacement.BottomCenter, new MapPoint(10, 10), 200);
        mapView.setAttributionPosition(GLMapPlacement.TopCenter);

        mapView.setCenterTileStateChangedCallback(this::updateMapDownloadButton);
        mapView.setMapDidMoveCallback(this::updateMapDownloadButtonText);
        //--------------------------------------------------------------------

    }

    protected int getLayoutID() {
        return R.layout.map_view_activity;
    }


    public void checkAndRequestLocationPermission() {
        // Create helper if not exist
        if (curLocationHelper == null) curLocationHelper = new CurLocationHelper(mapView,offlinelat,offlinelot);

        // Try to start location updates. If we need permissions - ask for them
        if (!curLocationHelper.initLocationManager(this))
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    0);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    curLocationHelper.initLocationManager(this);
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        GLMapManager.removeStateListener(this);
        if (mapView != null) {
            mapView.removeAllObjects();
            mapView.setCenterTileStateChangedCallback(null);
            mapView.setMapDidMoveCallback(null);
        }

        if (markerLayer != null) {
            markerLayer.dispose();
            markerLayer = null;
        }

        if (imageGroup != null) {
            imageGroup.dispose();
            imageGroup = null;
        }

        if (curLocationHelper != null) {
            curLocationHelper.onDestroy();
            curLocationHelper = null;
        }

        if (handler != null) {
            handler.removeCallbacks(trackRecordRunnable);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mapView.animate(animation -> mapView.setMapZoom(mapView.getMapZoom() - 1));
        return false;
    }

    @Override
    public void onStartDownloading(GLMapDownloadTask task) {}

    @Override
    public void onDownloadProgress(GLMapDownloadTask task) {
        updateMapDownloadButtonText();
    }

    @Override
    public void onFinishDownloading(GLMapDownloadTask task) {
        mapView.reloadTiles();
    }

    @Override
    public void onStateChanged(GLMapInfo map) {
        updateMapDownloadButtonText();
    }

    private void updateMapDownloadButtonText() {
        if (btnDownloadMap.getVisibility() == View.VISIBLE) {
            MapPoint center = mapView.getMapCenter();

            mapToDownload = GLMapManager.MapAtPoint(center);

            if (mapToDownload != null) {
                String text;
                GLMapDownloadTask task = GLMapManager.getDownloadTask(mapToDownload);
                if (task != null && task.total != 0) {
                    long progress = (long)task.downloaded * 100 / task.total;
                    text = String.format(Locale.getDefault(), "Downloading %s %d%%",
                            mapToDownload.getLocalizedName(localeSettings), progress);
                } else {
                    text = String.format(Locale.getDefault(), "Download %s",
                            mapToDownload.getLocalizedName(localeSettings));
                }
                btnDownloadMap.setText(text);
            } else {
                btnDownloadMap.setText("Download maps");
            }
        }
    }
    public void updateMapDownloadButton() {
        switch (mapView.getCenterTileState()) {
            case GLMapTileState.NoData:
            {
                if (btnDownloadMap.getVisibility() == View.INVISIBLE) {
                    btnDownloadMap.setVisibility(View.VISIBLE);
                    btnDownloadMap.getParent().requestLayout();
                    updateMapDownloadButtonText();
                }
                break;
            }

            case GLMapTileState.Loaded:
            {
                if (btnDownloadMap.getVisibility() == View.VISIBLE) {
                    btnDownloadMap.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case GLMapTileState.Unknown:
                break;
        }
    }

    void zoomToPoint() {
        // New York
        // MapPoint pt = new MapPoint(-74.0059700 , 40.7142700	);

        // Belarus
        // MapPoint pt = new MapPoint(27.56, 53.9);
        // ;

        // Move map to the Montenegro capital
        MapPoint pt = MapPoint.CreateFromGeoCoordinates(42.4341, 19.26);
        GLMapView mapView = this.findViewById(R.id.map_view);
        mapView.setMapCenter(pt);
        mapView.setMapZoom(16);
    }
    void addImage(final Button btn) {
        Bitmap bmp = mapView.imageManager.open("arrow-maphint.svgpb", 1, 0);
        image = new GLMapDrawable(bmp, 2);
        image.setOffset(bmp.getWidth(), bmp.getHeight() / 2);
        image.setRotatesWithMap(true);
        image.setAngle((float) Math.random() * 360);
        image.setPosition(mapView.getMapCenter());
        mapView.add(image);

        btn.setText("Move image");
        btn.setOnClickListener(v -> moveImage(btn));
    }

    void moveImage(final Button btn) {
        image.setPosition(mapView.getMapCenter());
        btn.setText("Remove image");
        btn.setOnClickListener(v -> delImage(btn));
    }

    void delImage(final Button btn) {
        if (image != null) {
            mapView.remove(image);
            image.dispose();
            image = null;
        }
        btn.setText("Add image");
        btn.setOnClickListener(v -> addImage(btn));
    }
    private void zoomToObjects(GLMapVectorObjectList objects) {
        // Zoom to bbox
        GLMapBBox bbox = objects.getBBox();
        mapView.doWhenSurfaceCreated(() -> {
            mapView.setMapCenter(bbox.center());
            mapView.setMapZoom(mapView.mapZoomForBBox(bbox, mapView.getWidth(), mapView.getHeight()));
        });
    }
    @Override
    public void screenCaptured(final Bitmap bmp) {
        this.runOnUiThread(() -> {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            try {
                FileOutputStream fo = openFileOutput("screenCapture", Context.MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                fo.close();

                File file = new File(getCacheDir()+"/Telkarnet/", "Test.jpg");
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Intent intent = new Intent(MapViewActivity.this, DisplayImageActivity.class);
//            Bundle b = new Bundle();
//            b.putString("imageName", "screenCapture");
//            intent.putExtras(b);
//            startActivity(intent);
        });
    }
}

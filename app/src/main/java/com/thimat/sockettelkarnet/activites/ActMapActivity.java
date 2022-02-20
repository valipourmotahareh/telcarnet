package com.thimat.sockettelkarnet.activites;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.R;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActMapActivity extends AppCompatActivity {
    @BindView(R.id.mapView1)
    MapView mapView;
    private String path="";
    private static final String MAPFILE = "iran.map";
    public TappableMarker marker;
    HillsRenderConfig hillsRenderConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        path=getCacheDir().getPath() + "/Telkarnet/";
        ButterKnife.bind(this);
        AndroidGraphicFactory.createInstance(getApplication());
        ///////////////////////////////getextra
        double offlinelat = getIntent().getDoubleExtra("lat", 0);
        double offlinelot = getIntent().getDoubleExtra("lon", 0);
        /////////////////////////////copy  map
        copyFileAssets();
        //////////////////////////////
        if (getMapFile() == null)
            return;
        if (getMapFile().exists() == false) {
            Toast.makeText(this, "فایل نقشه موجود نیست", Toast.LENGTH_LONG).show();
            finish();
        }
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);

        //////////////////////first
        try {
            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());
            MapDataStore mapDataStore = new MapFile(getMapFile());
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition,true,true,true,AndroidGraphicFactory.INSTANCE,
                    hillsRenderConfig);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
            mapView.getLayerManager().getLayers().add(tileRendererLayer);
             showlocation(offlinelat, offlinelot, 18);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    ///////////////////////////copy mapfunction
    private void copyFileAssets() {
        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(path + "iran.map");
            if (!dir.exists()) {
                in = assetManager.open("iran.map");
                out = new FileOutputStream(path + "iran.map");
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            }
        } catch (IOException e) {
            Toast.makeText(this, "خطا در کپی فایل نقشه", Toast.LENGTH_LONG).show();
        }

    }

    //-----------------------------------------------------
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    //----------------------------------------------------getmapfile
    private File getMapFile() {
        try {
            File file = new File(path, MAPFILE);
            return file;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    ///////////////////////////////////////////showlocation
    public void showlocation(double lat, double lon, int spees) {
        LatLong latlon = new LatLong(lat, lon);
        mapView.getModel().mapViewPosition.setCenter(latlon);
        marker = new TappableMarker(R.drawable.carlocation, latlon);
        mapView.getLayerManager().getLayers().add(marker);
        mapView.getModel().mapViewPosition.setZoomLevel((byte) spees);
    }

    ///////////////////////////////////////////////////////////////////////////////
    private class TappableMarker extends Marker {
        public TappableMarker(int icon, LatLong localLatLong) {
            super(localLatLong, AndroidGraphicFactory.convertToBitmap(ActMapActivity.this.getApplicationContext().getResources().getDrawable(icon)),
                    1 * (AndroidGraphicFactory.convertToBitmap(ActMapActivity.this.getApplicationContext().getResources().getDrawable(icon)).getWidth()) / 2,
                    -1 * (AndroidGraphicFactory.convertToBitmap(ActMapActivity.this.getApplicationContext().getResources().getDrawable(icon)).getHeight()) / 2);
        }

    }

    @Override
    protected void onDestroy() {
        /*
         * Whenever your activity exits, some cleanup operations have to be performed lest your app
         * runs out of memory.
         */
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }
}

package me.starainx.flutterpluginwechatscan;


import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.zxing.Result;
import com.king.zxing.CaptureActivity;
import com.king.zxing.ViewfinderView;

public class FlutterWechatScanActivity extends CaptureActivity {

    public final static String  KEY_TITLE = "__title";

    public final static String  KEY_TIP_TEXT = "__tip_text";

    public final static String  KEY_ALLOW_ZOOM = "__allow_zoom";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_capture_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        StatusBarUtils.immersiveStatusBar(this,toolbar,0.2f);
        TextView tvTitle = findViewById(R.id.tvTitle);

        String title = "二维码扫描";
        boolean allowZoom = getIntent().getBooleanExtra(KEY_ALLOW_ZOOM,true);

        if (getIntent() != null) {
            String argTitle = getIntent().getStringExtra(KEY_TITLE);
            if (argTitle != null) {
                title = argTitle;
            }

        }
        tvTitle.setText(title);
        setZoom(allowZoom);
        // tvTitle.setText(getIntent().getStringExtra(KEY_TITLE));

        // isContinuousScan = getIntent().getBooleanExtra(MainActivity.KEY_IS_CONTINUOUS,false);

        getBeepManager().setPlayBeep(true);
        getBeepManager().setVibrate(true);

        ViewfinderView viewFinder = findViewById(getViewFinderViewId());
        String tipText = getIntent().getStringExtra(KEY_TIP_TEXT);
        if (tipText != null) {
            viewFinder.setLabelText(tipText);
        }
//        setZoom(fals);
    }

    private void clickFlash(View v) {
        if (v.isSelected()) {
            offFlash();
            v.setSelected(false);
        } else {
            openFlash();
            v.setSelected(true);
        }

    }

    /**
     * 关闭闪光灯（手电筒）
     */
    private void offFlash() {
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        System.out.println(rawResult.getText());
        super.handleDecode(rawResult, barcode, scaleFactor);
    }

    /**
     * 开启闪光灯（手电筒）
     */
    public void openFlash() {
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }

    public void OnClick(View v) {
        int i = v.getId();
        if (i == R.id.ivLeft) {
            onBackPressed();

        } else if (i == R.id.ivFlash) {
            clickFlash(v);

        }
    }
}

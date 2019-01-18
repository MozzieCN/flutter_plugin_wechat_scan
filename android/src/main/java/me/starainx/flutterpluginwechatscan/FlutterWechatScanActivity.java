package me.starainx.flutterpluginwechatscan;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.king.zxing.CaptureActivity;

import android.graphics.Bitmap;


public class FlutterWechatScanActivity extends CaptureActivity {

    public final static String  KEY_TITLE = "__key";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_capture_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        StatusBarUtils.immersiveStatusBar(this,toolbar,0.2f);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("二维码扫码");
        // tvTitle.setText(getIntent().getStringExtra(KEY_TITLE));

        // isContinuousScan = getIntent().getBooleanExtra(MainActivity.KEY_IS_CONTINUOUS,false);

        getBeepManager().setPlayBeep(true);
        getBeepManager().setVibrate(true);
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

package me.starainx.flutterpluginwechatscan;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterPluginWechatScanPlugin
 */
public class FlutterPluginWechatScanPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener {
    /**
     * Plugin registration.
     */
    private static FlutterPluginWechatScanPlugin instance;
    private FlutterActivity activity;
    private Result pendingResult;
    Map<String, Object> arguments;
    private int REQUEST_CODE_CAMERA_PERMISSION = 73722;
    private int REQUEST_CODE_SCAN_ACTIVITY = 73721;
    private final String TAG = FlutterPluginWechatScanPlugin.class.getSimpleName();
    public FlutterPluginWechatScanPlugin(FlutterActivity activity) {
        this.activity = activity;
    }

    public static void registerWith(Registrar registrar) {
        if (instance == null) {
            final MethodChannel channel = new MethodChannel(registrar.messenger(), "me.starainx/flutter_plugin_wechat_scan");
            instance = new FlutterPluginWechatScanPlugin((FlutterActivity) registrar.activity());
            registrar.addActivityResultListener(instance);
            registrar.addRequestPermissionsResultListener(instance);
            channel.setMethodCallHandler(instance);
        }
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (pendingResult != null) {
            result.error("flutter_plugin_wechat_scan", "QR Code reader is already active", null);
            return;
        }
        pendingResult = result;

        if (call.method.equals("scan")) {
            if (!(call.arguments instanceof Map)) {
                throw new IllegalArgumentException("flutter_plugin_wechat need a map as arguments");
            }
            arguments = (Map<String, Object>) call.arguments;
            if (activity.checkPermission(Manifest.permission.CAMERA, android.os.Process.myPid(), android.os.Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    requestPermissions();

                } else {
                    requestPermissions();
                }
            } else {
                startActivity();
            }
        } else {
            result.notImplemented();
        }


//    if (call.method.equals("getPlatformVersion")) {
//      result.success("Android " );
//    } else {
//      result.notImplemented();
//    }
    }

    private void startActivity() {
        Log.i(TAG, String.valueOf(arguments));
        Intent intent = new Intent(activity, FlutterWechatScanActivity.class);
        if (arguments != null) {
             intent.putExtra(FlutterWechatScanActivity.KEY_TITLE, (String)arguments.get("title"));
        }
        activity.startActivityForResult(intent, REQUEST_CODE_SCAN_ACTIVITY);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        activity.requestPermissions(new String[]{
                Manifest.permission.CAMERA
        }, REQUEST_CODE_CAMERA_PERMISSION);
    }

    private boolean shouldShowRequestPermissionRationale(Activity activity,
                                                         String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int i, int i1, Intent intent) {
        if (i == REQUEST_CODE_SCAN_ACTIVITY) {
            if (i1 == Activity.RESULT_OK) {
                String result = intent.getStringExtra(FlutterWechatScanActivity.KEY_RESULT);
                pendingResult.success(result);
            } else {
                pendingResult.success(null);
            }
            pendingResult = null;
            arguments = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];
                if (permission.equalsIgnoreCase(Manifest.permission.CAMERA)) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        startActivity();
                    } else {
                        setNoPermissionsError();
                    }
                }
            }
        }
        return false;
    }

    private void setNoPermissionsError() {
        pendingResult.error("permission", "you don't have the user permission to access the camera", null);
        pendingResult = null;
        arguments = null;
    }
}

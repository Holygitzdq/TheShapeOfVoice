package com.pa.theshapeofvoice;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by nathanchen on 2017/8/21.
 */

public class PermissionsUtil {

    private static final String TAG = "PermissionsUtil*";

    public static void permissionPACheck(final Context context) {
        String spName = "PEM_FIRST_USE";
        SharedPreferences setting = context.getSharedPreferences(spName, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);

        boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                //有权限，不做操作
                Log.d(TAG, "有权限");
            } else {
                //无权限，启动提示
                Log.d(TAG, "无权限");
                if (!isTip) {
                    if (user_first) {//第一次
                        setting.edit().putBoolean("FIRST", false).commit();
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                        Log.d(TAG, "首次进入app");
                    } else {
                        //用户阻止弹窗再出现,提醒用户自行去设置界面打开权限
                        showMessageOKCancel(context, "请前往设置手动开启麦克风权限",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent localIntent = new Intent();
                                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                        context.startActivity(localIntent);
                                        Log.d(TAG, "onClick--动态申请permission");
                                    }
                                });
                        Log.d(TAG, "用户选择不再提醒，不弹系统权限申请窗口");
                        return;
                    }
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    Log.d(TAG, "请求权限--1--");
                }
            }
        }
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    private static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }


}

package org.apache.fineract.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import org.apache.fineract.R;
import org.apache.fineract.data.local.PreferencesHelper;

/**
 * This Class is the CheckSelfPermissionAndRequest Class
 * Created by Rajan Maurya on 03/08/16.
 */
public class CheckSelfPermissionAndRequest {


    /**
     * This Method Check the Permission is granted or not to the App. If the Permission granted,
     * returns true and If not permission denied then returns false.
     *
     * @param context    Context
     * @param permission Manifest.permission...Permission...
     * @return Boolean True or False.
     */
    public static Boolean checkSelfPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public static Boolean checkMultiplePermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * This Method is requesting to device to grant the permission. When App is trying to
     * request the device to grant the permission, then their is Three cases.
     * 1. First case Device Prompt the Permission Dialog to user and user accepted or denied the
     * Permission.
     * 2. Second case will come, if user will denied the permission, after onclick dialog denied
     * button and next time App ask for permission, It will show a Material Dialog and there
     * will be a message to tell the user that you have denied the permission before, So do
     * you want to give this permission to app or not, If yes then click on Re-Try dialog button
     * and if not then click on Dialog button "I'M Sure", to not to give this permission to the
     * app.
     * <p/>
     * And as user will click on "Re-Try" dialog button, he will be prompt with the with
     * permission dialog with "[-] never ask again" and have two options first one to click on
     * denied button again and put Un check the never ask check box. In this case, user will
     * prompt with permission dialog with "[-] never ask again" in the loop, whenever app ask
     * for that permission.
     * <p/>
     * and If user will click on "[_/] never ask again" check box then permission dialog with
     * that permission will not prompt to the user.
     * 3. Third case will came. when user have denied to accept permission with never ask again.
     * then user will prompt with dialog and message that you have denied this permission with
     * never ask again. but this is necessary permission to this app feature. and to grant
     * this permission please click on dialog app settings button and give the permission to
     * work with this feature.
     *
     * @param activity               AppCompatActivity
     * @param permissions            Manifest.permission...Permission...
     * @param permissionRequestCode  Permission Request Code.
     * @param dialogMessageRetry     Dialog Message Retry
     * @param messageNeverAskAgain   Dialog Message Never Ask Again
     * @param permissionDeniedStatus Permission Denied Status
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void requestPermissions(final AppCompatActivity activity,
            final String[] permissions,
            final int permissionRequestCode,
            final String[] dialogMessageRetry,
            final String[] messageNeverAskAgain,
            final String[] permissionDeniedStatus) {

        for (int i = 0; i < permissions.length; ++i) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new MaterialDialog.Builder().init(activity)
                        .setTitle(R.string.dialog_permission_denied)
                        .setMessage(dialogMessageRetry[i])
                        .setPositiveButton(R.string.dialog_action_re_try,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(activity, permissions,
                                                permissionRequestCode);
                                    }
                                })
                        .setNegativeButton(R.string.dialog_action_i_am_sure)
                        .createMaterialDialog()
                        .show();
            } else {
                requestSinglePermissions(activity, permissions, permissionRequestCode,
                        messageNeverAskAgain[i], permissionDeniedStatus[i]);
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void requestSinglePermissions(final AppCompatActivity activity,
            final String[] permissions,
            final int permissionRequestCode,
            final String messageNeverAskAgain,
            final String permissionDeniedStatus) {
        //Requesting Permission, first time to the device.
        PreferencesHelper preferencesHelper = new PreferencesHelper(activity.
                getApplicationContext());
        if (preferencesHelper.getBoolean(permissionDeniedStatus, true)) {
            preferencesHelper.putBoolean(permissionDeniedStatus, false);

            ActivityCompat.requestPermissions(activity, permissions,
                    permissionRequestCode);
        } else {
            //Requesting Permission, more the one time and opening the setting to change
            // the Permission in App Settings.
            new MaterialDialog.Builder().init(activity)
                    .setMessage(messageNeverAskAgain)
                    .setNegativeButton(R.string.dialog_action_cancel)
                    .setPositiveButton(R.string.dialog_action_app_settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Making the Intent to grant the permission
                                    Intent intent =
                                            new Intent(Settings
                                                    .ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts(activity.getResources().getString(
                                            R.string.package_name), activity.getPackageName()
                                            , null);
                                    intent.setData(uri);
                                    PackageManager pm = activity.getPackageManager();
                                    if (intent.resolveActivity(pm) != null) {
                                        activity.startActivityForResult(intent,
                                                ConstantKeys.REQUEST_PERMISSION_SETTING);
                                    } else {
                                        Toast.makeText(activity, activity.getString(
                                                R.string.msg_setting_activity_not_found)
                                                , Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                    .createMaterialDialog()
                    .show();
        }
    }

    /**
     * This Method is requesting to device to grant the permission. When App is trying to
     * request the device to grant the permission, then their is Three cases.
     * 1. First case Device Prompt the Permission Dialog to user and user accepted or denied the
     * Permission.
     * 2. Second case will come, if user will denied the permission, after onclick dialog denied
     * button and next time App ask for permission, It will show a Material Dialog and there
     * will be a message to tell the user that you have denied the permission before, So do
     * you want to give this permission to app or not, If yes then click on Re-Try dialog button
     * and if not then click on Dialog button "I'M Sure", to not to give this permission to the
     * app.
     * <p/>
     * And as user will click on "Re-Try" dialog button, he will be prompt with the with
     * permission dialog with "[-] never ask again" and have two options first one to click on
     * denied button again and put Un check the never ask check box. In this case, user will
     * prompt with permission dialog with "[-] never ask again" in the loop, whenever app ask
     * for that permission.
     * <p/>
     * and If user will click on "[_/] never ask again" check box then permission dialog with
     * that permission will not prompt to the user.
     * 3. Third case will came. when user have denied to accept permission with never ask again.
     * then user will prompt with dialog and message that you have denied this permission with
     * never ask again. but this is necessary permission to this app feature. and to grant
     * this permission please click on dialog app settings button and give the permission to
     * work with this feature.
     *
     * @param activity               AppCompatActivity
     * @param permission             Manifest.permission...Permission...
     * @param permissionRequestCode  Permission Request Code.
     * @param dialogMessageRetry     Dialog Message Retry
     * @param messageNeverAskAgain   Dialog Message Never Ask Again
     * @param permissionDeniedStatus Permission Denied Status
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void requestPermission(final AppCompatActivity activity,
            final String permission,
            final int permissionRequestCode,
            final String dialogMessageRetry,
            final String messageNeverAskAgain,
            final String permissionDeniedStatus) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new MaterialDialog.Builder().init(activity)
                    .setTitle(R.string.dialog_permission_denied)
                    .setMessage(dialogMessageRetry)
                    .setPositiveButton(R.string.dialog_action_re_try,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity, new
                                                    String[]{permission},
                                            permissionRequestCode);
                                }
                            })
                    .setNegativeButton(R.string.dialog_action_i_am_sure)
                    .createMaterialDialog()
                    .show();
        } else {

            //Requesting Permission, first time to the device.
            PreferencesHelper preferencesHelper = new PreferencesHelper(activity.
                    getApplicationContext());
            if (preferencesHelper.getBoolean(permissionDeniedStatus, true)) {
                preferencesHelper.putBoolean(permissionDeniedStatus, false);

                ActivityCompat.requestPermissions(activity, new String[]{permission},
                        permissionRequestCode);
            } else {
                //Requesting Permission, more the one time and opening the setting to change
                // the Permission in App Settings.
                new MaterialDialog.Builder().init(activity)
                        .setMessage(messageNeverAskAgain)
                        .setNegativeButton(R.string.dialog_action_cancel)
                        .setPositiveButton(R.string.dialog_action_app_settings,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Making the Intent to grant the permission
                                        Intent intent =
                                                new Intent(Settings
                                                        .ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts(activity.getResources().getString(
                                                R.string.package_name), activity.getPackageName()
                                                , null);
                                        intent.setData(uri);
                                        PackageManager pm = activity.getPackageManager();
                                        if (intent.resolveActivity(pm) != null) {
                                            activity.startActivityForResult(intent,
                                                    ConstantKeys.REQUEST_PERMISSION_SETTING);
                                        } else {
                                            Toast.makeText(activity, activity.getString(
                                                    R.string.msg_setting_activity_not_found)
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                        .createMaterialDialog()
                        .show();
            }
        }
    }
}
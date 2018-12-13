package com.hliejun.dev.widgets.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import android.widget.Button;

import com.hliejun.dev.widgets.R;
import com.hliejun.dev.widgets.ConfigurationActivity;
import com.hliejun.dev.widgets.NotificationManager;
import com.hliejun.dev.widgets.views.LockableViewPager;

public class SplashFragment extends SectionFragment {

    private static final String PACKAGE_NAME_WHATSAPP = "com.whatsapp";
    private static final int CONTACT_PERMISSION_REQUEST = 1;

    /*** Listeners ***/

    View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);
            if (!isAppInstalled(PACKAGE_NAME_WHATSAPP)) {
                NotificationManager.showSnackbar(
                        getContext(),
                        getString(R.string.splash_no_whatsapp),
                        getString(R.string.splash_download),
                        getDownloadListener(PACKAGE_NAME_WHATSAPP)
                );
            } else if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                unlockSwipeAndAdvance();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                NotificationManager.showDialog(
                        getContext(),
                        getString(R.string.splash_permission_title),
                        getString(R.string.splash_permission_explanation),
                        getString(R.string.dialog_ok),
                        permissionListener,
                        null,
                        null,
                        null,
                        null
                );
            } else {
                getPermissions();
            }
        }
    };

    View.OnClickListener settingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", getActivity().getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    DialogInterface.OnClickListener permissionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            getPermissions();
        }
    };

    /*** Listener Getters ***/

    private View.OnClickListener getDownloadListener(final String packageName) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "market://details?id=" + packageName;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        };
    }

    /*** Lifecycle ***/

    public SplashFragment() {
        super();
    }

    public static SplashFragment newInstance(int sectionNumber) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
        Button mStartButton = inflatedView.findViewById(R.id.section_button);
        mStartButton.setOnClickListener(startListener);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void reset() {
        super.reset();
    }

    /*** Auxiliary ***/

    private boolean isAppInstalled(String uri) {
        PackageManager manager = getActivity().getPackageManager();
        boolean isInstalled = false;
        try {
            manager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    private void unlockSwipeAndAdvance() {
        LockableViewPager pager = ((ConfigurationActivity)getActivity()).getPager();
        pager.incrementPage();
        pager.setSwipeable(true);
    }

    /*** Permissions ***/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CONTACT_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    unlockSwipeAndAdvance();
                } else {
                    NotificationManager.showSnackbar(
                            getContext(),
                            getString(R.string.splash_no_permission),
                            getString(R.string.splash_settings),
                            settingsListener
                    );
                }
                break;
        }
    }

    private void getPermissions() {
        String[] permissionsList = new String[] { Manifest.permission.READ_CONTACTS };
        requestPermissions(permissionsList, CONTACT_PERMISSION_REQUEST);
    }

}

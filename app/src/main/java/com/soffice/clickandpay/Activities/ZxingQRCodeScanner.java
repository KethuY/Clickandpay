package com.soffice.clickandpay.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.CameraSelectorDialogFragment;
import com.soffice.clickandpay.Fragments.FormatSelectorDialogFragment;
import com.soffice.clickandpay.Fragments.HomeFragment;
import com.soffice.clickandpay.Fragments.MessageDialogFragment;
import com.soffice.clickandpay.R;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Surya on 04-05-2016.
 */
public class ZxingQRCodeScanner extends BaseScannerActivity implements ZXingScannerView.ResultHandler,CameraSelectorDialogFragment.CameraSelectorDialogListener,MessageDialogFragment.MessageDialogListener
{

    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    RelativeLayout EnterManuallyTv;
    private ImageView camera_toggle;
    private ImageView flash_toggle;
    private boolean isFrontCam = false;
    private boolean mFlash;
    private boolean mAutoFocus;
    private int mCameraId = -1;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mCameraId = -1;
        }
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        EnterManuallyTv= (RelativeLayout) findViewById(R.id.enter_manual);
        EnterManuallyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        flash_toggle= (ImageView) findViewById(R.id.flash_toggle);
        flash_toggle.setImageResource(R.drawable.ic_flash_off);
        flash_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(!mFlash)
                 {
                     mFlash=true;
                     flash_toggle.setImageResource(R.drawable.ic_flash_on);
                     mScannerView.setFlash(mFlash);
                 }
                else
                 {
                     mFlash=false;
                     flash_toggle.setImageResource(R.drawable.ic_flash_off);
                     mScannerView.setFlash(mFlash);
                 }
            }
        });
        mScannerView = (ZXingScannerView) findViewById(R.id.mscannerview);
        setupFormats();
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem;

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);


        if(mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if(mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;

            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                CameraSelectorDialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getFragmentManager(),"camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        /*try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}*/
        //showMessageDialog("Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        Intent intent=new Intent();
        intent.putExtra("scan_result",rawResult.getText().toString());
        if(rawResult!=null) {
            setResult(RESULT_OK, intent);
        }
        else
        {
            setResult(RESULT_CANCELED,intent);
        }
        finish();
    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
        fragment.show(getFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }


    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        ArrayList<BarcodeFormat> formats=new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
            Intent i = new Intent(ZxingQRCodeScanner.this, PassCodeActivity.class);
            i.putExtra("fromActivity", "QRCode");
            startActivity(i);
            finish();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
        HomeFragment.fromActivity="QRScan";
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        mScannerView.resumeCameraPreview(this);
    }
}

package com.example.techmaster.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BLE extends AppCompatActivity {

    //init BLE
    private BluetoothAdapter mBluetoothAdapter;
    final int REQUEST_ENABLE_BT = 1;

    ////////////////////Scan BLE/////////////////////
    List<ScanFilter> mScanFilter = new ArrayList<>();////List ScanFilter
    BluetoothLeScanner mBluetoothLeScanner;//////bien scan blue
    ScanSettings mScanSettings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build();

    ///Scan thiết bị và tự động tắt sau một khoảng thời gian sử dụng postDelayed
    private void scanLeDevice(boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(false);
                    a.setAdapter(adapter);
                }
            }, 5000);
            mBluetoothLeScanner.startScan(mScanFilter,mScanSettings, mScanCallback);//bắt đầu scan
        } else {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(!LISTBLUETOOTH.contains(result.getDevice()))
            {
                LISTBLUETOOTH.add(result.getDevice());
                Log.i("Success","Success");
            }
            Log.e("Success",result.getDevice().getAddress());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {

                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {

            Log.e("Scan Failed", "Error Code: " + errorCode);
            BluetoothAdapter.getDefaultAdapter().disable();
            BluetoothAdapter.getDefaultAdapter().enable();
        }
    };

    /////////////////////dislay BLE//////////////////
    ArrayList<BluetoothDevice> LISTBLUETOOTH = new ArrayList<>();
    BLE_Adapter adapter;
    ListView a;

    /////////////Peripheral///////////////////////
    Button bt1;
    final Handler handler = new Handler();

    /////////////////////////////////////////Main_BLE/////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        /////init các phan tu
        a = findViewById(R.id.listview);
        bt1 = findViewById(R.id.btn1);
        adapter = new BLE_Adapter(BLE.this, LISTBLUETOOTH, R.layout.line);

        //kiểm tra tính khả dụng của BLE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        ////Enable Bluetooth
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        ////init scan bluetooth
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        assert bluetoothManager != null;
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        ///set sự kiện cho button
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        scanLeDevice(true);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        BluetoothAdapter.getDefaultAdapter().disable();/////sau khi thoát ứng dụng tự động tắt bluetooth
    }
}
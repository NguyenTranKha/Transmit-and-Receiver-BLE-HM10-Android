////giao tiếp giữa android và bluetooth 4.0 HM-10

package com.example.techmaster.android;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BLE extends AppCompatActivity {

    ArrayList<String> LISTBLUETOOTH = new ArrayList<String>();
    private boolean mScanning;
    //khởi tạo BluetoothAdapter và init nó
    private BluetoothAdapter mBluetoothAdapter;
    final int REQUEST_ENABLE_BT = 1;
    ListView a;
    ArrayAdapter adapter;
    Button bt1;
    Button bt2;

    //chuẩn bị cho scan bluetooth
    List<ScanFilter> mScanFilter = new ArrayList<ScanFilter>();
    BluetoothLeScanner mBluetoothLeScanner;
    ScanSettings mScanSettings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build();
    final Handler handler = new Handler();
    //////CallBack
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(!LISTBLUETOOTH.contains(result.getDevice().getAddress()))
            LISTBLUETOOTH.add(result.getDevice().getAddress());
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
        }
    };


    private void scanLeDevice(boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothLeScanner.stopScan(mScanCallback); //đừng toàn bộ BLE
                    a.setAdapter(adapter);
                }
            }, 5000);
            mScanning = true;
            mBluetoothLeScanner.startScan(mScanFilter,mScanSettings, mScanCallback);//bắt đầu scan
        } else {
            mScanning = false;
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
    }



    /////////////////////////////////////////Main_BLE/////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
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


        //init everything
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        ///mọi thứ đều phải thông qua BluetoothAdapter điều khiển.
        //////////////////////////////////////////////////////////////////////////////
        mBluetoothAdapter = bluetoothManager.getAdapter();////////notify notify///////
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        //////////////////////////////////////////////////////////////////////////////
        a = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter(BLE.this, android.R.layout.simple_list_item_1, LISTBLUETOOTH);
        bt1 = (Button) findViewById(R.id.btn1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        scanLeDevice(true); ///Scan khi nhấn Scan và hàm stop Scan sẽ được thực hiện sau khi chọn BLE
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        BluetoothAdapter.getDefaultAdapter().disable();
    }
}
package com.example.techmaster.android;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class ConnectBLE extends AppCompatActivity{

    BluetoothGatt bluetoothGatt;
    List<BluetoothGattService> testService;
    List<BluetoothGattCharacteristic> testCharacteristic;
    BluetoothGattCharacteristic bluetoothGattCharacteristic;
    BluetoothGattService bluetoothGattService;
    BluetoothDevice bluetoothDevice;

    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.i("result", "Success Connect");
                        bluetoothGatt.discoverServices();

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Log.i("result", "Fail Connect");
                    }
                    return;
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    Log.i("result", "Discovery Device");

                    bluetoothGattService = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
                    bluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));

                    //TEST FIND BLUETOOTH

                    /* testService = gatt.getServices();
                    for(BluetoothGattService gatte : testService){
                        testCharacteristic = gatte.getCharacteristics();
                        for(BluetoothGattCharacteristic a : testCharacteristic)
                        {
                            if(a.getUuid().equals(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")))
                            Log.i("result", gatte.getUuid().toString());
                        }
                    }

                    Log.i("result", "----------");

                    for(BluetoothGattCharacteristic chart : testCharacteristic){
                        Log.i("result", chart.getUuid().toString());
                    }*/
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    Log.i("result", "Characteristic");
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                    Log.i("result", "CharacteristicWrite");
                }

                @Override
                public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    super.onDescriptorWrite(gatt, descriptor, status);
                    Log.i("result", "DescriptorWrite");
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlble);
        Button b = findViewById(R.id.btn);

        bluetoothDevice = getIntent().getExtras().getParcelable("BLUETOOTH_DEVICE");
        Log.e("Success Pass", bluetoothDevice.getName());
        bluetoothGatt = bluetoothDevice.connectGatt(this,false,mGattCallback);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downdate();
            }
        });
    }

    void downdate(){
        bluetoothGattCharacteristic.setValue("NguyenTranKha");
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        bluetoothGatt.close();
    }
}
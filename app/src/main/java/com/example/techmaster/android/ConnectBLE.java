package com.example.techmaster.android;

import android.app.ActionBar;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ConnectBLE extends AppCompatActivity{

    BluetoothGatt bluetoothGatt;
    List<BluetoothGattService> testService;
    List<BluetoothGattCharacteristic> testCharacteristic;
    BluetoothGattCharacteristic bluetoothGattCharacteristic;
    BluetoothGattService bluetoothGattService;
    BluetoothDevice bluetoothDevice;
    SeekBar Servo;
    SeekBar DongCo;
    int GiaTriServo;
    int GiaTriDongCo;
    Button Buzz;
    Handler handler;

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
                    bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);

                    //TEST FIND BLUETOOTH UUID service/characterics
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
                    Log.i("result", "Change Characteristic: "+characteristic.getUuid());
                    Log.i("result kq ", characteristic.getStringValue(0).toString());
                    final BluetoothGattCharacteristic b = characteristic;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });


                    ///TEST RECEIVE
                   /* testService = gatt.getServices();
                    for(BluetoothGattService gatte : testService){
                        testCharacteristic = gatte.getCharacteristics();
                        for(BluetoothGattCharacteristic a : testCharacteristic)
                        {
                            if(a.getStringValue(0) != null) {
                                Log.i("result", a.getStringValue(0));
                                Log.i("characterics: ", a.getUuid().toString());
                                Log.i("service: ",gatte.getUuid().toString());
                            }
                        }
                    }*/
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
        ///////hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();*/
        setContentView(R.layout.controlble);
        Servo = findViewById(R.id.servo);
        DongCo = findViewById(R.id.motor);
        Buzz = findViewById(R.id.Buzz);
        bluetoothDevice = getIntent().getExtras().getParcelable("BLUETOOTH_DEVICE");
        Log.e("Success Pass", bluetoothDevice.getName());
        bluetoothGatt = bluetoothDevice.connectGatt(this,false,mGattCallback);
        Buzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothGattCharacteristic.setValue(new byte[] {(byte)GiaTriDongCo, (byte)GiaTriServo, 'A' ,'\n'});
                bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
            }
        });
        Servo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GiaTriServo = progress + 20;
                Log.d("result", "Servo: " + progress);
                bluetoothGattCharacteristic.setValue(new byte[] {(byte)GiaTriDongCo, (byte)GiaTriServo, '\n'});
                bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Servo.setProgress(10);
            }
        });

        DongCo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                GiaTriDongCo = progress + 20;
                Log.d("result", "Dong Co: " + progress);
                bluetoothGattCharacteristic.setValue(new byte[] {(byte)GiaTriDongCo, (byte)GiaTriServo, '\n'});
                bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DongCo.setProgress(10);
            }
        });

    }

    void downdate(){
        bluetoothGattCharacteristic.setValue("NguyenTranKha\n");
        bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        bluetoothGatt.close();
    }
}
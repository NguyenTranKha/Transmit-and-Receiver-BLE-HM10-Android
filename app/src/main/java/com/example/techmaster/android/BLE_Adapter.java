package com.example.techmaster.android;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;

public class BLE_Adapter  extends BaseAdapter{

    private Context context;
    private List<BluetoothDevice> DeviceLIST;
    private int layout;

    ///class holder
    private class ViewHolder{
        TextView Name;
        TextView Address;
        TextView UUID;
        BluetoothDevice device;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    BLE_Adapter(Context context, List<BluetoothDevice> infordatableLIST, int layout) {
        this.context = context;
        this.DeviceLIST = infordatableLIST;
        this.layout = layout;
    }

   /* public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }*/

    /*public List<BluetoothDevice> getInfordatableLIST() {
        return DeviceLIST;
    }

    public void setInfordatableLIST(List<BluetoothDevice> infordatableLIST) {
        this.DeviceLIST = infordatableLIST;
    }*/

    @Override

    public int getCount() {
        return DeviceLIST.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.Name= convertView.findViewById(R.id.NameBLE);
            holder.Address = convertView.findViewById(R.id.AddressBLE);
            holder.UUID = convertView.findViewById(R.id.UUID);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice device = DeviceLIST.get(position);

        holder.Name.setText(device.getName());
        holder.Address.setText(device.getAddress());
        holder.UUID.setText(Arrays.toString(device.getUuids()));

        return convertView;
    }
}

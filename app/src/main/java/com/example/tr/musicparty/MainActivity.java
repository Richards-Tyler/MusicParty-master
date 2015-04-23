package com.example.tr.musicparty;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements MainMenu.OnFragmentInteractionListener,
    JoinView.OnFragmentInteractionListener{

    protected FragmentManager fragmentManager;
    protected FragmentTransaction fragmentTransaction;
    public BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    /***************************************************************************************************
    *
    * `                                         On Create
    *
    **************************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewDevicesArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ListView newDevicesListView = (ListView)findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);


        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        MainMenu mainMenu = new MainMenu();
        fragmentTransaction.add(R.id.FragmentHolder, mainMenu);
        fragmentTransaction.addToBackStack(String.valueOf(mainMenu));
        fragmentTransaction.commit();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//Declare and get Adapter
        if(mBluetoothAdapter == null){//checks if bluetooth adapter is supported
            Toast.makeText(this,"Please Enable Bluetooth",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

       else if (!mBluetoothAdapter.isEnabled()){//checks if bluetooth is off
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /***************************************************************************************************
     *
     * `                                         join server
     *
     **************************************************************************************************/

    protected void joinServer(View view){


        mBluetoothAdapter.startDiscovery();//looks for devices

        JoinView joinView = new JoinView();
        fragmentTransaction.replace(R.id.FragmentHolder,joinView);
        //fragmentTransaction.add(R.id.FragmentHolder, joinView);
        fragmentTransaction.addToBackStack(String.valueOf(joinView));
        fragmentTransaction.commit();

    }

    /***************************************************************************************************
     *
     * `              Listens for Devices and populates them to a ListView
     *
     **************************************************************************************************/

    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
                public void onReceive(Context context, Intent intent){
                    String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                }


            }
        }
    };

    listview.setOnItemClickListener(new OnItemClickListener(){


        @Override
        public void onItemClick(AdapterView<?>adapter,View v, int position){

            ItemClicked item = adapter.getItem(position);

            Intent intent = new Intent(Activity.this,destinationActivity.class);
//based on item add info to intent
            startActivity(intent);

        }


    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //@Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}

package org.artoolkit.ar.samples.ARSimpleNativeCars;


import android.app.Activity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import android.os.Handler;

public class Data extends Activity {

    //Layout variables
    Button btnDis;
    TextView sensorView0, sensorView1;
    Handler bluetoothIn;

    //Identify handler message
    final int handlerState = 0;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    //SSP UUID service
    private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //MAC address stored in string
    String address = null;

    //private ProgressDialog progress;
    //private boolean isBtConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get address from previous activity
        //Intent newint = getIntent();
        //address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        //Set Layout
        setContentView(R.layout.activity_data);

        //Link textView to corresponding view
        sensorView0 = (TextView) findViewById(R.id.arduino_data);
        sensorView1 = (TextView) findViewById(R.id.arduino_data2);

        //Bluetooth disconnect
        btnDis = (Button) findViewById(R.id.button4);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) { //if the message is what we want
                    String readMessage = (String) msg.obj; //msg.arg1 = bytes from connected thread
                    recDataString.append(readMessage);  //keep appending to string until we reach the character ~
                    int endOfLineIndex = recDataString.indexOf("~"); //end of line
                    if (endOfLineIndex > 0) { //make sure there is data before ~
                        //String dataInPrint = recDataString.substring(0, endOfLineIndex); //extract string: dataInPrint was printed during testing
                        if (recDataString.charAt(0) == '#') { //if this starts with #
                            String sensor0 = recDataString.substring(1, 2); //get sensor value from string between indices 1-5
                            String sensor1 = recDataString.substring(3, 6); //get sensor value from string between indices 6-10

                            sensorView0.setText(sensor0); //update textView
                            sensorView1.setText(sensor1); //update textView
                        }
                        recDataString.delete(0, recDataString.length()); //clear all string data

                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();   //get Bluetooth adapter
        checkBTState();

        //Disconnect
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //create secure outgoing connection through Bluetooth using the specified UUID
        return device.createRfcommSocketToServiceRecord(myUUID);
    }

    //Disconnect() to close bluetooth socket
    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        //mConnectedThread.write("x");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {

        }
    }


    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;

        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

    }


}

package com.droidapps.triosample;

/**
 * Created by bt on 5/8/16.
 * The main activity which loads the JSON from the hosted devices json file
 * and displays the name in list.
 *
 * The project also demonstrates the usage of,
 * 1. Data Binding
 * 2. Logan Square for json serializing
 * 3. Expresso as test framework
 */
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.droidapps.triosample.adapters.ModelsRecyclerAdapter;
import com.droidapps.triosample.data.Device;
import com.droidapps.triosample.data.Devices;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TrioSample";
    private RecyclerView modelsList;
    private SwipeRefreshLayout contentView;
    private static final String JSON_URL = "https://s3.amazonaws.com/harmony-recruit/devices.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        asyncLoadDevices();
    }

    /*
     * Initializes the view and adds listeners
     */
    private void initView() {
        modelsList = (RecyclerView) findViewById(R.id.modelsList);
        contentView = (SwipeRefreshLayout) findViewById(R.id.contentView);
        contentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asyncLoadDevices();
            }
        });
        modelsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    /*
    Loads the json asynchronously in a separate thread
     */
    private void asyncLoadDevices() {
        showProgress(true);
        Thread reqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                loadDevices();
            }
        });
        reqThread.start();
    }

    /*
    To show/hide default progress bar in swiperefreshlayout
     */
    private void showProgress(final boolean show) {
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setRefreshing(show);
            }
        });
    }

    /*
    Connects to the URL, parses the downloaded json via LoganSquare
    and pass it to UI thread via handler
    Note: LoganSquare is the best JSON serializer for Android compared to Jackson and GSON
    See <a href="https://github.com/bluelinelabs/LoganSquare">LoganSquare</a>
     */

    private void loadDevices() {
        InputStream is = null;
        try {
            URLConnection urlConnection = new URL(JSON_URL).openConnection();
            is = urlConnection.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            mHandler.obtainMessage(-1, e.getMessage()).sendToTarget();
        }

        if (is != null) {
            try {
                Devices devices = LoganSquare.parse(is, Devices.class);
                Message message = mHandler.obtainMessage(0, devices.devices);
                message.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                mHandler.obtainMessage(-1, e.getMessage()).sendToTarget();
            }
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    /*
    Handler created with main looper to handle messages in UI Thread
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            showProgress(false);
            if (inputMessage.what==0) {
                modelsList.setAdapter(new ModelsRecyclerAdapter(getApplicationContext(), (List<Device>) inputMessage.obj));
            }else if(inputMessage.what==-1){
                Toast.makeText(MainActivity.this, "Error: "+inputMessage.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

}

package ubb.ro.templatetwo.list;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ubb.ro.templatetwo.Event;
import ubb.ro.templatetwo.EventApplication;
import ubb.ro.templatetwo.R;
import ubb.ro.templatetwo.api.EventService;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "TemplateApp";
    private EventService mEventService;
    private EventAdapter mEventAdapter;
    private boolean mConnected;
    private ScheduledThreadPoolExecutor mExec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mEventService = new EventService(this);

        final EventApplication application = (EventApplication) getApplication();
        application.setConnectivityListener(new ConnectivityStateListener() {
            @Override
            public void onConnected() {
                Log.d(TAG, "Connect signal in Activity");
                mConnected = true;
                getSupportActionBar().setTitle("Connected");
                startPeriodicFetch();
            }

            @Override
            public void onDisconnected() {
                Log.d(TAG, "Disconnect signal in Activity");
                mConnected = false;
                getSupportActionBar().setTitle("Disconnected");
                stopPeriodicFetch();
            }
        });


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventAdapter = new EventAdapter();
        recyclerView.setAdapter(mEventAdapter);
    }

    private void startPeriodicFetch() {
        mExec = new ScheduledThreadPoolExecutor(1);
        mExec.scheduleAtFixedRate(new Runnable() {
            public void run() {
                fetchNotes();
            }
        }, 0, 5, TimeUnit.SECONDS); // execute every 5 seconds
    }

    private void stopPeriodicFetch() {
        if (mExec != null) {
            mExec.shutdown();
        }
    }

    private void fetchNotes() {
        mEventService.fetchEventsAsync(new EventViewInterface() {
            @Override
            public void showCompleted() {
                Log.d(TAG, "Completed");
            }

            @Override
            public void showError(String msg) {
                Log.d(TAG, "Error: " + msg);
            }

            @Override
            public void showEvents(List<Event> events) {
                Log.d(TAG, "Received " + events.size() + " events");
                mEventAdapter.setEvents(events);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id== R.id.action_refresh) {
            fetchNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

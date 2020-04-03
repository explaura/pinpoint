package au.com.versent.pinpoint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsException;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.analytics.pinpoint.AmazonPinpointAnalyticsPlugin;
import com.amplifyframework.core.AmplifyConfiguration;

import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.client.AWSMobileClient;

import com.amplifyframework.core.category.CategoryConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INITIALIZATION_TIMEOUT_MS = 2000;

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

        // Initialize Mobile Client
        final AWSConfiguration awsConfiguration = new AWSConfiguration(getApplicationContext());
        final CountDownLatch mobileClientLatch = new CountDownLatch(1);
        AWSMobileClient.getInstance().initialize(getApplicationContext(), awsConfiguration,
                new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        Log.i(TAG, "Mobile client initialized");
                        mobileClientLatch.countDown();
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e(TAG, "Error initializing AWS Mobile Client", exception);
                    }
                });

        try {
            if (!mobileClientLatch.await(INITIALIZATION_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new AnalyticsException("Failed to initialize mobile client.",
                        "Please check your awsconfiguration json.");
            }
        } catch (InterruptedException | AnalyticsException exception) {
            throw new RuntimeException("Failed to initialize mobile client: " + exception.getLocalizedMessage());
        }

        // Configure Amplify framework
        try {
            AmplifyConfiguration configuration = AmplifyConfiguration.fromConfigFile(getApplicationContext(), R.raw.amplifyconfiguration);
            Amplify.addPlugin(new AmazonPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(configuration, getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }
        Amplify.Analytics.recordEvent("test-event");
    }

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
}

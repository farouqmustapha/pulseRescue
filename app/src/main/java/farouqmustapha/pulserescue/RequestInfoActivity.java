package farouqmustapha.pulserescue;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class RequestInfoActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
    private TextView time, status, name, age, height, weight, bloodType,
            address, phone, emergencyName, emergencyPhone, coordinate;
    private FloatingActionButton fabNavigate, fabFetch;
    private String mCoordinate = new String();

    private String KEY = new String();
    private double latitude, longitude;
    private double myLatitude, myLongitude;
    private String patientID = new String ();

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);

        KEY = getIntent().getExtras().getString("KEY"); //getting intent bundle value of firebase KEY
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference requestRef = mDatabase.child("AmbulanceRequest").child(KEY);

        time = (TextView) findViewById(R.id.requestTime);
        name = (TextView) findViewById(R.id.requestName);
        status = (TextView) findViewById(R.id.requestStatus);
        name = (TextView) findViewById(R.id.requestName);
        age = (TextView) findViewById(R.id.requestAge);
        height = (TextView) findViewById(R.id.requestHeight);
        weight = (TextView) findViewById(R.id.requestWeight);
        bloodType = (TextView) findViewById(R.id.requestBlood);
        address = (TextView) findViewById(R.id.requestAddress);
        phone = (TextView) findViewById(R.id.requestPhone);
        emergencyName = (TextView) findViewById(R.id.requestEmergencyName);
        emergencyPhone = (TextView) findViewById(R.id.requestEmergencyPhone);
        coordinate = (TextView) findViewById(R.id.requestCoordinate);
        fabNavigate = (FloatingActionButton) findViewById (R.id.fabNavigate);
        fabFetch = (FloatingActionButton) findViewById(R.id.fabFetch);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show();

        fabNavigate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+mCoordinate);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                else{
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                    }
                }
            }
        });

        fabFetch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float[] dist = new float[1];

                Location.distanceBetween(latitude,longitude,myLatitude,myLongitude,dist);

                if(dist[0] <= 500){
                    //inside 500m radius area
                    requestRef.child("requestStatus").setValue("Fetched");
                }
                else{
                    //outside 500m radius area
                    Toast.makeText(getApplicationContext(),"You are not in the radius of the patient's location!",Toast.LENGTH_LONG).show();
                }

            }
        });

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AmbulanceRequest ambulanceRequest = dataSnapshot.getValue(AmbulanceRequest.class);
                patientID = ambulanceRequest.getPatientKey();
                time.setText(ambulanceRequest.getTime());
                name.setText(ambulanceRequest.getName());
                status.setText(ambulanceRequest.getRequestStatus());
                latitude = Double.parseDouble((String)ambulanceRequest.getPatientLatitude());
                longitude = Double.parseDouble((String)ambulanceRequest.getPatientLongitude());
                mCoordinate = ambulanceRequest.getPatientLatitude() + "," + ambulanceRequest.getPatientLongitude();
                coordinate.setText(mCoordinate);

                DatabaseReference patientInfoRef = mDatabase.child("Users").child(patientID).child("personalInfo");
                patientInfoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PersonalInfo personalInfo = dataSnapshot.getValue(PersonalInfo.class);

                        age.setText(personalInfo.getAge());
                        height.setText(personalInfo.getHeight()+" meter");
                        weight.setText(personalInfo.getWeight()+" kg");
                        bloodType.setText(personalInfo.getBloodType());
                        address.setText(personalInfo.getAddress());
                        phone.setText(personalInfo.getPhone());
                        phone.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                        phone.setLinksClickable(true);
                        emergencyName.setText(personalInfo.getEmergencyPerson());
                        emergencyPhone.setText(personalInfo.getEmergencyNumber());
                        emergencyPhone.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                        emergencyPhone.setLinksClickable(true);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /*Ending the updates for the location service*/
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        settingRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, 90000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Current Location", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*Method to get the enable location settings dialog*/
    public void settingRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);    // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1000);   // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(RequestInfoActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, "Location Service not Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            /*Getting the location after acquiring location service*/
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, RequestInfoActivity.this);
//            }
        }
    }

    /*When Location changes, this method get called. */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        myLatitude = mLastLocation.getLatitude();
        myLongitude = mLastLocation.getLongitude();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            auth.signOut();
            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

package farouqmustapha.pulserescue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestInfoActivity extends AppCompatActivity {
    private TextView time, status, name, age, height, weight, bloodType,
            address, phone, emergencyName, emergencyPhone, coordinate;
    private String mCoordinate = new String();

    private String KEY = new String();
    private String patientID = new String ();

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);

        KEY = getIntent().getExtras().getString("KEY"); //getting intent bundle value of firebase KEY
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference requestRef = mDatabase.child("AmbulanceRequest").child(KEY);

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

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AmbulanceRequest ambulanceRequest = dataSnapshot.getValue(AmbulanceRequest.class);
                patientID = ambulanceRequest.getPatientKey();
                time.setText(ambulanceRequest.getTime());
                name.setText(ambulanceRequest.getName());
                status.setText(ambulanceRequest.getRequestStatus());
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
}

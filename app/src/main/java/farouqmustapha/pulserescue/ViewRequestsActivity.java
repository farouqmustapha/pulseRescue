package farouqmustapha.pulserescue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRequestsActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private List<AmbulanceRequest> requestList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RequestAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference symptomsDiary = mDatabase.child("AmbulanceRequest");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new RequestAdapter(requestList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        symptomsDiary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot ambulanceRequestSnapshot: dataSnapshot.getChildren()) {
                    AmbulanceRequest ambulanceRequest = ambulanceRequestSnapshot.getValue(AmbulanceRequest.class);
                    ambulanceRequest.setKey(ambulanceRequestSnapshot.getKey());
                    if(ambulanceRequest.getRequestStatus().equals("Requesting Aid")) {
                        requestList.add(ambulanceRequest);
                    }
                    Log.d("AmbulanceRequest KEY :",ambulanceRequestSnapshot.getKey());
                }
                Collections.reverse(requestList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AmbulanceRequest ambulanceRequest = requestList.get(position);
                Intent mIntent = new Intent(getApplicationContext(), RequestInfoActivity.class);
                mIntent.putExtra("KEY", ambulanceRequest.getKey());
                startActivity(mIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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
        // as you specify a parent name in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

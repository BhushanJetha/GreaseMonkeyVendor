package com.greasemonkey.vendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    /*ArrayList<RequestModel> allRequests;
    private RecyclerView mRecyclerView;
    private RequestListAdapter mAdapter;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar();

       /* allRequests=new ArrayList<>();
        requestList();
        mRecyclerView = (RecyclerView) findViewById(R.id.rvServiceRequest);

        if(allRequests.size()>0){

            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new RequestListAdapter(allRequests);
            mRecyclerView.setAdapter(mAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(llm);
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                RequestModel requests= allRequests.get(position);
                String status = requests.getStatus();
                if(status == "New Request"){
                    Intent i=new Intent(HomeActivity.this,RequestDetailActivity.class);
                    startActivity(i);
                    finish();
                }else  if(status == "WIP"){
                    Intent i=new Intent(HomeActivity.this,RequestStatusActivity.class);
                    startActivity(i);
                    finish();
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/
    }

    public void setToolbar(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grease Monkey");

       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
    }

    private void requestList(){

/*
        allRequests.add(new RequestModel("Bhushan Jetha","01 SEP", "One Time Service","GOLD","New Request"));
        allRequests.add(new RequestModel("Chetan Jetha","01 SEP", "One Time Service","GOLD","WIP"));
        allRequests.add(new RequestModel("Viraj Jetha","01 SEP", "One Time Service","GOLd","New Request"));
        allRequests.add(new RequestModel("Vishal Jetha","01 SEP", "One Time Service","GOLD","New Request"));
        allRequests.add(new RequestModel("Pavan Jetha","01 SEP", "One Time Service","GOLD","New Request"));*/
    }


}

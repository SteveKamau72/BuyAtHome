package com.softtech.stevekamau.buyathome.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.PurchasesAdapter;
import com.softtech.stevekamau.buyathome.app.AppConfig;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.databaseHandlers.PurchasesDB;
import com.softtech.stevekamau.buyathome.model.PurchasesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PurchasesList extends AppCompatActivity {
    DatabaseReference ref;
    SweetAlertDialog pDialog;
    PurchasesDB nb;
    RecyclerView recyclerView;
    List<PurchasesModel> itemList = new ArrayList<PurchasesModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Purchases");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nb = new PurchasesDB(this);
        createViews();
        getPurchases();

    }

    private void createViews() {
        recyclerView = (RecyclerView) findViewById(R.id.notifList);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        setUpAdapter();
    }

    private void setUpAdapter() {
        new Thread() {

            public void run() {
                itemList = nb.getAllPurchasedItems();
                //show Syncing Progress
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final PurchasesAdapter itemAdapter = new PurchasesAdapter
                                (PurchasesList.this, itemList);
                        recyclerView.setAdapter(itemAdapter);
                    }
                });

            }
        }.start();

    }

    private void getPurchases() {
        loadingDialog();
        // Creating volley request obj
        final JsonArrayRequest productReq = new JsonArrayRequest(getResources().getString(R.string.server_url) + AppConfig.URL_ALL_PURCHASES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismissWithAnimation();
                        Log.d("all_purchases_req", response.toString());
                        System.out.print(response.toString());
                        // Parsing json
                        parseMainData(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //displayRetry();
                //
                pDialog.dismissWithAnimation();
                errorDialog();
                Log.d("all_purchases_req", error.toString());

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(productReq);
    }

    private void loadingDialog() {
        pDialog = new SweetAlertDialog(PurchasesList.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.green_color));
    }

    private void errorDialog() {
        final SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        errorDialog.setTitleText("Ooops!")
                .setContentText("Something went wrong! Please retry.")
                .setCancelText("No, cancel!")
                .setConfirmText("Yes, retry!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        pDialog.dismissWithAnimation();
                        errorDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        errorDialog.dismissWithAnimation();
                        loadingDialog();
                        getPurchases();
                    }
                })
                .show();
    }

    private void parseMainData(String data) {
        String ordered_at = "";
        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);

//name, amount,des,quantity,phone,sub,user,city,lan,payment,payment-mode,shipping,ordered,receipt,viewdat,viewdby
                    nb.insertIntoPurchasesList(
                            obj.getString("id"), obj.getString("p_id"), obj.getString("p_name"),
                            obj.getString("sub_total"), "no description", obj.getString("email"), obj.getString("quantity"),
                            obj.getString("phone"), obj.getString("sub_total"), obj.getString("user_name"),
                            obj.getString("city"), obj.getString("landmark"), ""/*obj.getString("payment")*/,
                            obj.getString("payment_mode"), obj.getString("shipping_mode"), obj.getString("order_at"),
                            obj.getString("receipt_code"), obj.getString("viewed_at"), obj.getString("viewed_by"),
                            obj.getString("completion_status"));

                    setUpAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                    setUpAdapter();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            setUpAdapter();
        }

    }

    private void startFireBaseService() {
        ref = FirebaseDatabase.getInstance().getReference().child("sales");
//        ref = FirebaseDatabase.getInstance().getReference().child("sales/" + ap.getBusinessId() + "/" + getDateTime());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot user : snapshot.child("user").getChildren()) {
                        Log.d("on_child_added5", String.valueOf(user.child("city").getValue()));
                    }
                    /* Log.d("on_child_added2", String.valueOf(snapshot.child("1").getValue()));
                    Log.d("on_child_added3", String.valueOf(snapshot.child("user").getValue()));*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);

        // Create child event listener
       /* ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("on_child_added1", String.valueOf(map.get("user")));
                Log.d("on_child_added2", String.valueOf(map.get("1")));
                Log.d("on_child_added3", String.valueOf(map.get("amount")));
                Log.d("on_child_added4", String.valueOf(map.get("city")));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot user : snapshot.child("user").getChildren()) {
                        Log.d("on_child_added5", String.valueOf(user.child("city").getValue()));
                    }
                   *//* Log.d("on_child_added2", String.valueOf(snapshot.child("1").getValue()));
                    Log.d("on_child_added3", String.valueOf(snapshot.child("user").getValue()));*//*
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(childEventListener);*/
    }

    @Override
    protected void onResume() {
        setUpAdapter();
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

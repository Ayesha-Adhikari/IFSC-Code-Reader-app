package com.ayesha.ifsccodereader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText ifscCodeEdt;
    private TextView bankDetails;
    private Button btnGetDetails;

    String ifscCode;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ifscCodeEdt = findViewById(R.id.ifscCode);
        bankDetails = findViewById(R.id.bankDetails);
        btnGetDetails = findViewById(R.id.btnGetDetails);

        mRequestQueue = Volley.newRequestQueue(this);
        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifscCode = ifscCodeEdt.getText().toString();
                if (TextUtils.isEmpty(ifscCode)) {
                    Toast.makeText(MainActivity.this, "Please Enter a valid IFSC Code",
                            Toast.LENGTH_SHORT).show();
                } else {
                    getDataFromIFSCCode(ifscCode);
                }
            }
        });

    }

    private void getDataFromIFSCCode(String ifscCode) {
        // mRequestQueue.getCache().clear();
        //String url = "https://ifsc.razorpay.com/" + ifscCode;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://ifsc.razorpay.com/" + ifscCode;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            //JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // JSONObject dataObj = response.getJSONObject("data");
                    String state = response.getString("STATE");
                    String bankName = response.getString("BANK");
                    String branch = response.getString("BRANCH");
                    String address = response.getString("ADDRESS");
                    String contact = response.getString("CONTACT");
                    //String micrcode = response.getString("MICRCODE");
                    String city = response.getString("CITY");
                    String details = "Bank Name : " + bankName + "\nBranch : " + branch + "\nAddress : " + address +
                            "\nCity : " + city + "\nState : " + state + "\nContact : " + contact;

                    Log.d("myTag", details);
                    bankDetails.setText(details);
                } catch (JSONException e) {
                    e.printStackTrace();
                    bankDetails.setText("Invalid IFSC Code");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bankDetails.setText("Invalid IFSC Code");
            }
        });

        queue.add(jsonObjectRequest);
    }
}
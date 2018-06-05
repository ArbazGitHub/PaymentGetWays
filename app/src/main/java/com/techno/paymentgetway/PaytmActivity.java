package com.techno.paymentgetway;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.techno.paymentgetway.Utils.Paytm.CheckSumServiceHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PaytmActivity extends Activity {

    TextView response;
    private String orderID = "";
    public static final String MERCHANT_ID = "Thetat18299186991254";
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CHANNEL_ID = "WAP";//On Website: Website Name = WEB_STAGING and Channel ID = WEB//On App: App Name = APP_STAGING and Channel ID = WAP


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        response = findViewById(R.id.response);
        findViewById(R.id.btnPaytm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartTransaction(view);
            }
        });
    }

    public synchronized void onStartTransaction(View view) {
        PaytmPGService service = PaytmPGService.getStagingService();
        orderID = "OrderID111" + System.currentTimeMillis();
        //orderID = "TestMerchant000111FBN";

        String checkSum = checksumGeneration("MerchantID?", orderID, "CUST_ID?",
                "Retail", "WAP", "ammount?", "APP_STAGING",
                null, null, "merchant");//Creating checksum to pass it for transaction.

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", MERCHANT_ID);//Replace your merchant id here.
        paramMap.put("ORDER_ID", orderID);//This is entered by yourself but must be unique. Any unique here unique created by system timestamp.
        paramMap.put("CUST_ID", "CUST111");//Created by yourself.//Add your customer id like CUST123
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);//fix retail
        paramMap.put("CHANNEL_ID", CHANNEL_ID);//Fix WAP for Mobile App and WEB for website
        paramMap.put("TXN_AMOUNT", "10.00");//Transaction Ammount like 1.00
        paramMap.put("WEBSITE", "APP_STAGING");//(READ EMAIL FIRST )APP_STAGING or WEB_STAGING or your own website host name if you have entered (STAGING for given name)
        paramMap.put("EMAIL", "mahesh.bitsp@gmail.com");//User Email id of customer from whom you want to take payment(Optional)
        paramMap.put("MOBILE_NO", "8585958484");//User Mobile number of customer from whom you want to take payment(Optional)
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");//Callback url if you want to set yourself.
        paramMap.put("THEME", "merchant");//DEFAULT merchant
        paramMap.put("CHECKSUMHASH", checkSum);//Checksum generate by given above data need to pass here.

        PaytmOrder order = new PaytmOrder(paramMap);
        //PaytmClientCertificate clientCertificate = new PaytmClientCertificate(password,filename);//Enable after LIVE implementation
        service.initialize(order, null);
        service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        Log.e("LOG", "someUIErrorOccurred : " + inErrorMessage);
                        response.setText(inErrorMessage);
                        if (inErrorMessage != null)
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        response.setText("Payment Transaction : " + inResponse);
                        Log.e("LOG", "Payment Transaction : " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() {
                        response.setText("no network");
                        Log.e("LOG", "networkNotAvailable : ");
                        Toast.makeText(PaytmActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                        // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        response.setText(inErrorMessage);
                        Log.e("LOG", "clientAuthenticationFailed : " + inErrorMessage);
                        if (inErrorMessage != null)
                            Toast.makeText(PaytmActivity.this, inErrorMessage, Toast.LENGTH_SHORT).show();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        response.setText("onErrorLoadingWebPage : " + inErrorMessage + "inFailing Url : " + inFailingUrl + " iniErrorCode : " + iniErrorCode);
                        Log.e("LOG", "onErrorLoadingWebPage : " + inErrorMessage + "inFailing Url : " + inFailingUrl + " iniErrorCode : " + iniErrorCode);
                        try {
                            if (inErrorMessage != null)
                                Toast.makeText(PaytmActivity.this, inErrorMessage + "inFailing Url : " + inFailingUrl + " iniErrorCode : " + iniErrorCode, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {

                            response.setText("onBackPressedCancelTransaction");
                            Log.e("LOG", "onBackPressedCancelTransaction");
                            // TODO Auto-generated method stub

                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        response.setText("Payment Transaction Failed " + inErrorMessage);
                        Log.e("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }

    public synchronized String checksumGeneration(String mid, String orderId, String cust_id, String industry_type_id, String channel_id, String amount, String website, String email, String mobilenumber, String theme) {
        TreeMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("MID", MERCHANT_ID);
//        paramMap.put("MID", "MICROM85212039317160");
        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", "CUST111");
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID", CHANNEL_ID);
        paramMap.put("TXN_AMOUNT", "10.00");
        paramMap.put("WEBSITE", "APP_STAGING");
        paramMap.put("EMAIL", "mahesh.bitsp@gmail.com");
        paramMap.put("MOBILE_NO", "8585958484");
        paramMap.put("CALLBACK_URL", "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp");
        paramMap.put("THEME", "merchant");

       /* if (email != null)
            paramMap.put("EMAIL", email);//Optional
        if (mobilenumber != null)
            paramMap.put("MOBILE_NO", mobilenumber);//Optional*/

        String checkSum = "";
        try {
            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("bkWv3Wkh7Qdb1qZ7", paramMap);//(bkWv3Wkh7Qdb1qZ7 Merchant Key)

//            checkSum = CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum("SFM0KehhRPYfGxsE", paramMap);
//            paramMap.put("CHECKSUMHASH", checkSum);

            Log.e("Paytm Checksum: ", checkSum);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        return checkSum;
    }
}




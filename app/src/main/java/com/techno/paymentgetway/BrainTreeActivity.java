package com.techno.paymentgetway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.techno.paymentgetway.Global.Constants;

import java.util.HashMap;

public class BrainTreeActivity extends Activity implements PaymentMethodNonceCreatedListener {
    String amount = "10";
    private static final int REQUEST_CODE = 11111;
    private static final String BRAIN_TREE_TOKEN = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIyODVkNDA1NzI3NzM5YjVjMTBiYzZhZGM5NzBkOGI5Njc5YWUzZGQ2MTZhZDA4MjQ2NDA4YThiOGNiNGM3MjlhfGNyZWF0ZWRfYXQ9MjAxOC0wNC0xOFQxMzoxMDowNC4wMjMxMzU1MTYrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=";
    HashMap<String, String> paramHash;

    //For paypal
    private BraintreeFragment mBraintreeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_tree);

        try {
            //todo make api call first for token
            makeApiCall();

            mBraintreeFragment = BraintreeFragment.newInstance(this, BRAIN_TREE_TOKEN);//todo THIS IS DEFAULT TOKEN FOR LIVE SHOULD  MAKE API CALL  FIRST
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Button click for open both methods Paypal and Cards
        findViewById(R.id.btnDrop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //for both payment methods
                    DropInRequest dropInRequest = new DropInRequest()
                            .clientToken(BRAIN_TREE_TOKEN);//todo THIS IS DEFAULT TOKEN FOR LIVE SHOULD  MAKE API CALL  FIRST
                    startActivityForResult(dropInRequest.getIntent(BrainTreeActivity.this), REQUEST_CODE);
                    //todo you will ger response in this method onActivityResult()
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnPaypal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    mBraintreeFragment = BraintreeFragment.newInstance(BrainTreeActivity.this, BRAIN_TREE_TOKEN);
                    PayPal.authorizeAccount(mBraintreeFragment);   //onPaymentMethodNonceCreated()

                    /*// vault
                    PayPalRequest request = new PayPalRequest()
                            .localeCode("US")
                            .billingAgreementDescription("Your agreement description");
                    PayPal.requestBillingAgreement(mBraintreeFragment, request);*/

                    // checkout
                    PayPalRequest request1 = new PayPalRequest(amount)
                            .currencyCode(Constants.BRAIN_TREE_C_CODE)
                            .intent(PayPalRequest.INTENT_AUTHORIZE);
                    PayPal.requestOneTimePayment(mBraintreeFragment, request1);

                    //todo you will ger response in this method onPaymentMethodNonceCreated()
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void makeApiCall() {
        /*
         *ToDo :: make api call to Get token from server
         * ToDo :: if server send the successful token  llHolder.setVisibility(View.VISIBLE);
         * ToDo :: then user will fill the amount and than click on the button and call onBraintreeSubmit(
         */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                    // use the result to update your UI and send the payment method nonce to your server
                    PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                    String stringNonce = nonce.getNonce();
                    Log.e("BrainTree Nounce", "" + stringNonce);


                    paramHash = new HashMap<>();
                    paramHash.put("amount", amount);
                    paramHash.put("nonce", stringNonce);
                    sendPaymentDetails();//todo send data to server as per backEnd need
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // the user canceled
                } else {
                    // handle errors here, an exception may be available in
                    Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sendPaymentDetails() {
        /*
         *ToDo make api call to send send_payment_details to server and display response to the user
         */
    }


    //Todo Paypal button response here
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {

        PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;
        PostalAddress billingAddress = paypalAccountNonce.getBillingAddress();
        String streetAddress = billingAddress.getStreetAddress();
        String extendedAddress = billingAddress.getExtendedAddress();
        String locality = billingAddress.getLocality();
        String countryCodeAlpha2 = billingAddress.getCountryCodeAlpha2();
        String postalCode = billingAddress.getPostalCode();
        String region = billingAddress.getRegion();
        String nonce=paypalAccountNonce.getNonce();
        if (nonce != null) {
            finalApiCall();
        }
        /*
        // Send nonce to server
        String nonce = paymentMethodNonce.getNonce();
        if (paymentMethodNonce instanceof PayPalAccountNonce) {
            PayPalAccountNonce paypalAccountNonce1 = (PayPalAccountNonce) paymentMethodNonce;

            // Access additional information
            String email = paypalAccountNonce1.getEmail();
            String firstName = paypalAccountNonce1.getFirstName();
            String lastName = paypalAccountNonce1.getLastName();
            String phone = paypalAccountNonce1.getPhone();

            // See PostalAddress.java for details
            PostalAddress billingAddress1 = paypalAccountNonce.getBillingAddress();
            PostalAddress shippingAddress = paypalAccountNonce.getShippingAddress();
        }*/
    }

    private void finalApiCall() {
        //todo final api call here sed nonce to server
    }
}

package com.example.zy.echacks2016;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.BuyButtonText;
import com.google.android.gms.wallet.fragment.Dimension;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import java.util.ArrayList;


public class AcceptedPaymentActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    public ReceiveConfirmBroadcastReceiver receiver;
    public String place;
    public ArrayAdapter<String> mAdapter;
    public ArrayList<String> paymentList;

    private GoogleApiClient mGoogleApiClient;
    private SupportWalletFragment mWalletFragment;
    private SupportWalletFragment mXmlWalletFragment;

    private MaskedWallet mMaskedWallet;
    private FullWallet mFullWallet;

    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    public static final int FULL_WALLET_REQUEST_CODE = 889;

    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new ReceiveConfirmBroadcastReceiver();
        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);

        WalletFragmentInitParams startParams;
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(generateMaskedWalletRequest())
                .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE);

        startParams = startParamsBuilder.build();

        if(mWalletFragment == null) {
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(BuyButtonText.BUY_WITH_GOOGLE)
                    .setBuyButtonWidth(Dimension.MATCH_PARENT);

            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();

            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

            mWalletFragment.initialize(startParams);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                .commit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                        .build())
                .build();

        setContentView(R.layout.activity_accept_payment);
        Intent intent = getIntent();
        place = intent.getStringExtra(Constants.CITY_NAME);
        ListView listView = (ListView) findViewById(R.id.payment_list);
        paymentList = new ArrayList<>();
        paymentList.add("Pay with Visa");
        paymentList.add("Pay with Master Card");
        paymentList.add("Pay with TD");
        paymentList.add("Pay with Debit");
        mAdapter = new ArrayAdapter<String>(this,R.layout.city_listitem,paymentList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConfirmDialogBox dialogBox = new ConfirmDialogBox();
                dialogBox.show(getFragmentManager(), "string");
            }
        });
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter(Constants.DIALOGKEY));
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case MASKED_WALLET_REQUEST_CODE :
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mMaskedWallet =
                                data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        break;

                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case FULL_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        mFullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                        Toast.makeText(this, mFullWallet.getProxyCard().getPan().toString(), Toast.LENGTH_SHORT).show();
                        Wallet.Payments.notifyTransactionStatus(mGoogleApiClient,
                                generateNotifyTransactionStatusRequest(mFullWallet.getGoogleTransactionId(),
                                        NotifyTransactionStatusRequest.Status.SUCCESS));
                        break;
                    default:
                        Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                Toast.makeText(this, "An Error Occured", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(
            String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }

    private MaskedWalletRequest generateMaskedWalletRequest() {
        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()
                .setMerchantName("Google I/O Codelab")
                .setPhoneNumberRequired(true)
                .setShippingAddressRequired(true)
                .setCurrencyCode("USD")
                .setEstimatedTotalPrice("10.00")
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.00")
                        .addLineItem(LineItem.newBuilder().setCurrencyCode("USD")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .build())
                .build();
        return maskedWalletRequest;
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionID) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice(".10")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }

    public void requestFullWallet(View view) {
        if(mGoogleApiClient.isConnected()) {
            Wallet.Payments.loadFullWallet(mGoogleApiClient,
                    generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                    FULL_WALLET_REQUEST_CODE);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class ReceiveConfirmBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent newIntent = new Intent(context,ProofOfPaymentActivity.class);
            newIntent.putExtra(Constants.CITY_NAME, place);
            startActivity(newIntent);
        }
    }
}

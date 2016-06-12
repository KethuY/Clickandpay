package com.soffice.clickandpay.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.soffice.clickandpay.Activities.NearMeActivity;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.NearByStoresDTO;
import com.soffice.clickandpay.Pojo.StoreProfile;
import com.soffice.clickandpay.Pojo.StoresData;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoBoldEditText;
import com.soffice.clickandpay.UI.RobotoRegularAutoComplete;
import com.soffice.clickandpay.UI.RobotoRegularEditText;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 05-05-2016.
 */
public class NearMeMapFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMapClickListener,TaskListner,AdapterView.OnItemClickListener,TextWatcher,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{
    GoogleMap mMap;
    LocationManager locationmanager;
    JsonRequester DataRequester;
    final String REQUEST_TAG="near_by_stores_request";
    HashMap<String,StoreProfile> NearByStoresMap=new HashMap<>();
    HashMap<Integer,Marker> MarkerIDs=new HashMap<>();
    SessionManager session;
    String Classname;
    NearMeActivity ParentCntx;
    ProgressDialog progressDialog;
    List<StoresData> nearstores;
    List<StoresData> Searchstores;
    List<String> SearchResults=new ArrayList<>();
    boolean isFetchingLoc;
    String SEARCH_REQUEST_TAG="search_tag";
    RobotoRegularAutoComplete SearchEt;
    LatLng CurrentLatLng;
    int mSelectedLocation=-1;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    boolean isGpsEnabled;
    boolean isNetworkEnabled;
    boolean isDataAcquired=false;
    int which=1;

    public static NearMeMapFragment newInstance()
    {
        NearMeMapFragment instance=new NearMeMapFragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                                                .addConnectionCallbacks(this)
                                                .addOnConnectionFailedListener(this)
                                                .addApi(LocationServices.API)
                                                .build();
        }
        DataRequester = new JsonRequester(this);
        Classname=getClass().getCanonicalName();
        ParentCntx= (NearMeActivity) getActivity();
        ParentCntx.OnRequestLocation();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching nearby stores details");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nearme_map,container,false);
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SearchEt= (RobotoRegularAutoComplete) view.findViewById(R.id.nearme_places_searchEt);
        SearchEt.addTextChangedListener(this);
        SearchEt.setHint(Html.fromHtml("<font>&#128269; Search</font>"));
        SearchEt.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CurrentLatLng=new LatLng(Double.parseDouble(Searchstores.get(position).StoreDetails.get(0).getMerchantLatitude()),Double.parseDouble(Searchstores.get(position).StoreDetails.get(0).getMerchantLongitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MarkerIDs.get(position).getPosition(),15));
        SearchEt.clearFocus();
        SearchEt.clearListSelection();
        removePhoneKeypad();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapClickListener(this);
        UiSettings mapuisettings=mMap.getUiSettings();
        mapuisettings.setMapToolbarEnabled(false);
        mapuisettings.setMyLocationButtonEnabled(true);

    }

    private void RetrieveLocation()
    {
        if(!isDataAcquired) {
            if (Build.VERSION.SDK_INT >= 23) {
                which=1;
                CheckLocationPermission();
            } else {
                if(which==1){ FetchLocation();}else{FetchPlaces(SearchEt.getText().toString());}
            }
        }
    }

    /**Need to check for Android Version 6.0 and Above (RUN TIME PERMISSION CHECK) **/
    private void CheckLocationPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                Snackbar snackbar=Snackbar.make(ParentCntx.coordinatorlayout,"App requires an additional permission to complete the registration process",Snackbar.LENGTH_LONG);
                snackbar.setAction("Grant", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},535);
                    }
                });
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},535);
            }
        }
        else
        {
            if(which==1){ FetchLocation();}else{FetchPlaces(SearchEt.getText().toString());}
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==535){
            if(permissions.length==1&&permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)&&grantResults[0]==PackageManager.PERMISSION_GRANTED
                 &&permissions[1].equals(Manifest.permission.ACCESS_COARSE_LOCATION)&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                if(which==1){ FetchLocation();}else{FetchPlaces(SearchEt.getText().toString());}
            }
            else
            {
                Display.DisplayToast(getActivity(),"Unable to retrieve your current location. ");
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void FetchLocation()
    {
        if(progressDialog!=null)
        {
            progressDialog.show();
        }
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation!=null)
        {
            MarkUserLocation(mLastLocation);
        }
        else {
            locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled=locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled=locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(isGpsEnabled||isNetworkEnabled) {
                Criteria locriteria = new Criteria();
                locriteria.setPowerRequirement(Criteria.POWER_HIGH);
                locriteria.setAccuracy(isGpsEnabled?Criteria.ACCURACY_COARSE:Criteria.ACCURACY_FINE);
                locationmanager.requestSingleUpdate(locriteria, mLocationListener, null);
            }
            else
            {
                DisplayGpsEnableDialog();
            }
        }
    }

    private  void DisplayGpsEnableDialog()
    {
        AlertDialog.Builder gpsDialogbuilder=new AlertDialog.Builder(getActivity());
        gpsDialogbuilder.setCancelable(false);
        gpsDialogbuilder.setMessage("Please enable your GPS to find out merchants around your place");
        gpsDialogbuilder.setTitle("Enable GPS");
        gpsDialogbuilder.setIcon(R.mipmap.ic_launcher);
        gpsDialogbuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        gpsDialogbuilder.create().show();
    }
    private void MarkUserLocation(Location location)
    {
        MarkerOptions markerOptions=new MarkerOptions()
                .draggable(false)
                .title("Your location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(new LatLng(location.getLatitude(),location.getLongitude()));
        CurrentLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(markerOptions);
        CameraPosition campos=new CameraPosition(new LatLng(location.getLatitude(),location.getLongitude()),15,0.0f,112.0f);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(campos));
        ParentCntx.OnReceiverLocation(location.getLatitude(),location.getLongitude());
        FetchNearByStores(new LatLng(location.getLatitude(),location.getLongitude()));

    }

    private void FetchNearByStores(LatLng latLng)
    {
        if(Utils.CheckInternet(getActivity()))
        {
            Map<String,String> Params=new HashMap<>();
            Params.put("lat",String.valueOf(latLng.latitude));
            Params.put("log",String.valueOf(latLng.longitude));
            DataRequester.StringRequesterFormValues(Urls.near_by_stores_url, Request.Method.POST,Classname,Urls.near_by_stores_method,Params,REQUEST_TAG);

        }
        else {
            ((NearMeActivity)getActivity()).DisplaySnackbarMsg("Please check your internet connection and try again.");
        }
    }


    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        if(progressDialog!=null)
        {
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
        }
        if(cd==0)
        {
            Display.DisplayToast(getActivity(),"Failed to get near by stores details");
            isFetchingLoc=false;
        }
        else if(cd==5)
        {
            Log.d("response",response);
            if(_className.equalsIgnoreCase(getClass().getCanonicalName())&&_methodName.equalsIgnoreCase(Urls.near_by_stores_method)) {
                Gson g = new Gson();
                NearByStoresDTO stores = g.fromJson(response, NearByStoresDTO.class);
                if (stores.getCode().equalsIgnoreCase("200")) {
                    isDataAcquired=true;
                    nearstores = stores.getStores();
                    for (int i = 0; i < nearstores.size(); i++) {
                        StoreProfile sprofile = nearstores.get(i).StoreDetails.get(0);
                        MarkerOptions markerOpts = new MarkerOptions();
                        markerOpts.draggable(false);
                        markerOpts.position(new LatLng(Double.parseDouble(sprofile.getMerchantLatitude()), Double.parseDouble(sprofile.getMerchantLongitude())));
                        Marker marker=mMap.addMarker(markerOpts);
                        MarkerIDs.put(i,marker);
                        NearByStoresMap.put(marker.getId(), sprofile);
                    }
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            if (NearByStoresMap.containsKey(marker.getId())) {
                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_info_window, null);
                                StoreProfile mStore = NearByStoresMap.get(marker.getId());
                                ImageView logo = (ImageView) view.findViewById(R.id.store_logo);
                                TextView title = (TextView) view.findViewById(R.id.store_title);
                                TextView address = (TextView) view.findViewById(R.id.store_address);
                                Log.d("MerchantLogo",mStore.getMerchantLogo());
                                if (mStore.getMerchantLogo() != null) {
                                    if (!mStore.getMerchantLogo().equalsIgnoreCase("")) {
                                        Picasso.with(getActivity().getApplicationContext()).load(mStore.getMerchantLogo()).into(logo,new MarkerCallback(marker));
                                    }
                                }
                                title.setText(mStore.getMerchantstore());
                                address.setText(mStore.getMerchantAddress());
                                return view;
                            }
                            return null;
                        }
                    });
                }
            }
            else if(_className.equalsIgnoreCase(getClass().getCanonicalName())&&_methodName.equalsIgnoreCase(Urls.near_me_search_method))
            {
                isFetchingLoc=false;
                if(Searchstores!=null){Searchstores.clear();}
                if(SearchResults!=null){SearchResults.clear();}
                if(NearByStoresMap!=null){NearByStoresMap.clear();}
                if(MarkerIDs!=null){MarkerIDs.clear();}
                mMap.clear();
                Gson g = new Gson();
                NearByStoresDTO stores = g.fromJson(response, NearByStoresDTO.class);
                if (stores.getCode().equalsIgnoreCase("200")) {
                    Searchstores = stores.getStores();
                    for (int i = 0; i < Searchstores.size(); i++) {
                        StoreProfile sprofile = Searchstores.get(i).StoreDetails.get(0);
                        MarkerOptions markerOpts = new MarkerOptions();
                        markerOpts.draggable(false);
                        SearchResults.add(sprofile.getMerchantstore());
                        markerOpts.position(new LatLng(Double.parseDouble(sprofile.getMerchantLatitude()), Double.parseDouble(sprofile.getMerchantLongitude())));
                        Marker marker=mMap.addMarker(markerOpts);
                        MarkerIDs.put(i,marker);
                        NearByStoresMap.put(marker.getId(), sprofile);
                    }
                    ArrayAdapter<String> dropdownadapter=new ArrayAdapter<String>(getActivity(),R.layout.custom_autolistview,SearchResults);for(String name:SearchResults) {
                        Log.d("SearchResults", name);
                    }
                    SearchEt.setAdapter(dropdownadapter);
                    SearchEt.showDropDown();
                }

            }
        }
    }

    private void FetchPlaces(String tag)
    {
        if(!isFetchingLoc)
        {
            Log.d("SearchTag",tag);
            isFetchingLoc=true;
            if(Utils.CheckInternet(getActivity()))
            {
                HashMap<String,String> params=new HashMap<>();
                params.put("lat",String.valueOf(CurrentLatLng.latitude));
                params.put("log",String.valueOf(CurrentLatLng.longitude));
                params.put("storename",tag);
                Display.DisplayLogD("searchparams",params.toString());
                DataRequester.StringRequesterFormValues(Urls.near_me_search_store_url, Request.Method.POST,getClass().getCanonicalName(),Urls.near_me_search_method,params,SEARCH_REQUEST_TAG);
            }
            else
            {
                ParentCntx.DisplaySnackbarMsg("Please check your internet connection and try again.");
            }
        }
    }

    @Override
    public void onStart() {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        RetrieveLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @SuppressWarnings("MissingPermission")
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            MarkUserLocation(location);
            locationmanager.removeUpdates(this);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };



    @Override
    public void onMapClick(LatLng latLng) {
       /* ParentCntx.OnReceiverLocation(latLng.latitude,latLng.longitude);
        Log.d("loc",latLng.toString());
        if(progressDialog!=null)
        {
            progressDialog.show();
        }
        CurrentLatLng=latLng;
        FetchNearByStores(latLng);*/
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(SearchEt.getText().length()>2){
            which=2;
            if (Build.VERSION.SDK_INT >= 23)
            {
                CheckLocationPermission();
            } else
            {
                FetchPlaces(s.toString());
            }
        }
    }




    public interface LocationObserver
    {
        public void OnReceiverLocation(Double Lat,Double Lng);
        public void OnRequestLocation();
    }

    public void removePhoneKeypad() {

        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

}

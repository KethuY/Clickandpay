package com.soffice.clickandpay.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
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
import com.soffice.clickandpay.UI.RobotoBoldTextView;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 05-05-2016.
 */
public class NearMeListFragment extends Fragment implements TaskListner
{

    JsonRequester DataRequester;
    String Classname;
    List<StoresData> nearstores;
    ProgressBar Pbar;
    RecyclerView recyclerView;
    final String REQUEST_TAG="nearme_list_request";
    public static NearMeListFragment newInstance(Double Lat,Double Lng)
    {
        NearMeListFragment instance=new NearMeListFragment();
        Bundle bundle=new Bundle();
        bundle.putDouble("lat",Lat);
        bundle.putDouble("lng",Lng);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Classname=getClass().getCanonicalName();
        DataRequester=new JsonRequester(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_nearme_list,container,false);
        Pbar= (ProgressBar) view.findViewById(R.id.pbar);
        Utils.ApplyPbarColor(getActivity(),Pbar);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        FetchNearByStores(new LatLng(getArguments().getDouble("lat"),getArguments().getDouble("lng")));
        return view;
    }

    private void FetchNearByStores(LatLng latLng)
    {
        if(Utils.CheckInternet(getActivity()))
        {
            Pbar.setVisibility(View.VISIBLE);
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
        Pbar.setVisibility(View.GONE);
        if (cd == 0) {
            Display.DisplayToast(getActivity(), "Failed to get near by stores details");
        } else if (cd == 5) {
            Gson g = new Gson();
            NearByStoresDTO stores = g.fromJson(response, NearByStoresDTO.class);
            if (stores.getCode().equalsIgnoreCase("200")) {
                nearstores = stores.getStores();
                NearByAdapter adapter=new NearByAdapter();
                recyclerView.setAdapter(adapter);
            }
            else
            {
                Display.DisplayToast(getActivity(),"Failed to load near by stores details");
            }
        }
    }

    private class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.NearByViewHolder>
    {
        @Override
        public NearByViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.listchildview_nearbylist,parent,false);
            return new NearByViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NearByViewHolder holder, int position) {
                StoreProfile sprofile=nearstores.get(position).StoreDetails.get(0);
                holder.AddressTv.setText(sprofile.getMerchantAddress());
                holder.DistanceTv.setText(Html.fromHtml("<font color='#d12127'>"+Utils.CalculateDistance(getArguments().getDouble("lat"),getArguments().getDouble("lng"),Double.valueOf(sprofile.getMerchantLatitude()),Double.valueOf(sprofile.getMerchantLongitude()))+"</font>"));
                holder.DistanceTv.append(" Kms");
                holder.PlacenameTv.setText(sprofile.getMerchantstore());
                if(!sprofile.getMerchantLogo().equalsIgnoreCase("")) {
                    Picasso.with(getActivity()).load(sprofile.getMerchantLogo()).placeholder(ContextCompat.getDrawable(getActivity(), R.mipmap.c_n_p_logo)).into(holder.PlaceLogo);
                }
        }

        @Override
        public int getItemCount() {
            return nearstores.size();
        }

        public class NearByViewHolder extends RecyclerView.ViewHolder
        {
            RobotoLightTextView DistanceTv,AddressTv;
            RobotoBoldTextView PlacenameTv;
            ImageView PlaceLogo;

            public NearByViewHolder(View itemView) {
                super(itemView);
                DistanceTv= (RobotoLightTextView) itemView.findViewById(R.id.nearby_distance);
                AddressTv= (RobotoLightTextView) itemView.findViewById(R.id.nearby_address);
                PlacenameTv= (RobotoBoldTextView) itemView.findViewById(R.id.nearby_placename);
                PlaceLogo= (ImageView) itemView.findViewById(R.id.nearby_iv);
            }
        }
    }
}

package com.soffice.clickandpay.Services;

import android.content.Context;

import com.soffice.clickandpay.NetWork.Urls;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Anitha.
 */
public class RestClient implements RequestInterceptor {

    private CnpService cnpSerVices;

    public static final String TAG = RestClient.class.getSimpleName();

    public static RestClient getInstance() {
        return new RestClient();
    }


    /**
     * Creating rest adapter
     *
     * @param context
     */
    public CnpService getCPAService(Context context) {
        final String cnpBaseUrl = Urls.homeUrl;
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(cnpBaseUrl)
                .setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(this)
                .setClient(new retrofit.client.UrlConnectionClient()).build();
        cnpSerVices = restAdapter.create(CnpService.class);
        return cnpSerVices;
    }

    @Override
    public void intercept(RequestFacade request) {
        //do nothing
    }


}

package com.soffice.clickandpay.Adapters;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soffice.clickandpay.R;

public class ContentAdViewHolder {
	public TextView advertisement;
	public ImageView image;
	public TextView callToAction;
	public TextView headline;
	public TextView body;
	public ImageView logo;
	public TextView advertiser;
	public ImageView attributionIcon;
	public TextView attributionText,cl_smscount;
	public TextView cl_name, cl_message, cl_unread, cl_time, cl_avatar_letter,
			phoneno;
	public TextView cl_avatar_letter1,cl_name1,cl_message1,cl_unread1,cl_time1,cl_smscount1;
	public ImageView cl_image1;
	public RelativeLayout sms_list1;
	public ImageView cl_icon, cl_image;
	public RelativeLayout  parent;
	public RelativeLayout  msg_layout1,msg_layout, sms_list;
	public LinearLayout Sublayout;
	// public RelativeLayout ads;
	public View divider_SmsAD;

	public TextView AdHeader, AdDescription, favoritename, favoritenumber;
	public ImageView Adchoice, Ad_Circularimage, favorite_image;
	public LinearLayout ads;
	public TextView cl_number;
	public FrameLayout sms_avatar_container;
	static ContentAdViewHolder holder;

	/**
	 * Creates and returns an {@code ContentAdViewHolder} to hold the views of a
	 * content ad list item in memory.
	 */
	public static ContentAdViewHolder create(View contentAdLayout) {
		ContentAdViewHolder contentAdViewHolder = new ContentAdViewHolder();

		try {
			contentAdViewHolder.parent = (RelativeLayout) contentAdLayout
					.findViewById(R.id.sms_list);

		} catch (Exception e) {
			e.printStackTrace();
			return contentAdViewHolder;

		}

		return contentAdViewHolder;
	}

	public static ContentAdViewHolder create1(View contentAdLayout) {
		ContentAdViewHolder contentAdViewHolder = new ContentAdViewHolder();

		try {
			contentAdViewHolder.parent = (RelativeLayout) contentAdLayout
					.findViewById(R.id.sms_list);

			/*contentAdViewHolder.sms_avatar_container = (FrameLayout) contentAdLayout
					.findViewById(R.id.sms_avatar_container);*/
		} catch (Exception e) {
			e.printStackTrace();
			return contentAdViewHolder;

		}

		return contentAdViewHolder;
	}
}

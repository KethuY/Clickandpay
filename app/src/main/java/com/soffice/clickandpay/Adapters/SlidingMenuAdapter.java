package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soffice.clickandpay.Activities.ProfileSettings;
import com.soffice.clickandpay.Pojo.SlideMenu;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoBoldTextView;
import com.soffice.clickandpay.Utilty.SessionManager;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Surya on 27-04-2016.
 */
public class SlidingMenuAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_HEADERLAYOUT=2;
    private ArrayList<SlideMenu> mData = new ArrayList<SlideMenu>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    SessionManager sessionManager;
    Context mContext;

    public SlidingMenuAdapter(SessionManager sessionManager, Context mContext) {
        this.sessionManager = sessionManager;
        this.mContext = mContext;
    }

    public void addItem(final SlideMenu item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final SlideMenu item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    public void addHeaderLayoutSection(final SlideMenu item)
    {
        mData.add(item);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if(position==0)
        {
            return TYPE_HEADERLAYOUT;
        }
        else
        {
            return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SlideMenu getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_HEADERLAYOUT:
                    convertView = inflater.inflate(R.layout.navigation_headerlayout,parent,false);
                    holder.Username = (RobotoBoldTextView) convertView.findViewById(R.id.navigation_usernametv);
                    holder.EditProfile= (RobotoBoldTextView) convertView.findViewById(R.id.navigation_usernameedittv);
                    break;
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.navigation_item,parent,false);
                    holder.OptionsImage= (ImageView) convertView.findViewById(R.id.navigation_optioniv);
                    holder.Options = (TextView) convertView.findViewById(R.id.navigation_optiontv);
                    break;
                case TYPE_SEPARATOR:
                    convertView = inflater.inflate(R.layout.navigation_divider, parent,false);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(holder.Options!=null) {
            if (rowType == TYPE_ITEM) {
                holder.Options.setText(mData.get(position).getTitle());
                if(mData.get(position).getIcon()!=0) {
                    holder.OptionsImage.setImageResource(mData.get(position).getIcon());
                }
            }
        }
        else if(rowType==TYPE_HEADERLAYOUT)
        {
                holder.Username.setText(sessionManager.getUserName());
                holder.EditProfile.setText("Edit");
                /*holder.EditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent=new Intent(mContext, ProfileSettings.class);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(launchIntent);
                    }
                });*/

        }
       else {
            if (position != 1 && holder.divider != null) {
                holder.divider.setVisibility(View.VISIBLE);
            } else {
                if (holder.divider != null) {
                    holder.divider.setVisibility(View.GONE);
                }
            }
        }

        return convertView;

    }

    public class ViewHolder {
        public RobotoBoldTextView Username;
        public RobotoBoldTextView EditProfile;
        public TextView Options;
        public ImageView OptionsImage;
        public View divider;
    }
}

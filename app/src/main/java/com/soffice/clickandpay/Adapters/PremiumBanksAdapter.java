package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.soffice.clickandpay.Pojo.NetBanks;
import com.soffice.clickandpay.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surya on 16-04-2016.
 */
public class PremiumBanksAdapter extends ArrayAdapter<NetBanks>
{
    public PremiumBanksAdapter(Context context, int resource, List<NetBanks> objects) {
        super(context, resource, objects);
    }

    private class ViewHolder
    {
        ImageView iv;
        TextView tv;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.premiumbanks_childview,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.premium_banks_iv);
            viewHolder.tv= (TextView) convertView.findViewById(R.id.premium_banks_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(getItem(position).getCardName());
        if(getItem(position).getCardName().equalsIgnoreCase("axis bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.axis);
        }
        else if(getItem(position).getCardName().equalsIgnoreCase("sbi bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.sbi);
        }
        else if(getItem(position).getCardName().equalsIgnoreCase("icici bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.icici);
        }
        else if(getItem(position).getCardName().equalsIgnoreCase("hdfc bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.hdfc);
        }
        else if(getItem(position).getCardName().equalsIgnoreCase("yes bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.yesbank);
        }
        else if(getItem(position).getCardName().equalsIgnoreCase("canara bank"))
        {
            viewHolder.iv.setImageResource(R.drawable.canara);
        }

        return convertView;
    }
}

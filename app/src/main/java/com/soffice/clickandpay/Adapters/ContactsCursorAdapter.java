package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.StringMatcher;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactsCursorAdapter extends CursorAdapter implements
        SectionIndexer {
    private static final int VIEW_TYPE_COUNT = 4;
    private static final int VIEW_TYPE_ONE = 0;
    private static final int VIEW_TYPE_TWO = 1;
    private static final int VIEW_TYPE_THREE = 2;
    private static final int VIEW_TYPE_FOUR = 3;
    final int adPoition1;
    final String adtype;
    public Cursor cur;
    AlphabetIndexer mAlphabetIndexer;
    AssetFileDescriptor afd = null;
    int style = 0;
    Context cont;
    ContentAdViewHolder holder;
    Boolean flinging = true;
    String name2 = null;
    String phoneNumber2 = null;
    String result;
    String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    int rpost = 0;
    ArrayList lst = new ArrayList();

    public ContactsCursorAdapter(Context context, Cursor c,
                                 boolean autoRequery, int i, String adtype) {
        super(context, c, autoRequery);
        cont = context;
        cur = c;
        adPoition1 = i;
        this.adtype = adtype;

    }

    public static String capitalizeString(String string) {
        try {

            char[] chars = string.toLowerCase().toCharArray();
            boolean found = false;
            for (int i = 0; i < chars.length; i++) {
                if (!found && Character.isLetter(chars[i])) {
                    chars[i] = Character.toUpperCase(chars[i]);
                    found = true;
                } else if (Character.isWhitespace(chars[i]) || chars[i] == '.'
                        || chars[i] == '\'') { // You can add other chars here
                    found = false;
                }
            }
            return String.valueOf(chars);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void bindView(View rowView, Context arg1, final Cursor cur1) {
        int viewType = getItemViewType(cur.getPosition());

                holder = (ContentAdViewHolder) rowView.getTag();
                setDatainLayout(cur1, rowView, holder);
            }

    private void setDatainLayout(final Cursor cur, View rowView,
                                 ContentAdViewHolder holder) {
        if (cur != null) {
            try {
                // favroites = data.getString(data.getColumnIndex("_id"));

                if (cur != null) {
                    holder.cl_name.setText(capitalizeString(cur.getString(cur
                            .getColumnIndex("name"))));
                    holder.cl_number.setText(cur.getString(cur
                            .getColumnIndex("number")));
                    /*holder.cl_avatar_letter.setText(AvatarLetter(holder.cl_name
                            .getText().toString()));
                    style = 1;
                    holder.cl_avatar_letter.setVisibility(View.VISIBLE);*/
                    /*holder.cl_icon.setVisibility(View.GONE);
                    style = cur.getInt(cur.getColumnIndex("style"));*/


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap loadContactPhotoThumbnail(String photoData) {
        try {
            Uri thumbUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                thumbUri = Uri.parse(photoData);
            } else {
                final Uri contactUri = Uri.withAppendedPath(
                        Contacts.CONTENT_URI, photoData);
                thumbUri = Uri.withAppendedPath(contactUri,
                        Photo.CONTENT_DIRECTORY);
            }
            afd = cont.getContentResolver().openAssetFileDescriptor(thumbUri,
                    "r");
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            if (fileDescriptor != null) {
                return BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
                        null);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();

			/*
             * Handle file not found errors
			 */
        }
        // In all cases, close the asset file descriptor
        finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        return null;
    }

    @Override
    public View newView(Context arg0, Cursor cursor, ViewGroup parent) {
        cur = cursor;

        int viewType = getItemViewType(cursor.getPosition());
        View view = null;
        try {
                    view = LayoutInflater.from(cont).inflate(
                            R.layout.adapter_all_smslist, parent, false);
                    holder = new ContentAdViewHolder();

                    holder = new ContentAdViewHolder();
                    /*holder.cl_avatar_letter = (TextView) view
                            .findViewById(R.id.sms_avatar_letter);
                    holder.cl_icon = (ImageView) view
                            .findViewById(R.id.sms_avatar_image);*/
                    holder.cl_name = (TextView) view
                            .findViewById(R.id.sms_address);
                    holder.cl_number = (TextView) view
                            .findViewById(R.id.sms_message);
                    view.setTag(holder);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return view;

    }

    @Override
    public Object[] getSections() {

        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }

    @Override
    public int getPositionForSection(int section) {

        // If there is no item for current section, previous section will be
        // selected
        // try {

        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < cur.getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        cur.moveToPosition(j);

                        if (StringMatcher.match(
                                String.valueOf(cur.getString(
                                        cur.getColumnIndex("name")).charAt(0)),
                                String.valueOf(k))) {
                            return j;
                        }
                        cur.moveToFirst();

                    }

                } else {
                    if (cur != null) {
                        try {
                            cur.moveToPosition(j);
                            if (StringMatcher.match(
                                    String.valueOf(cur.getString(
                                            cur.getColumnIndex("name")).charAt(
                                            0)),
                                    String.valueOf(mSections.charAt(i)))) {
                                return j;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        cur.moveToFirst();
                    }

                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == adPoition1) {
            // rpost = 2;
            if (adtype.equals("Native")) {
                rpost = 0;
            } else if (adtype.equals("Native Install")) {
                rpost = 1;
            } else if (adtype.equals("internal")) {
                rpost = 3;
            } else {
                rpost = 2;
            }
        } else {
            rpost = 2;
        }
        return rpost;
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != OnScrollListener.SCROLL_STATE_FLING) {
            flinging = false;
        } else {
            flinging = true;
        }
    }

    public String AvatarLetter(String name) {
        try {
            String regex = "(.)*(\\d)(.)*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = null;
            if (name.length() > 2) {
                matcher = pattern.matcher(name.substring(0, 2).trim());

                boolean isMatched = matcher.matches();
                if (isMatched) {
                    result = "#";

                } else {
                    if (name.trim().length() > 2) {
                        String[] str = name.split(" ");
                        if (str.length > 1) {
                            result = str[0].substring(0, 1) + str[1].substring(0, 1);
                            result = result.toUpperCase();
                        } else {
                            result = name.trim().substring(0, 2).toUpperCase();
                        }
                    } else {
                        result = "#";
                    }

                }
            } else {
                result = "#";
            }
            return result.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

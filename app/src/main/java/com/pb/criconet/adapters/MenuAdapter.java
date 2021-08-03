package com.pb.criconet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.Drawer;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Drawer> text;
    Context context;
    SharedPreferences prefs;

    public MenuAdapter(Context a,ArrayList<Drawer> list) {
        context = a;
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        text =list;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return text.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return text.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private ViewHolder holder = null;

    private class ViewHolder {
        TextView textView_list_item, counts;
        ImageView imageView;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_adapter_item, null);
            holder = new ViewHolder();
            holder.textView_list_item =  convertView.findViewById(R.id.textView_list_item);
            holder.counts =  convertView.findViewById(R.id.count);
            holder.imageView = convertView.findViewById(R.id.imageView_icons);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_list_item.setText(text.get(position).getTitle());
        holder.imageView.setImageResource(text.get(position).getImage());

        if (text.get(position).isCounter()) {
            holder.counts.setVisibility(View.VISIBLE);
            switch (position) {
                case 4:
                    if (SessionManager.get_Friends(prefs).equalsIgnoreCase("0"))
                        holder.counts.setVisibility(View.GONE);
                    holder.counts.setText(SessionManager.get_Friends(prefs));
                    break;
                case 5:
                    if (SessionManager.get_Notification(prefs).equalsIgnoreCase("0"))
                        holder.counts.setVisibility(View.GONE);
                    holder.counts.setText(SessionManager.get_Notification(prefs));
                    break;
            }
        } else {
            holder.counts.setVisibility(View.GONE);
        }

        return convertView;
    }
}

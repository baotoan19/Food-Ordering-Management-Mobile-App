package com.example.quanlycuahangbandoanvat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlycuahangbandoanvat.DTO.OptionCategory;
import com.example.quanlycuahangbandoanvat.R;

import java.util.List;

public class CustomAdapterGridViewCategory extends BaseAdapter {
    private List<OptionCategory> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    public CustomAdapterGridViewCategory(Context aContext, List<OptionCategory> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_category_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgCategory);
            holder.titleCategory = (TextView)
                    convertView.findViewById(R.id.tvTitleCategory);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OptionCategory optionCategory = this.listData.get(position);
        holder.titleCategory.setText(optionCategory.getTitle());
        int imageId = this.getMipmapResIdByName(String.valueOf(optionCategory.getImage()));
        holder.imageView.setImageResource(imageId);
        return convertView;
    }
    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomGridView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }
    static class ViewHolder {
        ImageView imageView;
        TextView titleCategory;
    }
}

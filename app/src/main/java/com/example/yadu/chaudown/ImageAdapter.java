package com.example.yadu.chaudown;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Yadu on 1/27/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public static Bitmap bm[];

    public ImageAdapter(Context c, Bitmap inBm[]) {
        mContext = c;
        this.bm = inBm;
        //Log.d("LOOOOOOOOOOOOOOOOOOOL", bm.toString());
    }

    public int getCount() {
        return bm.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(-1, -1));
            imageView.setScaleType(ImageView.ScaleType.CENTER);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bm[position]);
        //imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}

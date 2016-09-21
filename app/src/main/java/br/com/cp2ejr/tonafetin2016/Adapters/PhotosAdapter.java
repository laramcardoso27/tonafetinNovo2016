package br.com.cp2ejr.tonafetin2016.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.cp2ejr.tonafetin2016.Models.Photo;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 04/08/2016.
 */
public class PhotosAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Photo> mPhotoList = new ArrayList<>();
    private LayoutInflater mInflater;

    public PhotosAdapter(Context mContext, ArrayList<Photo> mPhotoList) {
        this.mContext = mContext;
        this.mPhotoList = mPhotoList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Photo photo = mPhotoList.get(position);

        ItemHolder itemHolder = new ItemHolder();

        view = mInflater.inflate(R.layout.item_list_view_instagram, null);

        itemHolder.ivPhoto = (ImageView) view.findViewById(R.id.iv_photo_instagram);
        itemHolder.tvUser  = (TextView) view.findViewById(R.id.tv_user_instagram);
        itemHolder.tvLegend = (TextView) view.findViewById(R.id.tv_legend_instagram);

        itemHolder.ivPhoto.setImageBitmap(getBitmapFromURL(photo.getUrlPhoto()));
        itemHolder.tvUser.setText(photo.getUserInstagram());
        itemHolder.tvLegend.setText(photo.getLegendPhoto());

        view.setTag(itemHolder);

        return view;
    }

    public class ItemHolder {
        ImageView ivPhoto;
        TextView tvUser;
        TextView tvLegend;
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();

            Log.e("Exception", e.getMessage());

            return null;
        }
    }


}

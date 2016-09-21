package br.com.cp2ejr.tonafetin2016.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.cp2ejr.tonafetin2016.Models.Project;
import br.com.cp2ejr.tonafetin2016.R;

/**
 * Created by Lara on 31/08/2016.
 */
public class RankingAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Project> mProjects = new ArrayList<>();
    private LayoutInflater mInflater;

    public RankingAdapter(Context mContext, ArrayList<Project> mProjects) {
        this.mContext  = mContext;
        this.mProjects = mProjects;
        this.mInflater = LayoutInflater.from(mContext);
    }



    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public Object getItem(int i) {
        return mProjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ItemHolder itemHolder = new ItemHolder();

        view = mInflater.inflate(R.layout.item_list_view_ranking, null);

        itemHolder.tvPosition = (TextView) view.findViewById(R.id.tv_ranking_position);
        itemHolder.tvProject  = (TextView) view.findViewById(R.id.tv_ranking_project);
        itemHolder.tvVotes    = (TextView) view.findViewById(R.id.tv_ranking_votes);

        int projectPosition = mProjects.get(position).getPosition() + 1;

        itemHolder.tvPosition.setText(""+projectPosition+"o");

        switch (projectPosition) {
            case 1:
                itemHolder.tvPosition.setTextColor(Color.parseColor("#ffd700"));
                break;
            case 2:
                itemHolder.tvPosition.setTextColor(Color.parseColor("#c0c0c0"));
                break;
            case 3:
                itemHolder.tvPosition.setTextColor(Color.parseColor("#cd7f32"));
                break;
        }

        itemHolder.tvProject.setText(""+mProjects.get(position).getName());
        itemHolder.tvVotes.setText(""+mProjects.get(position).getVotes());

        view.setTag(itemHolder);

        return view;
    }

    public class ItemHolder {
        TextView tvPosition;
        TextView tvProject;
        TextView tvVotes;
    }
}

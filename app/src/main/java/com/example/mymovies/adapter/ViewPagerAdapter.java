package com.example.mymovies.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymovies.R;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.datepicker, R.drawable.language};
    private String[] positionone;
    private String[] positontwo;

    public ViewPagerAdapter(Context context, String[] positionone, String[] positontwo) {
        this.context = context;
        this.positionone = positionone;
        this.positontwo = positontwo;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if(position == 0) {
            view = layoutInflater.inflate(R.layout.row_viewpager, null);
            Button btnreleasedate = (Button)view.findViewById(R.id.btnreleasedate);
            btnreleasedate.setText(positionone[0]);
            Button btnstar = (Button)view.findViewById(R.id.btnstar);
            btnstar.setText(positionone[1]);
            Button btnlanguage = (Button)view.findViewById(R.id.btnlanguage);
            btnlanguage.setText(positionone[2]);
        }else
        {
            view = layoutInflater.inflate(R.layout.row_viewpager2, null);
            Button btnadult = (Button)view.findViewById(R.id.btnadult);
            btnadult.setText(positontwo[0].equalsIgnoreCase("true")?"YES":"NO");


        }
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
package com.mirkowu.baselibrarysample.ui.start;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.ui.main.MainActivity;
import com.mirkowu.baselibrarysample.utils.ImageUtil;
import com.mirkowu.baselibrarysample.utils.SPManager;
import com.mirkowu.statusbarutil.StatusBarUtil;
import com.softgarden.baselibrary.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.tvStart)
    AppCompatTextView tvStart;

    public static void start(Context context) {
        Intent starter = new Intent(context, GuideActivity.class);
//	    starter.putExtra( );
        context.startActivity(starter);
    }

    private ImagePagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initialize() {
        StatusBarUtil.setImmersiveTransparentStatusBar(this);

        adapter = new ImagePagerAdapter();
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvStart.setEnabled(position == adapter.getCount() - 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @OnClick(R.id.tvStart)
    public void onViewClicked() {
        SPManager.setIsFirstLaunch(false);
        MainActivity.start(getActivity());
        finish();
    }


    public class ImagePagerAdapter extends PagerAdapter {
        int[] resId = new int[]{R.mipmap.header3, R.mipmap.header3, R.mipmap.header3};

        public ImagePagerAdapter() {

        }

        @Override
        public int getCount() {
            return resId.length;
        }

        //  来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        //  PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView((View) object);
        }

        //  当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = new ImageView(view.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageUtil.loadSrc(imageView, resId[position]);//viewPager直接加载大图会导致卡顿，可使用第三方工具加载
            view.addView(imageView);
            return imageView;
        }
    }
}

package com.mirkowu.baselibrary.utils;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mirkowu.baselibrary.R;


/**
 * Created by Administrator on 2016/12/28 0028.
 */

public class ImageUtil {


    public static String getImageUrl(String url) {
        return url;
    }

    /**
     * 检查url 是否拼接
     *
     * @param url
     * @return
     */
    public static String checkUrl(String url) {
        //有完整路径的url不用拼接
        if (url != null && (url.startsWith("http"))) {
        } else
            url = getImageUrl(url);

        return url;
    }


    /**
     * 加载图片正方形图片
     * 默认图是正方形
     *
     * @param imageView
     * @param url
     */
    public static void load(ImageView imageView, @Nullable String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    /**
     * 加载图片长方形图片
     * 默认图是长方形
     *
     * @param imageView
     * @param url
     */
    public static void loadRectangle(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    /**
     * 加载头像
     *
     * @param imageView
     * @param url
     */
    public static void loadHeader(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    /**
     * 加载大头像
     * 只是默认图不同
     *
     * @param imageView
     * @param url
     */
    public static void loadBigHeader(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }


    /**
     * 高斯模糊
     *
     * @param imageView
     * @param url
     */
    public static void loadBlur(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
                // .bitmapTransform(new BlurTransformation(imageView.getContext(), 1, 3))
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadBlur(ImageView imageView, String url, int radius, int sampling) {
        // radius "23":模糊度；sampling "4":图片缩放4倍后再进行模糊a
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                // .placeholder(R.mipmap.ic_launcher)
                // .bitmapTransform(new BlurTransformation(imageView.getContext(), radius, sampling))
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void loadAsGif(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .asGif()
                .thumbnail(0.5f)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

//    public static Observable<Boolean> clearCache(BaseActivity context) {
//        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
//            Glide.get(context).clearDiskCache();
//            e.onNext(true);
//            e.onComplete();
//
//        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> context.showProgressDialog())
//                .doOnTerminate(context::hideProgressDialog)
//                .compose(context.bindUntilEvent(ActivityEvent.DESTROY));
//
//    }


    public static void requestListener(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(checkUrl(url))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

}

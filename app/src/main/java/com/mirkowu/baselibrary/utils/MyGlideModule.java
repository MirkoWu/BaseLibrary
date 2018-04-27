package com.mirkowu.baselibrary.utils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.AppGlideModule;
import com.softgarden.baselibrary.BaseApplication;

import java.io.File;


@GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                //设置glide的缓存目录,与缓存文件夹的大小限制
                File cacheLocation = new File(BaseApplication.getInstance().getExternalCacheDir(), "images");
                cacheLocation.mkdirs();
                return DiskLruCacheWrapper.create(cacheLocation, 150 * 1024 * 1024);
            }
        });
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
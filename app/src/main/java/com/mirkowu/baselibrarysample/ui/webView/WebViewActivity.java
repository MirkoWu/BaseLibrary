package com.mirkowu.baselibrarysample.ui.webView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.ToolbarActivity;
import com.mirkowu.basetoolbar.BaseToolbar;

import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;

public class WebViewActivity extends ToolbarActivity {

    public static void start(Context context, String title, String url) {
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(KEY_TITLE, title);
        starter.putExtra(KEY_DATA, url);
        context.startActivity(starter);
    }

    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.mProgressBar)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder;
    }

    @Override
    protected void initialize() {
        String title = getIntent().getStringExtra(KEY_TITLE);
        String url = getIntent().getStringExtra(KEY_DATA);
        getToolbar().setTitle(title);

        initWebView();
        loadUrl(url);
    }


    /***
     * 方式1：加载url
     *
     * @param url
     */
    private void loadUrl(String url) {
        WebSettings webSettings = mWebView.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        mWebView.loadUrl(url);
    }

    /**
     * 方式2：加载html文本
     *
     * @param content
     */
    private void loadText(String content) {
        WebSettings webSettings = mWebView.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(false); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小


        /*** 完美自适应屏幕 **/
        StringBuilder sb = new StringBuilder();
        sb.append(content)
                .append("<html>")
                .append("<head>")
                .append("<meta charset=\\\"utf-8\\\">")
                .append("<meta id=\\\"viewport\\\" name=\\\"viewport\\\" content=\\\"width=device-width*0.9,initial-scale=1.0,maximum-scale=1.0,user-scalable=false\\\" />")
                .append("<meta name=\\\"apple-mobile-web-app-capable\\\" content=\\\"yes\\\" />")
                .append("<meta name=\\\"apple-mobile-web-app-status-bar-style\\\" content=\\\"black\\\" />")
                .append("<meta name=\\\"black\\\" name=\\\"apple-mobile-web-app-status-bar-style\\\" />")
                .append("<style>img{width:100%;}</style>")
                .append("<style>iframe{width:100%;}</style>")
                .append("<style>table{width:100%;}</style>")
                .append("<style>body{font-size:18px;}</style>")
                .append("<title>mWebView</title>");


        mWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
    }


    private void initWebView() {
        // mWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTheme)); // 设置背景色
        WebSettings webSettings = mWebView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);


//        //设置自适应屏幕，两者合用  这样会使加载文本时 文字变小
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //开启DomStorage缓存
        webSettings.setDomStorageEnabled(true);
//        //启用数据库
        webSettings.setDatabaseEnabled(true);
//        //设置定位的数据库路径
//        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//        webSettings.setGeolocationDatabasePath(dir);

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //不使用缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //这个是加载的地址是https的，一些资源文件使用的是http方法的，
        // 从安卓4.4之后对webview安全机制有了加强，webview里面加载https url的时候，
        // 如果里面需要加载http的资源或者重定向的时候，webview会block页面加载。需要设置MixedContentMode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 99) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

}

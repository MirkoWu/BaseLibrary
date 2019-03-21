package com.mirkowu.baselibrarysample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.ToolbarActivity;
import com.mirkowu.baselibrarysample.utils.SPManager;
import com.mirkowu.basetoolbar.BaseToolbar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends ToolbarActivity {

    @BindView(R.id.tvSystem)
    AppCompatButton tvSystem;
    @BindView(R.id.tvChinese)
    AppCompatButton tvChinese;
    @BindView(R.id.tvEnglish)
    AppCompatButton tvEnglish;

    public static void start(Context context) {
        Intent starter = new Intent(context, LanguageActivity.class);
//	    starter.putExtra( );
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("切换语言");
    }

    @Override
    protected void initialize() {

    }

    @OnClick({R.id.tvSystem, R.id.tvChinese, R.id.tvEnglish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSystem:
                SPManager.setFollowSystemLanguage(true);
                //设置 跟随系统 true 就行了
                //千万不要调用 changeLanguage() 因为内部肯已经设置 false;
                break;
            case R.id.tvChinese:
                changeLanguage(Locale.SIMPLIFIED_CHINESE);
                break;
            case R.id.tvEnglish:
                changeLanguage(Locale.ENGLISH);
                break;
        }


        //切换完 2种方式 显示
        //1.直接返回首页 清掉中间界面 相当于重启应用 （例如：微信）
//                Intent starter = new Intent(getContext(), MainActivity.class);
//                starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(starter);


        //2.直接结束当前界面  返回上一个界面 代码会检测 当前语言是否切换 切换了会重启当前Actiivty
        // （缺点：会闪下屏）
        finish();
    }
}

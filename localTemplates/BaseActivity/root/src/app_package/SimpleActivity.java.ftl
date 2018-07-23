package ${packageName};

import ${superClassFqcn};
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import com.softgarden.baselibrary.base.BaseActivity;
<#if includeCppSupport!false>
import android.widget.TextView;
</#if>

public class ${activityClass} extends BaseActivity {

	public static void start(Context context) {
		Intent starter = new Intent(context, ${activityClass}.class);
//	    starter.putExtra( );
		context.startActivity(starter);
	}
	
    @Override
    protected int getLayoutId() {
		<#if generateLayout>
         return R.layout.${layoutName};
</#if>
<#include "../../../../common/jni_code_usage.java.ftl">
    }
<#include "../../../../common/jni_code_snippet.java.ftl">

    @Override
    protected void initialize() {

    }
}

package ${packageName};

import ${superClassFqcn};
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.basetoolbar.BaseToolbar;
<#if includeCppSupport!false>
import android.widget.TextView;
</#if>

public class ${activityClass} extends ToolbarActivity {

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

	@Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return null;
    }
	
    @Override
    protected void initialize() {

    }
}

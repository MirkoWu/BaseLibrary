package com.mirkowu.baselibrary.ui.testRefresh;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mirkowu.baselibrary.R;
import com.softgarden.baselibrary.base.BaseRVHolder;

/**
 * @author by DELL
 * @date on 2018/7/31
 * @describe
 */
public class MuiltTypeAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T, BaseRVHolder> {

    public MuiltTypeAdapter() {
        super(null);
        addItemType(MultiBean.TYPE_HEADER, R.layout.item_flexbox);
        addItemType(MultiBean.TYPE_ITEM, R.layout.item_flexbox_type2);
        //  addItemType(MultiBean.TYPE_FOOTER, R.layout.item_preview_order_footer);
    }

    //    @Override
//    protected View getItemView(int layoutResId, ViewGroup parent) {
//      //  ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
//        if (binding == null) {
//            return super.getItemView(layoutResId, parent);
//        }
//      //  View view = binding.getRoot();
//      //  view.setTag(R.id.XRVAdapter_databinding_support, binding);
//      //  return view;
//    }
    @Override
    protected void convert(BaseRVHolder helper, T item) {
        switch (helper.getItemViewType()) {
            case MultiBean.TYPE_HEADER:

                break;
            case MultiBean.TYPE_ITEM:

                break;
        }
    }
}

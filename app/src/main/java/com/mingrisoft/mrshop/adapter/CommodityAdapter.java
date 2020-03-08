package com.mingrisoft.mrshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.adapter.recylerview.base.MyRecyclerViewAdapter;
import com.mingrisoft.mrshop.entity.Commodity;
import com.mingrisoft.mrshop.network.HttpHelper;
import com.mingrisoft.mrshop.utils.DownLoadImageUtils;
import com.mingrisoft.mrshop.utils.FormatUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;

import java.util.List;

/**
 * 作者： LYJ
 * 功能： 商城列表的适配器
 * 创建日期： 2017/5/9
 */

public class CommodityAdapter extends MyRecyclerViewAdapter<Commodity,CommodityAdapter.CommodityViewHolder> {

    private int layoutRes = R.layout.item_commodity;
    public CommodityAdapter(Context mContext, List<Commodity> mList) {
        super(mContext, mList);
    }
    public CommodityAdapter(Context mContext, List<Commodity> mList, boolean isChange) {
        super(mContext, mList);
        if (isChange) layoutRes = R.layout.item_commodity2;
    }
    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    protected void onBindView(final CommodityViewHolder holder, int position) {
        Commodity commodity = mList.get(position);//获取指定位置的数据
        holder.title.setText(commodity.getTitle());//设置标题
        holder.price.setText(FormatUtils.getPriceText(commodity.getPrice()));//设置价格
        String imageUrls = commodity.getImageUrls();
        String url[] = imageUrls.split(StaticUtils.THESEPARATOR);
        //加载第一张图片
        final String imageUrl = HttpHelper.HTTP_URL + url[0];
        //加载图片
        DownLoadImageUtils.loadToImage(mContext,imageUrl,holder.image);
    }

    /**
     * 创建ViewHoler
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CommodityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(layoutRes,parent,false);
        return new CommodityViewHolder(view);
    }

    /**
     * 复用类
     */
    static class CommodityViewHolder extends RecyclerView.ViewHolder{
        TextView title,price;//标题，价钱
        ImageView image;//图片
        public CommodityViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_commodity_title);
            price = (TextView) itemView.findViewById(R.id.item_commodity_price);
            image = (ImageView) itemView.findViewById(R.id.item_commodity_image);
        }
    }
}

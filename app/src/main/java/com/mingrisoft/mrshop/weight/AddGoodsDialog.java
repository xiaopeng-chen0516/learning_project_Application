package com.mingrisoft.mrshop.weight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingrisoft.mrshop.R;
import com.mingrisoft.mrshop.db.DBStateCode;
import com.mingrisoft.mrshop.db.DaoUtils;
import com.mingrisoft.mrshop.entity.GoodDetails;
import com.mingrisoft.mrshop.listener.ImageDownLoadListener;
import com.mingrisoft.mrshop.utils.DownLoadImageUtils;
import com.mingrisoft.mrshop.utils.FileUtils;
import com.mingrisoft.mrshop.utils.FormatUtils;
import com.mingrisoft.mrshop.utils.GetViewTextUtils;
import com.mingrisoft.mrshop.utils.StaticUtils;
import com.mingrisoft.mrshop.utils.ToastUtils;
import com.mingrisoft.mrshop.weight.custom.CustomDialog;

/**
 * 作者： LYJ
 * 功能： 添加购物车弹窗
 * 创建日期： 2017/5/13
 */

public class AddGoodsDialog extends CustomDialog implements ImageDownLoadListener, View.OnClickListener {
    private GoodDetails goodDetails;//商品详情
    private String firstImageUrl;//第一张图片的网络连接
    private ImageView image;//展示图片
    private Button decision;//确定
    private TextView title;//标题
    private TextView brand;//品牌
    private TextView merchant;//品牌
    private TextView price;//价钱
    private TextView id;//商品ID
    //选择商品的数量
    private ImageButton addCount;//增加数量
    private ImageButton cutCount;//减少数量
    private EditText inputCount;//输入数量
    private int selectGoodsCounts;//选择的商品的数量
    private String imageSavePath;//图片保存的路径
    private Bitmap resultBitmap;//返回的图片
    private DaoUtils daoUtils;//操作数据库

    public AddGoodsDialog(Context context) {
        this(context, true);
    }

    public AddGoodsDialog(Context context, boolean dimEnabled) {
        super(context, dimEnabled);
    }

    @Override
    protected void onCreateView(WindowManager windowManager) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        setDialogWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setDialogGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_add_goods);
        findId();//绑定控件
        showViewMessage();//展示商品信息
    }

    /**
     * 绑定控件
     */
    private void findId() {
        image = (ImageView) findViewById(R.id.dialog_image);
        decision = (Button) findViewById(R.id.confirm);
        title = (TextView) findViewById(R.id.dialog_title);
        brand = (TextView) findViewById(R.id.dialog_brand);
        merchant = (TextView) findViewById(R.id.dialog_merchant);
        price = (TextView) findViewById(R.id.dialog_price);
        id = (TextView) findViewById(R.id.dialog_id);
        addCount = (ImageButton) findViewById(R.id.add_count);
        cutCount = (ImageButton) findViewById(R.id.cut_count);
        inputCount = (EditText) findViewById(R.id.input_count);
        decision.setOnClickListener(this);
        addCount.setOnClickListener(this);
        cutCount.setOnClickListener(this);
        /**
         * 输入框文本发生改变的监听
         */
        inputCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int messageLength = s.length();//获取输入内容的长度
                int setCount;//获取设置的数量
                switch (messageLength) {
                    case 0://数据长度为0
                        addCount.setBackgroundResource(R.drawable.add_count);//设置增加数量
                        addCount.setEnabled(true);
                        cutCount.setBackgroundResource(R.drawable.mr_cut_n);//设置减数量
                        cutCount.setEnabled(false);
                        selectGoodsCounts = 1;
                        break;
                    case 1://数据长度为1
                        selectGoodsCounts = setCount = Integer.parseInt(s.toString());//获取设置的数量
                        if (setCount <= 1) {
                            if (setCount < 1) {
                                inputCount.setText(String.valueOf(1));//设置输入框中的内容为1
                            }
                            cutCount.setBackgroundResource(R.drawable.mr_cut_n);//设置减数量
                            cutCount.setEnabled(false);
                        } else {
                            cutCount.setBackgroundResource(R.drawable.cut_count);//设置减数量
                            cutCount.setEnabled(true);
                        }
                        addCount.setBackgroundResource(R.drawable.add_count);//设置增加数量
                        addCount.setEnabled(true);
                        break;
                    case 2://数据长度为2
                        selectGoodsCounts = setCount = Integer.parseInt(s.toString());//获取设置的数量
                        if (setCount < 99) {
                            addCount.setBackgroundResource(R.drawable.add_count);//设置增加数量
                            addCount.setEnabled(true);
                        } else {
                            addCount.setBackgroundResource(R.drawable.mr_add_n);//设置增加数量
                            addCount.setEnabled(false);
                        }
                        cutCount.setBackgroundResource(R.drawable.cut_count);//设置减数量
                        cutCount.setEnabled(true);
                        break;
                }
                inputCount.setSelection(messageLength);//设置光标的位置
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 初始化界面显示效果
     */
    private void initView() {
        selectGoodsCounts = 1;//设置默认值为1
        setFocusSelectInput(false);//初始化焦点
        inputCount.setText(String.valueOf(selectGoodsCounts));//设置文本内容
        setFocusSelectInput(true);//初始化焦点
    }

    /**
     * 初始化输入框焦点
     */
    private void setFocusSelectInput(boolean isFocus) {
        inputCount.setFocusable(isFocus);
        inputCount.setFocusableInTouchMode(isFocus);
    }

    /**
     * 展示商品信息
     */
    private void showViewMessage() {
        daoUtils = new DaoUtils(getContext());
        DownLoadImageUtils.loadBitmap(getContext(), firstImageUrl, this);//显示图片
        brand.setText(StaticUtils.BRAND + goodDetails.getBrand());//显示品牌
        merchant.setText(StaticUtils.MERCHANT + goodDetails.getMerchant());//显示品牌
        price.setText(FormatUtils.getPriceText(goodDetails.getNowPrice()));//价钱
        id.setText(StaticUtils.GID + goodDetails.getId());//商品编号
        title.post(new Runnable() {//每行字体排满在这行
            @Override
            public void run() {
                GetViewTextUtils.setText(title, goodDetails.getTitle());
            }
        });
    }

    /**
     * 设置商品详情
     *
     * @param goodDetails
     */
    public void setGoodDetails(GoodDetails goodDetails) {
        this.goodDetails = goodDetails;
    }

    /**
     * 第一张图片的网址
     *
     * @param firstImageUrl
     */
    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    /**
     * 获取下载到的图片
     *
     * @param bitmap
     */
    @Override
    public void getDownLoadBitmap(Bitmap bitmap) {
        resultBitmap = bitmap;
        image.setImageBitmap(resultBitmap);
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm://确定加入购物车
                if (null == goodDetails) {
                    //数据为空关闭弹窗
                    ToastUtils.shortToast(getContext(),"加入购物车失败！");
                    dismiss();//关闭弹窗
                    return;
                }
                //保存图片的路径
                imageSavePath = FileUtils.saveBitmapToLoad(resultBitmap, goodDetails.getId());
                //判断数据是否存在并加数据保存到数据库里
                if (addSelectCountToTable(isHaveData())){
                    //增加数据
                    if (null != goodsChangeListener)goodsChangeListener.addCount(selectGoodsCounts);
                }else {
                    //数据修改失败
                    ToastUtils.shortToast(getContext(),"加入购物车失败！");
                }
                dismiss();//关闭弹窗
                break;
            case R.id.add_count://添加数量
                int addCount = selectGoodsCounts + 1;//每点击一次数量增加一个
                selectGoodsCounts = addCount > 99 ? 99 : addCount;//防止数据错误(正常来讲，这里应该是库存数量)
                inputCount.setText(String.valueOf(selectGoodsCounts));//设置显示的内容
                break;
            case R.id.cut_count://减少数量
                int cutCount = selectGoodsCounts - 1;//每点击一次数量减少一个
                selectGoodsCounts = cutCount < 1 ? 1 : cutCount;//防止数据错误
                inputCount.setText(String.valueOf(selectGoodsCounts));//设置显示的内容
                break;
        }
    }

    /**
     * 判断数据是否存在，存在就更新数据，不存在就添加数据
     *
     * @param haveData
     */
    private boolean addSelectCountToTable(boolean haveData) {
        ContentValues values = new ContentValues();
        values.put("_id", goodDetails.getId());//商品ID编号
        values.put("title", goodDetails.getTitle());//标题
        values.put("price", goodDetails.getNowPrice());//价钱
        values.put("brand", goodDetails.getBrand());//品牌
        values.put("image_url", goodDetails.getImageUrls());//图片的网址
        values.put("image", imageSavePath);//图片
        values.put("merchant", goodDetails.getMerchant());//商家
        boolean dataChange;
        if (haveData) {//修改数据库中的数据
            Cursor cursor = daoUtils.getResultCursor(StaticUtils.CART_TABLE,
                    new String[]{"count"},"_id = ?",new String[]{goodDetails.getId()},null);
            cursor.moveToFirst();//将游标移动到最开始
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            cursor.close();//关闭游标
            daoUtils.closedDB();//关闭数据库的连接
            values.put("count", selectGoodsCounts + count);//数量
            dataChange = daoUtils.update(StaticUtils.CART_TABLE, values, "_id = ?",
                    new String[]{goodDetails.getId()})
                    == DBStateCode.OPERATION_SUCCESS ? true : false;
        } else {//向数据库添加新的数据
            values.put("count", selectGoodsCounts);//数量
            dataChange = daoUtils.insert(StaticUtils.CART_TABLE, values)
                    == DBStateCode.OPERATION_SUCCESS ? true : false;
        }
        return dataChange;
    }

    /**
     * 检查数据库中是否存在指定的数据
     *
     * @return true 有 false没有
     */
    private boolean isHaveData() {
        //获取游标
        Cursor cursor = daoUtils.getResultCursor(StaticUtils.CART_TABLE,
                null, "_id = ?", new String[]{goodDetails.getId()}, null);
        boolean isHave = cursor.getCount() == 0 ? false : true;
        cursor.close();//关闭游标
        daoUtils.closedDB();//关闭数据库
        return isHave;
    }

    /**
     * 商品数量变化接口
     */
    public interface OnGoodsChangeListener {
        void addCount(int selectCount);
    }

    private OnGoodsChangeListener goodsChangeListener;//接口

    /**
     * 设置监听
     *
     * @param goodsChangeListener
     */
    public void setOnGoodsChangeListener(OnGoodsChangeListener goodsChangeListener) {
        this.goodsChangeListener = goodsChangeListener;
    }

    /**
     * 弹窗显示时
     */
    @Override
    protected void onStart() {
        super.onStart();
        initView();//初始化
    }
}

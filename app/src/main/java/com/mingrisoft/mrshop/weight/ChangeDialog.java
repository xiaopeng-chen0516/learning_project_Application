package com.mingrisoft.mrshop.weight;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mingrisoft.mrshop.R;

/**
 * 作者： LYJ
 * 功能： 这块还可以封装（可自行进行封装）
 * 创建日期： 2017/5/22
 */

public class ChangeDialog extends MineDialog implements View.OnClickListener{
    private ImageButton cutCount;//减少数量
    private ImageButton addCount;//增加数量
    private EditText inputCount;//输入数量
    private int selectGoodsCounts;//选择的商品的数量

    /**
     * 构造器
     *
     * @param context
     */
    public ChangeDialog(Context context, int count) {
        super(context);
        this.selectGoodsCounts = count;//设置默认值
        View view = LayoutInflater.from(context).inflate(R.layout.view_body_change_count, null);
        setAddView(view);//添加控件
        cutCount = (ImageButton) view.findViewById(R.id.cut_count);
        addCount = (ImageButton) view.findViewById(R.id.add_count);
        inputCount = (EditText) view.findViewById(R.id.input_count);
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
     * 设置功能
     */
    @Override
    protected void setFunction() {
        setTitleStr("修改购买数量");
        setCancelStr("取消");
        setDecisionStr("确定");
        inputCount.setText(String.valueOf(selectGoodsCounts));
        initViewShow();
        inputCount.requestFocus();
        inputCount.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    /**
     * 回调接口
     */
    @Override
    protected void wantToDo() {
        if (null != listener) {
            listener.result(selectGoodsCounts);
        }
    }
    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
     * 监听弹窗的关闭
     */
    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if(view instanceof EditText) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
        super.dismiss();
    }

    /**
     * 监听数量的变化
     */
    public interface DecisionListener {
        void result(int count);
    }

    /**
     * 接口对象
     */
    private DecisionListener listener;

    /**
     * 设置接口
     *
     * @param listener
     */
    public void setListener(DecisionListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化界面显示效果
     */
    private void initViewShow() {
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
}

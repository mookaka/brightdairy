package com.brightdairy.personal.brightdairy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/1.
 */
public class AddSubtractionBtn extends FrameLayout
{
    private int minValue;
    private int currentValue;

    private TextView addBtn;
    private TextView subtractionBtn;
    private EditText showInputNum;
    private AddSubBtnListener mAddSubBtnListener;

    public interface AddSubBtnListener
    {
        void addSubBtnClick(int curValue);
    }

    private CompositeSubscription compositeSubscription;

    public AddSubtractionBtn(Context context)
    {
        this(context, null);
    }

    public AddSubtractionBtn(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AddSubtractionBtn(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        TypedArray attrss = context.obtainStyledAttributes(attrs, R.styleable.AddSubtractionBtn);
        minValue = attrss.getInt(R.styleable.AddSubtractionBtn_min_value, 1);
        attrss.recycle();

        View addSubBtn = LayoutInflater.from(context).inflate(R.layout.add_subtraction_btn, null);
        addView(addSubBtn);
        addBtn = (TextView)addSubBtn.findViewById(R.id.txtview_btn_add);
        subtractionBtn = (TextView)addSubBtn.findViewById(R.id.txtview_btn_subtraction);
        showInputNum = (EditText)addSubBtn.findViewById(R.id.edit_input_show_num);


       setCurrentValue(minValue);
        showInputNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        initListener();
    }


    private void initListener()
    {

        compositeSubscription = new CompositeSubscription();

        compositeSubscription.add(RxView.clicks(addBtn)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        currentValue = currentValue +1;
                        showInputNum.setText(String.valueOf(currentValue));
                        subtractionBtn.setEnabled(true);

                        if(mAddSubBtnListener != null)
                        {
                            mAddSubBtnListener.addSubBtnClick(currentValue);
                        }
                    }
                }));

        compositeSubscription.add(RxView.clicks(subtractionBtn)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        currentValue = currentValue -1;

                        if(currentValue < minValue)
                        {
                            currentValue = minValue;
                            subtractionBtn.setEnabled(false);
                        }

                        if(mAddSubBtnListener != null)
                        {
                            mAddSubBtnListener.addSubBtnClick(currentValue);
                        }

                        showInputNum.setText(String.valueOf(currentValue));
                    }
                }));

        compositeSubscription.add(RxTextView.textChanges(showInputNum)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence charSequence)
                    {

                        if(TextUtils.isEmpty(charSequence))
                        {
                            charSequence = "1";
                            showInputNum.setText(charSequence);
                        }

                        currentValue = Integer.parseInt(String.valueOf(charSequence));

                    }
                }));

    }

    public void setAddSubBtnListener(AddSubBtnListener addSubBtnListener)
    {
        this.mAddSubBtnListener = addSubBtnListener;
    }

    public void unSubscribe()
    {
        if(compositeSubscription != null && compositeSubscription.hasSubscriptions())
        {
            compositeSubscription.unsubscribe();
        }
    }

    public void setCurrentValue(int currentValue)
    {
        this.currentValue = currentValue;
        showInputNum.setText(String.valueOf(currentValue));
    }

    public int  getCurrentValue()
    {
        return currentValue;
    }
}

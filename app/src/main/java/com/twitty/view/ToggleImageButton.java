package com.twitty.view;

import com.twitty.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

public class ToggleImageButton extends ImageButton implements Checkable {

    public interface OnCheckedChangeListener {
        void onCheckedChanged(ToggleImageButton button, boolean isChecked);
    }

    private OnCheckedChangeListener listener;

    public ToggleImageButton(Context context) {
        super(context);
    }

    public ToggleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToggleImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleImageButton);
        setChecked(typedArray.getBoolean(R.styleable.ToggleImageButton_android_checked, false));
        typedArray.recycle();
    }

    @Override public void setChecked(boolean checked) {
        setSelected(checked);

        if (listener != null) {
            listener.onCheckedChanged(this, checked);
        }
    }

    @Override public boolean isChecked() {
        return isSelected();
    }

    @Override public void toggle() {
        setSelected(!isSelected());
    }

    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}

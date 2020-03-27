package cn.yuxirey.codeverifyeditext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

/**
 * 验证码输入框
 *
 * @author Rey
 * created on 2020/3/25 2:48 PM
 */
public class CodeVerifyEditText extends View {
    private final static int DEFAULT_LINE_HEIGHT = 1;
    private final static int DEFAULT_LINE_MARGIN_TOP = 16;
    private final static int DEFAULT_INACTIVE_COLOR = Color.parseColor("#999999");
    private final static int DEFAULT_ACTIVE_COLOR = Color.parseColor("#333333");

    @ColorInt
    private int mTextInactiveColor;

    @ColorInt
    private int mTextActiveColor;

    private int mTextSize;

    @IntRange(from = 4, to = 6)
    private int mMaxLength;

    private Paint mPaint;

    @Nullable
    private String mCodeStr;

    @Nullable
    private OnCompleteListener mCompleteListener;

    @ColorInt
    private int mLineInactiveColor;

    @ColorInt
    private int mLineActiveColor;

    private int mLineMarginTop;
    private int mLIneHeight;
    private int mLineWidth;

    private int mTextWidth;

    private boolean mOnlyShownLine = false;


    public CodeVerifyEditText(Context context) {
        this(context, null);
    }

    public CodeVerifyEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeVerifyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CodeVerifyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CodeVerifyEditText);
        mMaxLength = typedArray.getInt(R.styleable.CodeVerifyEditText_cvedt_maxLength, 4);
        mMaxLength = mMaxLength > 6 ? 6 : mMaxLength;
        mMaxLength = mMaxLength < 4 ? 4 : mMaxLength;
        mCodeStr = typedArray.getString(R.styleable.CodeVerifyEditText_cvedt_text);
        if (!TextUtils.isEmpty(mCodeStr) && mCodeStr.length() > mMaxLength) {
            mCodeStr = mCodeStr.substring(0, mMaxLength);
        }
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CodeVerifyEditText_cvedt_textSize, toPx(30));
        mTextInactiveColor = typedArray.getColor(R.styleable.CodeVerifyEditText_cvedt_textInactiveColor, DEFAULT_INACTIVE_COLOR);
        mTextActiveColor = typedArray.getColor(R.styleable.CodeVerifyEditText_cvedt_textActiveColor, DEFAULT_ACTIVE_COLOR);
        mLineInactiveColor = typedArray.getColor(R.styleable.CodeVerifyEditText_cvedt_lineInactiveColor, DEFAULT_INACTIVE_COLOR);
        mLineActiveColor = typedArray.getColor(R.styleable.CodeVerifyEditText_cvedt_lineActiveColor, DEFAULT_ACTIVE_COLOR);
        mLIneHeight = typedArray.getDimensionPixelSize(R.styleable.CodeVerifyEditText_cvedt_lineHeight, toPx(DEFAULT_LINE_HEIGHT));
        mLineMarginTop = typedArray.getDimensionPixelSize(R.styleable.CodeVerifyEditText_cvedt_lineMarginTop, toPx(DEFAULT_LINE_MARGIN_TOP));
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.CodeVerifyEditText_cvedt_lineWidth, toPx(24));
        mOnlyShownLine = typedArray.getBoolean(R.styleable.CodeVerifyEditText_cvedt_onlyShownLine, false);
        typedArray.recycle();
        initPaint();
        mTextWidth = getWordWidth();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas, mCodeStr);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new CodeVerifyInputConnection(this, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //InputMethodManager来控制输入法弹起和缩回。
            requestFocus();
            InputMethodManager m = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (m != null) {
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setMaxLength(@IntRange(from = 4, to = 6) int length) {
        mMaxLength = length;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setCode(int code) {
        setCode(String.valueOf(code));
    }

    public void setCode(String code) {
        mCodeStr = code;
    }

    public int getCode() {
        return TextUtils.isEmpty(mCodeStr) ? 0 : Integer.parseInt(mCodeStr);
    }

    public String getCodeString() {
        return mCodeStr;
    }


    public void setTextActiveColor(@ColorInt int color) {
        mTextActiveColor = color;
    }

    public void setTextInactiveColor(@ColorInt int color) {
        mTextInactiveColor = color;
    }

    public void setLineActiveColor(@ColorInt int color) {
        mLineActiveColor = color;
    }

    public void setLineMarginTop(int topMargin) {
        mLineMarginTop = topMargin;
    }

    public void setLineHeight(int lineHeight) {
        mLIneHeight = lineHeight;
    }

    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
    }

    public void setLineInactiveColor(@ColorInt int color) {
        mLineInactiveColor = color;
    }

    public void setOnlyShownLine(boolean isOnlyDrawShownLine) {
        mOnlyShownLine = isOnlyDrawShownLine;
    }

    public void setOnCompleteListener(@Nullable OnCompleteListener listener) {
        mCompleteListener = listener;
    }


    public interface OnCompleteListener {
        void onComplete(String code);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    private int computeWidth() {
        return getPaddingLeft() + getPaddingRight() + computeTextWidth();
    }

    private int computeHeight() {
        return getPaddingTop() + getPaddingBottom() + computeTextHeight() + mLineMarginTop + mLIneHeight;
    }

    private int computeTextHeight() {
        final Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        return fmi.descent - fmi.ascent;
    }

    private int computeTextWidth() {
        if (!TextUtils.isEmpty(mCodeStr)) {
            return (int) mPaint.measureText(mCodeStr);
        }
        return Math.max(mLineWidth, mTextSize);
    }

    private int getBlockWidth() {
        return getWidth() / mMaxLength;
    }

    private int getWordWidth() {
        final Rect rectF = new Rect();
        mPaint.getTextBounds("0", 0, 1, rectF);
        return rectF.right - rectF.left;
    }


    private int measureWidth(int widthMeasureSpec) {
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int resultWidth = measureWidth;
        switch (measureMode) {

            case MeasureSpec.AT_MOST:
                resultWidth = computeWidth();
                break;

            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                resultWidth = measureWidth;
                break;

            default:
                break;
        }
        return resultWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int measureMode = MeasureSpec.getMode(heightMeasureSpec);
        int resultHeight = measureHeight;
        switch (measureMode) {

            case MeasureSpec.AT_MOST:
                resultHeight = computeHeight();
                break;

            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                resultHeight = measureHeight;
                break;

            default:
                break;
        }
        return resultHeight;
    }

    private void drawText(Canvas canvas, String text) {
        final int blockWidth = getBlockWidth();
        final int textBaseLine = getDrawTextBaseLinePosition();
        int startX = (blockWidth - mTextWidth) >> 1;
        final int len = mOnlyShownLine ? TextUtils.isEmpty(mCodeStr) ? 0 : mCodeStr.length() : mMaxLength;
        for (int i = 0; i < len; i++) {
            if (!TextUtils.isEmpty(mCodeStr) && i < mCodeStr.length()) {
                mPaint.setColor(i == mCodeStr.length() - 1 ? mTextActiveColor : mTextInactiveColor);
                canvas.drawText(String.valueOf(text.charAt(i)), startX, textBaseLine, mPaint);
            }
            drawUnderline(canvas, startX + mTextWidth / 2, textBaseLine, mLineWidth, !TextUtils.isEmpty(text) && i == text.length() - 1);
            startX += blockWidth;
        }
    }

    private void drawUnderline(Canvas canvas, int centerX, int textBaseLine, int lineWidth, boolean isActive) {
        final int linePosition = textBaseLine + mLineMarginTop;
        mPaint.setColor(isActive ? mLineActiveColor : mLineInactiveColor);
        mPaint.setStrokeWidth(mLIneHeight);
        canvas.drawLine(centerX - lineWidth / 2f, linePosition, centerX + lineWidth / 2f, linePosition, mPaint);
    }

    private int getDrawTextBaseLinePosition() {
        return getHeight() / 2 - mLineMarginTop - mLIneHeight + computeTextHeight() / 2;
    }


    private class CodeVerifyInputConnection extends BaseInputConnection {
        private final StringBuilder mBuilder = new StringBuilder(mMaxLength);

        CodeVerifyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
            mBuilder.append(mCodeStr);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            final boolean notCommitText = TextUtils.isEmpty(text) || !TextUtils.isDigitsOnly(text) || mBuilder.length() >= mMaxLength;
            if (notCommitText) {
                return false;
            }
            mBuilder.append(text);
            mCodeStr = mBuilder.toString();
            if (mBuilder.length() == mMaxLength) {
                if (mCompleteListener != null) {
                    mCompleteListener.onComplete(mCodeStr);
                }
            }
            invalidate();
            return true;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (TextUtils.isEmpty(mCodeStr) || afterLength < 0) {
                return false;
            }
            mBuilder.deleteCharAt(afterLength);
            mCodeStr = mBuilder.toString();
            invalidate();
            return true;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() != 0)
                return false;
            if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                return deleteSurroundingText(mBuilder.length(), mBuilder.length() - 1);
            }
            return commitText(String.valueOf(event.getKeyCode() - 7), mMaxLength);
        }


        @Override
        public void closeConnection() {
            clearBuilder();
            super.closeConnection();
        }

        private void clearBuilder() {
            if (mBuilder.length() > 0) {
                mBuilder.delete(0, mBuilder.length());
            }
        }
    }

    private int toPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

}

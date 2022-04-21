package com.dz.dzbook.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.dz.dzbook.R;

import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

/**
 * @author wj
 * Date 2022/4/18 3:46 下午
 * 流式布局
 */
public class JFFlowLayout extends ViewGroup {
    public static final int LINE_NO_LIMIT = -1;

    private int lineSpacing;
    private int itemSpacing;
    private boolean singleLine;
    private int maxLine;
    //是否超过
    private boolean isExceedingMaxLimit;

    public JFFlowLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public JFFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JFFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.loadFromAttributes(context, attrs);
    }

    @TargetApi(21)
    public JFFlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.loadFromAttributes(context, attrs);
    }

    private void loadFromAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JFFlowLayout, 0, 0);
        this.lineSpacing = array.getDimensionPixelSize(R.styleable.JFFlowLayout_lineSpacing, 0);
        this.itemSpacing = array.getDimensionPixelSize(R.styleable.JFFlowLayout_itemSpacing, 0);
        this.singleLine = array.getBoolean(R.styleable.JFFlowLayout_singleLine, false);
        this.maxLine = array.getInt(R.styleable.JFFlowLayout_maxLine, LINE_NO_LIMIT);
        array.recycle();
    }

    protected int getLineSpacing() {
        return this.lineSpacing;
    }

    protected void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    protected int getItemSpacing() {
        return this.itemSpacing;
    }

    protected void setItemSpacing(int itemSpacing) {
        this.itemSpacing = itemSpacing;
    }

    protected boolean isSingleLine() {
        return this.singleLine;
    }

    public void setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int maxWidth = widthMode != MeasureSpec.AT_MOST && widthMode != MeasureSpec.EXACTLY ? 2147483647 : width;
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();
        int childBottom = childTop;
        int maxChildRight = 0;
        int maxRight = maxWidth - this.getPaddingRight();

        int finalWidth;
        int line = 1;
        for (finalWidth = 0; finalWidth < this.getChildCount(); ++finalWidth) {
            View child = this.getChildAt(finalWidth);
            if (child.getVisibility() != GONE) {
                this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
                LayoutParams lp = child.getLayoutParams();
                int leftMargin = 0;
                int rightMargin = 0;
                if (lp instanceof MarginLayoutParams) {
                    MarginLayoutParams marginLp = (MarginLayoutParams) lp;
                    leftMargin += marginLp.leftMargin;
                    rightMargin += marginLp.rightMargin;
                }

                int childRight = childLeft + leftMargin + child.getMeasuredWidth();
                if (childRight > maxRight && !this.isSingleLine()) {
                    childLeft = this.getPaddingLeft();
                    childTop = childBottom + this.lineSpacing;
                    line++;
                    if (maxLine > 0 && line > maxLine) {
                        //最大行
                        break;
                    }
                }

                childRight = childLeft + leftMargin + child.getMeasuredWidth();
                childBottom = childTop + child.getMeasuredHeight();
                if (childRight > maxChildRight) {
                    maxChildRight = childRight;
                }

                childLeft += leftMargin + rightMargin + child.getMeasuredWidth() + this.itemSpacing;
            }
        }
        //是否超过最大行数
        if (maxLine > 0 && line > maxLine) {
            isExceedingMaxLimit = true;
        } else {
            isExceedingMaxLimit = false;
        }

        finalWidth = getMeasuredDimension(width, widthMode, maxChildRight);
        int finalHeight = getMeasuredDimension(height, heightMode, childBottom);
        this.setMeasuredDimension(finalWidth, finalHeight);
    }

    private static int getMeasuredDimension(int size, int mode, int childrenEdge) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
                return Math.min(childrenEdge, size);
            case MeasureSpec.EXACTLY:
                return size;
            default:
                return childrenEdge;
        }
    }

    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        if (this.getChildCount() != 0) {
            boolean isRtl = ViewCompat.getLayoutDirection(this) ==  ViewCompat.LAYOUT_DIRECTION_RTL;
            int paddingStart = isRtl ? this.getPaddingRight() : this.getPaddingLeft();
            int paddingEnd = isRtl ? this.getPaddingLeft() : this.getPaddingRight();
            int childStart = paddingStart;
            int childTop = this.getPaddingTop();
            int childBottom = childTop;
            int maxChildEnd = right - left - paddingEnd;

            int line = 1;
            for (int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() != GONE) {
                    LayoutParams lp = child.getLayoutParams();
                    int startMargin = 0;
                    int endMargin = 0;
                    if (lp instanceof MarginLayoutParams) {
                        MarginLayoutParams marginLp = (MarginLayoutParams) lp;
                        startMargin = MarginLayoutParamsCompat.getMarginStart(marginLp);
                        endMargin = MarginLayoutParamsCompat.getMarginEnd(marginLp);
                    }

                    int childEnd = childStart + startMargin + child.getMeasuredWidth();
                    if (!this.singleLine && childEnd > maxChildEnd) {
                        childStart = paddingStart;
                        childTop = childBottom + this.lineSpacing;
                        line++;
                        if (maxLine > 0 && line > maxLine) {
                            break;
                        }
                    }

                    childEnd = childStart + startMargin + child.getMeasuredWidth();
                    childBottom = childTop + child.getMeasuredHeight();
                    if (isRtl) {
                        child.layout(maxChildEnd - childEnd, childTop, maxChildEnd - childStart - startMargin, childBottom);
                    } else {
                        child.layout(childStart + startMargin, childTop, childEnd, childBottom);
                    }

                    childStart += startMargin + endMargin + child.getMeasuredWidth() + this.itemSpacing;
                }
            }

        }
    }


    /**
     * 设置的数据是否超过了最大限制
     *
     * @return
     */
    public boolean isExceedingMaxLimit() {
        return isExceedingMaxLimit;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        requestLayout();
    }
}

package info.smemo.swiperecyclerview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 支持左滑右滑菜单的Holder
 * Created by suzhenpeng on 2015/7/7.
 */
public abstract class RecyclerViewDragHolder {
    /**
     * 左滑菜单
     */
    public static final int EDGE_LEFT = 1;
    /**
     * 右滑菜单
     */
    public static final int EDGE_RIGHT = 2;
    /**
     * 默认右滑菜单
     */
    private int mTrackingEdges = EDGE_RIGHT;
    /**
     * 背后布局
     */
    private View bgView;
    /**
     * 主布局
     */
    private View topView;
    /**
     * 拖动布局
     */
    protected DragLinearLayout itemView;
    /**
     * 上下文对象
     */
    private Context context;

    private DragViewHolder dragViewHolder;


    public RecyclerViewDragHolder(Context context, View bgView, View topView) {
        this.bgView = bgView;
        this.topView = topView;
        this.context = context;
        init();
    }

    public RecyclerViewDragHolder(Context context, View bgView, View topView, int mTrackingEdges) {
        this.mTrackingEdges = mTrackingEdges;
        this.bgView = bgView;
        this.topView = topView;
        this.context = context;
        init();
    }

    private void init() {
        itemView = new DragLinearLayout(context);
        itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        itemView.setmTrackingEdges(mTrackingEdges);
        itemView.initView(topView, bgView);
        dragViewHolder = new DragViewHolder(this, itemView);
    }

    public void close() {
        itemView.close();
    }

    public void open() {
        itemView.open();
    }

    public boolean isOpen() {
        return itemView.state == DragLinearLayout.STATE_OPNE;
    }

    public DragViewHolder getDragViewHolder() {
        return dragViewHolder;
    }

    /**
     * 初始化控件
     *
     * @param itemView
     */
    public abstract void initView(View itemView);

    /**
     * 获取this
     *
     * @param viewHolder
     * @return
     */
    public static RecyclerViewDragHolder getHolder(RecyclerView.ViewHolder viewHolder) {
        return ((DragViewHolder) viewHolder).holder;
    }

    class DragViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewDragHolder holder;

        public DragViewHolder(RecyclerViewDragHolder holder, View itemView) {
            super(itemView);
            this.holder = holder;
            initView(itemView);

        }
    }

    private class DragLinearLayout extends FrameLayout {

        private ViewDragHelper mDragHelper;

        /**
         * 展示类
         */
        private View topView;
        /**
         * 背景菜单类
         */
        private View bgView;
        /**
         * 左侧起点
         */
        private int viewX;
        /**
         * 背景大小
         */
        private int bgWidth;
        /**
         * 主界面宽度
         */
        private int topWidth;
        /**
         * 默认右滑菜单
         */
        private int mTrackingEdges = EDGE_RIGHT;
        /**
         * 滑动百分比，超过百分比松开自动完成，不到则还原
         */
        private float scrollPercent = 0.2f;
        /**
         * 当前是否打开
         */
        protected int state = STATE_CLOSE;
        /**
         * 打开
         */
        protected static final int STATE_OPNE = 1;
        /**
         * 关闭
         */
        protected static final int STATE_CLOSE = 2;

        public DragLinearLayout(Context context) {
            super(context);
            init();
        }

        public DragLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DragLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public void initView(View topView, View bgView) {
            this.topView = topView;
            this.bgView = bgView;
//            viewX = topView.getLeft();
            viewX = 0;
            addView(createBgView(bgView));
            addView(topView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (widthMeasureSpec != 0) {
                bgWidth = bgView.getWidth();
                topWidth = topView.getWidth();
            }
        }

        /**
         * 生成背景
         *
         * @param view
         * @return
         */
        private View createBgView(View view) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setGravity(mTrackingEdges == EDGE_RIGHT ? Gravity.RIGHT : Gravity.LEFT);
            linearLayout.addView(view);
            return linearLayout;
        }

        private void init() {
            mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallBack());
            mDragHelper.setEdgeTrackingEnabled(mTrackingEdges == EDGE_RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
        }

        private class ViewDragCallBack extends ViewDragHelper.Callback {

            public ViewDragCallBack() {
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == topView;
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mDragHelper.captureChildView(topView, pointerId);
                if (bgWidth != 0)
                    bgView.setVisibility(View.GONE);

            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (left != viewX) {
                    if (bgView.getVisibility() == View.GONE)
                        bgView.setVisibility(View.VISIBLE);
                } else {
                    if (bgView.getVisibility() == View.VISIBLE)
                        bgView.setVisibility(View.GONE);

                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild != topView)
                    return;
                int newLeft;
                if (mTrackingEdges == EDGE_LEFT) {
                    if (topView.getLeft() < (int) ((float) bgWidth * scrollPercent) || state == STATE_OPNE) {
                        newLeft = viewX;
                        state = STATE_CLOSE;
                    } else {
                        newLeft = bgWidth;
                        state = STATE_OPNE;
                    }
                } else {
                    if (topView.getLeft() > -(int) ((float) bgWidth * scrollPercent) || state == STATE_OPNE) {
                        newLeft = viewX;
                        state = STATE_CLOSE;
                    } else {
                        newLeft = -1 * bgWidth;
                        state = STATE_OPNE;
                    }
                }
                if (mDragHelper.smoothSlideViewTo(topView, newLeft, 0)) {
                    ViewCompat.postInvalidateOnAnimation(DragLinearLayout.this);
                }
                invalidate();
            }

            /**
             * 水平方向处理
             *
             * @param child 被拖动到view
             * @param left  移动到达的x轴的距离
             * @param dx    建议的移动的x距离
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (mTrackingEdges == EDGE_LEFT) {
                    if (left > bgWidth && dx > 0) return bgWidth;
                    if (left < 0 && dx < 0) return 0;
                } else {
                    if (left > 0 && dx > 0) return 0;
                    if (left < -bgWidth && dx < 0) return -bgWidth;
                }
                return left;
            }
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (state == STATE_CLOSE)
                bgView.setVisibility(View.GONE);
            return mDragHelper.shouldInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mDragHelper.processTouchEvent(event);
            return true;
        }

        @Override
        public void computeScroll() {
            super.computeScroll();
            if (mDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        public void setmTrackingEdges(int mTrackingEdges) {
            this.mTrackingEdges = mTrackingEdges;
            mDragHelper.setEdgeTrackingEnabled(mTrackingEdges == EDGE_RIGHT ? ViewDragHelper.EDGE_RIGHT : ViewDragHelper.EDGE_LEFT);
        }

        public void open() {
            int newLeft;
            if (mTrackingEdges == EDGE_LEFT) {
                newLeft = bgWidth;
            } else {
                newLeft = -1 * bgWidth;
            }
            state = STATE_OPNE;
            if (mDragHelper.smoothSlideViewTo(topView, newLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(DragLinearLayout.this);
            }
            invalidate();
        }

        public void close() {
            state = STATE_CLOSE;
            if (mDragHelper.smoothSlideViewTo(topView, viewX, 0)) {
                ViewCompat.postInvalidateOnAnimation(DragLinearLayout.this);
            }
            invalidate();
        }
    }
}

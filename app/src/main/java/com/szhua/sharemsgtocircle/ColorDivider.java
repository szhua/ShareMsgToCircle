package com.szhua.sharemsgtocircle;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by 单志华 on 2016/10/26.
 */
public class ColorDivider extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private final int mSize;
    private final int mOrientation;

    public ColorDivider(Resources resources, @ColorRes int color,
                                    int size, int orientation) {
        mDivider = new ColorDrawable(resources.getColor(color));
        mSize = size;
        mOrientation = orientation;
    }

    //// TODO: 2016/10/26
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left;
        int right;
        int top;
        int bottom;
//            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
//                top = parent.getP addingTop();
//                bottom = parent.getHeight() - parent.getPaddingBottom();
//                final int childCount = parent.getChildCount();
//                for (int i = 0; i < childCount - 1; i++) {
//                    final View child = parent.getChildAt(i);
//                    final RecyclerView.LayoutParams params =
//                            (RecyclerView.LayoutParams) child.getLayoutParams();
//                    left = child.getRight() + params.rightMargin;
//                    right = left + mSize;
//                    mDivider.setBounds(left, top, right, bottom);
//                    mDivider.draw(c);
//                }
//            } else {
        //vertical
        top = parent.getPaddingTop();
        bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            left = child.getRight() + params.rightMargin;
            right=left+mSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        //horizontal;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        //      }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, mSize, 0);
        } else {
            if(itemPosition%2==1){
                outRect.set(0,0,0, mSize);
            }else {
                outRect.set(0, 0,mSize, mSize);
            }
        }
    }

//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//            super.getItemOffsets(outRect, view, parent, state);
//        }

// TODO: 2016/10/26  why itdoesn't work ;
//        @Override
//        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//         //  super.getItemOffsets(outRect, view, parent, state);
////            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
////                outRect.set(0, 0, mSize, 0);
////            } else {
//                Log.i("leilei","pos:"+((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition());
//                int pos =((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition() ;
//                if((pos/2)==1){
//                    outRect.set(0,0,0, mSize*2);
//                }else {
//                    outRect.set(0, 0,mSize, mSize*8);
//                }
//
//          //  }
//
//        }
}


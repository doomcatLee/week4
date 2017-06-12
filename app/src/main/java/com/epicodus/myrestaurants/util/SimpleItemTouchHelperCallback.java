package com.epicodus.myrestaurants.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

/**
 * Created by Guest on 6/12/17.
 */

public class SimpleItemTouchHelperCallback extends android.support.v7.widget.helper.ItemTouchHelper.Callback{
    private final com.epicodus.myrestaurants.util.ItemTouchHelper mAdapter;

    public SimpleItemTouchHelperCallback(com.epicodus.myrestaurants.util.ItemTouchHelper adapter){
        mAdapter = adapter;
    }
    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }
    @Override
    public boolean isItemViewSwipeEnabled(){
        return true;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target){
        if (source.getItemViewType() != target.getItemViewType()){
            return false;
        }
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i){
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

}

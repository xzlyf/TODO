package com.xz.todolist.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.xz.todolist.base.BaseRecyclerAdapter;
import com.xz.todolist.base.BaseRecyclerViewHolder;
import com.xz.todolist.entity.Event;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/16
 */
public class EventAdapter extends BaseRecyclerAdapter<Event> {
	public EventAdapter(Context context) {
		super(context);
	}

	@Override
	protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {

	}

	@Override
	protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
		return null;
	}
}

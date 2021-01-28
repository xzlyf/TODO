package com.xz.todolist.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.TimeUtils;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseRecyclerAdapter;
import com.xz.todolist.base.BaseRecyclerViewHolder;
import com.xz.todolist.entity.Event;
import com.xz.todolist.utils.DateFormat;
import com.xz.utils.appUtils.TimeUtil;

import butterknife.BindView;

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
		ViewHolder viewHolder = (ViewHolder) holder;
		Event entity = mList.get(position);
		viewHolder.tvTitle.setText(entity.getShortTitle());
		viewHolder.tvContent.setText(entity.getContent());
		if (entity.getRemindTime() == null) {
			viewHolder.remindView.setVisibility(View.GONE);
		} else {
			viewHolder.remindTime.setText(TimeUtil.getSimMilliDate(DateFormat.STANDARD_DEC, entity.getRemindTime().getTime()));
		}

	}

	@Override
	protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_event, parent, false));
	}


	static class ViewHolder extends BaseRecyclerViewHolder {
		@BindView(R.id.tv_title)
		TextView tvTitle;
		@BindView(R.id.remind_time)
		TextView remindTime;
		@BindView(R.id.remind_view)
		LinearLayout remindView;
		@BindView(R.id.tv_content)
		TextView tvContent;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
		}
	}
}

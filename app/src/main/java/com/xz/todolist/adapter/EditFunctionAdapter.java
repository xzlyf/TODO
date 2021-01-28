package com.xz.todolist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xz.todolist.R;
import com.xz.todolist.base.BaseRecyclerAdapter;
import com.xz.todolist.base.BaseRecyclerViewHolder;
import com.xz.todolist.entity.EditFunction;

import butterknife.BindView;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2021/1/28
 */
public class EditFunctionAdapter extends BaseRecyclerAdapter<EditFunction> {


	public EditFunctionAdapter(Context context) {
		super(context);
	}

	@Override
	protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
		ViewHolder viewHolder = (ViewHolder) holder;
		viewHolder.itemImg.setImageResource(mList.get(position).getFunctionImg());
		viewHolder.itemTips.setText(mList.get(position).getFunctionName());
	}

	@Override
	protected ViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(mInflater.inflate(R.layout.item_function, parent, false));
	}

	static class ViewHolder extends BaseRecyclerViewHolder {
		@BindView(R.id.item_img)
		ImageView itemImg;
		@BindView(R.id.item_tips)
		TextView itemTips;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
		}
	}

}

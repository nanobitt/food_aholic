package com.vpapps.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.vpapps.Foodaholic.R;

import java.util.List;

public class AdapterFaqExpandable extends ExpandableRecyclerViewAdapter<AdapterFaqExpandable.QuesViewHolder, AdapterFaqExpandable.AnsViewHolder> {


    public AdapterFaqExpandable(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public QuesViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ques, parent, false);

        return new QuesViewHolder(itemView);
    }

    @Override
    public AnsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ans, parent, false);

        return new AnsViewHolder(itemView);
    }

    @Override
    public void onBindChildViewHolder(AnsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        holder.tv_faq_ans.setText((String)group.getItems().get(childIndex));
    }

    @Override
    public void onBindGroupViewHolder(QuesViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tv_faq_ques.setText(group.getTitle());

    }

    class QuesViewHolder extends GroupViewHolder
    {
        private TextView tv_faq_ques;
        private ImageView imageView_arrow;

        QuesViewHolder(View view) {
            super(view);
            tv_faq_ques = view.findViewById(R.id.tv_faq_ques);
            imageView_arrow = view.findViewById(R.id.iv_menucat_arrow);
        }

        @Override
        public void expand() {
            imageView_arrow.animate().rotation(90).setDuration(250).setInterpolator(new OvershootInterpolator());
        }

        @Override
        public void collapse() {
            imageView_arrow.animate().rotation(0).setDuration(250).setInterpolator(new OvershootInterpolator());
        }
    }

    class AnsViewHolder extends ChildViewHolder
    {
        private TextView tv_faq_ans;

        public AnsViewHolder(View itemView) {
            super(itemView);
            tv_faq_ans = itemView.findViewById(R.id.tv_faq_ans);
        }
    }

}

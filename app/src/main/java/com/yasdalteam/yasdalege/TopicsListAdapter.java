package com.yasdalteam.yasdalege;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Topic> topics;
    private Listener listener;
    private Subject subject;

    public TopicsListAdapter(Listener listener, List<Topic> topics, Subject subject)
    {
        this.topics = topics;
        this.subject = subject;
        if (listener instanceof Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public TopicsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_topics_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicsListAdapter.ViewHolder holder, int position)
    {
        Topic topic = topics.get(position);
        if (subject.getId() == 64 || subject.getName().equals("Английский язык"))
        {
            holder.topicTitleView.setText(topic.getTitle());
        }
        else
        {
            holder.topicTitleView.setText(topic.getNumber() + ". " + topic.getTitle());
        }
    }

    @Override
    public int getItemCount()
    {
        return topics.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position, long topicId, long number);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvTopicTitle) TextView topicTitleView;
        private View view;

        ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            this.view = view;
        }

        @Override
        public void onClick(View view)
        {   Topic topic = topics.get(getAdapterPosition());
            listener.OnViewHolderClick(getAdapterPosition(), topic.getId(), topic.getNumber());
        }
    }
}

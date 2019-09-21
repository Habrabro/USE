package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.use.Networking.ExerciseDatum;
import com.example.use.Networking.SubjectDatum;
import com.example.use.Networking.Topic;
import com.example.use.Networking.TopicDatum;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<TopicDatum> topics;
    private Listener listener;

    TopicsListAdapter(Listener listener, List<TopicDatum> topics)
    {
        this.topics = topics;
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
        TopicDatum topic = topics.get(position);
        holder.topicNumberView.setText(Long.toString(topic.getNumber()));
        holder.topicTitleView.setText(topic.getTitle());
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
        @BindView(R.id.tvTopicNumber) TextView topicNumberView;
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
        {   TopicDatum topic = topics.get(getAdapterPosition());
            listener.OnViewHolderClick(getAdapterPosition(), topic.getId(), topic.getNumber());
        }
    }
}

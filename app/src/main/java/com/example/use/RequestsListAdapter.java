package com.example.use;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Request> requests;
    private Listener listener;

    public RequestsListAdapter(RequestsListAdapter.Listener listener, List<Request> requests)
    {
        this.requests = requests;
        if (listener instanceof RequestsListAdapter.Listener)
        {
            this.listener = listener;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.requests_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Request request = requests.get(position);
        ((ViewHolder)holder).bindRequest(request);
    }

    @Override
    public int getItemCount()
    {
        return requests.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private View view;
        private Request request;

        @BindView(R.id.tvRequestId)
        TextView tvRequestId;
        @BindView(R.id.tvRequestTitle)
        TextView tvRequestTitle;
        @BindView(R.id.tvAttachmentsDesc)
        TextView tvAttachmentsDesc;
        @BindView(R.id.tvSentDesc)
        TextView tvSentDesc;
        @BindView(R.id.tvStatusDesc)
        TextView tvStatusDesc;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        public void bindRequest(Request request)
        {
            this.request = request;

            int filesCount = 0;
            Pattern pattern = Pattern.compile("\\n");
            Matcher matcher = pattern.matcher(request.getAttachment());
            while (matcher.find())
            {
                filesCount++;
            }

            String hasText = request.getText() != null ? ", текст" : "";

            tvRequestId.setText("Запрос № " + request.getId());
            tvRequestTitle.setText(request.getSubjectName() + " – Задание " + request.getTopicNumber());
            tvAttachmentsDesc.setText("файлы(" + filesCount + ")" + hasText);
            tvSentDesc.setText(request.getTStamp());
            tvStatusDesc.setText(request.getStatus());
        }
    }

    public interface Listener
    {
        void OnViewHolderClick(int position, long topicId, long number);
    }

    public enum RequestStatuses
    {
        AWAITING("awaiting", "#949494"),
        CHECKING("checking", "#7462CE"),
        CHECKED("checked", "#2DE17C"),
        REJECTED("rejected", "#FAB03C");

        private String value;
        private String color;

        RequestStatuses(String value, String color)
        {
            this.value = value;
            this.color = color;
        }
    }
}

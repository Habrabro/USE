package com.example.use;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Request> requests;
    private Listener listener;
    private HashMap<String, RequestStatuses> statusesMap = new HashMap<>();

    public RequestsListAdapter(RequestsListAdapter.Listener listener, List<Request> requests)
    {
        this.requests = requests;
        if (listener instanceof RequestsListAdapter.Listener)
        {
            this.listener = listener;
        }
        statusesMap.put("awaiting", RequestStatuses.AWAITING);
        statusesMap.put("checking", RequestStatuses.CHECKING);
        statusesMap.put("checked", RequestStatuses.CHECKED);
        statusesMap.put("rejected", RequestStatuses.REJECTED);
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

        public Bundle getBundle()
        {
            return bundle;
        }

        Bundle bundle = new Bundle();

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

            Pattern pattern = Pattern.compile("\\\\n");
            String[] attachments = request.getAttachment().split(pattern.toString());
            int filesCount = attachments.length;
            if (attachments[0].equals("")) { filesCount = 0; }

            String hasText = !request.getText().isEmpty() ? ", текст" : "";

            String requestId = "Запрос № " + request.getId();
            String requestTitle = request.getSubjectName() + " – Задание " + request.getTopicNumber();
            String attachmentsDesc = "файлы(" + filesCount + ")" + hasText;
            String sentDesc = request.getTStamp();
            String status = statusesMap.get(request.getStatus()).value;
            int statusColor = Color.parseColor(statusesMap.get(request.getStatus()).color);
            String text = request.getText();
            String message = request.getMessage();

            final SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
            Date date = new Date();
            serverDateFormat.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));
            try { date = serverDateFormat.parse(sentDesc); }
            catch (ParseException exception) {}
            dateFormat.setTimeZone(TimeZone.getDefault());
            String dateTime = dateFormat.format(date);

            tvRequestId.setText(requestId);
            tvRequestTitle.setText(requestTitle);
            tvAttachmentsDesc.setText(attachmentsDesc);
            tvSentDesc.setText(dateTime);
            tvStatusDesc.setText(status);
            tvStatusDesc.setTextColor(statusColor);

            bundle.putString("requestId", requestId);
            bundle.putString("requestTitle", requestTitle);
            bundle.putString("attachmentsDesc", attachmentsDesc);
            bundle.putString("sentDesc", dateTime);
            bundle.putString("status", status);
            bundle.putInt("statusColor", statusColor);
            bundle.putStringArray("attachments", attachments);
            bundle.putString("text", text);
            bundle.putString("message", message);

            view.setOnClickListener(view ->
            {
                listener.OnViewHolderClick(bundle);
            });
        }
    }

    public interface Listener
    {
        void OnViewHolderClick(Bundle bundle);
    }

    public enum RequestStatuses
    {
        AWAITING("ОЖИДАЕТ ПРОВЕРКИ", "#949494"),
        CHECKING("ПРОВЕРЯЕТСЯ", "#7462CE"),
        CHECKED("ПРОВЕРЕН", "#2DE17C"),
        REJECTED("ОТКЛОНЁН", "#FAB03C");

        private String value;
        private String color;

        RequestStatuses(String value, String color)
        {
            this.value = value;
            this.color = color;
        }
    }
}

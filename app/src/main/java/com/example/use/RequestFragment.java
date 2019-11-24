package com.example.use;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.use.Networking.BaseResponse;

import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestFragment extends BaseFragment
{
    private final String REQUEST_ID = "requestId";
    private final String REQUEST_TITLE = "requestTitle";
    private final String ATTACHMENT_DESC = "attachmentsDesc";
    private final String SENT_DESC = "sentDesc";
    private final String STATUS = "status";
    private final String STATUS_COLOR = "statusColor";
    private final String ATTACHMENTS = "attachments";
    private final String TEXT = "text";
    private final String MESSAGE = "message";

    private String requestId;
    private String requestTitle;
    private String attachmentsDesc;
    private String sentDesc;
    private String status;
    private int statusColor;
    private String[] attachments;
    private String text;
    private String message;

    @BindView(R.id.tvRequestId)
    TextView tvRequestId;
    @BindView(R.id.tvRequestTitle)
    TextView tvRequestTitle;
    @BindView(R.id.tvSentDesc)
    TextView tvSentDesc;
    @BindView(R.id.tvStatusDesc)
    TextView tvStatusDesc;
    @BindView(R.id.llAttachmentsList)
    LinearLayout llAttachmentsList;
    @BindView(R.id.tvTextDesc)
    TextView tvTextDesc;
    @BindView(R.id.tvMessageDesc)
    TextView tvMessageDesc;

    public RequestFragment() {}

    public static RequestFragment newInstance(Bundle bundle)
    {
        RequestFragment fragment = new RequestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            Bundle bundle = getArguments();
            requestId = bundle.getString(REQUEST_ID);
            requestTitle = bundle.getString(REQUEST_TITLE);
            attachmentsDesc = bundle.getString(ATTACHMENT_DESC);
            sentDesc = bundle.getString(SENT_DESC);
            status = bundle.getString(STATUS);
            statusColor = bundle.getInt(STATUS_COLOR);
            attachments = bundle.getStringArray(ATTACHMENTS);
            text = bundle.getString(TEXT);
            message = bundle.getString(MESSAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.request_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        tvRequestId.setText(requestId);
        tvRequestTitle.setText(requestTitle);
        tvSentDesc.setText(sentDesc);
        tvStatusDesc.setText(status);
        tvStatusDesc.setTextColor(statusColor);

        int attachmentsSize = attachments.length;
        if (attachments[0].equals("")) { attachmentsSize = 0; }
        for (int i = 0; i < attachmentsSize; i++)
        {
            final String attachment = attachments[i];
            final String fileName = URLUtil.guessFileName(attachment, null, null);
            final TextView tvAttachment = getTvAttachment(fileName, attachment);
            llAttachmentsList.addView(tvAttachment);
        }

        ExpandableTextBlock expandableTextBlock = new ExpandableTextBlock(tvTextDesc, text);
        if (!message.equals("false"))
        {
            tvMessageDesc.setText(message);
        }
    }

    private TextView getTvAttachment(String text, String url)
    {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getContext());
        final Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ic_arrow_point_to_right);
        drawable.setBounds(0, 0, 30, 30);

        textView.setLayoutParams(layoutParams);
        textView.setTextSize(19);
        textView.setTextColor(Color.parseColor("#1a0dab"));
        textView.setCompoundDrawablesRelative(null, null, drawable, null);
        textView.setText(text);
        textView.setOnClickListener(view ->
        {
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });
        return textView;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);
    }

    private class ExpandableTextBlock implements View.OnClickListener
    {
        private TextView textView;
        private String textData;
        private String text;

        public boolean isExpanded()
        {
            return expanded;
        }

        public void switchExpanded()
        {
            expanded = !expanded;
            if (expanded)
            {
                text = textData;
            }
            else
            {
                try { text = textData.substring(0, 200) + " [...]"; }
                catch (IndexOutOfBoundsException exception) {}
            }
            textView.setText(text);
        }

        private boolean expanded = true;

        public ExpandableTextBlock(TextView textView, String textData)
        {
            this.textView = textView;
            this.textData = textData;
            text = textData;
            switchExpanded();
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            switchExpanded();
        }
    }
}

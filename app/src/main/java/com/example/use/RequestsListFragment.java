package com.example.use;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.RequestResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestsListFragment extends BaseFragment implements RequestsListAdapter.Listener
{
    private List<Request> requests = new ArrayList<>();
    RequestsListAdapter requestsListAdapter;


    RecyclerView rvRequestsList;

    public RequestsListFragment() {}

    public static RequestsListFragment newInstance()
    {
        RequestsListFragment fragment = new RequestsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.requests_list_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rvRequestsList = view.findViewById(R.id.rvRequestsList);

        DividerItemDecoration horizontalSeparator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalSeparator.setDrawable(getResources().getDrawable(R.drawable.horizontal_separator));

        requestsListAdapter = new RequestsListAdapter(this, requests);

        rvRequestsList.addItemDecoration(horizontalSeparator);
        rvRequestsList.setAdapter(requestsListAdapter);

        NetworkService.getInstance(this).getUserRequests();
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);

        List<Request> requests = ((RequestResponse)response).getData();
        this.requests.addAll(requests);
        requestsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnViewHolderClick(int position, long topicId, long number)
    {

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
}

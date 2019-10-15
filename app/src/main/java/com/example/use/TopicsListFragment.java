package com.example.use;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Subject;
import com.example.use.Networking.TopicResponse;
import com.example.use.Networking.Topic;
import com.example.use.database.DbRequestListener;
import com.example.use.database.DbService;

import java.util.ArrayList;
import java.util.List;

public class TopicsListFragment extends BaseFragment implements TopicsListAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private long subjectId;

    private Listener mListener;
    private TopicsListAdapter topicsListAdapter;

    private List<Topic> topics;

    public TopicsListFragment()
    {

    }

    public static TopicsListFragment newInstance(long subjectId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_1, subjectId);
        TopicsListFragment fragment = new TopicsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            subjectId = getArguments().getLong(PARAM_1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_topics_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        topics = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvTopicsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration horizontalSeparator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalSeparator.setDrawable(getResources().getDrawable(R.drawable.horizontal_separator));
        recyclerView.addItemDecoration(horizontalSeparator);

        topicsListAdapter = new TopicsListAdapter(this, topics);
        recyclerView.setAdapter(topicsListAdapter);

        DbService.getInstance().getTopics(subjectId, (DbRequestListener<List<Topic>>) topics ->
        {
            TopicsListFragment.this.topics.addAll(topics);
            topicsListAdapter.notifyDataSetChanged();
            DbService.getInstance().updateDb(result ->
                    DbService.getInstance().getTopics(subjectId, (DbRequestListener<List<Topic>>) topics1 ->
            {
                TopicsListFragment.this.topics.clear();
                TopicsListFragment.this.topics.addAll(topics1);
                topicsListAdapter.notifyDataSetChanged();
            }));
        });
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (Listener)getActivity();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);
    }

    @Override
    public void OnViewHolderClick(int position, long topicId, long number)
    {
        mListener.onTopicsListFragmentInteraction(topicId, number);
    }

    public interface Listener
    {
        void onTopicsListFragmentInteraction(long topicId, long number);
    }
}

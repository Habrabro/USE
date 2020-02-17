package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;
import com.yasdalteam.yasdalege.Networking.ResponseHandler;
import com.yasdalteam.yasdalege.Networking.TopicResponse;

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

        if (App.shared().getTopics().isEmpty())
        {
            Loader.show();
            NetworkService.getInstance(new ResponseHandler() {
                @Override
                public void onResponse(BaseResponse response)
                {
                    super.onResponse(response);
                    List<Topic> topics = ((TopicResponse)response).getData();
                    TopicsListFragment.this.topics.addAll(topics);
                    App.shared().getTopics().addAll(topics);
                    topicsListAdapter.notifyDataSetChanged();
                    Loader.hide();
                }

                @Override
                public void onError(String error)
                {
                    super.onError(error);
                    if (error.equals("404"))
                    {
                        View rlNoContentStub = getView().findViewById(R.id.llNoContentStub);
                        LinearLayout.LayoutParams showParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        rlNoContentStub.setLayoutParams(showParams);
                    }
                    Loader.hide();
                }
            }).getTopics(null, subjectId);
        }
        else
        {
            TopicsListFragment.this.topics.addAll(App.shared().getTopics());
            topicsListAdapter.notifyDataSetChanged();
        }
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
        ((MainActivity)getActivity()).replaceFragment(
                ExercisesListFragment.newInstance(listener ->
                {
                    String limit = ((ExercisesListFragment)listener).getPage() *
                            ((ExercisesListFragment)listener).getItemsPerLoad() + "," +
                            ((ExercisesListFragment)listener).getItemsPerLoad();
                    NetworkService networkService = NetworkService.getInstance(listener);
                    networkService.getExercises(
                            null, topicId, limit);
                }),
                "exercisesListFragment");
    }

    public interface Listener
    {
        void onTopicsListFragmentInteraction(long topicId, long number);
    }
}

package com.example.use;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.SubjectsResponse;
import com.example.use.Networking.Subject;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.TopicDatum;
import com.example.use.Networking.UpdatesResponse;
import com.example.use.database.DbService;
import com.example.use.database.OnDbUpdatedListener;
import com.example.use.database.SubjectsDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectsListFragment extends BaseFragment implements SubjectsListAdapter.Listener
{
    private Listener mListener;
    private SubjectsListAdapter subjectsListAdapter;

    private List<Subject> subjects = new ArrayList<>();

    public SubjectsListFragment()
    {

    }

    public static SubjectsListFragment newInstance()
    {
        SubjectsListFragment fragment = new SubjectsListFragment();
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
        View view = inflater.inflate(R.layout.fragment_subjects_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvSubjectsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        subjectsListAdapter = new SubjectsListAdapter(this, subjects);
        recyclerView.setAdapter(subjectsListAdapter);

        new AsyncTask<Void, Void, List<Subject>>()
        {
            @Override
            protected List<Subject> doInBackground(Void... voids)
            {
                SubjectsDao subjectsDao = DbService.getInstance().getDatabase().subjectsDao();
                List<Subject> _subjects = subjectsDao.getAll();
                return _subjects;
            }

            @Override
            protected void onPostExecute(List<Subject> _subjects)
            {
                super.onPostExecute(_subjects);
                subjects.addAll(_subjects);
                subjectsListAdapter.notifyDataSetChanged();

                NetworkService.getInstance(SubjectsListFragment.this)
                        .getUpdates("2019-09-01", null);
            }
        }.execute();
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
        subjects.clear();
        if (response.getClass() == SubjectsResponse.class)
        {
            subjects.addAll(((SubjectsResponse)response).getData());
        }
        if (response.getClass() == UpdatesResponse.class)
        {
            DbService.getInstance().getUpdateManager().update(
                    ((UpdatesResponse) response).getData(), new OnDbUpdatedListener()
                    {
                        @Override
                        public void onDbUpdated()
                        {
                            new AsyncTask<Void, Void, List<Subject>>()
                            {
                                @Override
                                protected List<Subject> doInBackground(Void... voids)
                                {
                                    SubjectsDao subjectsDao = DbService.getInstance().getDatabase().subjectsDao();
                                    List<Subject> _subjects = subjectsDao.getAll();
                                    return _subjects;
                                }

                                @Override
                                protected void onPostExecute(List<Subject> _subjects)
                                {
                                    super.onPostExecute(_subjects);
                                    subjects.addAll(_subjects);
                                    subjectsListAdapter.notifyDataSetChanged();
                                }
                            }.execute();
                        }
                    });
        }
    }

    @Override
    public void OnViewHolderClick(int position, long subjectId)
    {
        mListener.onSubjectsListFragmentInteraction(subjectId);
    }

    interface Listener
    {
        void onSubjectsListFragmentInteraction(long subjectId);
    }
}

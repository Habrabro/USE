package com.yasdalteam.yasdalege;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yasdalteam.yasdalege.Networking.BaseResponse;
import com.yasdalteam.yasdalege.Networking.DirectoryResponse;
import com.yasdalteam.yasdalege.Networking.NetworkService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectoryFragment extends BaseFragment implements DirectoryAdapter.Listener
{
    @BindView(R.id.llNoContentStub)
    LinearLayout rlNoContentStub;

    private final static String PARAM_1 = "param_1";
    private long subjectId;

    private Listener mListener;
    private DirectoryAdapter directoryAdapter;

    public void setDirectories(List<Directory> directories)
    {
        this.directories = directories;
    }
    private List<Directory> directories = new ArrayList<>();
    private Subject subject;
    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public DirectoryFragment()
    {

    }

    public static DirectoryFragment newInstance(Subject subject)
    {
        DirectoryFragment fragment = getDirectoryFragmentInstance(subject);
        return fragment;
    }

    public static DirectoryFragment newInstance(Directory directory)
    {
        DirectoryFragment fragment = getDirectoryFragmentInstance(null);
        List<Directory> directories = new ArrayList<>();
        directories.add(directory);
        fragment.setDirectories(directories);
        return fragment;
    }

    private static DirectoryFragment getDirectoryFragmentInstance(Subject subject)
    {
        DirectoryFragment fragment = new DirectoryFragment();
        if (subject != null)
        {
            fragment.setSubject(subject);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (subject != null)
        {
            subjectId = subject.getId();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        RecyclerView recyclerView = view.findViewById(R.id.rvDirectory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration horizontalSeparator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        horizontalSeparator.setDrawable(getResources().getDrawable(R.drawable.horizontal_separator));
        recyclerView.addItemDecoration(horizontalSeparator);

        directoryAdapter = new DirectoryAdapter(this, directories, subject);
        recyclerView.setAdapter(directoryAdapter);

        if (directories.isEmpty())
        {
            Loader.show();
            NetworkService networkService = NetworkService.getInstance(this);
            networkService.getDirectories(null, subjectId, null);
        }

        rlNoContentStub.setVisibility(View.INVISIBLE);
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
        mListener = null;
    }

    @Override
    public void onResponse(BaseResponse response)
    {
        super.onResponse(response);

        Loader.hide();
        DirectoryResponse directoryResponse = ((DirectoryResponse)response);
        directories.clear();
        directories.addAll(directoryResponse.getData());
        directoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String error)
    {
        super.onError(error);

        Loader.hide();
        if (error.equals("404"))
        {
            rlNoContentStub.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnListItemClick(int position)
    {
        ((MainActivity)getActivity()).replaceFragment(newInstance(directories.get(position)), "directoryTopicFragment");
    }

    @Override
    public void OnDirectoryTopicClick(int position)
    {

    }

    public interface Listener
    {
        void onFragmentInteraction();
    }
}

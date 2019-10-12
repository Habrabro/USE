package com.example.use;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.Directory;
import com.example.use.Networking.DirectoryResponse;
import com.example.use.Networking.Exercise;
import com.example.use.Networking.ExerciseResponse;
import com.example.use.Networking.IResponseReceivable;
import com.example.use.Networking.NetworkService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DirectoryFragment extends BaseFragment implements DirectoryAdapter.Listener
{
    private final static String PARAM_1 = "param_1";
    private long subjectId;

    private Listener mListener;
    private DirectoryAdapter directoryAdapter;

    private List<Directory> directories = new ArrayList<>();

    public DirectoryFragment()
    {

    }

    public static DirectoryFragment newInstance(long topicId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_1, topicId);
        DirectoryFragment fragment = new DirectoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_directory, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvDirectory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        directoryAdapter = new DirectoryAdapter(this, directories, subjectId);
        recyclerView.setAdapter(directoryAdapter);

        NetworkService networkService = NetworkService.getInstance(this);
        networkService.getDirectories(null, subjectId, null);
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
        DirectoryResponse directoryResponse = ((DirectoryResponse)response);
        directories.clear();
        directories.addAll(directoryResponse.getData());
        directoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnViewHolderClick(int position)
    {

    }

    public interface Listener
    {
        void onFragmentInteraction();
    }
}

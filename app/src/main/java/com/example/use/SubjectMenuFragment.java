package com.example.use;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.use.Networking.BaseResponse;
import com.example.use.Networking.NetworkService;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectDatum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectMenuFragment extends BaseFragment
{
    private static final String PARAM_1 = "param_1";
    private long subjectId;

    @BindView(R.id.btnDirectory) Button directoryView;
    @BindView(R.id.btnStartTraining) Button startTrainingView;
    @BindView(R.id.btnVariantsList) Button variantsListView;

    private SubjectMenuFragment.Listener mListener;

    public SubjectMenuFragment()
    {

    }

    public static SubjectMenuFragment newInstance(long subjectId)
    {
        Bundle bundle = new Bundle();
        bundle.putLong(PARAM_1, subjectId);
        SubjectMenuFragment fragment = new SubjectMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_subject_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        directoryView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
        startTrainingView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
        variantsListView.setOutlineProvider(new SubjectMenuButtonOutlineProvider());
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (SubjectMenuFragment.Listener)getActivity();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.btnVariantsList)
    public void onDirectoryViewClick()
    {
        mListener.onSubjectMenuFragmentInteraction(subjectId);
    }

    interface Listener
    {
        void onSubjectMenuFragmentInteraction(long subjectId);
    }
}

package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.use.Networking.ExerciseDatum;
import com.example.use.Networking.SubjectDatum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectsListAdapter extends RecyclerView.Adapter<SubjectsListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<SubjectDatum> subjects;
    private Listener listener;

    SubjectsListAdapter(Listener listener, List<SubjectDatum> subjects)
    {
        this.subjects = subjects;
        if (listener instanceof Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public SubjectsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_subjects_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubjectsListAdapter.ViewHolder holder, int position)
    {
        SubjectDatum subject = subjects.get(position);
        holder.subjectNameView.setText(subject.getName());
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvSubjectName) TextView subjectNameView;

        ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            listener.OnViewHolderClick(getAdapterPosition());
        }
    }
}

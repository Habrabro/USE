package com.example.use;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.use.Networking.ExerciseDatum;
import com.example.use.Networking.Subject;
import com.example.use.Networking.SubjectDatum;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
        int outerPadding = (int)App.getInstance().getResources().getDimension(R.dimen.subjectIconOuterPadding);
        int innerPadding = (int)App.getInstance().getResources().getDimension(R.dimen.subjectIconInnerPadding);
        int leftPadding = position % 2 == 0
                ?outerPadding
                :innerPadding;
        int rightPadding = position % 2 == 0
                ?innerPadding
                :outerPadding;
        holder.view.setPadding(leftPadding, outerPadding, rightPadding, 0);

        SubjectDatum subject = subjects.get(position);
        holder.subjectNameView.setText(subject.getName());
        Glide
                .with(App.getInstance())
                .load(subject.getImg())
                .placeholder(new ColorDrawable(Color.GREEN))
                .error(new ColorDrawable(Color.RED))
                .fallback(new ColorDrawable(Color.GRAY))
                .into(holder.subjectIconView);
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position, long subjectId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvSubjectName) TextView subjectNameView;
        @BindView(R.id.ivSubjectIcon) ImageView subjectIconView;
        private View view;

        ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            this.view = view;
        }

        @Override
        public void onClick(View view)
        {
            SubjectDatum subject = subjects.get(getAdapterPosition());
            listener.OnViewHolderClick(getAdapterPosition(), subject.getId());
        }
    }
}

package com.yasdalteam.yasdalege;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectsListAdapter extends RecyclerView.Adapter<SubjectsListAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Subject> subjects;
    private Listener listener;

    SubjectsListAdapter(Listener listener, List<Subject> subjects)
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
        int outerPadding = (int)App.shared().getResources().getDimension(R.dimen.subjectIconOuterPadding);
        int innerPadding = (int)App.shared().getResources().getDimension(R.dimen.subjectIconInnerPadding);
        int leftPadding = position % 2 == 0 ?outerPadding :innerPadding;
        int rightPadding = position % 2 == 0 ?innerPadding :outerPadding;
        int bottomPadding = position == subjects.size() - 1 ?outerPadding :0;
        holder.view.setPadding(leftPadding, outerPadding, rightPadding, bottomPadding);

        Subject subject = subjects.get(position);
        Glide
                .with(App.shared())
                .load(subject.getImg())
                .placeholder(new ColorDrawable(
                        App.shared().getResources().getColor(R.color.glidePlaceholderColor)))
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.subjectIconView);
    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position, Subject subject);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
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
            Subject subject = subjects.get(getAdapterPosition());
            listener.OnViewHolderClick(getAdapterPosition(), subject);
        }
    }
}

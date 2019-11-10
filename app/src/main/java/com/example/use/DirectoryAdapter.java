package com.example.use;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.regex.Pattern.compile;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Directory> directories;
    private DirectoryAdapter.Listener listener;
    private View view;
    private long subjectId;
    private boolean dataIsLoading = true;
    private boolean allDataLoaded = false;

    public boolean isDataIsLoading()
    {
        return dataIsLoading;
    }

    public void setDataIsLoading(boolean dataIsLoading)
    {
        this.dataIsLoading = dataIsLoading;
    }

    public boolean isAllDataLoaded()
    {
        return allDataLoaded;
    }

    public void setAllDataLoaded(boolean allDataLoaded)
    {
        this.allDataLoaded = allDataLoaded;
    }

    DirectoryAdapter(DirectoryAdapter.Listener listener, List<Directory> directories, long subjectId)
    {
        this.directories = directories;
        this.subjectId = subjectId;
        if (listener instanceof DirectoryAdapter.Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public DirectoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.fragment_directory_item, parent, false);
        return new DirectoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DirectoryAdapter.ViewHolder holder, int position)
    {
        Directory directory = directories.get(position);
        holder.fragmentDirectoryTitle.setText(directory.getTitle());
        DirectoryParser directoryParser = new DirectoryParser(view);
        directoryParser.parseContent(directory.getContent());
    }

    @Override
    public int getItemCount()
    {
        return directories.size();
    }

    public interface Listener
    {
        void OnViewHolderClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvFragmentDirectoryTitle)
        TextView fragmentDirectoryTitle;

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

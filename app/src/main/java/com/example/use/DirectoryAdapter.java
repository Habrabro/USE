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

public class DirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Directory> directories;
    private DirectoryAdapter.Listener listener;
    private View view;
    private long subjectId;
    private Subject subject;
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

    DirectoryAdapter(DirectoryAdapter.Listener listener, List<Directory> directories, Subject subject)
    {
        this.directories = directories;
        if (subject != null)
        {
            this.subject = subject;
        }
        if (listener instanceof DirectoryAdapter.Listener)
        {
            this.listener = listener;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        inflater = LayoutInflater.from(parent.getContext());
        ViewHolderTypes type = ViewHolderTypes.values()[viewType];
        switch (type) {
            case LIST_ITEM:
                view = inflater.inflate(R.layout.directory_fragment_list_item, parent, false);
                return new ListItem(view);
            case DIRECTORY_TOPIC:
                view = inflater.inflate(R.layout.fragment_directory_item, parent, false);
                return new DirectoryTopic(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Directory directory = directories.get(position);
        ViewHolderTypes type = ViewHolderTypes.values()[holder.getItemViewType()];
        switch (type)
        {
            case LIST_ITEM:
                ((ListItem)holder).bind(directory);
                break;
            case DIRECTORY_TOPIC:
                ((DirectoryTopic)holder).fragmentDirectoryTitle.setText(directory.getTitle());
//                DirectoryParser directoryParser = new DirectoryParser(view);
//                directoryParser.parseContent(directory.getContent());
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return directories.size();
    }

    public interface Listener
    {
        void OnListItemClick(int position);
        void OnDirectoryTopicClick(int position);
    }

    @Override
    public int getItemViewType(int position)
    {
        if (subject == null || !subject.hasDirectoryTopics())
        {
            return ViewHolderTypes.DIRECTORY_TOPIC.getType();
        }
        else
        {
            return ViewHolderTypes.LIST_ITEM.getType();
        }
    }

    class DirectoryTopic extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvFragmentDirectoryTitle)
        TextView fragmentDirectoryTitle;

        DirectoryTopic(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            listener.OnDirectoryTopicClick(getAdapterPosition());
        }
    }

    class ListItem extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tvDirectoryTitle)
        TextView directoryTitle;

        public ListItem(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bind(Directory directory)
        {
            this.directoryTitle.setText(directory.getTitle());
        }

        @Override
        public void onClick(View view)
        {
            listener.OnListItemClick(getAdapterPosition());
        }
    }

    public enum ViewHolderTypes
    {
        LIST_ITEM(0),
        DIRECTORY_TOPIC(1);

        public int getType()
        {
            return type;
        }

        private int type;

        ViewHolderTypes(int type)
        {
            this.type = type;
        }
    }
}

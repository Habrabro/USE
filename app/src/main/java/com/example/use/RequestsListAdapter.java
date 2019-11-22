package com.example.use;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder>
{
    private List<Request> requests;
    private Listener listener;

    public RequestsListAdapter(RequestsListAdapter.Listener listener, List<Request> requests)
    {
        this.requests = requests;
        if (listener instanceof RequestsListAdapter.Listener)
        {
            this.listener = listener;
        }
    }

    @Override
    public RequestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.requests_list_item_layout, parent, false);
        return new RequestsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestsListAdapter.ViewHolder holder, int position)
    {
        Request request = requests.get(position);
        holder.bindRequest(request);
    }

    @Override
    public int getItemCount()
    {
        return requests.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private View view;
        private Request request;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
        }

        public void bindRequest(Request request)
        {
            this.request = request;
        }
    }

    public interface Listener
    {
        void OnViewHolderClick(int position, long topicId, long number);
    }
}

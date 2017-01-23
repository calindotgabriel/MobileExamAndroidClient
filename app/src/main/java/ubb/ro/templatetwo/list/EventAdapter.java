package ubb.ro.templatetwo.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ubb.ro.templatetwo.Event;
import ubb.ro.templatetwo.R;

/**
 * Created by calin on 23.01.2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mEvents;

    public void setEvents(List<Event> mEvents) {
        this.mEvents = mEvents;
        notifyDataSetChanged();
    }

    public EventAdapter() {
        this.mEvents = new ArrayList<>();
    }

    public EventAdapter(List<Event> events) {
        this.mEvents = events;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        final Event event = mEvents.get(position);
        holder.mItemTextView.setText(event.toString());
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mItemTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mItemTextView = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
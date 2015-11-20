package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import it.hopapps.villaggiorock.EventActivity;
import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.asyncTasks.BitmapSetter;
import it.hopapps.villaggiorock.models.EventsItem;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MenuItemViewHolder> implements View.OnClickListener{
    List<EventsItem> eventsItems;
    private static Context sContext;

    public EventsAdapter(List<EventsItem> eventsItems, Context c ){
        this.eventsItems = eventsItems;
        this.sContext = c;
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_layout_events, parent, false);
        MenuItemViewHolder mivh = new MenuItemViewHolder(v);
        mivh.itemName.setOnClickListener(EventsAdapter.this);
        mivh.itemPhoto.setOnClickListener(EventsAdapter.this);
        mivh.itemName.setTag(mivh);
        mivh.itemPhoto.setTag(mivh);
        return mivh;
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder menuItemViewHolder, int i) {
        menuItemViewHolder.itemName.setText(eventsItems.get(i).getName());
        BitmapSetter bitmapSet = new BitmapSetter(menuItemViewHolder.itemPhoto, eventsItems.get(i).getPhotoUrl());
        bitmapSet.execute();
    }

    @Override
    public int getItemCount() {
        return eventsItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        MenuItemViewHolder holder = (MenuItemViewHolder) view.getTag();
        String eventId = null;
        for(int i=0; i<eventsItems.size(); i++){
            if(eventsItems.get(i).getName().equals(holder.itemName.getText())){
                eventId = eventsItems.get(i).getId();
            }
        }
        Intent intent = new Intent(view.getContext(), EventActivity.class);
        intent.putExtra("id", eventId);
        view.getContext().startActivity(intent);
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView itemName;
        ImageView itemPhoto;

        MenuItemViewHolder (View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_menu);
            itemName = (TextView)itemView.findViewById(R.id.menu_link_name);
            itemPhoto = (ImageView)itemView.findViewById(R.id.menu_link_image);
        }
    }
}



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

import it.hopapps.villaggiorock.AlbumPhotosActivity;
import it.hopapps.villaggiorock.AlbumsActivity;
import it.hopapps.villaggiorock.EventsActivity;
import it.hopapps.villaggiorock.GadgetActivity;
import it.hopapps.villaggiorock.GadgetsActivity;
import it.hopapps.villaggiorock.LiveActivity;
import it.hopapps.villaggiorock.MenuActivity;
import it.hopapps.villaggiorock.PlaylistActivity;
import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.ReservationActivity;
import it.hopapps.villaggiorock.models.CustomMenuItem;

public class RVMenuAdapter extends RecyclerView.Adapter<RVMenuAdapter.MenuItemViewHolder> implements View.OnClickListener{
    List<CustomMenuItem> menuItems;
    private static Context sContext;

    public RVMenuAdapter(List<CustomMenuItem> menuItems, Context c ){
        this.menuItems = menuItems;
        this.sContext = c;
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_layout, parent, false);
        MenuItemViewHolder menuItemViewHolder = new MenuItemViewHolder(v);
        menuItemViewHolder.itemName.setOnClickListener(RVMenuAdapter.this);
        menuItemViewHolder.itemPhoto.setOnClickListener(RVMenuAdapter.this);
        menuItemViewHolder.itemName.setTag(menuItemViewHolder);
        menuItemViewHolder.itemPhoto.setTag(menuItemViewHolder);
        return menuItemViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder menuItemViewHolder, int i) {
        menuItemViewHolder.itemName.setText(menuItems.get(i).getName());
        menuItemViewHolder.itemPhoto.setImageResource(menuItems.get(i).getPhotoId());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View view) {
        MenuItemViewHolder holder = (MenuItemViewHolder) view.getTag();
        if(view.getContext() instanceof MenuActivity) {
            if (view.getId() == holder.itemName.getId() || view.getId() == holder.itemPhoto.getId()) {
                if (holder.itemName.getText().equals(view.getContext().getResources().getString(R.string.events_menu_name))) {
                    Intent intent = new Intent(view.getContext(), EventsActivity.class);
                    view.getContext().startActivity(intent);
                } else if (holder.itemName.getText().equals(view.getContext().getResources().getString(R.string.photos_menu_name))) {
                    Intent intent = new Intent(view.getContext(), AlbumsActivity.class);
                    view.getContext().startActivity(intent);
                } else if (holder.itemName.getText().equals(view.getContext().getResources().getString(R.string.playlist_menu_name))){
                    Intent intent = new Intent(view.getContext(), PlaylistActivity.class);
                    view.getContext().startActivity(intent);
                } else if (holder.itemName.getText().equals(view.getContext().getResources().getString(R.string.gadget_menu_name))) {
                    Intent intent = new Intent(view.getContext(), GadgetsActivity.class);
                    view.getContext().startActivity(intent);
                } else if (holder.itemName.getText().equals(view.getContext().getResources().getString(R.string.live_menu_name))){
                    Intent intent = new Intent(view.getContext(), LiveActivity.class);
                    view.getContext().startActivity(intent);
                }
            }
        } else if (view.getContext() instanceof GadgetsActivity){
            Intent intent = new Intent(view.getContext(), GadgetActivity.class);
            intent.putExtra("gadget_name", holder.itemName.getText());
            int gadgetImage = -1;
            for (CustomMenuItem customMenuItem: menuItems) {
                if (customMenuItem.getName().equals(holder.itemName.getText())){
                    gadgetImage = customMenuItem.getPhotoId();
                }
            }
            intent.putExtra("gadget_image", gadgetImage);
            view.getContext().startActivity(intent);
        } else if (view.getContext() instanceof AlbumsActivity){
            Intent intent = new Intent(view.getContext(), AlbumPhotosActivity.class);
            view.getContext().startActivity(intent);
        }
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

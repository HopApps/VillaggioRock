package it.hopapps.villaggiorock.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.models.MenuItem;

public class RVMenuAdapter extends RecyclerView.Adapter<RVMenuAdapter.MenuItemViewHolder>{
    List<MenuItem> menuItems;

    public RVMenuAdapter(List<MenuItem> menuItems){
        this.menuItems = menuItems;
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_layout, parent, false);
        MenuItemViewHolder mivh = new MenuItemViewHolder(v);
        return mivh;
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

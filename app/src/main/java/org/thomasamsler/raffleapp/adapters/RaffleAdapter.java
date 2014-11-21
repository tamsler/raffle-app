package org.thomasamsler.raffleapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.thomasamsler.raffleapp.R;
import org.thomasamsler.raffleapp.fragments.RaffleListFragment;

/**
 * Created by tamsler on 11/20/14.
 */
public class RaffleAdapter extends CursorAdapter {


    public static class ViewHolder {
        public final TextView nameView;

        public ViewHolder(View view) {

            nameView = (TextView) view.findViewById(R.id.list_item_raffle_name);
        }
    }

    public RaffleAdapter(Context context, Cursor cursor, int flags) {

        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_raffle_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String raffleName = cursor.getString(RaffleListFragment.COL_RAFFLE_NAME);
        viewHolder.nameView.setText(raffleName);
    }
}

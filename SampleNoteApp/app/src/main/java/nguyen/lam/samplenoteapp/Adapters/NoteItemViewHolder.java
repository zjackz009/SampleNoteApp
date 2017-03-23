package nguyen.lam.samplenoteapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nguyen.lam.samplenoteapp.R;

public class NoteItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvNoteItemName;

    public NoteItemViewHolder(View itemView) {
        super(itemView);
        tvNoteItemName = (TextView) itemView.findViewById(R.id.tv_note_item_name);
    }
}

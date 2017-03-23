package nguyen.lam.samplenoteapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nguyen.lam.samplenoteapp.Activities.MainActivity;
import nguyen.lam.samplenoteapp.Listeners.NoteItemListener;
import nguyen.lam.samplenoteapp.R;
import nguyen.lam.samplenoteapp.Utilities.Constant;

/**
 * Created by JackSilver on 3/23/2017.
 */

public class NoteItemAdapter extends RecyclerView.Adapter<NoteItemViewHolder> {

    private ArrayList<String> listNotes;
    private NoteItemListener noteItemListener;
    public  NoteItemAdapter (ArrayList<String> listNotes, NoteItemListener listener){
        this.listNotes = listNotes;
        this.noteItemListener = listener;
    }

    @Override
    public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_item,parent,false);
        return new NoteItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteItemViewHolder holder, int position) {
        if(null != listNotes && listNotes.size()>0){
            if(!TextUtils.isEmpty(listNotes.get(position))){
                final String name = listNotes.get(position);
                holder.tvNoteItemName.setText(name.substring(0,name.indexOf(Constant.SYMBOL_)).toUpperCase());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != noteItemListener){
                            noteItemListener.onItemClick(name);
                        }
                    }
                });
            }
        }

    }

    public void remove(int position) {
        if(null != noteItemListener){
            noteItemListener.onItemRemove(listNotes.get(position));
        }
        listNotes.remove(position);
        notifyItemRemoved(position);

    }


    @Override
    public int getItemCount() {
        if(null != listNotes && listNotes.size()>0){
            return listNotes.size();
        }
        return 0;
    }
}

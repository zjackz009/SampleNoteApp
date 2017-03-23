package nguyen.lam.samplenoteapp.Adapters;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class NoteItemSwipeHelper extends ItemTouchHelper.SimpleCallback {

    private NoteItemAdapter noteItemAdapter;

    public NoteItemSwipeHelper(NoteItemAdapter  adapter) {

        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.noteItemAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        noteItemAdapter.remove(viewHolder.getAdapterPosition());
    }
}

package ttc.project.stoku.callback;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    boolean onItemMoveCompleted(int endPosition);


    void onItemDismiss(int position);
}

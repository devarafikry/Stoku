package ttc.project.stoku.callback;

import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.viewholder.ItemStockViewHolder;

public interface ItemStockInterface {
    public void updateItem(UserItem userItem, int position);
    public void editItem(UserItem userItem, int position);
    public void deleteItem(UserItem userItem, int position);
    public void firstItemReady(ItemStockViewHolder holder);
}

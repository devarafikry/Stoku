package ttc.project.stoku.callback;

import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.model.UserItem;

public interface ShoppingListInterface {
    public void updateItem(ListItem listItem, int position);
    public void updateNotes(ListItem listItem, int position);
    public void deleteItem(ListItem listItem, int position);
    public void editItem(ListItem listItem, int position);
}

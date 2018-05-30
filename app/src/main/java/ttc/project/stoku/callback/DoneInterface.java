package ttc.project.stoku.callback;

import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.model.UserItem;

public interface DoneInterface {
    public void done(ListItem userItem, boolean add);
}

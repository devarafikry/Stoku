package ttc.project.stoku.callback;

import ttc.project.stoku.model.Category;

public interface CategoryInterface {
    public void editCategory(Category category, int position);
    public void deleteCategory(Category category, int position);
}

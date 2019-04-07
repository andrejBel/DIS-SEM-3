package GUI;

import java.util.function.Function;

public class TableColumnItem<T> {

    public Function<T, String> spracuj_;
    public String viewName_;

    public TableColumnItem(Function<T, String> spracuj, String viewName_) {
        this.spracuj_ = spracuj;
        this.viewName_ = viewName_;
    }


}
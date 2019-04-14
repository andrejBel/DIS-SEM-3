package GUI;

import java.util.function.Function;

public class TableColumnItem<T> {

    public Function<T, String> spracuj_;
    public String viewName_;
    public double minWidth_;

    public TableColumnItem(Function<T, String> spracuj, String viewName) {
        this(spracuj, viewName, 100.0);
    }

    public TableColumnItem(Function<T, String> spracuj, String viewName, double minWidth) {
        this.spracuj_ = spracuj;
        this.viewName_ = viewName;
        this.minWidth_ = minWidth;
    }
}
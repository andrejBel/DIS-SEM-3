package GUI;

import java.util.function.Function;

public class TableColumnItem<T> {

    public static final double IGNORE_MAX_WIDTH = -1;

    public Function<T, String> spracuj_;
    public String _viewName;
    public double _minWidth;
    public double _maxWidth;

    public TableColumnItem(Function<T, String> spracuj, String viewName) {
        this(spracuj, viewName, 100.0, IGNORE_MAX_WIDTH);
    }

    public TableColumnItem(Function<T, String> spracuj, String viewName, double minWidth, double maxWidth) {
        this.spracuj_ = spracuj;
        this._viewName = viewName;
        this._minWidth = minWidth;
        this._maxWidth = maxWidth;
    }
}
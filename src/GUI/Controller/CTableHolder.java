package GUI.Controller;

import GUI.TableColumnItem;
import Utils.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import simulation.SimulaciaWrapper;

import java.util.List;
import java.util.function.Predicate;

public class CTableHolder<T> extends CWindowBase {

    private String viewName_;
    private boolean selected_ = false;

    @FXML
    private TableView<T> tableView;

    private ObservableList<T> tableViewDataLazy_;

    private Predicate<? super T> _predicateForFiltering;

    public CTableHolder(SimulaciaWrapper simulaciaWrapper, Stage stage, String viewName, List<TableColumnItem<T>> tableColumnItems, Predicate<? super T> predicateForFiltering) {
        super(simulaciaWrapper, stage);
        getStage().setTitle(viewName);
        this.viewName_ = viewName;
        Helper.PridajTabulkeStlpce(tableView, tableColumnItems);
        tableViewDataLazy_ = tableView.getItems();
        _predicateForFiltering = predicateForFiltering;
        //Helper.InstallCopyPasteHandler(tableView);
    }

    @Override
    public Runnable getRunnableOnSelection() {
        return () -> {
            setTableData();
            selected_ = true;
        };
    }

    @Override
    public Runnable getRunnableOnUnSelection() {
        return () -> {
            selected_ = false;
        };
    }

    @Override
    protected String getViewFileName() {
        return "tableHolder.fxml";
    }

    @Override
    public String getViewName() {
        return this.viewName_;
    }

    public TableView<T> getTableView() {
        return tableView;
    }

    public Tab getTab() {
        Tab tab = new Tab(this.viewName_);
        tab.setContent(getView());
        tab.setOnSelectionChanged(event -> {
            if (getRunnableOnSelection() != null) {
                if (tab.isSelected()) {
                    getRunnableOnSelection().run();
                }
            }
            if (getRunnableOnUnSelection() != null) {
                if (!tab.isSelected()) {
                    getRunnableOnUnSelection().run();
                }
            }
        });
        return tab;
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setPredicateForFiltering(Predicate<? super T> predicateForFiltering) {
        this._predicateForFiltering = predicateForFiltering;
        if (selected_) {
            setTableData();
        }
    }

    public void setTableViewDataLazy(ObservableList<T> list) {
        tableViewDataLazy_ = list;
        if (selected_) {
            setTableData();
        }
    }

    public ObservableList<T> getTableViewData() {
        return tableView.getItems();
    }

    public void clearTableViewData() {
        this.tableView.setItems(new FilteredList<>(FXCollections.observableArrayList()));
    }

    private void setTableData() {
        FilteredList<T> filtered = new FilteredList<>(tableViewDataLazy_);
        filtered.setPredicate(_predicateForFiltering);
        tableView.setItems(filtered);
    }

}

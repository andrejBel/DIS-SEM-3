package GUI.Controller;

import GUI.TableColumnItem;
import Utils.Helper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import simulation.SimulaciaWrapper;

import java.util.List;

public class CTableHolder<T> extends CWindowBase {

    private String viewName_;
    private boolean selected_ = false;

    @FXML
    private TableView<T> tableView;
    private ObservableList<T> tableViewData_;

    public CTableHolder(SimulaciaWrapper simulaciaWrapper, Stage stage, String viewName, List<TableColumnItem<T>> tableColumnItems) {
        super(simulaciaWrapper, stage);
        getStage().setTitle(viewName);
        this.viewName_ = viewName;
        Helper.PridajTabulkeStlpce(tableView, tableColumnItems);
        Helper.InstallCopyPasteHandler(tableView);
        tableViewData_ = tableView.getItems();
    }

    @Override
    public Runnable getRunnableOnSelection() {
        return () -> { selected_ = true; };
    }

    @Override
    public Runnable getRunnableOnUnSelection() {
        return () -> { selected_ = false; };
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

    public void setTableViewData(List<T> tableViewData) {
        tableViewData_.clear();
        tableViewData_.addAll(tableViewData);
    }

    public ObservableList<T> getTableViewData() {
        return tableViewData_;
    }

}

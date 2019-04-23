package GUI.Controller;

import GUI.TableColumnItem;
import Model.Info.CestujuciInfo;
import Utils.Helper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simulation.SimulaciaWrapper;

import java.util.List;

public class CCestujuciVozidlo extends CWindowBase {

    @FXML
    private Label labelFrontNastupujucich;

    @FXML
    private TableView<CestujuciInfo> tableViewNastupujuci;

    @FXML
    private Label labelFrontCestujucich;

    @FXML
    private TableView<CestujuciInfo> tableViewCestujuci;

    @FXML
    private Label labelFrontVystupujucich;

    @FXML
    private TableView<CestujuciInfo> tableViewVystupujuci;

    private ObservableList<CestujuciInfo> tableDataNastupujuciLazy_;
    private ObservableList<CestujuciInfo> tableDataCestujuciLazy_;
    private ObservableList<CestujuciInfo> tableDataVystupujuciLazy_;


    private String viewName_;

    public CCestujuciVozidlo(SimulaciaWrapper simulaciaWrapper, Stage stage, String viewName, List<TableColumnItem<CestujuciInfo>> tableColumnItems) {
        super(simulaciaWrapper, stage);
        getStage().setTitle(viewName);
        viewName_ = viewName;
        getStage().initModality(Modality.NONE);

        Helper.PridajTabulkeStlpce(tableViewNastupujuci, tableColumnItems);
        Helper.PridajTabulkeStlpce(tableViewCestujuci, tableColumnItems);
        Helper.PridajTabulkeStlpce(tableViewVystupujuci, tableColumnItems);
        tableDataNastupujuciLazy_ = tableViewNastupujuci.getItems();
        tableDataCestujuciLazy_ = tableViewCestujuci.getItems();
        tableDataVystupujuciLazy_ = tableViewVystupujuci.getItems();

        setOnOpen(() -> {
            tableViewNastupujuci.setItems(tableDataNastupujuciLazy_);
            tableViewCestujuci.setItems(tableDataCestujuciLazy_);
            tableViewVystupujuci.setItems(tableDataVystupujuciLazy_);
        });
    }

    @Override
    protected String getViewFileName() {
        return "cestujuciVozidlo.fxml";
    }

    @Override
    public String getViewName() {
        return viewName_;
    }

    public void setTableViewDataNastupujuciLazy(ObservableList<CestujuciInfo> list) {
        tableDataNastupujuciLazy_ = list;
        if (isActive()) {
            tableViewNastupujuci.setItems(list);
        }
    }

    public void setTableViewDataCestujuciLazy(ObservableList<CestujuciInfo> list) {
        tableDataCestujuciLazy_ = list;
        if (isActive()) {
            tableViewCestujuci.setItems(list);
        }
    }

    public void setTableViewDataVystupujuciLazy(ObservableList<CestujuciInfo> list) {
        tableDataVystupujuciLazy_ = list;
        if (isActive()) {
            tableViewVystupujuci.setItems(list);
        }
    }

    public void setTableViewDatasLazy(ObservableList<CestujuciInfo> nastupujuci, ObservableList<CestujuciInfo> cestujuci, ObservableList<CestujuciInfo> vystupujuci) {
        setTableViewDataNastupujuciLazy(nastupujuci);
        setTableViewDataCestujuciLazy(cestujuci);
        setTableViewDataVystupujuciLazy(vystupujuci);
    }

}

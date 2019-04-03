package GUI.Controller;

import GUI.CAS_UPDATU;
import GUI.IObserver;
import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Info.PoBehuReplikacieInfo;
import Simulacia.Model.Restauracia.Info.StatistikaInfo;
import Simulacia.SimulaciaRestauracia;
import Simulacia.SimulaciaWrapper;
import Utils.Helper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class CGrafyZavislosti extends ControllerBase implements IObserver {

    @FXML
    private JFXTextField textFieldPocetReplikaciiC;

    @FXML
    private JFXTextField textFieldPocetKucharov;

    @FXML
    private JFXTextField textFieldMinPocetCasnikov;

    @FXML
    private JFXTextField textFieldMaxPocetCasnikov;

    @FXML
    private JFXButton buttonSpustiZavislostCasnici;

    @FXML
    private JFXButton buttonStopCasnici;

    @FXML
    private JFXCheckBox checkBoxChladenieCasnik;

    @FXML
    private LineChart<Number, Number> lineChartZavislostCasnici;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistikaZavislostCasnik;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnNazovStatistikyC;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnHodnotaStatistikyC;

    @FXML
    private JFXTextField textFieldPocetReplikaciiK;

    @FXML
    private JFXTextField textFieldPocetCasnikov;

    @FXML
    private JFXTextField textFieldMinPocetKucharov;

    @FXML
    private JFXTextField textFieldMaxPocetKucharov;

    @FXML
    private JFXButton buttonSpustiZavislostKuchari;

    @FXML
    private JFXButton buttonStopKuchari;

    @FXML
    private JFXCheckBox checkBoxChladenieKuchar;

    @FXML
    private LineChart<Number, Number> lineChartZavislostKuchari;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistikaZavislostKuchar;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnNazovStatistikyK;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnHodnotaStatistikyK;

    private XYChart.Series<Number, Number> chartValuesCasniciZavislost_ = new XYChart.Series<>();
    private XYChart.Series<Number, Number> chartValuesKuchariZavislost_ = new XYChart.Series<>();


    private SimpleBooleanProperty pocetReplikaciiCOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty pocetKucharovCOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty minPocetCasnikovCOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty maxPocetCasnikovCOK = new SimpleBooleanProperty(false);

    private List<ReadOnlyBooleanProperty> simpleBooleanPropertiesCasnici = Arrays.asList(
            pocetReplikaciiCOK,
            pocetKucharovCOK,
            minPocetCasnikovCOK,
            maxPocetCasnikovCOK
    );

    private SimpleBooleanProperty pocetReplikaciiKOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty pocetCasnikovKOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty minPocetKucharovKOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty maxPocetKucharovKOK = new SimpleBooleanProperty(false);

    private List<ReadOnlyBooleanProperty> simpleBooleanPropertiesKuchari = Arrays.asList(
            pocetReplikaciiKOK,
            pocetCasnikovKOK,
            minPocetKucharovKOK,
            maxPocetKucharovKOK
    );

    private List<JFXTextField> textFieldsCasnici;
    private List<JFXTextField> textFieldsKuchari;



    public CGrafyZavislosti(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        initView();
        simulaciaWrapper.getSimulaciaTestZavislostiCasnici().registrujObservera(this);
        simulaciaWrapper.getSimulaciaTestZavislostiKuchari().registrujObservera(this);

        lineChartZavislostCasnici.getData().add(chartValuesCasniciZavislost_);

        textFieldsCasnici = Arrays.asList(
                textFieldPocetReplikaciiC,
                textFieldPocetKucharov,
                textFieldMinPocetCasnikov,
                textFieldMaxPocetCasnikov
        );
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetReplikaciiC, pocetReplikaciiCOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetKucharov, pocetKucharovCOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldMinPocetCasnikov, minPocetCasnikovCOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldMaxPocetCasnikov, maxPocetCasnikovCOK);


        lineChartZavislostKuchari.getData().add(chartValuesKuchariZavislost_);
        textFieldsKuchari = Arrays.asList(
                textFieldPocetReplikaciiK,
                textFieldPocetCasnikov,
                textFieldMinPocetKucharov,
                textFieldMaxPocetKucharov
        );

        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetReplikaciiK, pocetReplikaciiKOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetCasnikov, pocetCasnikovKOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldMinPocetKucharov, minPocetKucharovKOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldMaxPocetKucharov, maxPocetKucharovKOK);

        buttonSpustiZavislostCasnici.setOnAction(event -> {
            if (Helper.DisableButton(buttonSpustiZavislostCasnici, simpleBooleanPropertiesCasnici, () -> textFieldsCasnici.forEach(JFXTextField::validate))) {
                return;
            }
            long pocetReplikacii = Long.parseLong(textFieldPocetReplikaciiC.getText());
            long pocetKucharov = Long.parseLong(textFieldPocetKucharov.getText());
            long minPocetCasnikov = Long.parseLong(textFieldMinPocetCasnikov.getText());
            long maxPocetCasnikov = Long.parseLong(textFieldMaxPocetCasnikov.getText());

            this.tableViewStatistikaZavislostCasnik.getItems().clear();
            this.chartValuesCasniciZavislost_.getData().clear();
            simulaciaWrapper_.spustiZavislostCasnici(pocetReplikacii, pocetKucharov, minPocetCasnikov, maxPocetCasnikov, checkBoxChladenieCasnik.isSelected());

        });
        buttonStopCasnici.setOnAction(event -> {
            simulaciaWrapper.getSimulaciaTestZavislostiCasnici().stop();
        });

        buttonSpustiZavislostKuchari.setOnAction(event -> {
            if (Helper.DisableButton(buttonSpustiZavislostKuchari, simpleBooleanPropertiesKuchari, () -> textFieldsKuchari.forEach(JFXTextField::validate))) {
                return;
            }
            long pocetReplikacii = Long.parseLong(textFieldPocetReplikaciiK.getText());
            long pocetCasnikov = Long.parseLong(textFieldPocetCasnikov.getText());
            long minPocetKucharov = Long.parseLong(textFieldMinPocetKucharov.getText());
            long maxPocetKucharov = Long.parseLong(textFieldMaxPocetKucharov.getText());
            this.tableViewStatistikaZavislostKuchar.getItems().clear();
            this.chartValuesKuchariZavislost_.getData().clear();

            simulaciaWrapper_.spustiZavislostKuchari(pocetReplikacii, pocetCasnikov, minPocetKucharov, maxPocetKucharov, checkBoxChladenieKuchar.isSelected());

        });

        buttonStopKuchari.setOnAction(event -> {
            simulaciaWrapper.getSimulaciaTestZavislostiKuchari().stop();

        });

        tableColumnNazovStatistikyC.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNazovStatistiky()));
        tableColumnHodnotaStatistikyC.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHodnotaStatistiky()));

        tableColumnNazovStatistikyK.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNazovStatistiky()));
        tableColumnHodnotaStatistikyK.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHodnotaStatistiky()));

        Helper.InstallCopyPasteHandler(tableViewStatistikaZavislostCasnik);
        Helper.InstallCopyPasteHandler(tableViewStatistikaZavislostKuchar);
    }

    @Override
    protected void initView() {
        loadView();
    }

    @Override
    protected String getViewFileName() {
        return "grafyZavislosti.fxml";
    }

    @Override
    public String getViewName() {
        return "Grafy závislosti";
    }

    @Override
    public void update(SimulacneJadro simulacneJadro, CAS_UPDATU casUpdatu) {
        SimulaciaRestauracia simulacia = (SimulaciaRestauracia) simulacneJadro;
        if (simulaciaWrapper_.getSimulaciaTestZavislostiCasnici() == simulacia) {
            if (casUpdatu == CAS_UPDATU.PO_SIMULACII) {
                int pocetCasnikov = simulacia.getRestauracia().getPocetVsetkychCasnikov();
                PoBehuReplikacieInfo posimulacneInfo = simulacia.getStatistikyPoBehuReplikacie();
                Platform.runLater( () -> {
                    this.chartValuesCasniciZavislost_.getData().add(new XYChart.Data<>(pocetCasnikov, posimulacneInfo.priemernyCasCakaniaZakaznikov_));
                    this.tableViewStatistikaZavislostCasnik.getItems().add(new StatistikaInfo("Štatistika pre počet čašníkov: " + pocetCasnikov, ""));
                    this.tableViewStatistikaZavislostCasnik.getItems().addAll(posimulacneInfo.statistiky_);
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (simulaciaWrapper_.getSimulaciaTestZavislostiKuchari() == simulacia) {
            if (casUpdatu == CAS_UPDATU.PO_SIMULACII) {
                int pocetKucharov = simulacia.getRestauracia().getPocetVsetkychKucharov();
                PoBehuReplikacieInfo posimulacneInfo = simulacia.getStatistikyPoBehuReplikacie();
                Platform.runLater(() -> {
                    this.chartValuesKuchariZavislost_.getData().add(new XYChart.Data<>(pocetKucharov, posimulacneInfo.priemernyCasCakaniaZakaznikov_));
                    this.tableViewStatistikaZavislostKuchar.getItems().add(new StatistikaInfo("Štatistika pre počet kuchárov: " + pocetKucharov, ""));
                    this.tableViewStatistikaZavislostKuchar.getItems().addAll(posimulacneInfo.statistiky_);
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}

package GUI.Controller;

import Model.Info.BehReplikacieInfo;
import Model.Info.BehSimulacieInfo;
import Model.Info.CestujuciInfo;
import Model.Info.VozidloInfo;
import Model.Vozidlo;
import Model.ZastavkaKonfiguracia;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import Statistiky.StatistikaInfo;
import Utils.Helper;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import simulation.SimulaciaDopravy;
import simulation.SimulaciaWrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSimulacia extends ControllerBase implements ISimDelegate {

    @FXML
    private JFXTextField textFieldReplications;

    @FXML
    private JFXButton buttonStart;

    @FXML
    private JFXButton buttonStop;

    @FXML
    private JFXButton buttonKonfiguraciaVozidiel;

    @FXML
    private JFXButton buttonPause;

    @FXML
    private JFXButton buttonResume;

    @FXML
    private JFXCheckBox checkBoxNormalny;

    @FXML
    private JFXCheckBox checkBoxZrychleny;

    @FXML
    private JFXCheckBox checkBoxKrokovanie;

    @FXML
    private JFXSlider sliderSpomalenie;

    @FXML
    private JFXSlider sliderInterval;

    @FXML
    private JFXSlider sliderStep;

    @FXML
    private JFXSlider sliderSkip;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private JFXCheckBox checkBoxStatistikyReplikacie;

    @FXML
    private JFXCheckBox checkBoxStatistikySimulacie;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistiky;
    ObservableList<StatistikaInfo> statistikyReplikacieData_ = FXCollections.observableArrayList();
    ObservableList<StatistikaInfo> statistikySimulacieData_ = FXCollections.observableArrayList();

    @FXML
    private TableView<VozidloInfo> tableViewVozidla;

    @FXML
    private JFXTabPane tabPaneZastavky;

    HashMap<String, CTableHolder<CestujuciInfo>> zastavkyInfo_ = new HashMap<>();

    private SimpleBooleanProperty isReplicationOK = new SimpleBooleanProperty(false);


    private List<ReadOnlyBooleanProperty> simpleBooleanProperties = Arrays.asList(
            isReplicationOK
    );

    private List<JFXTextField> textFields;
    private List<JFXCheckBox> checkBoxesToDisable;

    private CKonfiguracie _oknoKonfiguracie = null;
    private SimulaciaDopravy _simulacia;

    public CSimulacia(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);

        _simulacia = simulaciaWrapper.getSimulaciaDopravy();
        _simulacia.registerDelegate(this);
        _oknoKonfiguracie = new CKonfiguracie(simulaciaWrapper, stage);

        // TABULKY
        Helper.PridajTabulkeStlpce(tableViewStatistiky, StatistikaInfo.ATRIBUTY);
        Helper.InstallCopyPasteHandler(tableViewStatistiky);

        Helper.PridajTabulkeStlpce(tableViewVozidla, VozidloInfo.ATRIBUTY);
        Helper.InstallCopyPasteHandler(tableViewVozidla);

        TableColumn zakazniciCol = new TableColumn("Cestujúci");

        Callback<TableColumn<VozidloInfo, String>, TableCell<VozidloInfo, String>> cellFactory
                = //
                new Callback<TableColumn<VozidloInfo, String>, TableCell<VozidloInfo, String>>() {
                    @Override
                    public TableCell call(final TableColumn<VozidloInfo, String> param) {
                        final TableCell<VozidloInfo, String> cell = new TableCell<VozidloInfo, String>() {
                            HBox hbox = new HBox();
                            JFXButton buttonZakaznici = new JFXButton("Cestujúci");

                            {
                                buttonZakaznici.setStyle("-fx-background-color: #99ffff; ");
                                hbox.setAlignment(Pos.CENTER);
                                hbox.getChildren().add(buttonZakaznici);
                            }
                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonZakaznici.setOnAction(event -> {
                                        VozidloInfo vozidloInfo = getTableView().getItems().get(getIndex());
                                        CTableHolder<VozidloInfo> holder = new CTableHolder<>(simulaciaWrapper_, stage, "Vozidlá", VozidloInfo.ATRIBUTY);
                                        holder.setModality(Modality.WINDOW_MODAL);
                                        ObservableList<VozidloInfo> data = FXCollections.observableArrayList();

                                        data.add(vozidloInfo);

                                        holder.setTableViewData(data);
                                        holder.openWindow();
                                        System.out.println("Id vozidla: " + vozidloInfo.getIdVozidla());
                                    });
                                    setGraphic(hbox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        zakazniciCol.setCellFactory(cellFactory);
        zakazniciCol.setMinWidth(100.0);
        zakazniciCol.setMaxWidth(150.0);
        zakazniciCol.setSortable(false);
        tableViewVozidla.getColumns().add(zakazniciCol);

        // CHECKBOXY
        checkBoxNormalny.selectedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue) {
                checkBoxKrokovanie.setDisable(false);
            }
            if (!newValue && !checkBoxZrychleny.isSelected()) {
                checkBoxZrychleny.setSelected(true);
            }
        });
        checkBoxZrychleny.selectedProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue) {
                checkBoxKrokovanie.setDisable(true);
                checkBoxKrokovanie.setSelected(false);
            }
            if (!newValue && !checkBoxNormalny.isSelected()) {
                checkBoxNormalny.setSelected(true);
            }

        });
        checkBoxNormalny.setSelected(true);

        Helper.nastavVypnutieOstatnych(checkBoxNormalny, checkBoxZrychleny);

        Helper.nastavVypnutieOstatnych(checkBoxZrychleny, checkBoxNormalny);


        checkBoxStatistikyReplikacie.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                tableViewStatistiky.setItems(statistikyReplikacieData_);
            }
        });
        checkBoxStatistikySimulacie.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                tableViewStatistiky.setItems(statistikySimulacieData_);
            }
        });

        Helper.nastavVypnutieOstatnych(checkBoxStatistikyReplikacie, checkBoxStatistikySimulacie);
        Helper.nastavVypnutieOstatnych(checkBoxStatistikySimulacie ,checkBoxStatistikyReplikacie);
        checkBoxStatistikyReplikacie.setSelected(true);

        checkBoxesToDisable = Arrays.asList(checkBoxNormalny, checkBoxZrychleny);

        checkBoxKrokovanie.selectedProperty().addListener( (observable, oldValue, newValue) -> {
            _simulacia.setKrokovanie(newValue);
        });

        // TEXTFIELDS
        textFields = Arrays.asList(
                textFieldReplications
        );

        Helper.DecorateNumberTextFieldWithValidator(textFieldReplications, isReplicationOK);
        textFieldReplications.setText("1");


        // BUTTONS
        buttonStart.setOnAction(event -> {
            if (Helper.DisableButton(buttonStart, simpleBooleanProperties, () -> textFields.forEach(JFXTextField::validate))) {
                return;
            }
            //simulaciaWrapper_.zastavSimulaciu();
            //computation.cancel();
            int pocetReplikacii = Integer.parseInt(textFieldReplications.getText());
            double koncovyCasReplikacie = Helper.CASOVE_JEDNOTKY.DEN.getPocetSekund();

            _simulacia.setKrokovanie(checkBoxKrokovanie.isSelected());

            _simulacia.simulateAsync(pocetReplikacii, koncovyCasReplikacie);

            buttonStart.disableProperty().unbind();
        });


        buttonStop.setOnAction(event -> {
            _simulacia.stopSimulation();

        });

        buttonPause.setOnAction(event -> {
            _simulacia.pauseSimulation();
        });

        buttonResume.setOnAction(event -> {
            _simulacia.resumeSimulation();
        });

        buttonKonfiguraciaVozidiel.setOnAction(event -> {
            _oknoKonfiguracie.openWindow();
        });

        List<ZastavkaKonfiguracia> zastavky = _simulacia.getZoznamZastavok();

        for (ZastavkaKonfiguracia zastavka: zastavky) {
            CTableHolder<CestujuciInfo> statistikaInfoCTableHolder = new CTableHolder<>(simulaciaWrapper_, stage, zastavka.getNazovZastavky(), CestujuciInfo.ATRIBUTY);
            zastavkyInfo_.put(zastavka.getNazovZastavky(), statistikaInfoCTableHolder);

            tabPaneZastavky.getTabs().add(statistikaInfoCTableHolder.getTab());
        }

        _simulacia.onSimulationWillStart(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(true));
            buttonStart.setDisable(true);
            buttonKonfiguraciaVozidiel.setDisable(true);

            statistikyReplikacieData_.clear();
            statistikySimulacieData_.clear();

        });

        _simulacia.onReplicationWillStart(s -> {
            setSimulationSpeed();

            Platform.runLater(() -> {
                zastavkyInfo_.forEach( (n, holder) -> { holder.clearTableViewData();});
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });


        _simulacia.onReplicationDidFinish(s -> {

            BehSimulacieInfo behSimulacieInfo = _simulacia.getStatistikySimulacie();


            Platform.runLater(() -> {
                statistikySimulacieData_.clear();
                statistikySimulacieData_.addAll(behSimulacieInfo.statistiky_);
            });
            if (checkBoxStatistikySimulacie.isSelected()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // TODO vypis globalnych statistik
        });

        _simulacia.onSimulationDidFinish(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(false));
            buttonStart.setDisable(false);
            buttonKonfiguraciaVozidiel.setDisable(false);
        });

        sliderInterval.valueProperty().addListener((observable, oldValue, newValue) -> {
            setSimulationSpeed();
        });
        sliderSpomalenie.valueProperty().addListener((observable, oldValue, newValue) -> {
            setSimulationSpeed();
        });


    }

    @Override
    protected String getViewFileName() {
        return "simulacia.fxml";
    }

    @Override
    public String getViewName() {
        return "Simulácia";
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation simulation) {
        //System.out.println(_simulacia.currentTime());
        double simTime = _simulacia.currentTime();

        BehReplikacieInfo behReplikacieInfo = _simulacia.getStatistikyVRamciPreplikacie();

        Platform.runLater(() -> {
            statistikyReplikacieData_.clear();
            statistikyReplikacieData_.addAll(behReplikacieInfo.statistiky_);
            tableViewVozidla.setItems(behReplikacieInfo.vozidlaInfo_);

            for (Map.Entry<String, CTableHolder<CestujuciInfo>> zastavkaEntry: zastavkyInfo_.entrySet()) {

                String nazovZastavky = zastavkaEntry.getKey();
                CTableHolder<CestujuciInfo> tableHolder = zastavkaEntry.getValue();
                if (_simulacia.isKrokovanie()) {
                    tableHolder.setTableViewData(behReplikacieInfo.cestujuciInfo_.get(nazovZastavky));
                } else {
                    tableHolder.setTableViewDataLazy(behReplikacieInfo.cestujuciInfo_.get(nazovZastavky));
                }

            }


        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setSimulationSpeed() {
        if (!checkBoxZrychleny.isSelected()) {
            double spomalenieCasu = sliderSpomalenie.getValue() * 0.001;
            double interval =  sliderInterval.getValue() * 0.01;
            _simulacia.setSimSpeed(interval, spomalenieCasu);
        } else {
            _simulacia.setMaxSimSpeed();
        }
    }

}

package GUI.Controller;

import Model.Info.*;
import Model.VozidloKonfiguracia;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import simulation.SimulaciaDopravy;
import simulation.SimulaciaWrapper;

import java.util.*;

public class CSimulacia extends ControllerBase implements ISimDelegate {

    private static class ZastavkaInfo {
        public CTableHolder<CestujuciInfo> cestujuciInfoHolder;
        public CTableHolder<VozidloInfo> vozidlaInfoHolder;

        public ZastavkaInfo(CTableHolder<CestujuciInfo> cestujuciInfoHolder, CTableHolder<VozidloInfo> vozidlaInfoHolder) {
            this.cestujuciInfoHolder = cestujuciInfoHolder;
            this.vozidlaInfoHolder = vozidlaInfoHolder;
        }

        public void clearTableViewData() {
            cestujuciInfoHolder.clearTableViewData();
            vozidlaInfoHolder.clearTableViewData();
        }

    }

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
    private JFXCheckBox checkBoxStatistikyReplikacie;

    @FXML
    private JFXCheckBox checkBoxStatistikySimulacie;

    @FXML
    private LineChart<Number, Number> lineChartCakanieNaZastavke;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistiky;
    ObservableList<StatistikaInfo> statistikyReplikacieData_ = FXCollections.observableArrayList();
    ObservableList<StatistikaInfo> statistikySimulacieData_ = FXCollections.observableArrayList();

    @FXML
    private TableView<VozidloInfo> tableViewVozidla;

    @FXML
    private JFXTextField textFieldFilter;

    @FXML
    private JFXButton buttonVozidlaNaZastavke;

    @FXML
    private JFXTabPane tabPaneZastavky;

    private HashMap<String, ZastavkaInfo> zastavkyInfo_ = new HashMap<>();
    private HashMap<Long, CCestujuciVozidlo> cestujuciInfo_ = new HashMap<>();

    private XYChart.Series<Number, Number> chartValuesCasCakaniaCestujucehoRep_ = new XYChart.Series<>();
    private XYChart.Series<Number, Number> chartValuesCasCakaniaCestujucehoSim_ = new XYChart.Series<>();

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
        //KonfiguraciaVozidiel konfiguraciaVozidiel = new KonfiguraciaVozidiel(null, null);
        //_simulacia.nacitajKonfiguraciuVozidiel("vychodzie.csv", konfiguraciaVozidiel);
        //_oknoKonfiguracie.konfiguraciaToGUI(konfiguraciaVozidiel);

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
                                        CCestujuciVozidlo cestujuciVozidlo = cestujuciInfo_.get(vozidloInfo.getIdVozidla());
                                        cestujuciVozidlo.openWindow();

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
        checkBoxNormalny.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkBoxKrokovanie.setDisable(false);
            }
            if (!newValue && !checkBoxZrychleny.isSelected()) {
                checkBoxZrychleny.setSelected(true);
            }
        });
        checkBoxZrychleny.selectedProperty().addListener((observable, oldValue, newValue) -> {
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
                Platform.runLater(() -> {
                    tableViewStatistiky.setItems(statistikyReplikacieData_);
                    lineChartCakanieNaZastavke.getData().clear();
                    lineChartCakanieNaZastavke.getData().add(chartValuesCasCakaniaCestujucehoRep_);
                    lineChartCakanieNaZastavke.getXAxis().setLabel("Čas replikácie");
                });
            }
            if (!newValue && !checkBoxStatistikySimulacie.isSelected()) {
                checkBoxStatistikySimulacie.setSelected(true);
            }
        });
        checkBoxStatistikySimulacie.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> {
                    tableViewStatistiky.setItems(statistikySimulacieData_);
                    Platform.runLater(() -> {
                        lineChartCakanieNaZastavke.getData().clear();
                        lineChartCakanieNaZastavke.getData().add(chartValuesCasCakaniaCestujucehoSim_);
                        lineChartCakanieNaZastavke.getXAxis().setLabel("Číslo replikácie");
                    });
                });

            }
            if (!newValue && !checkBoxStatistikyReplikacie.isSelected()) {
                checkBoxStatistikyReplikacie.setSelected(true);
            }
        });

        Helper.nastavVypnutieOstatnych(checkBoxStatistikyReplikacie, checkBoxStatistikySimulacie);
        Helper.nastavVypnutieOstatnych(checkBoxStatistikySimulacie, checkBoxStatistikyReplikacie);
        checkBoxStatistikyReplikacie.setSelected(true);
        
        checkBoxesToDisable = Arrays.asList(checkBoxNormalny, checkBoxZrychleny);

        checkBoxKrokovanie.selectedProperty().addListener((observable, oldValue, newValue) -> {
            _simulacia.setKrokovanie(newValue);
        });

        // TEXTFIELDS
        textFields = Arrays.asList(
                textFieldReplications
        );

        Helper.DecorateNumberTextFieldWithValidator(textFieldReplications, isReplicationOK);
        textFieldReplications.setText("1");
        Helper.SetActionOnEnter(Arrays.asList(textFieldReplications), () -> {
            buttonStart.fire();
        });

        // BUTTONS
        buttonStart.setOnAction(event -> {
            if (Helper.DisableButton(buttonStart, simpleBooleanProperties, () -> textFields.forEach(JFXTextField::validate))) {
                return;
            }
            //simulaciaWrapper_.zastavSimulaciu();
            //computation.cancel();
            int pocetReplikacii = Integer.parseInt(textFieldReplications.getText());
            double koncovyCasReplikacie = Double.MAX_VALUE;

            _simulacia.setKrokovanie(checkBoxKrokovanie.isSelected());

            _simulacia.setKonfiguracia(_oknoKonfiguracie.getKonfiguracie());

            if (_simulacia.getKonfiguraciaVozidiel().getPrevadzkaLiniek() == null) {
                showWarningDialog("Nesprávna konfigurácia");
                return;
            }

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

        TreeMap<String, ZastavkaKonfiguracia> zastavky = _simulacia.getZastavkyKonfiguracia();

        for (Map.Entry<String, ZastavkaKonfiguracia> zastavkaEntry : zastavky.entrySet()) {
            ZastavkaKonfiguracia zastavka = zastavkaEntry.getValue();
            CTableHolder<CestujuciInfo> cestujuciInfoHolder = new CTableHolder<>(simulaciaWrapper_, stage, zastavka.getNazovZastavky(), CestujuciInfo.ATRIBUTY, cestujuciInfo -> true);
            CTableHolder<VozidloInfo> vozidloInfoHolder = new CTableHolder<>(simulaciaWrapper, stage, "Vozidlá na zastávke " + zastavka.getNazovZastavky(), VozidloInfo.ATRIBUTY_FRONT_VOZIDIEL, vozidloInfo -> true);

            ZastavkaInfo zastavkaInfo = new ZastavkaInfo(cestujuciInfoHolder, vozidloInfoHolder);

            zastavkyInfo_.put(zastavka.getNazovZastavky(), zastavkaInfo);

            tabPaneZastavky.getTabs().add(cestujuciInfoHolder.getTab());
        }

        textFieldFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            for (Map.Entry<String, ZastavkaInfo> holderEntry : zastavkyInfo_.entrySet()) {
                CTableHolder<CestujuciInfo> holder = holderEntry.getValue().cestujuciInfoHolder;
                holder.setPredicateForFiltering(cestujuciInfo -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String filterText = textFieldFilter.getText().toLowerCase();
                    String idCestujuceho = String.valueOf(cestujuciInfo.getId());
                    String zastavakCestujuceho = cestujuciInfo.getZastavkaNaKtoruPrisiel().toLowerCase();
                    return idCestujuceho.startsWith(filterText) || zastavakCestujuceho.startsWith(filterText);
                });
            }
        });

        buttonVozidlaNaZastavke.setOnAction(event -> {
            for (Map.Entry<String, ZastavkaInfo> holderEntry : zastavkyInfo_.entrySet()) {
                CTableHolder<CestujuciInfo> cestujuciHolder = holderEntry.getValue().cestujuciInfoHolder;
                if (cestujuciHolder.isSelected()) {
                    System.out.println("Nazov zastavky: " + holderEntry.getKey());
                    holderEntry.getValue().vozidlaInfoHolder.openWindow();
                    break;
                }
            }
        });

        _simulacia.onSimulationWillStart(s -> {



            Platform.runLater(() -> {
                checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(true));
                buttonStart.setDisable(true);
                buttonKonfiguraciaVozidiel.setDisable(true);
                sliderSkip.setDisable(true);
                statistikyReplikacieData_.clear();
                statistikySimulacieData_.clear();


                cestujuciInfo_.clear();
                chartValuesCasCakaniaCestujucehoSim_.getData().clear();

                tableViewVozidla.getItems().clear();
            }); // TODO

            ArrayList<VozidloKonfiguracia> vozidla = _simulacia.getKonfiguraciaVozidiel().getKonfiguraciaVozidiel();

            Platform.runLater(() -> {
                long indexVozidla = 1;
                for (VozidloKonfiguracia vozidlo : vozidla) {
                    cestujuciInfo_.put(indexVozidla, new CCestujuciVozidlo(simulaciaWrapper, stage, "Cestujúci vozidlo: " + indexVozidla, CestujuciInfo.ATRIBUTY));
                    indexVozidla++;
                }
            });


        });

        _simulacia.onReplicationWillStart(s -> {
            setSimulationSpeed();
            Platform.runLater(() -> {
                textFieldFilter.setText("");
            });

            if (!checkBoxZrychleny.isSelected()) {
                Platform.runLater(() -> {
                    zastavkyInfo_.forEach((n, holder) -> {
                        holder.clearTableViewData();
                    });
                    chartValuesCasCakaniaCestujucehoRep_.getData().clear();
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (_simulacia.currentReplication() == 0) {
                Platform.runLater(() -> {
                    zastavkyInfo_.forEach((n, holder) -> {
                        holder.clearTableViewData();
                    });
                    chartValuesCasCakaniaCestujucehoRep_.getData().clear();
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });


        _simulacia.onReplicationDidFinish(s -> {


            int kolkoVynechat = (int) Math.floor(((double)_simulacia.replicationCount() / 100.0) * (sliderSkip.getValue()));
            long step = (long) Math.floor(sliderStep.getValue());


            BehSimulacieInfo behSimulacieInfo = _simulacia.getStatistikySimulacie();
            //System.out.println("Replikacia: " + behSimulacieInfo._cisloReplikacie);
            if ((_simulacia.currentReplication() + 1) > kolkoVynechat && ((_simulacia.currentReplication() + 1) == _simulacia.replicationCount() || _simulacia.currentReplication() % step == 0)) {
                Platform.runLater(() -> {
                    statistikySimulacieData_.clear();
                    for (StatistikaInfo statistika: behSimulacieInfo.statistiky_) {
                        statistikySimulacieData_.add(statistika);
                    }
                    for (StatistikaInfo statistika: behSimulacieInfo.statistikyZastavky_) {
                        statistikySimulacieData_.add(statistika);
                    }
                    for (StatistikaInfo statistika: behSimulacieInfo.statistikyVozidla_) {
                        statistikySimulacieData_.add(statistika);
                    }
                    chartValuesCasCakaniaCestujucehoSim_.getData().add(new XYChart.Data<>(behSimulacieInfo._cisloReplikacie, behSimulacieInfo._priemernyCasCakaniaNaZastavke));
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        _simulacia.onSimulationDidFinish(s -> {
            Platform.runLater(() -> {
                checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(false));
                buttonStart.setDisable(false);
                buttonKonfiguraciaVozidiel.setDisable(false);
                sliderSkip.setDisable(false);
                cestujuciInfo_.forEach((id, info) -> {
                    if (info.isActive()) {
                        info.closeWindow();
                    }
                });
                zastavkyInfo_.forEach((nazov, zastavkaInfo) -> {
                    if (zastavkaInfo.vozidlaInfoHolder.isActive()) {
                        zastavkaInfo.vozidlaInfoHolder.closeWindow();
                    }
                });
            });

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

        BehReplikacieInfo behReplikacieInfo = _simulacia.getStatistikyVRamciReplikacie();

        int kolkoVynechat = (int) Math.floor(((double)_simulacia.getCasZaciatkuZapasu() / 100.0) * (sliderSkip.getValue()));


        if (simTime > kolkoVynechat ) {
            Platform.runLater(() -> {
                statistikyReplikacieData_.clear();
                statistikyReplikacieData_.addAll(behReplikacieInfo.statistiky_);
                tableViewVozidla.setItems(behReplikacieInfo.vozidlaInfo_);


                chartValuesCasCakaniaCestujucehoRep_.getData().add(new XYChart.Data<>(simTime ,behReplikacieInfo._priemernyCasCakaniaCestujucehoNaZastavke));

                for (VozidloInfo vozidloInfo : behReplikacieInfo.vozidlaInfo_) {
                    CCestujuciVozidlo cestujuciVozidlo = cestujuciInfo_.get(vozidloInfo.getIdVozidla());

                    cestujuciVozidlo.setTableViewDatasLazy(
                            vozidloInfo.getNastupujuci(),
                            vozidloInfo.getCestujuci(),
                            vozidloInfo.getVystupujuci()
                    );
                }

                for (Map.Entry<String, ZastavkaInfo> zastavkaEntry : zastavkyInfo_.entrySet()) {

                    String nazovZastavky = zastavkaEntry.getKey();
                    CTableHolder<CestujuciInfo> cestujuciInfoHolder = zastavkaEntry.getValue().cestujuciInfoHolder;
                    CTableHolder<VozidloInfo> vozidloInfoHolder = zastavkaEntry.getValue().vozidlaInfoHolder;
                    Model.Info.ZastavkaInfo zastavkaInfo = behReplikacieInfo.zastavkyInfo_.get(nazovZastavky);
                    cestujuciInfoHolder.setTableViewDataLazy(zastavkaInfo.getCestujuciInfo());
                    vozidloInfoHolder.setTableViewDataLazy(zastavkaInfo.getVozidloInfo()); // TODO
                }


            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private void setSimulationSpeed() {
        if (!checkBoxZrychleny.isSelected()) {
            double spomalenieCasu = sliderSpomalenie.getValue() * 0.001;
            double interval = sliderInterval.getValue() * 0.01;
            _simulacia.setSimSpeed(interval, spomalenieCasu);
        } else {
            _simulacia.setMaxSimSpeed();
        }
    }

}

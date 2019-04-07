package GUI.Controller;

import GUI.CAS_UPDATU;
import GUI.IObserver;
import Simulacia.Jadro.SimulacneJadro;
import Simulacia.Model.Restauracia.Info.BehReplikacieInfo;
import Simulacia.Model.Restauracia.Info.PoBehuReplikacieInfo;
import Simulacia.Model.Restauracia.Info.StatistikaInfo;
import Simulacia.Model.Restauracia.Restauracia;
import Simulacia.SimulaciaRestauracia;
import Simulacia.SimulaciaWrapper;
import Simulacia.Statistiky.StatistikaPriemer;
import Utils.Helper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSimulacia extends ControllerBase implements IObserver {

    @FXML
    private JFXTextField textFieldReplications;

    @FXML
    private JFXTextField textFieldPocetDniReplikacie;

    @FXML
    private JFXTextField textFieldPosunSimCasu;

    @FXML
    private JFXTextField textFieldPocetCasnikov;

    @FXML
    private JFXTextField textFieldPocetKucharov;

    @FXML
    private JFXButton buttonStart;

    @FXML
    private JFXButton buttonStop;

    @FXML
    private JFXButton buttonPause;

    @FXML
    private JFXButton buttonResume;

    @FXML
    private JFXButton buttonStavObjektov;

    @FXML
    private JFXCheckBox checkBoxNormalny;

    @FXML
    private JFXCheckBox checkBoxZrychleny;

    @FXML
    private JFXCheckBox checkBoxKrokovanie;

    @FXML
    private JFXCheckBox checkBoxChladenie;

    @FXML
    private JFXCheckBox checkBoxPlanujPosunCasu;

    @FXML
    private Label labelLenTak;

    @FXML
    private JFXSlider sliderSpomalenie;

    @FXML
    private JFXSlider sliderStep;

    @FXML
    private JFXSlider sliderSkip;

    @FXML
    private Label labelCisloReplikacie;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistikyR;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnNazovStatistikyR;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnHodnotaStatistikyR;

    @FXML
    private TableView<StatistikaInfo> tableViewStatistikyS;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnNazovStatistikyS;

    @FXML
    private TableColumn<StatistikaInfo, String> tableColumnHodnotaStatistikyS;


    private boolean tabSelected = false;

    private XYChart.Series<Number, Number> chartValues_ = new XYChart.Series<>();

    private SimpleBooleanProperty isReplicationOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isReplicationDaysOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isPosunOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isCasnikOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isKucharOK = new SimpleBooleanProperty(false);

    private List<ReadOnlyBooleanProperty> simpleBooleanProperties = Arrays.asList(
            isReplicationOK,
            isPosunOK,
            isCasnikOK,
            isKucharOK
    );

    private List<JFXTextField> textFields;
    private List<JFXCheckBox> checkBoxesToDisable;
    private CStavObjektov stavObjektov_ = null;

    public CSimulacia(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        initView();
        stavObjektov_ = new CStavObjektov(simulaciaWrapper, stage);

        simulaciaWrapper.getSimulacia().registrujObservera(this);



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

        checkBoxKrokovanie.selectedProperty().addListener( (observable, oldValue, newValue) -> {
            simulaciaWrapper.getSimulacia().setKrokovanieSimulacie(newValue);
        });

        checkBoxNormalny.setSelected(true);
        checkBoxPlanujPosunCasu.setSelected(true);

        Helper.nastavVypnutieOstatnych(checkBoxNormalny, checkBoxZrychleny);

        Helper.nastavVypnutieOstatnych(checkBoxZrychleny, checkBoxNormalny);
        checkBoxesToDisable = Arrays.asList(checkBoxNormalny, checkBoxZrychleny, checkBoxChladenie, checkBoxPlanujPosunCasu);

        lineChart.getData().add(chartValues_);

        textFields = Arrays.asList(
                textFieldReplications,
                textFieldPocetDniReplikacie,
                textFieldPosunSimCasu,
                textFieldPocetCasnikov,
                textFieldPocetKucharov
        );

        Helper.DecorateNumberTextFieldWithValidator(textFieldReplications, isReplicationOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetDniReplikacie, isReplicationDaysOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPosunSimCasu, isPosunOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetCasnikov, isCasnikOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldPocetKucharov, isKucharOK);
        textFieldReplications.setText("1");
        textFieldPocetDniReplikacie.setText("1");
        textFieldPosunSimCasu.setText("10000");
        textFieldPocetCasnikov.setText("2");
        textFieldPocetKucharov.setText("2");

        StatistikaInfo s = new StatistikaInfo("", "");

        Helper.PridajTabulkeStlpce(tableViewStatistikyR, StatistikaInfo.ATRIBUTY);
        //tableColumnNazovStatistikyR.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNazovStatistiky()));
        //tableColumnHodnotaStatistikyR.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHodnotaStatistiky()));

        tableColumnNazovStatistikyS.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNazovStatistiky()));
        tableColumnHodnotaStatistikyS.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHodnotaStatistiky()));

        Helper.InstallCopyPasteHandler(tableViewStatistikyR);
        Helper.InstallCopyPasteHandler(tableViewStatistikyS);

        buttonStart.setOnAction(event -> {
            if (Helper.DisableButton(buttonStart, simpleBooleanProperties, () -> textFields.forEach(JFXTextField::validate))) {
                return;
            }
            //simulaciaWrapper_.zastavSimulaciu();
            //computation.cancel();
            long pocetReplikacii = Long.parseLong(textFieldReplications.getText());
            long pocetDniReplikacie = Long.parseLong(textFieldPocetDniReplikacie.getText());
            long jednotkaSimulacnehoCasu = Long.parseLong(textFieldPosunSimCasu.getText());
            int pocetCasnikov = (int) Long.parseLong(textFieldPocetCasnikov.getText());
            int pocetKucharov = (int) Long.parseLong(textFieldPocetKucharov.getText());

            long spomalenieCasu = (long)sliderSpomalenie.getValue();

            chartValues_.getData().clear();

            simulaciaWrapper.spustiSimulaciu(
                            pocetCasnikov,
                            pocetKucharov,
                            Restauracia.DENNA_OTVARACIA_DOBA * pocetDniReplikacie,
                            jednotkaSimulacnehoCasu,
                            spomalenieCasu,
                            pocetReplikacii,
                            checkBoxZrychleny.isSelected(),
                            checkBoxKrokovanie.isSelected(),
                            checkBoxChladenie.isSelected(),
                            checkBoxPlanujPosunCasu.isSelected()
            );
            buttonStart.disableProperty().unbind();
        });

        buttonStop.setOnAction(event -> {

            simulaciaWrapper_.stop();

        });

        buttonPause.setOnAction( event -> {

            simulaciaWrapper_.pause();
        } );

        buttonResume.setOnAction(event -> {

            simulaciaWrapper_.resume();

        });



        buttonStavObjektov.disableProperty().bind(checkBoxKrokovanie.selectedProperty().not());
        buttonStavObjektov.setOnAction(event -> {
            if (!simulaciaWrapper.getSimulacia().spustenaSimulacia()) {
                showWarningDialog("Simulácia nie je spustená!");
                return;
            }
            stavObjektov_.nacitajStavObjektov(simulaciaWrapper.getSimulacia(), false);
            stavObjektov_.openWindow();

        });


        //labelLenTak.textProperty().bind(simulaciaWrapper.simulacnyCas.asString());
        sliderSpomalenie.valueProperty().addListener((observable, oldValue, newValue) -> {
            simulaciaWrapper.setSpomalenieCasu(newValue.longValue());
        });


    }

    @Override
    protected void initView() {
        loadView();
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
    public Runnable getRunnableOnSelection() {
        return () -> {
            tabSelected = true;
        };
    }

    @Override
    public Runnable getRunnableOnUnSelection() {
        return () -> {
            tabSelected = false;
        };
    }

    @Override
    public void update(SimulacneJadro simulacneJadro, CAS_UPDATU casUpdatu) {
        SimulaciaRestauracia simulaciaRestauracia = (SimulaciaRestauracia) simulacneJadro;

        if (casUpdatu == CAS_UPDATU.PRED_SIMULACIOU) {
            Platform.runLater(() -> {
                labelCisloReplikacie.setText("0");
                checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(true));
                buttonStart.setDisable(true);
                sliderSkip.setDisable(true);

                chartValues_.getData().clear();
                tableViewStatistikyR.getItems().clear();
                tableViewStatistikyS.getItems().clear();
            });
        }

        if (casUpdatu == casUpdatu.PRED_REPLIKACIOU) {
            long cisloRepliakcie = simulacneJadro.getCisloReplikacie();
            Platform.runLater(() -> {
                labelCisloReplikacie.setText(String.format("%d", cisloRepliakcie));
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (casUpdatu == CAS_UPDATU.PO_REPLIKACII) {
            PoBehuReplikacieInfo poBehuReplikacieInfo = simulaciaRestauracia.getStatistikyPoBehuReplikacie();
            int kolkoVynechat = (int) Math.floor(((double)poBehuReplikacieInfo.celkovyPocetReplikacii_ / 100.0) * (sliderSkip.getValue()));
            long pocetReplikacii = simulaciaRestauracia.getPocetReplikacii();
            int step = (int) sliderStep.getValue();
            Platform.runLater(() -> {
                if (poBehuReplikacieInfo.cisloReplikacie_ > kolkoVynechat) {

                    if (poBehuReplikacieInfo.cisloReplikacie_ % step == 0 || poBehuReplikacieInfo.cisloReplikacie_ == pocetReplikacii) {
                        chartValues_.getData().add(new XYChart.Data<>(poBehuReplikacieInfo.cisloReplikacie_, poBehuReplikacieInfo.priemernyCasCakaniaZakaznikov_ ));
                        tableViewStatistikyS.setItems(poBehuReplikacieInfo.statistiky_);
                    }

                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        if (casUpdatu == CAS_UPDATU.POCAS_SIMULACIE_REPLIKACIE) {
            BehReplikacieInfo behReplikacieInfo = simulaciaRestauracia.getStatistikyVRamciReplikacie();
            Platform.runLater(() -> {
                 tableViewStatistikyR.setItems(behReplikacieInfo.statistiky_);
            });

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (casUpdatu == CAS_UPDATU.PO_SIMULACII) {
            Platform.runLater(() -> {
                checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(false));
                buttonStart.setDisable(false);
                sliderSkip.setDisable(false);
            });
        }

    }
}

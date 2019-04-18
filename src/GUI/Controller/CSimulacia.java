package GUI.Controller;

import Model.Info.VozidloInfo;
import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import Statistiky.StatistikaInfo;
import Utils.Helper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import simulation.SimulaciaDopravy;
import simulation.SimulaciaWrapper;

import java.util.Arrays;
import java.util.List;

public class CSimulacia extends ControllerBase implements ISimDelegate {

    @FXML
    private JFXTextField textFieldReplications;

    @FXML
    private JFXTextField textFieldReplicationTime;

    @FXML
    private JFXButton buttonStart;

    @FXML
    private JFXButton buttonStop;

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
    private JFXCheckBox checkBoxPlanujPosunCasu;

    @FXML
    private JFXSlider sliderSpomalenie;

    @FXML
    private JFXSlider sliderInterval;

    @FXML
    private JFXSlider sliderStep;

    @FXML
    private JFXSlider sliderSkip;

    @FXML
    private Label labelCisloReplikacie;

    @FXML
    private TableView<StatistikaInfo> tableViewInfo;
    private ObservableList<StatistikaInfo> tableViewInfoData_;

    @FXML
    private TableView<VozidloInfo> tableViewVozidla;
    private ObservableList<VozidloInfo> tableViewVozidloData_;

    private SimpleBooleanProperty isReplicationOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isReplicationTimeOK = new SimpleBooleanProperty(false);

    private List<ReadOnlyBooleanProperty> simpleBooleanProperties = Arrays.asList(
            isReplicationOK,
            isReplicationTimeOK
    );

    private List<JFXTextField> textFields;
    private List<JFXCheckBox> checkBoxesToDisable;


    private SimulaciaDopravy _simulacia;

    public CSimulacia(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);

        _simulacia = simulaciaWrapper.getSimulaciaDopravy();
        _simulacia.registerDelegate(this);

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
        checkBoxPlanujPosunCasu.setSelected(true);
        Helper.nastavVypnutieOstatnych(checkBoxNormalny, checkBoxZrychleny);

        Helper.nastavVypnutieOstatnych(checkBoxZrychleny, checkBoxNormalny);

        checkBoxesToDisable = Arrays.asList(checkBoxNormalny, checkBoxZrychleny, checkBoxPlanujPosunCasu);


        textFields = Arrays.asList(
                textFieldReplications,
                textFieldReplicationTime
        );

        Helper.DecorateNumberTextFieldWithValidator(textFieldReplications, isReplicationOK);
        Helper.DecorateNumberTextFieldWithValidator(textFieldReplicationTime, isReplicationTimeOK);

        textFieldReplications.setText("1");
        textFieldReplicationTime.setText(String.valueOf((long) Helper.CASOVE_JEDNOTKY.DEN.getPocetSekund() ));

        Helper.PridajTabulkeStlpce(tableViewInfo, StatistikaInfo.ATRIBUTY);
        tableViewInfoData_ = tableViewInfo.getItems();
        Helper.InstallCopyPasteHandler(tableViewInfo);

        Helper.PridajTabulkeStlpce(tableViewVozidla, VozidloInfo.ATRIBUTY);
        tableViewVozidloData_ = tableViewVozidla.getItems();
        Helper.InstallCopyPasteHandler(tableViewVozidla);

        buttonStart.setOnAction(event -> {
            if (Helper.DisableButton(buttonStart, simpleBooleanProperties, () -> textFields.forEach(JFXTextField::validate))) {
                return;
            }
            //simulaciaWrapper_.zastavSimulaciu();
            //computation.cancel();
            int pocetReplikacii = Integer.parseInt(textFieldReplications.getText());
            double koncovyCasReplikacie =  Long.parseLong(textFieldReplicationTime.getText());

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



        _simulacia.onSimulationWillStart(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(true));
            buttonStart.setDisable(true);
            sliderInterval.setDisable(true);
            sliderSpomalenie.setDisable(true);
        });

        _simulacia.onReplicationWillStart(s -> {

            if (!checkBoxZrychleny.isSelected()) {
                double spomalenieCasu = sliderSpomalenie.getValue() * 0.001;
                double interval =  sliderInterval.getValue() * 0.01;
                _simulacia.setSimSpeed(interval, spomalenieCasu);
            } else {
                _simulacia.setMaxSimSpeed();
            }
        });

        _simulacia.onReplicationDidFinish(s -> {
            // TODO vypis globalnych statistik
        });

        _simulacia.onSimulationDidFinish(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(false));
            buttonStart.setDisable(false);
            sliderInterval.setDisable(false);
            sliderSpomalenie.setDisable(false);

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
        System.out.println(_simulacia.currentTime());
        double simTime = _simulacia.currentTime();

        Platform.runLater(() -> {
            labelCisloReplikacie.setText(String.valueOf(simTime));
            tableViewInfoData_.clear();
            tableViewInfoData_.add(new StatistikaInfo("rep time", Helper.FormatujSimulacnyCas(simTime)));


        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

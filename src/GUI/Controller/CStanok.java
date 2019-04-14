package GUI.Controller;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;

import Simulacia.Model.Info.StatistikaInfo;
import Simulacia.SimulaciaWrapper;
import Stanok.SimulaciaStanok;
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

import java.util.Arrays;
import java.util.List;

public class CStanok extends ControllerBase implements ISimDelegate {

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
    private ObservableList<StatistikaInfo> tableViewData_;



    private SimpleBooleanProperty isReplicationOK = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isReplicationTimeOK = new SimpleBooleanProperty(false);

    private List<ReadOnlyBooleanProperty> simpleBooleanProperties = Arrays.asList(
            isReplicationOK,
            isReplicationTimeOK
    );

    private List<JFXTextField> textFields;
    private List<JFXCheckBox> checkBoxesToDisable;
    private SimulaciaStanok simulaciaStanok_;

    public CStanok(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        simulaciaStanok_ = simulaciaWrapper_.getSimulaciaStanok();
        simulaciaStanok_.registerDelegate(this);

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
        tableViewData_ = tableViewInfo.getItems();

        Helper.InstallCopyPasteHandler(tableViewInfo);

        buttonStart.setOnAction(event -> {
            if (Helper.DisableButton(buttonStart, simpleBooleanProperties, () -> textFields.forEach(JFXTextField::validate))) {
                return;
            }
            //simulaciaWrapper_.zastavSimulaciu();
            //computation.cancel();
            int pocetReplikacii = Integer.parseInt(textFieldReplications.getText());
            double koncovyCasReplikacie =  Long.parseLong(textFieldReplicationTime.getText());

            simulaciaStanok_.simulateAsync(pocetReplikacii, koncovyCasReplikacie);

            buttonStart.disableProperty().unbind();
        });


        buttonStop.setOnAction(event -> {
            simulaciaStanok_.stopSimulation();

        });

        buttonPause.setOnAction(event -> {
            simulaciaStanok_.pauseSimulation();
        });

        buttonResume.setOnAction(event -> {
            simulaciaStanok_.resumeSimulation();
        });



        simulaciaStanok_.onSimulationWillStart(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(true));
            buttonStart.setDisable(true);

        });

        simulaciaStanok_.onReplicationWillStart(s -> {

            if (!checkBoxZrychleny.isSelected()) {
                double spomalenieCasu = sliderSpomalenie.getValue() * 0.001;
                double interval =  sliderInterval.getValue() * 0.01;
                simulaciaStanok_.setSimSpeed(interval, spomalenieCasu);
            } else {
                simulaciaStanok_.setMaxSimSpeed();
            }
        });

        simulaciaStanok_.onSimulationDidFinish(s -> {
            checkBoxesToDisable.forEach(checkBox -> checkBox.setDisable(false));
            buttonStart.setDisable(false);
        });

    }

    @Override
    protected void initView() {

    }

    @Override
    protected String getViewFileName() {
        return "stanok.fxml";
    }

    @Override
    public String getViewName() {
        return "Stánok";
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation simulation) {
        System.out.println(simulaciaStanok_.currentTime());
        double simTime = simulaciaStanok_.currentTime();
        double priemernyCas =  simulaciaStanok_.priemernyCasCakaniaZakaznikaRep_.mean();

        Platform.runLater(() -> {
            labelCisloReplikacie.setText(String.valueOf(simTime));
            tableViewData_.clear();
            tableViewData_.add(new StatistikaInfo("priemerny cas", String.valueOf(priemernyCas )));
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

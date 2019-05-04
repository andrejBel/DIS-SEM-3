package GUI;

import GUI.Controller.*;
import simulation.SimulaciaWrapper;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class Aplikacia {


    //SW
    private final SimulaciaWrapper simulaciaWrapper_ = new SimulaciaWrapper();

    // javafx
    private JFXTabPane tabPane_;
    private Stage stage_;

    private List<ControllerBase> controllers;

    public Aplikacia(Stage stage) {

        stage_ = stage;

        tabPane_ = new JFXTabPane();
        tabPane_.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        controllers = Arrays.asList(
                new CSimulacia(simulaciaWrapper_, stage)
        );

        for (ControllerBase controller : controllers) {
            Tab tab = new Tab(controller.getViewName());
            tab.setContent(controller.getView());
            tab.setOnSelectionChanged(event -> {
                if (controller.getRunnableOnSelection() != null) {
                    if (tab.isSelected()) {
                        controller.getRunnableOnSelection().run();
                    }
                }
                if (controller.getRunnableOnUnSelection() != null) {
                    if (!tab.isSelected()) {
                        controller.getRunnableOnUnSelection().run();
                    }
                }
            });
            tabPane_.getTabs().add(tab);
        }



        Scene scene = new Scene(tabPane_, 800, 800);
        stage.setTitle("Diskrétna simulácia - semestrálna práca 3");
        stage.setScene(scene);
        stage.setMaximized(true);

        /*
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("***Default exception handler***");
            if (Platform.isFxApplicationThread()) {
                controllers.get(tabPane_.getSelectionModel().getSelectedIndex()).showWarningDialog("Neznáma chyba");
                System.err.println("An unexpected error occurred in "+ t);
                System.err.println(e);
            } else {
                System.err.println("An unexpected error occurred in "+ t);
                System.err.println(e);
            }
        });
*       */
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            simulaciaWrapper_.getSimulaciaDopravy().stopSimulation();
            // TODO zastavit beziace simulacie
        } );
    }


    public void run() {
        stage_.show();
    }


}

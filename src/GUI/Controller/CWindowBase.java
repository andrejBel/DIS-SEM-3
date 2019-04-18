package GUI.Controller;

import simulation.SimulaciaWrapper;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class CWindowBase extends ControllerBase {

    private Scene scene_ = null;
    private Stage stageWindow_ = null;
    private boolean active_ = false;

    public CWindowBase(SimulaciaWrapper simulaciaWrapper, Stage stage) {
        super(simulaciaWrapper, stage);
        initView();
    }


    protected void initView() {
        scene_= new Scene(getView());
        stageWindow_ = new Stage();
        stageWindow_.setScene(scene_);
        stageWindow_.setTitle(getViewName());
        stageWindow_.initOwner(stage_);
        stageWindow_.setOnCloseRequest(event -> {
            active_ = false;
        });
    }

    public void openWindow() {
        active_ = true;
        stageWindow_.show();
    }

    public void closeWindow() {
        stageWindow_.close();
    }

    public Scene getScene() {
        return scene_;
    }

    public Stage getStage() {
        return stageWindow_;
    }

    public void setOnClose(Runnable runnable) {
        stageWindow_.setOnCloseRequest(event -> {
            active_ = false;
            runnable.run();
        });
    }

    public void setOnOpen(Runnable runnable) {
        stageWindow_.setOnShown(event -> {
            active_ = false;
            runnable.run();
        });
    }

    public void setModality(Modality modality) {
        stageWindow_.initModality(modality);
    }

    public boolean isActive() {
        return active_;
    }
}

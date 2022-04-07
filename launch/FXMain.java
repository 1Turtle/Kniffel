package launch;

import bluej.Config;
import greenfoot.core.Simulation;
//import greenfoot.export.GreenfootScenarioApplication;
import greenfoot.export.GreenfootScenarioViewer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threadchecker.OnThread;
import threadchecker.Tag;

public class FXMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @OnThread(Tag.FXPlatform)
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        GreenfootScenarioViewer greenfootScenarioViewer = new GreenfootScenarioViewer();
        Scene scene = new Scene(greenfootScenarioViewer);
        scene.getStylesheets().add("greenfoot.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setWidth(60*7);
        primaryStage.setOnHiding((e) -> {
            Simulation.getInstance().abort();
            Thread exiter = new Thread("Greenfoot exit") {
                public void run() {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException var2) {
                    }

                    System.exit(1);
                }
            };
            exiter.setDaemon(true);
            exiter.start();
        });
    }
}

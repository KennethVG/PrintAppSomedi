package be.somedi.printen;

import be.somedi.printen.config.PropertiesDev;
import be.somedi.printen.config.PropertiesProd;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Import({PropertiesDev.class, PropertiesProd.class})
@SpringBootApplication
public class PrintAppApplication extends Application {

    private ConfigurableApplicationContext context;
    private Parent root;

    public static void main(String[] args) {
        launch(PrintAppApplication.class, args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(PrintAppApplication.class);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/externalcaregiver.fxml"));
        loader.setControllerFactory(context::getBean);
        root = loader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/print.png")));
        primaryStage.setResizable(true);
        primaryStage.setTitle("Print Job Application");
        primaryStage.show();
    }

    @Override
    public void stop() {
        context.stop();
    }

//    public static void main(String[] args) {
//        SpringApplication.run(PrintAppApplication.class, args);
//    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
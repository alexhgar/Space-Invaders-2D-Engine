package alexjulenerik.demo.view;
import alexjulenerik.demo.GameApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
//ERIK
public class ViewFactory {
    public static void mostrarPantallaInicio() throws IOException{
        FXMLLoader fmxlLoader = new FXMLLoader(GameApplication.class.getResource("inicio-view.fxml"));
        //Establecer tamaño del menu
        Scene scene = new Scene(fmxlLoader.load(),600,400);
        //Crear el stage
        Stage stage = new Stage();
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();
    }

    public static void mostrarPantallaJuego() throws IOException{
        //Cargar el gameview
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("gameview.fxml"));
        //Tamaño 600x1000 ya que 60x100 se veria muy pequeño (mismas proporciones)
        Scene scene = new Scene(fxmlLoader.load(),1000,600);
        //Crear el stage
        Stage stage = new Stage();
        stage.setTitle("Space Invaders");
        stage.setScene(scene);
        stage.show();
    }

}

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class Main extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Sopa de Letras ZigZag")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()

    // Obtém o controlador do arquivo FXML
    val controller = fxmlLoader.getController[Controller]

    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    // Valores considerados ótimos para os elementos no ecrã
    primaryStage.setMinWidth(750)
    primaryStage.setMinHeight(500)
    primaryStage.show()
  }
}

object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}

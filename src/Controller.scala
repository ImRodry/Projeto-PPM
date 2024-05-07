import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}

class Controller {

  @FXML
  private var reiniciarJogoButton: Button = _
  @FXML
  private var novoJogoButton: Button = _
  @FXML
  private var selecionarPalavraButton: Button = _
  @FXML
  private var palavraTextField: TextField = _
  @FXML
  private var selecionarButton: Button = _

  def onReiniciarJogoClicked(): Unit = {
    toggleTextFieldAndButton(false)
  }

  def onNovoJogoClicked(): Unit = {
    toggleTextFieldAndButton(false)
  }

  def onSelecionarPalavraClicked(): Unit = {
    toggleTextFieldAndButton(true)
  }

  def onSelecionarClicked(): Unit = {
    toggleTextFieldAndButton(false)
  }
  // Set visibility of TextField and Button
  def toggleTextFieldAndButton(b:Boolean): Unit = {
    palavraTextField.setVisible(b)
    selecionarButton.setVisible(b)
  }
}

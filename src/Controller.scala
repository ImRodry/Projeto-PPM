import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.GridPane
import javafx.scene.text.Font

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
  @FXML
  private var palavraLabel: Label = _
  @FXML
  private var tabuleiroGridPane: GridPane = _

  def onReiniciarJogoClicked(): Unit = {
    toggleTextFieldAndButton(false)
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
  }

  def onNovoJogoClicked(): Unit = {
    toggleTextFieldAndButton(false)   // Esconde o TextField e o Button
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
    tabuleiroGridPane.getChildren.clear() // Limpa o tabuleiro atual
    tabuleiroGridPane.setGridLinesVisible(true)   // Mostra as linhas do tabuleiro
    val newBoard = Utils.startGame(5, 5)          // Inicia um novo jogo
    // Preencher o GridPane com as novas letras
    for (i <- newBoard.indices) {
      for (j <- newBoard(i).indices) {
        val letter = newBoard(i)(j)
        val label = new Label(letter.toString)
        label.setFont(new Font("System", 40))      // Define o tamanho da fonte para 40
        tabuleiroGridPane.add(label, j, i)
      }
    }

  }

  def onSelecionarPalavraClicked(): Unit = {
    toggleTextFieldAndButton(true)
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
    palavraTextField.clear()          // Limpa o TextField
  }

  def onSelecionarClicked(): Unit = {
    toggleTextFieldAndButton(false)
    palavraLabel.setVisible(true)
    palavraLabel.setText(palavraTextField.getText)
    // se a palavra é válida fica a palavraLabel a verde e a palavra do tabuleiro tambem a verde
    // se a palavra não é válida fica a palavraLabel a vermelho e a palavra do tabuleiro tambem a vermelho
    // se a palavra for valida risca se da lista de palavras a encontrar
    // se a palavra for invalida informa-se que a palavra nao é valida
  }

  // Set visibility of TextField and Button
  def toggleTextFieldAndButton(b:Boolean): Unit = {
    palavraTextField.setVisible(b)
    selecionarButton.setVisible(b)
  }
}

import Types.{Board, Direction}
import ZigZag.play
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.GridPane
import javafx.scene.text.Font

class Controller {

  private var board: Board = _

  @FXML
  private var reiniciarJogoButton: Button = _
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
  @FXML
  private var coordTextField: TextField = _
  @FXML
  private var directionTextField: TextField = _
  @FXML
  private var palavraLegendaLabel: Label = _
  @FXML
  private var coordLegendaLabel: Label = _
  @FXML
  private var directionLegendaLabel: Label = _
  @FXML
  private var iniciarJogoButton: Button = _

  def onIniciarJogoClicked(): Unit = {
    reiniciarJogoButton.setVisible(true) // Mostra o botão de reiniciar jogo
    toggleTextFieldsAndButton(false)   // Esconde o TextField e o Button
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
    iniciarJogoButton.setVisible(false) // Esconde o botão de iniciar jogo
    tabuleiroGridPane.getChildren.clear() // Limpa o tabuleiro atual
    tabuleiroGridPane.setGridLinesVisible(true)   // Mostra as linhas do tabuleiro
    board = Utils.startGame(5, 5)          // Inicia um novo jogo
    // Preencher o GridPane com as novas letras
    for (i <- board.indices) {
      for (j <- board(i).indices) {
        val letter = board(i)(j)
        val label = new Label(letter.toString)
        label.setFont(new Font("System", 40))      // Define o tamanho da fonte para 40
        tabuleiroGridPane.add(label, j, i)
      }
    }
  }

  def onReiniciarJogoClicked(): Unit = {
    toggleTextFieldsAndButton(false)   // Esconde o TextField e o Button
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
    tabuleiroGridPane.getChildren.clear() // Limpa o tabuleiro atual
    tabuleiroGridPane.setGridLinesVisible(true)   // Mostra as linhas do tabuleiro
    board = Utils.startGame(5, 5)          // Inicia um novo jogo
    // Preencher o GridPane com as novas letras
    for (i <- board.indices) {
      for (j <- board(i).indices) {
        val letter = board(i)(j)
        val label = new Label(letter.toString)
        label.setFont(new Font("System", 40))      // Define o tamanho da fonte para 40
        tabuleiroGridPane.add(label, j, i)
      }
    }
  }

  def onSelecionarPalavraClicked(): Unit = {
    toggleTextFieldsAndButton(true)
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
  }

  def onSelecionarClicked(): Unit = {
    palavraLabel.setVisible(true)
    palavraLabel.setText(palavraTextField.getText)
    val word = palavraTextField.getText
    val coord = coordTextField.getText
    val coordParts = coord.split(",") // Suponha que as coordenadas são dadas como "row,column"
    val coord2D = (coordParts(0).trim.toInt, coordParts(1).trim.toInt)
    val directionString = directionTextField.getText
    val direction = Direction.withName(directionString)
    // Verifica se a palavra está no tabuleiro
    if (play(board,word, coord2D, direction)) {
      palavraLabel.setStyle("-fx-text-fill: green") // Muda a cor do texto para verde
    } else {
      palavraLabel.setStyle("-fx-text-fill: red") // Muda a cor do texto para vermelho

    }
    toggleTextFieldsAndButton(false)
  }

  // Set visibility of TextField and Button
  def toggleTextFieldsAndButton(b:Boolean): Unit = {
    palavraTextField.setVisible(b)
    selecionarButton.setVisible(b)
    coordTextField.setVisible(b)
    directionTextField.setVisible(b)
    palavraLegendaLabel.setVisible(b)
    coordLegendaLabel.setVisible(b)
    directionLegendaLabel.setVisible(b)
    palavraTextField.clear()
    coordTextField.clear()
    directionTextField.clear()
  }
}

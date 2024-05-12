import BoardType._
import Types.{Board, Direction}
import ZigZag._
import javafx.fxml.FXML
import javafx.scene.control.{Alert, Button, ButtonType, ComboBox, Label, TextField}
import javafx.scene.layout.{GridPane, VBox}
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.geometry.Pos
import javafx.util.Duration

class Controller {
  private var board: Board = _
  private var words: List[String] = _
  private var positions: List[List[(Int, Int)]] = _
  private var checkedWords: List[String] = Nil

  val startTime = System.currentTimeMillis()
  var score = 0

  lazy val timeline: Timeline = new Timeline()


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
  private var rowTextField: TextField = _
  @FXML
  private var colTextField: TextField = _
  @FXML
  private var directionDropdown: ComboBox[String] = _
  @FXML
  private var palavraLegendaLabel: Label = _
  @FXML
  private var rowLabel: Label = _
  @FXML
  private var colLabel: Label = _
  @FXML
  private var directionLegendaLabel: Label = _
  @FXML
  private var iniciarJogoButton: Button = _
  @FXML
  private var wordsVBox: VBox = _
  @FXML
  private var timeTextLabel: Label = _
  @FXML
  private var timeValueLabel: Label = _
  @FXML
  private var scoreTextLabel: Label = _
  @FXML
  private var scoreValueLabel: Label = _

  // Inicializa o controlador
  def initialize(): Unit = {
    toggleTextFieldsAndButton(false)
    selecionarPalavraButton.setVisible(false)
    reiniciarJogoButton.setVisible(false)
    palavraLabel.setVisible(false)
    timeTextLabel.setVisible(false)
    scoreTextLabel.setVisible(false)
  }

  //Inicializa o jogo
  def onIniciarJogoClicked(): Unit = {
    val startGameOutput = Utils.startGame(Utils.boardWidth, Utils.boardHeight, GUI)   // Inicia um novo jogo
    board = startGameOutput._1
    words = startGameOutput._2
    positions = startGameOutput._3
    initializeWords()
    reiniciarJogoButton.setVisible(true)
    selecionarPalavraButton.setVisible(true)
    toggleTextFieldsAndButton(false)
    iniciarJogoButton.setVisible(false)
    tabuleiroGridPane.getChildren.clear() // Limpa o tabuleiro atual
    // Preencher o GridPane com as novas letras
    for (i <- board.indices) {
      for (j <- board(i).indices) {
        val letter = board(i)(j)
        val label = new Label(letter.toString)
        label.setFont(new Font("System", 40))
        tabuleiroGridPane.add(label, j, i)
      }
    }
    // Inicia o timer
    val startTime = System.currentTimeMillis()
    // Para a timeline anterior
    timeline.stop()
    // Define o KeyFrame da timeline
    timeTextLabel.setVisible(true)
    timeline.getKeyFrames.setAll(new KeyFrame(Duration.seconds(1), _ => {   // Atualiza o tempo a cada segundo
      val elapsedTime = getElapsedTime(startTime)     // Calcula o tempo decorrido
      val timeRemaining = 120000 - elapsedTime        // Calcula o tempo restante
      if (isGameOver(startTime, words, checkedWords)) {
        timeline.stop()
        timeValueLabel.setText("Acabou!")
      } else {
        timeValueLabel.setText(timeRemaining / 1000 + "s")
      }
    }))

    scoreTextLabel.setVisible(true)
    scoreValueLabel.setText(score.toString)
    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.play()
  }


  // Seleção do botão de "Reiniciar Jogo"
  def onReiniciarJogoClicked(): Unit = {
    onIniciarJogoClicked()
    checkedWords = Nil
  }

  // Seleção do botão de "Selecionar Palavra"
  def onSelecionarPalavraClicked(): Unit = {
    selecionarPalavraButton.setVisible(false) // Esconde o botão de selecionar palavra
    toggleTextFieldsAndButton(true)
  }

  // Seleção do botão de "Selecionar"
  def onSelecionarClicked(): Unit = {
    if (isGameOver(startTime, words, checkedWords)) {
      sendAlertAndEnd("Acabou o tempo!")
    }
    palavraLabel.setVisible(false)
    val word = palavraTextField.getText().toUpperCase()
    val row = rowTextField.getText.trim.toInt       // extrai a linha
    val column = colTextField.getText.trim.toInt    // extrai a coluna
    val coord2D = (row, column)                     // cria a coordenada 2D
    val directionString = directionDropdown.getSelectionModel().getSelectedItem()
    val direction = Direction.withName(directionString)
    // Verifica se a palavra está no tabuleiro
    if (play(board, word, words, coord2D, direction, GUI)) {
      if (!checkedWords.contains(word)) {
        val wordIndex = words.indexOf(word)
        if (wordIndex != -1) {
          val wordPositions = positions(wordIndex)
          paintWordOnBoard(word, wordPositions)
          for (i <- 0 until wordsVBox.getChildren.size()) {
            val label = wordsVBox.getChildren.get(i).asInstanceOf[Label]
            if (label.getText == word)
              label.setTextFill(Color.LIMEGREEN)
          }
          score += 500
          checkedWords = checkedWords :+ word
        }
      } else {
        palavraLabel.setText("Palavra já\nselecionada") // newline porque de outra forma não cabia
        palavraLabel.setStyle("-fx-text-fill: red")
        palavraLabel.setVisible(true)
      }
    } else {
      palavraLabel.setText("Errado")
      palavraLabel.setStyle("-fx-text-fill: red")
      palavraLabel.setVisible(true)
      score -= 100
    }
    scoreValueLabel.setText(score.toString)
    toggleTextFieldsAndButton(true)
    if (checkedWords.length == words.length) sendAlertAndEnd("Encontrou todas as palavras!")
  }

  private def paintWordOnBoard(word: String, wordPositions: List[(Int, Int)]) = {
    for ((row, col) <- wordPositions) {
      val label = tabuleiroGridPane.getChildren.get(row * Utils.boardWidth + col).asInstanceOf[Label]
      label.setTextFill(Color.LIMEGREEN) // Muda a cor do texto para verde
    }
  }

  // Visibilidade dos campos de texto e botoes
  def toggleTextFieldsAndButton(b:Boolean): Unit = {
    palavraTextField.setVisible(b)
    selecionarButton.setVisible(b)
    rowTextField.setVisible(b)
    colTextField.setVisible(b)
    directionDropdown.setVisible(b)
    palavraLegendaLabel.setVisible(b)
    rowLabel.setVisible(b)
    colLabel.setVisible(b)
    directionLegendaLabel.setVisible(b)
    palavraTextField.clear()
    rowTextField.clear()
    colTextField.clear()
    directionDropdown.getSelectionModel.clearSelection()
  }

  // Inicializa as palavras
  def initializeWords(): Unit = {
    wordsVBox.getChildren.clear() // Limpa as palavras antigas
    for (i <- words.indices) {
      val label = new Label(words(i))
      label.setAlignment(Pos.CENTER)
      label.setLayoutX(10.0)
      label.setLayoutY(132.0)
      label.setPrefSize(138.0, 18.0)
      label.setFont(new Font(24.0))
      label.setVisible(true)
      label.setTextFill(Color.BLACK)
      wordsVBox.getChildren().add(i, label) // set at that index to push the other label to the end
    }
  }

  def sendAlertAndEnd(message: String): Unit = {
    val alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
    alert.showAndWait();

    if (alert.getResult() == ButtonType.OK) {
      System.exit(0);
    }
  }

}

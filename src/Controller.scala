import BoardType._
import Types.{Board, Direction}
import Utils.readWordsAndPositions
import ZigZag._
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.animation.{Animation, KeyFrame, Timeline}
import javafx.util.Duration

class Controller {
  private var board: Board = _

  val startTime = System.currentTimeMillis()

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
  private var directionTextField: TextField = _
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
  private var firstWord: Label = _
  @FXML
  private var secondWord: Label = _
  @FXML
  private var thirdWord: Label = _
  @FXML
  private var timeLabel: Label = _

  // Crie um Timeline como membro da classe
  lazy val timeline: Timeline = new Timeline()

  def onIniciarJogoClicked(): Unit = {
    initializeWords()
    reiniciarJogoButton.setVisible(true) // Mostra o botão de reiniciar jogo
    selecionarPalavraButton.setVisible(true) // Mostra o botão de selecionar palavra
    toggleTextFieldsAndButton(false)   // Esconde o TextField e o Button
    iniciarJogoButton.setVisible(false) // Esconde o botão de iniciar jogo
    tabuleiroGridPane.getChildren.clear() // Limpa o tabuleiro atual
    board = Utils.startGame(Utils.boardWidth, Utils.boardHeight, GUI)          // Inicia um novo jogo
    // Preencher o GridPane com as novas letras
    for (i <- board.indices) {
      for (j <- board(i).indices) {
        val letter = board(i)(j)
        val label = new Label(letter.toString)
        label.setFont(new Font("System", 40))      // Define o tamanho da fonte para 40
        tabuleiroGridPane.add(label, j, i)
      }
    }

    // Inicie o tempo
    val startTime = System.currentTimeMillis()

    // Pare o Timeline anterior
    timeline.stop()

    // Defina o KeyFrame do Timeline
    timeline.getKeyFrames.setAll(new KeyFrame(Duration.seconds(1), _ => {
      val elapsedTime = getElapsedTime(startTime)
      val timeRemaining = 30000 - elapsedTime
      if (isGameOver(startTime)) {
        timeline.stop()
        timeLabel.setText("Acabou!")
      } else {
        timeLabel.setText(timeRemaining / 1000 + "s")
      }
    }))

    timeline.setCycleCount(Animation.INDEFINITE)
    timeline.play()
  }


  def onReiniciarJogoClicked(): Unit = {
    onIniciarJogoClicked()
  }

  def onSelecionarPalavraClicked(): Unit = {
    selecionarPalavraButton.setVisible(false) // Esconde o botão de selecionar palavra
    toggleTextFieldsAndButton(true)
  }

  def onSelecionarClicked(): Unit = {
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
    val word = palavraTextField.getText().toUpperCase()
    val row = rowTextField.getText.trim.toInt // Suponha que rowTextField é o campo de texto para a linha
    val column = colTextField.getText.trim.toInt // Suponha que columnTextField é o campo de texto para a coluna
    val coord2D = (row, column)
    val directionString = directionTextField.getText
    val direction = Direction.withName(directionString)

    // Verifica se a palavra está no tabuleiro
    if (play(board, word, coord2D, direction, GUI)) {
      val (words, positions) = Utils.readWordsAndPositions("words.txt")
      val wordIndex = words.indexOf(word)
      if (wordIndex != -1 && positions.length > wordIndex) {
        val wordPositions = positions(wordIndex)
        paintWordOnBoard(word, wordPositions)
        //pinta a firstWord, secondWord ou thirdWord
        if (word == firstWord.getText()) {
          firstWord.setTextFill(Color.LIMEGREEN)
        } else if (word == secondWord.getText()) {
          secondWord.setTextFill(Color.LIMEGREEN)
        } else if (word == thirdWord.getText()) {
          thirdWord.setTextFill(Color.LIMEGREEN)
        }
      }
    }
    else {
      palavraLabel.setText("Errado")
      palavraLabel.setStyle("-fx-text-fill: red")
      palavraLabel.setVisible(true)
    }
    // Mantenha os campos de texto e o botão visíveis após um erro
    toggleTextFieldsAndButton(true)
  }


  private def paintWordOnBoard(word: String, wordPositions: List[(Int, Int)]) = {
    for ((row, col) <- wordPositions) {
      val label = tabuleiroGridPane.getChildren.get(row * Utils.boardWidth + col).asInstanceOf[Label]
      label.setTextFill(Color.LIMEGREEN) // Muda a cor do texto para verde
    }
  }



  // Set visibility of TextField and Button
  def toggleTextFieldsAndButton(b:Boolean): Unit = {
    palavraTextField.setVisible(b)
    selecionarButton.setVisible(b)
    rowTextField.setVisible(b)
    colTextField.setVisible(b)
    directionTextField.setVisible(b)
    palavraLegendaLabel.setVisible(b)
    rowLabel.setVisible(b)
    colLabel.setVisible(b)
    directionLegendaLabel.setVisible(b)
    palavraTextField.clear()
    rowTextField.clear()
    colTextField.clear()
    directionTextField.clear()
  }

  // Inicializa as palavras
  def initializeWords(): Unit = {
    val (words, positions) = readWordsAndPositions("words.txt")
    firstWord.setText(words(0))
    secondWord.setText(words(1))
    thirdWord.setText(words(2))
    firstWord.setVisible(true)
    secondWord.setVisible(true)
    thirdWord.setVisible(true)
    firstWord.setTextFill(Color.BLACK)
    secondWord.setTextFill(Color.BLACK)
    thirdWord.setTextFill(Color.BLACK)
  }
}

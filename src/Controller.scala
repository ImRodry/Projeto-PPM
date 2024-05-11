import BoardType._
import Types.{Board, Direction}
import Utils.readWordsAndPositions
import ZigZag.play
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
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

  def onIniciarJogoClicked(): Unit = {
    val (words, positions) = readWordsAndPositions("words.txt")
    firstWord.setText(words(0))
    secondWord.setText(words(1))
    thirdWord.setText(words(2))
    reiniciarJogoButton.setVisible(true) // Mostra o botão de reiniciar jogo
    selecionarPalavraButton.setVisible(true) // Mostra o botão de selecionar palavra
    toggleTextFieldsAndButton(false)   // Esconde o TextField e o Button
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
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
  }


  def onReiniciarJogoClicked(): Unit = {
    toggleTextFieldsAndButton(false)   // Esconde o TextField e o Button
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
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
  }

  def onSelecionarPalavraClicked(): Unit = {
    toggleTextFieldsAndButton(true)
    palavraLabel.setVisible(false)    // Esconde a palavra anterior
  }

  def onSelecionarClicked(): Unit = {
    palavraLabel.setVisible(true)
    palavraLabel.setText(palavraTextField.getText)
    palavraLabel.setText(palavraLabel.getText().toUpperCase())
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
          firstWord.setStyle("-fx-text-fill: green")
        } else if (word == secondWord.getText()) {
          secondWord.setStyle("-fx-text-fill: green")
        } else if (word == thirdWord.getText()) {
          thirdWord.setStyle("-fx-text-fill: green")
        }
      } else {
        palavraLabel.setStyle("-fx-text-fill: green") // Se a palavra não estiver no arquivo, mantenha a cor preta
      }
    } else {
      palavraLabel.setStyle("-fx-text-fill: red") // Muda a cor do texto para vermelho
    }
    toggleTextFieldsAndButton(false)
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
}

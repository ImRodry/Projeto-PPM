import scala.io.StdIn.readLine

object TextUI {
  def mainMenu(): Unit = {
    println("Bem-vindo ao jogo de palavras cruzadas!")
    println("1. Iniciar novo jogo")
    println("2. Selecionar palavra")
    println("3. Reiniciar jogo")
    println("4. Alterar cor do texto")
    println("5. Sair")
  }

  def getInput(prompt: String): String = {
    print(prompt + ": ")
    readLine().trim
  }

  def selectWord(): Unit = {
    val word = getInput("Digite a palavra que deseja selecionar")
    // Lógica para selecionar a palavra
  }

  def main(args: Array[String]): Unit = {
    var running = true

    while (running) {
      mainMenu()
      val choice = getInput("Escolha uma opção")

      choice match {
        case "1" => // Lógica para iniciar novo jogo
        case "2" => selectWord()
        case "3" => // Lógica para reiniciar jogo
        case "4" => // Lógica para alterar cor do texto
        case "5" => running = false
        case _ => println("Opção inválida")
      }
    }

    println("Obrigado por jogar!")
  }
}

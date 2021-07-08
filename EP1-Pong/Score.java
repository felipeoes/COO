import java.awt.*;

/**
	Esta classe representa um placar no jogo. A classe princial do jogo (Pong)
	instancia dois objeto deste tipo, cada um responsável por gerenciar a pontuação
	de um player, quando a execução é iniciada.
*/

public class Score {

	/**
		Construtor da classe Score.

		@param playerId uma string que identifica o player ao qual este placar está associado.
	*/
	String playerId;
	int points;

	public Score(String playerId){
		this.playerId = playerId;
		this.points = 0;
	}

	/**
		Método de desenho do placar.
	*/

	public void draw(){

		if(playerId == Pong.PLAYER1) {
			Color color = new Color(0, 255, 0);
			GameLib.setColor(color);
			GameLib.drawText(playerId + ": " + Integer.toString(points), 70, GameLib.ALIGN_LEFT);
		}
		
		if(playerId == Pong.PLAYER2) {
			Color color = new Color(0, 0, 255);
			GameLib.setColor(color);
			GameLib.drawText(playerId + ": " + Integer.toString(points), 70, GameLib.ALIGN_RIGHT);
		}
	}

	/**
		Método que incrementa em 1 unidade a contagem de pontos.
	*/

	public void inc(){
		this.points++;
	}

	/**
		Método que devolve a contagem de pontos mantida pelo placar.

		@return o valor inteiro referente ao total de pontos.
	*/

	public int getScore(){

		return this.points;
	}
}

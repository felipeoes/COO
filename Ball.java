import java.awt.*;

/**
	Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
	instancia um objeto deste tipo quando a execução é iniciada.
*/

public class Ball {

	/**
		Construtor da classe Ball. Observe que quem invoca o construtor desta classe define a velocidade da bola 
		(em pixels por millisegundo), mas não define a direção deste movimento. A direção do movimento é determinada 
		aleatóriamente pelo construtor.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
	*/

	double cx, cy, width, height, speed;
	Color color;


	public Ball(double cx, double cy, double width, double height, Color color, double speed){
		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.color = color;
	}


	/**
		Método chamado sempre que a bola precisa ser (re)desenhada.
	*/

	public void draw(){

		GameLib.setColor(color);
		GameLib.fillRect(cx, cy, height, width);
	}

	/**
		Método chamado quando o estado (posição) da bola precisa ser atualizado.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
		this.cx += delta;
		this.cy += delta;
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com um jogador.
	
		@param playerId uma string cujo conteúdo identifica um dos jogadores.
	*/

	public void onPlayerCollision(String playerId){

	}

	/**
		Método chamado quando detecta-se uma colisão da bola com uma parede.

		@param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	*/

	public void onWallCollision(String wallId){
		if(wallId == Pong.TOP) { // verifica se bateu nas paredes superior ou inferior e continua o jogo
			this.cx += 2;
			this.cy += 2;
		}

		if(wallId == Pong.BOTTOM) {
			this.cx -= 2;
			this.cy -= 2;
		}

		if(wallId == Pong.RIGHT) { // jogador1 pontua
			this.cx = 400; // retorna a bola para a posicao inicial
			this.cy = 300;
		}
		
		else { // jogador2 pontua
			this.cx = 400; // retorna a bola para a posicao inicial
			this.cy = 300;
		}
	}

	/**
		Método que verifica se houve colisão da bola com uma parede.

		@param wall referência para uma instância de Wall contra a qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	
	public boolean checkCollision(Wall wall){
		if(this.cx == wall.getCx() && this.cy == wall.getCy()) return true;
		return false;
	}

	/**
		Método que verifica se houve colisão da bola com um jogador.

		@param player referência para uma instância de Player contra o qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/	

	public boolean checkCollision(Player player){
		// Pegando hitbox do player.
		double pTop = player.getCy() - (player.getHeight() / 2);
		double pBottom = player.getCy() + (player.getHeight() / 2);
		double pLeft = player.getCx() - (player.getWidth() / 2);
		double pRight = player.getCx() + (player.getWidth() / 2);
		
		// Pegando hitbox da bola.
		double bTop = this.cy - (this.height/2);
		double bBottom = this.cy + (this.height / 2);
		double bLeft = this.cx - (this.width / 2);
		double bRight = this.cx + (this.width / 2);
		
		// Checando as colisões.
		boolean leftCol = pLeft <= bRight;
		boolean rightCol = pRight >= bLeft;
		// Checando colisões com as bordas inferiores e superiores.
		boolean bpTop = bBottom >= pTop;
		boolean bpBottom = bTop <= pBottom;
		
		// Caso haja uma colisão.
		boolean check = leftCol && rightCol && bpTop && bpBottom

		return check;
	}

	/**
		Método que devolve a coordenada x do centro do retângulo que representa a bola.
		@return o valor double da coordenada x.
	*/
	
	public double getCx(){

		return this.cx;
	}

	/**
		Método que devolve a coordenada y do centro do retângulo que representa a bola.
		@return o valor double da coordenada y. 
	*/

	public double getCy(){

		return this.cy;
	}

	/**
		Método que devolve a velocidade da bola.
		@return o valor double da velocidade.

	*/

	public double getSpeed(){

		return this.speed;
	}

}

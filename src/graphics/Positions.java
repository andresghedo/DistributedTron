package graphics;

//Tiene conto della posizione del player, andando a mettere le sue coordinate gia' esplorate in una pila
public class Positions {
	
	int x;
	int y;

	public Positions(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}

/**
 * Seged view class az elemek megjelenitesehez. Tarolja az egyes elemek X, Y
 * koordintait, tovabba az elem ID-jet, amely a kapcsolatot jelenti az elem es a
 * hozza tartozo View kozott
 * 
 */
public class viewElem {
	/** az elem ID-ja, amihez a View osztaly tartozik */
	public String ID;
	/** Az elem X koordinataja */
	public int X;
	/** Az elem Y koordinataja */
	public int Y;

	/** Konstruktor. letrehozzuk az adott IDju elemhez tartozo View objektumot. */
	public viewElem(int xa, int ya, String ida) {
		X = xa;
		Y = ya;
		ID = ida;
	}
}

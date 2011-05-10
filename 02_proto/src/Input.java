/**
 * <table border=0>
 * <tr align=left>
 * <th>Nev:</th>
 * <td width=30>&nbsp;&nbsp;Input</td>
 * </tr>
 * <tr align=left>
 * <th>Tipus:</th>
 * <td>&nbsp;&nbsp;Class</td>
 * </tr>
 * <tr align=left>
 * <th>Interface:</th>
 * <td>&nbsp;&nbsp;---</td>
 * </tr>
 * <tr align=left>
 * <th>Szulok:</th>
 * <td>&nbsp;&nbsp;DigitalObject</td>
 * </table>
 * <br>
 * Bemenet nelkuli aramkori elem, melynek kimenete vagy adott idokozonkent, vagy
 * felhasznaloi interakcio soran valtozhat meg.
 */
public abstract class Input extends DigitalObject {
	/* ATTRIBUTUMOK */
	/**
	 * Az adott Input objektum erteket tarolja
	 */
	protected int Value;

	/* METODUSOK */
	/**
	 * Az Output osztalynak nincs feedback tombje, igy a metodus nincs
	 * implementalva
	 */
	public void AddToFeedbacks(DigitalObject object) {
		; // Inputoknak nincs feedbackje.
	};
	
	/**
	 * KONSTRUKTOR
	 * Minden Input-ra jellemzo ertek beallitasa.
	 * Ilyen: nincs wireIn-juk es az alapertelmezett eretek 0
	 */
	public Input(){
		wireIn = null;
		Value=0;
	}
}

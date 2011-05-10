import java.util.ArrayList;

/**
 * <table border=0>
 * <tr align=left>
 * <th>Nev:</th>
 * <td width=30>&nbsp;&nbsp;LED</td>
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
 * <td>&nbsp;&nbsp;DigitalObject-->Output</td>
 * </table>
 * <br>
 * A bemenet megjelenitesere szolgalo objektum/osztaly. Pontosan egy erteket
 * tarol, a bemenete legutolso erteket.
 */
public class LED extends Output {

	/**
	 * KONSTRUKTOR
	 * 
	 * @param strCompositName
	 *            A LEDet tartalmazo Composit elem neve. Szukseges, hogy a
	 *            kulonbozo compositokban szerepelhessen azonos nevu objektum
	 * @param LEDName
	 *            a LEd kivant neve
	 * @param wirein1
	 *            A LED egyetlen bemente
	 */
	public LED(String strCompositName, String LEDName) {
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ LEDName;
		wireOut = new ArrayList<Wire>();
		wireIn = new ArrayList<Wire>();

		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created by User.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}

	/* METODUSOK */
	/**
	 * A bemenet erteket megjeleniti
	 * 
	 * @return egyetlen bemenetenek erteke
	 * @throws ExceptionElementHasNoInputs
	 *             Ha a LED-nek nincs bemenete
	 * @throws ExceptionElementInputSize
	 *             Ha a LED-nek nincs meg a megfelelo szamu bemenete (1 darab)
	 */
	public int Count() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<" + this.GetID() + " Count>");
		
		/* Lekerdezzuk a bemenetek ertekeit */
		if (wireIn == null || wireIn.isEmpty()) {
			throw new ExceptionElementHasNoInputs(this);
		} else {
			if (wireIn.size() != 1) {
				throw new ExceptionElementInputSize(this);
			} else {
				Value = wireIn.get(0).GetValue();
			}
		}
		// LOGOLAS;
		Logger.Log(Logger.log_type.INFO, "<" + this.GetName() + "> value is "+Value);
		return Value;
	};

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e
	 * 
	 * @return mindig {@code true } ertekkel ter vissza, hiszen egy Output elem
	 *         mindig stabil.
	 * @throws ExceptionElementHasNoInputs
	 *             Ha a LED-nek nincs bemenete
	 * @throws ExceptionElementInputSize
	 *             Ha a LED-nek nincs meg a megfelelo szamu bemenete (1 darab)
	 */
	public boolean Step() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<" + this.GetID() + " Step>");
		
		Count();
		return true;
	};
}

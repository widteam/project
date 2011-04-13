import java.util.ArrayList;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;LED</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interface: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;DigitalObject-->Output</td>
 * </table> 
*<br>
* A bemenet megjelenitesere szolgalo objektum/osztaly. Pontosan egy erteket tarol, a bemenete legutolso erteket.

*/
public class LED extends Output{
	/*  ATTRIBUTUMOK  */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int LEDCount;
	
	
	/**  KONSTRUKTOR  
	 * @param strCompositName
	 *            A LEDet tartalmazo Composit elem neve. Szukseges, hogy a
	 *            kulonbozo compositokban szerepelhessen azonos nevu objektum
	 * @param wirein1
	 *            A LED egyetlen bemente*/
	public LED(String strCompositName, Wire WireIn){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(LEDCount++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireOut = new ArrayList<Wire>();;
		wireIn = new ArrayList<Wire>();
	}

	
	
	/*	METODUSOK	*/
	/**
	 * A bemenet erteket megjeleniti
	 * @return egyetlen bemenetenek erteke
	 * @throws ElementHasNoInputsException
	 *             Ha a LED-nek nincs bemenete
	 * @throws ElementInputSizeException
	 *             Ha a LED-nek nincs meg a megfelelo szamu bemenete (1 darab)
	 */
	public int Count(){	
		/* Lekerdezzuk a bemenetek ertekeit */
		if (wireIn == null || wireIn.isEmpty()) {
			// throw ElementHasNoInputsException;
		} else {
			if (wireIn.size() != 1) {
				// throw ElementInputSizeException
			} else {
				Value = wireIn.get(0).GetValue();
			}
		}
		return Value;
	};		
	
	/**
	 * Feladata az adott elem ertekenek kiszamitasa, 
	 * ill. annak eldontese, hogy a DigitalObject stabil-e
	 * @return mindig {@code true } ertekkel ter vissza, hiszen egy Output elem mindig stabil.
	 */
	public boolean Step(){
		Count();
		return true;
	};
}

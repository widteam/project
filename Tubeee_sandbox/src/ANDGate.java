/*  IMPORTOK  */
import java.util.ArrayList;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;ANDGate</td>
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
 * 		<td>&nbsp;&nbsp;DigitalObject-->Gate</td>
 * </table> 
*<br>
 * Logikai ES kaput megvalosito objektum. A bemeneteirol beol-
 * vasott ertekekbol kiszamolja es tovabbadja a kimenetere az
 * uj erteket. Az uj erteket az ES kapu igazsagtablaja szerint
 * szamolja ki, mely ket bemenet eseten a kovetkezo:
* <table border=1>
* <tr align=center><th colspan=2>Bemenet</th><th>Kimenet</th></tr>
* <tr align=center><th colspan>A</th><th colspan>B</th><th>A OR B</th></tr>
* <tr align=center><td>0</td><td>0</td><td>0</td></tr>
* <tr align=center><td>0</td><td>1</td><td>0</td></tr>
* <tr align=center><td>1</td><td>0</td><td>0</td></tr>
* <tr align=center><td>1</td><td>1</td><td>1</td></tr>
* <tr align=center><td>0</td><td>X</td><td>0</td></tr>
* <tr align=center><td>X</td><td>0</td><td>0</td></tr>
* <tr align=center><td>1</td><td>X</td><td>X</td></tr>
* <tr align=center><td>X</td><td>1</td><td>X</td></tr>
* <tr align=center><td>X</td><td>X</td><td>X</td></tr>
* </table>
* Jelentesek: 0: logikai HAMIS ertek | 1: logikai IGAZ ertek | X: don't care
 */

public class ANDGate extends Gate {
	/* ATTRIBUTUMOK */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int ANDCounts;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param strCompositName
	 *            A kaput tartalmazo Composit elem neve. Szukseges, hogy a
	 *            kulonbozo compositokban szerepelehssen azonos nevu objektum
	 * @param wirein1
	 *            A kapu egyik bemente
	 * @param wirein2
	 *            A kapu masodik bemenete
	 * */
	public ANDGate(String strCompositName, Wire wirein1, Wire wirein2) {
		final String strIDDelimiter = "#";
		String strIDNumber = String.valueOf(ANDCounts++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strIDNumber;

		wireIn = new ArrayList<Wire>(); // Inicializaljuk a wireIn listat
		wireOut = new ArrayList<Wire>();// Inicializaljuk a wireOut listat
		wireIn.add(wirein1); // a konstruktorban megadott bemenetet bedrotozzuk
		wireIn.add(wirein2); // a masik bemenetet is bedrotozzuk
	}

	/* METODUSOK */
	/**
	 * Kiszamolja egy DigitalObject erteket
	 * 
	 * @return A belso implementalt igazsagtabla tovabba a ket bemenet alapjan
	 *         kapott ertek
	 * @throws ElementHasNoInputsException
	 *             Ha a kapunak nincs bemenete
	 * @throws ElementInputSizeException
	 *             Ha a kapunak nincs meg a megfelelo szamu bemenete (2 darab)
	 * @throws GateNotConnectedException
	 *             Ha a kapu kimenetehez egyetlen tovabbi elem sem csatlakozik
	 */
	public int Count() {
		int Result = 0;
		if (wireIn == null || wireIn.isEmpty()) {
			// throw ElementHasNoInputsException;
		} else {
			if (wireIn.size() != 2) {
				// throw ElementInputSizeException
			} else {
				Result = wireIn.get(0).GetValue();
				if (Result == 0)
					Result = 0;
				if (Result == 1)
					Result = wireIn.get(1).GetValue();
				else if (wireIn.get(1).GetValue() == 0)
					Result = 0;
				else
					Result = -1;
			}
		}

		/* Vegignezzuk az osszes kimenetet... */
		if (wireOut == null || wireOut.isEmpty()) { // ha nincs csatlakoztatva
													// semmihez, hibat dob
			// throw GateNotConnectedWarning
		} else {
			for (Wire OutPut : wireOut) {
				OutPut.SetValue(Result);
			}
		}
		return Result;
	};

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e
	 * 
	 * @return Ha van a kapunak feedbacks tombje, haromszor vegigiteral a
	 *         tombelemen, s minden alkalommal osszehasonlitja a kapott
	 *         kapuerteket az elozo ertekkel. Ha az utolso ketto megegyezik,
	 *         stabil a kapu,{@code true} a visszateresi ertek, kulonben
	 *         {@code false}
	 */
	public boolean Step() {

		boolean Result = true; // A vegso eredmeny: Stabil-e az aramkor
		PreviousValue = Count(); // Megnezzuk az elso futas erredmenyet
		if (Feedbacks != null && !Feedbacks.isEmpty()) {// Ha nem ures a
														// Feedback tomb
			int NewValue; // Lokalis valtozo
			for (DigitalObject obj : Feedbacks) { // Feedback osses elemen vegig
				obj.Count();
			}
			NewValue = Count(); // Megnezzuk ujol az eredmenyt
			Result = (PreviousValue == NewValue); // Elter-e a ketto?( Prev es a
													// mostani)

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);

			for (DigitalObject obj : Feedbacks) {
				obj.Count();
			}
			NewValue = Count();
			Result = (PreviousValue == NewValue);
		}
		return Result;
	};
}

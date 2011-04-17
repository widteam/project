import java.util.ArrayList;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;SWITCH</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interfacek: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;DigitalObject-->Input</td>
 * </table> 
*<br>
* Specialis Input objektum, mely a kimenetet ciklikusan valtoztatja egy,
* felhasznalo altal szerkesztheto szekvencia alapjan. 
* Az objektum nyilvantartja a szekvenciat, illetve az aktualis poziciot
* a mintaban. A generator Reset utasitasanak hivasaval a pozicio a 
* minta elejere allithato.
*/
public class SWITCH extends Input {
	/* ATTRIBUTUMOK */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int SWITCHCount;

	/** KONSTRUKTOR */
	private void init(){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		Value = 0;	
	}
	/**
	 * KONSTRUKTOR. 
	 * @param strCompositName   az elemet tartalmazo kompozit neve
	 */
	public SWITCH(String strCompositName) {
		// Egyedi ID kiszamitasa
		String strIDNumber = String.valueOf(SWITCHCount++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;
		init();
	}
	/**
	 * KONSTRUKTOR
	 * @param strCompositName az elemet tartalmazo kompozit neve
	 * @param SwitchName az elem egyedi neve
	 */
	public SWITCH(String strCompositName,String SwitchName) {
		// Egyedi ID kiszamitasa
		final String strIDName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter+ SwitchName;
		init();
	}
	/* METODUSOK */
	/** A Value valtozo erteket allitja nullabol egybe, egybol nullaba */
	public void Toggle() {
		if (Value == 0)
			Value = 1;
		else
			Value = 0;
	};

	/**
	 * Kiszamolja egy DigitalObject erteket
	 * 
	 * @return A legutolso Toggle() utani erteket adja vissza
	 * @throws InputNotConnectedException
	 *             Ha nem csatlakozik egyetlen masik digitalObjecthez sem
	 */
	public int Count() {
		/* Az OSSZES kimenetre kiadjuk a kiszamitott eredmenyt. */
		if (wireOut == null || wireOut.isEmpty()) {
			// throw InputNotConnectedException
		}
		for (Wire OutPut : wireOut) {
			OutPut.SetValue(Value);
		}
		return Value;
	};

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e
	 * 
	 * @return Mindig {@code  true } ertekkel ter vissza
	 */
	public boolean Step() {
		Count(); // Meghivja a Count metodust
		return true; // A SWITCH mindig igazzal ter vissza
	}
}

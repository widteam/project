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

	/**
	 * KONSTRUKTOR
	 * @param strCompositName az elemet tartalmazo kompozit neve
	 * @param SwitchName az elem egyedi neve
	 */
	public SWITCH(String strCompositName,String SwitchName) {
		// Egyedi ID kiszamitasa
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter+ SwitchName;	
		
		wireOut = new ArrayList<Wire>();		

		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created by User.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}
	
	/* METODUSOK */
	/** A Value valtozo erteket allitja nullabol egybe, egybol nullaba */
	public void Toggle() {
		if (Value == 0)
			Value = 1;
		else
			Value = 0;
		
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, this.GetID()+" has been  toggled. New value is "+ Value);
		Logger.Log(Logger.log_type.USER, this.GetName()+"'s value changed");
			
	};

	/**
	 * Kiszamolja egy DigitalObject erteket
	 * 
	 * @return A legutolso Toggle() utani erteket adja vissza	 
	 */
	public int Count() {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<"+this.GetID()+" Count>");

		return Value;
	};

	/**
	 * a Step lepesben kiszamolja a sajat erteket (Count() hivas,lekerdezi a Value-t)
	 * es azt tovabbitja minden hozza csatlakozo drotnak
	 * 
	 * @return Mindig {@code  true } ertekkel ter vissza, SWITCH nem lehet isntabil
	 * @throws ExceptionElementNotConnected
	 *             Ha nem csatlakozik egyetlen masik digitalObjecthez sem
	 */
	public boolean Step() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<"+this.GetID()+" Step>");
		
		Count();		
		if (wireOut == null || wireOut.isEmpty()) {
			 throw new ExceptionElementNotConnected(this);
		}
		for (Wire OutPut : wireOut) {
			OutPut.SetValue(Value);
		}

		return true; // A SWITCH mindig igazzal ter vissza
	}
	
	/**
	 * A metodus a Switch Value attributumat irja
	 * @param value az uj ertek
	 * @return a kapcsolo uj erteke
	 */
	public int SetValue(int value){
		if(value==1)
			Value=value;
		else
			Value = 0;

		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, this.GetID() + "set to "+Value );

		return Value;
	}
}

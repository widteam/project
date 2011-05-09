import java.util.ArrayList;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;GENERATOR</td>
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
 * 		<td>&nbsp;&nbsp;DigitalObject-->Input</td>
 * </table> 
*<br>
* Specialis Input objektum, mely a kimenetet ciklikusan valtoztatja egy,
* felhasznalo altal szerkesztheto szekvencia alapjan. 
* Az objektum nyilvantartja a szekvenciat, illetve az aktualis poziciot
* a mintaban. A generator Reset utasitasanak hivasaval a pozicio a 
* minta elejere allithato.
*/
public class GENERATOR extends Input {
	/* ATTRIBUTUMOK */

	/**
	 * generator leptetesenek a gyakorisagat taroljuk ebben. Az itt megadott
	 * szamu {@code Step()} hivas utan fog csak lepni.
	 */
	private int Frequency;

	/**
	 * Ebben taroljuk a kimenetre kikuldendo mintat. Ertelmezese binaris.
	 */
	private String Sequence;

	/**
	 * Az attributum tartja nyilvan az aktualis poziciot a szekvenciaban. A
	 * {@code Reset()} hivas 0 ertekre allitja
	 */
	private int SequencePos;

	/**
	 * Egy segedszamlalo, mely nyilvantartja, hogy meg mennyi Count maradt hatra
	 */
	private int FrequencyCounter;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param strCompositName
	 *            A GENERATOR objektumot tartalmazo Composit neve, annak
	 *            erdekeben, hogy lehessenek azonos nevu elemek az egyes eltereo
	 *            compositokban
	 * @param StartFrequency
	 *            Kezdofrekvencia
	 * @param StartSequence
	 *            Letrehozaskor megadhato minta, ujabb ertek beallitasaig ezt
	 *            ismetli
	 * @param WiresOut
	 *            A GENERATOR-hoz csatlakozo Wire objektum referenciaja
	 * @throws ExceptionWrongParameter 
	 */
	public GENERATOR(String strCompositName, String GeneratorName,
			int StartFrequency, String StartSequence) throws ExceptionWrongParameter {
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ GeneratorName;
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		
		if(Frequency>0 ){
			Frequency = StartFrequency;
		}
		else{
			Frequency=1;
		}
		FrequencyCounter = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		Value = 0;

		// LOGOLAS
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created automtic.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}

	/* METODUSOK */
	/**
	 * A SequencePos. erteket allitja alapertelmezettre, azaz a minta elejere
	 */
	public void Reset() {
		// LOGOLAS
		Logger.Log(Logger.log_type.ADDITIONAL, "called "+this.GetType()+" (" +ID+")'s Reset()");
		
		SequencePos = 0; // Poziciot alapra
		Value = Integer.parseInt(String.valueOf(Sequence.charAt(SequencePos))); // Kiszamoljuk
																		// erteket
		for (Wire OutPut : wireOut) {
			OutPut.SetValue(Value); // Kiadjuk a kimenetre az aktualis erteker
		}// end for
	};

	/**
	 * a Frequency erteket allitja be, a parameterben megadott ertekre.
	 * 
	 * @param NewSequence
	 *            Az a minta melyet be kivanunk allitani
	 */
	public void SetSequence(String NewSequence) {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, this.GetID()+"'s Sequence has been  set. New Sequence is "+ NewSequence);
		Logger.Log(Logger.log_type.USER, this.GetName()+"’s Sequence is set to "+NewSequence);


		Sequence = NewSequence; // Beallitjuk a szekvenciat
		Reset();
	};

	/**
	 * a GENERATOR frekvenciajanak erteket allitja be, a parameterben megadott
	 * ertekre.
	 * 
	 * @param NewFrequency
	 *            Az uj frekvencia erteke
	 * @throws ExceptionWrongParameter 
	 */
	public void SetFrequency(int NewFrequency) throws ExceptionWrongParameter {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, this.GetID()+"'s Frequency has been  set. New Frequency is "+ NewFrequency);
		Logger.Log(Logger.log_type.USER, this.GetName()+"’s Frequency is set to "+NewFrequency);

		if(Frequency>0){
			Frequency = NewFrequency; // Beallitjuk a frekvenciat
			FrequencyCounter = NewFrequency; // Hany count maradt meg hatra
		}
		else{
			throw new ExceptionWrongParameter(this);
		}
	};

	
	public int Count(){ 
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<"+this.GetID()+" Count>");
		
		if(SequencePos < Sequence.length()){	// a szekvenciaban elore megyunk.. mar ha lehet
			Value = Integer.parseInt(String.valueOf(Sequence.charAt(SequencePos++)));	// Kiszamoljuk az uj erteket				
		}else{
			SequencePos = 0;
		}	
		Logger.Log(Logger.log_type.INFO, "<"+this.GetName()+"> value is "+Value+").");
		return Value; 
	}
	 
	/** 
	 * /** Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese,
	 * hogy a GENERATOR stabil-e
	 * 
	 * @return Mindig {@code  true } ertekkel ter vissza * @throws
	 *         InputNotConnectedException Ha nem csatlakozik egyetlen masik
	 *         digitalObjecthez sem
	 * @throws ExceptionElementNotConnected 
	 */
	public boolean Step() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<"+this.GetID()+" Step>");

		/* Az OSSZES kimenetre kiadjuk a kiszamitott eredmenyt. */
		FrequencyCounter--; // Csokkentjuk a szamlalot

		if (FrequencyCounter <= 0) { // HA megfelo szamu count eltelt mar
			Count(); // vegre meghivhatjuk a Count()-ot
			FrequencyCounter = Frequency; // Ujra az elejerol szamolunk
		}
		
		/* ha nincs drot csatlakoztava, hibat dob */
		if (wireOut == null || wireOut.isEmpty()) {
			 throw new ExceptionElementNotConnected(this);
		} else {
			for (Wire OutPut : wireOut) {
				OutPut.SetValue(Value); // Kiadjuk a kimenetre az aktualis
										// erteket
			}// end for
			
		}
		
		return true; // A GENERATOR mindig igazzal ter vissza
	};
	/**
	 * egyszeru getter metodus a szekvencia lekerdezesere
	 * @return a szekvencia
	 */
	public String getSequence(){
		return Sequence;
	}
	/**
	 * getter a frekvenciahoz
	 * @return a frekvencia
	 */
	public int getFrequency(){
		return Frequency;
	}
}

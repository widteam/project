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
	 */
	public GENERATOR(String strCompositName, String GeneratorName,
			int StartFrequency, String StartSequence) {
		final String strIDName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter
				+ GeneratorName;
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		Frequency = StartFrequency;
		FrequencyCounter = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		Value = 0;

	}

	/* METODUSOK */
	/**
	 * A SequencePos. erteket allitja alapertelmezettre, azaz a minta elejere
	 */
	public void Reset() {
		SequencePos = 0; // Poziciot alapra
		Value = Integer.parseInt(String.valueOf(Sequence.charAt(SequencePos))); // Kiszamoljuk
																				// az
																				// uj
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
		Sequence = NewSequence; // Beallitjuk a szekvenciat
		Reset();
	};

	/**
	 * a GENERATOR frekvenciajanak erteket allitja be, a parameterben megadott
	 * ertekre.
	 * 
	 * @param NewFrequency
	 *            Az uj frekvencia erteke
	 */
	public void SetFrequency(int NewFrequency) {

		Frequency = NewFrequency; // Beallitjuk a frekvenciat
		FrequencyCounter = NewFrequency; // Hany count maradt meg hatra
	};

	
	public int Count(){ 
		if(DebugMode){
			System.out.println("<"+this.GetID()+" Count>");
		}
		if(SequencePos >= Sequence.length()){	// a szekvenciaban elore megyunk.. mar ha lehet
			SequencePos = 0;				
		}else{
			SequencePos++;
			Value = Integer.parseInt(String.valueOf(Sequence.charAt(SequencePos)));	// Kiszamoljuk az uj erteket
		}	
		return Value; 
	}
	 
	/** 
	 * /** Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese,
	 * hogy a GENERATOR stabil-e
	 * 
	 * @return Mindig {@code  true } ertekkel ter vissza * @throws
	 *         InputNotConnectedException Ha nem csatlakozik egyetlen masik
	 *         digitalObjecthez sem
	 */
	public boolean Step() {
		if (DebugMode)
			System.out.println("<" + this.GetID() + " step>");

		int Result = 0;
		/* Az OSSZES kimenetre kiadjuk a kiszamitott eredmenyt. */
		FrequencyCounter--; // Csokkentjuk a szamlalot

		if (FrequencyCounter == 0) { // HA megfelo szamu count eltelt mar
			Count(); // vegre meghivhatjuk a Count()-ot
			/* ha nincs drot csatlakoztava, hibat dob */
			if (wireOut == null || wireOut.isEmpty()) {
				// throw new InputNotConnectedException
			} else {
				for (Wire OutPut : wireOut) {
					OutPut.SetValue(Value); // Kiadjuk a kimenetre az aktualis
											// erteket
				}// end for
				FrequencyCounter = Frequency; // Ujra az elejerol szamolunk
			}

		} else {
			for (Wire OutPut : wireOut) {
				OutPut.SetValue(Result);
			}
		}
		wireOut.get(0).SetValue(Value);// a rakotott drotot ertesitjuk
		return true; // A GENERATOR mindig igazzal ter vissza
	};

}

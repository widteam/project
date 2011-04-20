import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;Oscilloscope</td>
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
* A bemenet megjelenitesere szolgalo objektum/osztaly.
* Adott mennyisegu elozo erteket tarol es jelenit meg.
*/
public class Oscilloscope extends Output {
	/* ATTRIBUTUMOK */
	/** Statikus valtozo az egyedi ID ertekhez */
	private static int OscilloscopeCount;

	/** A mintat tarolo FIFO lista */
	private Queue<Integer> Samples;

	/* KONSTRUKTOROK */
	private void init(){
		wireOut = new ArrayList<Wire>();
		wireIn = new ArrayList<Wire>();		
	}
	public Oscilloscope(String strCompositName,  int SampleSize) {
		String strIDNumber = String.valueOf(OscilloscopeCount++);
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
				+ strClassName + strIDNumber;
		init();
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
	}
	
	public Oscilloscope(String strCompositName,  String OscilloscopeName,int SampleSize) {
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
		+ OscilloscopeName;
		init();
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
	}
	/* METODUSOK */
	/**
	 * Megkapja a bemenetenek erteket, es eltarolja azt.
	 * 
	 * @return a minta legregebbi eleme
	 * @throws ElementHasNoInputsException
	 *             Amennyiben az objektumnak nincs bemenete
	 * @throws ElementInputSizeException
	 *             Amennyiben az objektumnak nincs meg a megfelelo szamu
	 *             bemenete
	 */
	public int Count() {
		/* Lekerdezzuk a bemenetek ertekeit */
		if (wireIn == null || wireIn.isEmpty()) {
			// throw ElementHasNoInputsException;
		} else {
			if (wireIn.size() != 1) {
				// throw ElementInputSizeException
			} else {
				Value = Samples.poll(); // Utolso elem a mintabol kiesik
				Samples.add(wireIn.get(0).GetValue()); // hozzaadjuk az uj
														// erteket
			}
		}
		return Value;
	}

	/**
	 * Feladata az adott elem ertekenek kiszamitasa, ill. annak eldontese, hogy
	 * a DigitalObject stabil-e
	 * 
	 * @return mindig {@code true } ertekkel ter vissza, hiszen egy Output elem
	 *         mindig stabil.
	 */
	public boolean Step() {
		int i = Count();
		System.out.println(ID + " " + i);
		return true;
	}
	

}

import java.util.ArrayList;
import java.util.Iterator;
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

	/** A mintat tarolo FIFO lista */
	private Queue<Integer> Samples;
	private int SampleSize = 10;
	/* KONSTRUKTOR */
	public Oscilloscope(String strCompositName,  String OscilloscopeName,int _SampleSize) {
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
		+ OscilloscopeName;
		
		wireIn = new ArrayList<Wire>();	
		wireOut = new ArrayList<Wire>();
		SampleSize = _SampleSize;
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
		
		if(DebugMode)
			System.out.println(strClassName+" ("+ID+") has been  created by User.");
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
		if(DebugMode){
			System.out.println("<"+this.GetID()+" Count>");
		}
		/* Lekerdezzuk a bemenetek ertekeit */
		if (wireIn == null || wireIn.isEmpty()) {
			// throw new ElementHasNoInputsException;
		} else {
			if (wireIn.size() != 1) {
				// throw new ElementInputSizeException
			} else {
				Value=wireIn.get(0).GetValue();
				if(Samples.size() <= SampleSize){
					Samples.add(Value); // hozzaadjuk az uj erteket
				}
				else {
					Samples.poll();
					Samples.add(Value); // hozzaadjuk az uj erteket
				}
			}
		}
		if(DebugMode){
			System.out.print("<"+this.GetID()+" Sample is [");
			Iterator<Integer> it = Samples.iterator();
			while(it.hasNext())
				System.out.print(it.next());
			
			System.out.print("]\n");
		}
		return Value;
	}

	/**
	 * Megnezi a bemeno drot(ok) erteket es eltarolja  azt.
	 * Mivel nyelo, a Step() csak a Countot hivja, nem csinal mast.
	 * 
	 * @return mindig {@code true } ertekkel ter vissza, hiszen egy Output elem
	 *         mindig stabil.
	 */
	public boolean Step() {
		if (DebugMode)
			System.out.println("<" + this.GetID() + " step>");
		Count();
		return true;
	}

	public void SetSample(int _SampleSize) {
		if (DebugMode)
			System.out.print(this.GetID() + "'s SampleSize set to "
					+ _SampleSize);
		SampleSize = _SampleSize;
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
	}
}

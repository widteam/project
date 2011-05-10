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

	/**
	 * KONSTRUKTOR
	 * @param strCompositName Az elemet tartalmazo Composit neve
	 * @param OscilloscopeName az elem kivant neve
	 * @param _SampleSize a tarolando minta merete
	 * @throws ExceptionWrongParameter hibas parameter 
	 */
	public Oscilloscope(String strCompositName,  String OscilloscopeName,int _SampleSize) throws ExceptionWrongParameter {
		final String strClassName = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strClassName + strIDDelimiter
		+ OscilloscopeName;
		
		wireIn = new ArrayList<Wire>();	
		wireOut = new ArrayList<Wire>();
		if(_SampleSize>1){
			SampleSize = _SampleSize;
			
		}
		else{
			SampleSize = 10;
		}
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
			
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, strClassName+" ("+ID+") has been  created by User.");
		Logger.Log(Logger.log_type.USER, "create "+this.GetName()+" ("+strClassName+").");
	}
	
	/* METODUSOK */
	/**
	 * Megkapja a bemenetenek erteket, es eltarolja azt.
	 * 
	 * @return a minta legregebbi eleme
	 * @throws ExceptionElementHasNoInputs
	 *             Amennyiben az objektumnak nincs bemenete
	 * @throws ExceptionElementInputSize
	 *             Amennyiben az objektumnak nincs meg a megfelelo szamu
	 *             bemenete
	 */
	public int Count() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<"+this.GetID()+" Count>");

		/* Lekerdezzuk a bemenetek ertekeit */
		if (wireIn == null || wireIn.isEmpty()) {
			 throw new ExceptionElementHasNoInputs(this);
		} else {
			if (wireIn.size() != 1) {
				 throw new ExceptionElementInputSize(this);
			} else {
				Value=wireIn.get(0).GetValue();
				if(Samples.size() < SampleSize){
					Samples.add(Value); // hozzaadjuk az uj erteket
				}
				else {
					Samples.poll();
					Samples.add(Value); // hozzaadjuk az uj erteket
				}
			}
		}
		
		String str_values="";
		Iterator<Integer> it = Samples.iterator();
		while(it.hasNext())
			str_values+=it.next();
		Logger.Log(Logger.log_type.INFO, "<"+this.GetName()+"> value is ["+str_values +"]");

		return Value;
	}

	/**
	 * Megnezi a bemeno drot(ok) erteket es eltarolja  azt.
	 * Mivel nyelo, a Step() csak a Countot hivja, nem csinal mast.
	 * 
	 * @return mindig {@code true } ertekkel ter vissza, hiszen egy Output elem
	 *         mindig stabil.
	 * @throws ExceptionElementHasNoInputs
	 *             Amennyiben az objektumnak nincs bemenete
	 * @throws ExceptionElementInputSize
	 *             Amennyiben az objektumnak nincs meg a megfelelo szamu
	 * 
	 */
	public boolean Step() throws ExceptionsWithConnection {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, "<" + this.GetID() + " Step>");
		
		Count();
		return true;
	}

	/**
	 * Beallitja az Oscilloscope tarolt mintajanak nagysagat
	 * @param _SampleSize az uj tarolt minta hossza
	 * @throws ExceptionWrongParameter 
	 */
	public void SetSample(int _SampleSize) throws ExceptionWrongParameter {
		// LOGOLAS;
		Logger.Log(Logger.log_type.DEBUG, this.GetID()+"'s SampleSize has been  set. New SampleSize is "+ SampleSize);
		Logger.Log(Logger.log_type.USER, this.GetName()+"’s Sample size is set to "+SampleSize);

		if(_SampleSize>1){
			SampleSize = _SampleSize;
			Samples = new ArrayBlockingQueue<Integer>(SampleSize);
		}
		else{
			throw new ExceptionWrongParameter(this);
		}
	}
	/**
	 * Getter metodus a tarolt mintahoz
	 * @return a tarolt minta
	 */
	public int[] getSamples(){
		int i = 0;
		int[] returnvalue = new int[Samples.size()] ;
		Iterator<Integer> it = Samples.iterator();
		while(it.hasNext())
			returnvalue[i++] = (int)it.next();
		return returnvalue;
	}
	
	/**
	 * Visszaadja, hogy mekkora minta tarolasa van beallitva
	 * @return a tarolt minta maximuma
	 */
	public int getSampleSize(){
		return SampleSize;
	}
}

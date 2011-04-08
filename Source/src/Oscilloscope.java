/*
* N�v: 			Oscilloscope
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		DigitalObject-->Output
* 
*********** Le�r�s **********
* A bemenet megjelen�t�s�re szolg�l� objektum/oszt�ly.
* Adott mennyis�g� el�z� �rt�ket t�rol �s jelen�t meg.
*/

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Oscilloscope extends Output{
	/*  ATTRIB�TUMOK  */
	private static int OscilloscopeCount;
	private Queue<Integer> Samples;
	
	/*  KONSTRUKTOROK  */
	public Oscilloscope(String strCompositName, Wire WireIn, int SampleSize){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(OscilloscopeCount++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
		Samples = new ArrayBlockingQueue<Integer>(SampleSize);
	}
	
	/*  MET�DUSOK  */
	public int Count() {
		// Le�r�s: Megkapja a bemenet�nek �rt�k�t, �s elt�rolja azt.	
		if(wireIn == null || wireIn.isEmpty()){
			//throw ElementHasNoInputsException;
		}else{
			Value = Samples.poll();	// Utols� elem a mint�b�l kiesik	
			Samples.add(wireIn.get(0).GetValue());	// hozz�adjuk az �j �rt�ket
		}
		return Value;
	}

	public boolean Step() {
		/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
		 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
		*/	
			Count();
			return true;
	}

}

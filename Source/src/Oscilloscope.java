/*
* Név: 			Oscilloscope
* Típus: 		Class
* Interfacek:	---
* Szülõk		DigitalObject-->Output
* 
*********** Leírás **********
* A bemenet megjelenítésére szolgáló objektum/osztály.
* Adott mennyiségû elõzõ értéket tárol és jelenít meg.
*/

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Oscilloscope extends Output{
	/*  ATTRIBÚTUMOK  */
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
	
	/*  METÓDUSOK  */
	public int Count() {
		// Leírás: Megkapja a bemenetének értékét, és eltárolja azt.	
		if(wireIn == null || wireIn.isEmpty()){
			//throw ElementHasNoInputsException;
		}else{
			Value = Samples.poll();	// Utolsó elem a mintából kiesik	
			Samples.add(wireIn.get(0).GetValue());	// hozzáadjuk az új értéket
		}
		return Value;
	}

	public boolean Step() {
		/* Leírás: Feladata az adott elem értékének kiszámítása, 
		 * ill. annak eldöntése, hogy a DigitalObject stabil-e
		*/	
			Count();
			return true;
	}

}

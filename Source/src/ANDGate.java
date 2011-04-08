/*
 * Név: 			ANDGate
 * Típus: 		Class
 * Interfacek:	iComponent
 * Szülõk		DigitalObject-->Gate
 * 
 *********** Leírás **********
 * Logikai ÉS kaput megvalósító objektum. A bemeneteirõl beol-
 * vasott értékekbõl kiszámolja és továbbadja a kimenetére az
 * új értéket. Az új értékét az ÉS kapu igazságtáblája szerint
 * számolja ki, mely két bemenet esetén a következõ:
 * 
 * kimenet   bemenet
 *	A	B	A  AND B
 *	0	0		0
 *	0	1		0
 *	1	0		0
 *	1	1		1
 *	X	0		0
 *	X	1		X
 *	0	X		0
 *	1	X		X
 *	X	X		X
 * Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */

/*  IMPORTOK  */
import java.util.*;

public class ANDGate extends Gate {
	/* ATTRIBÚTUMOK */
	private static int ANDCounts; // Statikus változó az egyedi ID értékhez

	/* KONSTRUKTOR */
	public ANDGate(String strCompositName, Wire wirein1, Wire wirein2){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(ANDCounts++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = new ArrayList<Wire>(); // Inicializáljuk a wireIn listát
		wireOut = new ArrayList<Wire>();// Inicializáljuk a wireOut listát		
		wireIn.add(wirein1); // a konstruktorban megadott bemenetet bedrótozzuk
		wireIn.add(wirein2); // a másik bemenetet is bedrótozzuk
	}

	/* METÓDUSOK */
	public int Count() {
	// Leírás: Kiszámolja egy DigitalObject értékét
		int Result = 0;
		if(wireIn.size()!=2 ){
			if(wireIn == null || wireIn.isEmpty()){
				//throw ElementHasNoInputsException;
			}else{
				// throw GateInputSizeError
			}
		}else{
			Result = wireIn.get(0).GetValue();
			if(Result == 0)
				Result  = 0;
			if(Result == 1)
				Result  = wireIn.get(1).GetValue();
			else
				if(wireIn.get(1).GetValue() == 0) Result =0;
				else Result = -1;
		}
		/* Végignézzük az összes kimenetet... */
		if(wireOut == null || wireOut.isEmpty()){	// ha nincs csatlakoztatva semmihez, hibát dob
			// throw GateNotConnectedWarning
		}else{
			for(Wire OutPut:wireOut){
				OutPut.SetValue(Result);
			}
		}
		return Result;
	};

	public boolean Step() {
		/*
		 * Leírás: Feladata az adott elem értékének kiszámítása, ill. annak
		 * eldöntése, hogy a DigitalObject stabil-e
		 */
		boolean Result = true; 		// A végsõ eredmény: Stabil-e az áramkör
		PreviousValue = Count(); 	// Megnézzük az elsõ futás erredményét
		if (Feedbacks != null && !Feedbacks.isEmpty()) {// Ha nem üres a
														// Feedback tömb
			int NewValue; // Lokális változó
			for (DigitalObject obj : Feedbacks) { // Feedback össes elemén végig
				obj.Count();
			}
			NewValue = Count();		 // Megnézzük újól az eredményt
			Result = (PreviousValue == NewValue); 	// Eltér-e a kettõ?( Prev és a mostani)

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

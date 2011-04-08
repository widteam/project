import java.util.ArrayList;

/*
* Név: 			ORGate
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai VAGY kaput megvalósító objektum. A bemeneteirõl beol-
* vasott értékekbõl kiszámolja és továbbadja a kimenetére az
* új értéket. Az új értékét a VAGY kapu igazságtáblája szerint
* számolja ki, mely két bemenet esetén a következõ:
* 
* kimenet   bemenet
*	A	B	A  OR B
*	0	0		0
*	0	1		1
*	1	0		1
*	1	1		1
*	X	0		X
*	X	1		1
*	0	X		X
*	1	X		1
*	X	X		X
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */
public class ORGate extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int ORCounts;
	
	
	/*  KONSTRUKTOR  */
	public ORGate(String strCompositName, Wire wirein1, Wire wirein2){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(ORCounts++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}

	/* METÓDUSOK */
	public int Count() {
	// Leírás: Kiszámolja egy DigitalObject értékét
		int Result = 0;

		// Érték kiszámolása. HA van közte 1, akkor 1es, ha csak X van köte, X különben 0
		if(wireIn.size()!=2 ){
			if(wireIn == null || wireIn.isEmpty()){
				//throw ElementHasNoInputsException;
			}else{
				// throw GateInputSizeError
			}
		}else{
			Result = wireIn.get(0).GetValue();
			if(Result == 0)
				Result  = wireIn.get(1).GetValue();
			if(Result == -1)
				Result  = wireIn.get(1).GetValue();
			else
				Result =1;	
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
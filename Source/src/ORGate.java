import java.util.ArrayList;

/*
* N�v: 			ORGate
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Gate
* 
*********** Le�r�s **********
* Logikai VAGY kaput megval�s�t� objektum. A bemeneteir�l beol-
* vasott �rt�kekb�l kisz�molja �s tov�bbadja a kimenet�re az
* �j �rt�ket. Az �j �rt�k�t a VAGY kapu igazs�gt�bl�ja szerint
* sz�molja ki, mely k�t bemenet eset�n a k�vetkez�:
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
* Jelent�sek: 0: logikai HAMIS �rt�k | 1: logikai IGAZ �rt�k | X: don�t care

 */
public class ORGate extends Gate{
	/*  ATTRIB�TUMOK  */
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

	/* MET�DUSOK */
	public int Count() {
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t
		int Result = 0;

		// �rt�k kisz�mol�sa. HA van k�zte 1, akkor 1es, ha csak X van k�te, X k�l�nben 0
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
		/* V�gign�zz�k az �sszes kimenetet... */
		if(wireOut == null || wireOut.isEmpty()){	// ha nincs csatlakoztatva semmihez, hib�t dob
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
		 * Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, ill. annak
		 * eld�nt�se, hogy a DigitalObject stabil-e
		 */

		boolean Result = true; 		// A v�gs� eredm�ny: Stabil-e az �ramk�r
		PreviousValue = Count(); 	// Megn�zz�k az els� fut�s erredm�ny�t
		if (Feedbacks != null && !Feedbacks.isEmpty()) {// Ha nem �res a
														// Feedback t�mb
			int NewValue; // Lok�lis v�ltoz�
			for (DigitalObject obj : Feedbacks) { // Feedback �sses elem�n v�gig
				obj.Count();
			}
			NewValue = Count();		 // Megn�zz�k �j�l az eredm�nyt
			Result = (PreviousValue == NewValue); 	// Elt�r-e a kett�?( Prev �s a mostani)

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
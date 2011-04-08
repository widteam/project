/*
* Név: 			Gate
* Típus: 		Abstract Class
* Interfacek:	iComponent
* Szülõk		DigitalObject
* 
*********** Leírás **********
* Absztrakt osztály a logikai kapuk (pl. ÉS, VAGY, Inverter) leszármaztatására.
* A bemeneti vezeték(ek)nek lekérdezi az értékét, és kiszámolja a kimenet új
* értékét, és be is állítja azt, az leszármazott osztály igazságtáblája szerint

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class Gate extends DigitalObject{
	/*	ATTRIBÚTUMOK	*/
	protected int PreviousValue ;
	/* Leírás: A legutolsó ciklus ( Step() ) eredményét (kimeneti értékét) tárolja,
	 *  a stabilitásellenõrzés céljából (Count() metódus).
	*/	

	public Gate(){
		Feedbacks = new ArrayList<DigitalObject>();	// Feedback tömb inicializálva
		PreviousValue = -1;				// Don't care
	}
	
	/*	METÓDUSOK	*/
	public void AddToFeedbacks(DigitalObject feedback){
	// Leírás: Hozzáadja a paraméterként kapott DigitalObjectet a Feedbacks tömbjéhez.
		if(Feedbacks != null)
			Feedbacks.add(feedback);		// Hozzáadjuk a feedbackshez	
	};	
	public void AddOutput(Wire WireToOutput){
		if(wireOut != null)
			wireOut.add(WireToOutput);
	};
	
}

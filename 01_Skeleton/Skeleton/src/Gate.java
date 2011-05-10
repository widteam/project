/*
* Nev: 			Gate
* Tipus: 		Abstract Class
* Interfacek:	iComponent
* Szulok		DigitalObject
* 
*********** Leiras **********
* Absztrakt osztaly a logikai kapuk (pl. eS, VAGY, Inverter) leszarmaztatasara.
* A bemeneti vezetek(ek)nek lekerdezi az erteket, es kiszamolja a kimenet uj
* erteket, es be is allitja azt, az leszarmazott osztaly igazsagtablaja szerint

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class Gate extends DigitalObject{
	/*	ATTRIBuTUMOK	*/
	protected int PreviousValue ;
	/* Leiras: A legutolso ciklus ( Step() ) eredmenyet (kimeneti erteket) tarolja,
	 *  a stabilitasellenorzes celjabol (Count() metodus).
	*/
	
	protected List<DigitalObject>Feedbacks;
	/* Leiras: Ha egy Gate egyik bemenete egy visszacsatolas kezdete, 
	 * akkor tartalmaz egy Feedbacks tombot, mely referenciat tarol az osszes, 
	 * az adott visszacsatolasban resztvevo DigitalObject-re
	*/
	public Gate(){
		Feedbacks = new ArrayList<DigitalObject>();	// Feedbaxk tomb inicializalva
		PreviousValue = -1;				// Don't care
	}
	
	/*	METoDUSOK	*/
	public void AddToFeedbacks(DigitalObject feedback){
	// Leiras: Hozzaadja a parameterkent kapott DigitalObjectet a Feedbacks tombjehez.
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(feedback.ID,"", "");	/* TEST */
		Feedbacks.add(feedback);		// Hozzaadjuk a feedbackshez	
		stack.PrintTail(feedback.ID,"", ""); 	/* TEST */
	};	
	public void AddOutput(Wire WireToOutput){

	};
	
}

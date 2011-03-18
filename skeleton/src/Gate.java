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
	
	private ArrayList<DigitalObject>Feedbacks;
	/* Leírás: Ha egy Gate egyik bemenete egy visszacsatolás kezdete, 
	 * akkor tartalmaz egy Feedbacks tömböt, mely referenciát tárol az összes, 
	 * az adott visszacsatolásban résztvevõ DigitalObject-re
	*/
	public Gate(){
		PreviousValue = -1;
	}
	
	/*	METÓDUSOK	*/
	public void AddToFeedbacks(DigitalObject feedback){
	// Leírás: Hozzáadja a paraméterként kapott DigitalObjectet a Feedbacks tömbjéhez.
	};	
}

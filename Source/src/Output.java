/*
* Név: 			Output
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject
*  
*********** Leírás **********
* A bemenet megjelenítésére szolgáló objektum/osztály

*/
public abstract class Output extends DigitalObject{
	/*  ATTRIBÚTUMOK  */
	protected int Value;
	// Leírás: Az adott Output objektum értékét tárolja
	
	
	/*	METÓDUSOK	*/
	public void AddToFeedbacks(DigitalObject object) {
		;		// Outputnak nincs feedbackje.
	};
}

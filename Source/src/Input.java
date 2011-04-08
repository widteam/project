/*
* Név: 			Input
* Típus: 		Class
* Interfacek:	---
* Szülõk		DigitalObject
* 
*********** Leírás **********
* Bemenet nélküli áramköri elem, melynek kimenete vagy adott idõközönként, 
* vagy felhasználói interakció során változhat meg.

*/
public abstract class Input extends DigitalObject{
	/*  ATTRIBÚTUMOK  */
	protected int Value;
	// Leírás: Az adott Input objektum értékét tárolja
	
	
	/*	METÓDUSOK	*/
	public void AddToFeedbacks(DigitalObject object) {
		;		// Inputoknak nincs feedbackje.
	};
}

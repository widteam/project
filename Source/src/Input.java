/*
* N�v: 			Input
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		DigitalObject
* 
*********** Le�r�s **********
* Bemenet n�lk�li �ramk�ri elem, melynek kimenete vagy adott id�k�z�nk�nt, 
* vagy felhaszn�l�i interakci� sor�n v�ltozhat meg.

*/
public abstract class Input extends DigitalObject{
	/*  ATTRIB�TUMOK  */
	protected int Value;
	// Le�r�s: Az adott Input objektum �rt�k�t t�rolja
	
	
	/*	MET�DUSOK	*/
	public void AddToFeedbacks(DigitalObject object) {
		;		// Inputoknak nincs feedbackje.
	};
}

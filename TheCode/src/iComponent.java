/*
* Nev: 			iComponent
* Tipus: 		Interface
* Interfacek:	---
* Szulok		---
* 
*********** Leiras **********
* Interfesz, amely a DigitalBoard osztaly 
 * szamara egyseges hozzaferesi feluletet 
 * biztosit a kulonbozo DigitalObject 
 * objektumokhoz

*/

public interface iComponent {
	
	/*	METoDUSOK	*/
	public int Count();
	/* LEiras: Ha a DigitalObject kapu vagy kimenet, akkor a bemeno vezetekeirol
	 * lekerdezi az erteket, kiszamolja a logikai fuggvenyet. 
	 * Ha a DigitalObject kapu vagy bemenet, akkor beallitja a kimeno 
	 * vezetekeinek az erteket a korabbi szamitasi eredmenyere vagy a belso
	 * ertekere.
	*/
	
	public String GetID();
	// Leiras: Visszater az objektum egyedi azonositojaval
	
	public boolean Step();
	/* Leiras: Feladata az adott elem ertekenek kiszamitasa, ill. 
	 * annak eldontese, hogy a DigitalObject stabil-e. Ezt ugy teszi,
	 * hogy kiszamolja az erteket Count()-tal, ha egy visszacsatolas 
	 * kovetkezik a kimeneten, akkor vegigmegy a tombon es mindegyikre 
	 * kiszamoltatja az adott elem erteket. Ezt megismeteli meg ketszer 
	 * ugy, hogy minden alkalommal, amikor vegzett a Feedbacks tombon 
	 * valo szamolassal kiszamolja az erteket ujbol es megnezi, 
	 * hogy a sajat korabbi ertekevel megegyezo erteket szamolt ki. 
	 * Ha a harmadik Count() utan megegyezik a ket ertek, 
	 * akkor stabil a kapu es true-val ter vissza egyebkent false-al.
	*/
}

/*
* Név: 			iComponent
* Típus: 		Interface
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
* Interfész, amely a DigitalBoard osztály 
 * számára egységes hozzáférési felületet 
 * biztosít a különbözõ DigitalObject 
 * objektumokhoz

*/

public interface iComponent {
	
	/*	METÓDUSOK	*/
	public int Count();
	/* LEírás: Ha a DigitalObject kapu vagy kimenet, akkor a bemenõ vezetékeirõl
	 * lekérdezi az értéket, kiszámolja a logikai függvényét. 
	 * Ha a DigitalObject kapu vagy bemenet, akkor beállítja a kimenõ 
	 * vezetékeinek az értékét a korábbi számítási eredményére vagy a belsõ
	 * értékére.
	*/
	
	public String GetID();
	// Leírás: Visszatér az objektum egyedi azonosítójával
	
	public boolean Step();
	/* Leírás: Feladata az adott elem értékének kiszámítása, ill. 
	 * annak eldöntése, hogy a DigitalObject stabil-e. Ezt úgy teszi,
	 * hogy kiszámolja az értékét Count()-tal, ha egy visszacsatolás 
	 * következik a kimenetén, akkor végigmegy a tömbön és mindegyikre 
	 * kiszámoltatja az adott elem értékét. Ezt megismételi még kétszer 
	 * úgy, hogy minden alkalommal, amikor végzett a Feedbacks tömbön 
	 * való számolással kiszámolja az értékét újból és megnézi, 
	 * hogy a saját korábbi értékével megegyezõ értéket számolt ki. 
	 * Ha a harmadik Count() után megegyezik a két érték, 
	 * akkor stabil a kapu és true-val tér vissza egyébként false-al.
	*/
}

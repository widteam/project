/*
* Név: 			DigitalObject
* Típus: 		Abstract Class
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
* A Wire mellett ez a másik legfontosabb építõeleme a digitális
* áramköröknek. Meghatározzák, megjelenítik, vagy egy  belsõ függvény 
* szerint a kimenetükre csatlakozó Wire típusú objektumok értékeit 
* megváltoztatják. Minden DigitalObject típusú objektum egyedi azonosítóval
* rendelkezik.

*/

/*  IMPORTOK  */
import java.util.*;

public abstract class DigitalObject {
	/*	ATTRIBÚTUMOK	*/
	protected String ID;
	// Leírás: Egyedi azonosító. Tartalmazza az osztály nevét és egy számot
	
	protected List<Wire> wireIn;
	/* Leírás: Wire objektum-referenciákból álló lista azon Wire objektumokról, 
	 * melyek az objektum bemeneteihez kapcsolódnak.
	*/
	
	protected List<Wire> wireOut;
	/* Leírás: Wire objektum-referenciákból álló lista azon Wire objektumokról,
	 * melyek az objektum kimeneteihez kapcsolódnak	
	*/
	protected List<DigitalObject>Feedbacks;
	/* Leírás: Ha egy Gate egyik bemenete egy visszacsatolás kezdete, 
	 * akkor tartalmaz egy Feedbacks tömböt, mely referenciát tárol az összes, 
	 * az adott visszacsatolásban résztvevõ DigitalObject-re
	*/
	
	/*  KONSTRUKTOR  */
	public DigitalObject(){
	}
	/*  METÓDUSOK  */
	abstract public int Count();
	/* LEírás: Ha a DigitalObject kapu vagy kimenet, akkor a bemenõ vezetékeirõl
	 * lekérdezi az értéket, kiszámolja a logikai függvényét. 
	 * Ha a DigitalObject kapu vagy bemenet, akkor beállítja a kimenõ 
	 * vezetékeinek az értékét a korábbi számítási eredményére vagy a belsõ
	 * értékére.
	*/	
	abstract public boolean Step();
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
	abstract public void AddToFeedbacks(DigitalObject object);
	// Leírás: Hozzáadja a paraméterként kapott DigitalObjectet a Feedbacks tömbjéhez.
	
	public String GetID(){
	// Leírás: Visszaadja az objektumok egyedi azonosítóját
		return ID;
	}
}

/*
* Név: 			DigitalObject
* Típus: 		Abstract Class
* Interfacek:	iComponent
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

public abstract class DigitalObject implements iComponent {
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
	
	/*  KONSTRUKTOR  */
	public DigitalObject(){
	}
	/*  METÓDUSOK  */
	public String GetID(){
	// Leírás: Visszaadja az objektumok egyedi azonosítóját
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", ID + ":String");	/* TEST */		
		stack.PrintTail(ID,"", ID + ":String"); 	/* TEST */
		return null;
	}
}

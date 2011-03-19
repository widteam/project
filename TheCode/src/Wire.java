/*
* Név: 			Wire
* Típus: 		Class
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
 * Az áramkörben található értékek tárolására szolgáló objektum. 
 * DigitalObject típusú objektumok között teremt kapcsolatot.

*/

/*  IMPORTOK  */
import java.util.*;

public class Wire{
	/*  ATTRIBÚTUMOK  */
	private static int WIRECounts;	// Statikus változó az egyedi ID értékhez
		
	private String ID;
	/* Leírás: Egyedi karakteres azonosító, mely egyértelmûen meghatároz 
	 * egy Wire objektumot.
	*/
	
	private int Value;
	/* Leírás: A Wire objektumok által tárolt érték. 
	 * A Gate objektumok ezek alapján számolják ki kimenetüket
	*/
	
	private List<DigitalObject> wireIn;
	/* Leírás: DigitalObject objektum-referencia, amely a vezeték 
	 * bemenetéhez kapcsolódik.
	*/
	
	private List<DigitalObject> wireOut;
	/* Leírás: DigitalObject objektum-referencia, amely a vezeték 
	 * kimenetéhez kapcsolódik.
	*/
	
	/*  KONSTRUKTOR  */
	public Wire(){
		ID = "WIRE" + String.valueOf(WIRECounts++);
	}
	/*	METÓDUSOK	*/
	public String GetID(){
	// Leírás: A Wire egyedi azonosítójának lekérdezésére szolgáló metódus.
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszatérési érték, ide kell írni!
		return null; 
	
	};
	public int GetValue(){
	// Leírás: A Wire objektum értékének lekérdezésére szolgáló metódus
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszatérési érték, ide kell írni!
		return 0; 
	};
	public void SetValue(int NewValue){
	// Leírás: // Leírás: A Wire objektum értékének beállítására szolgáló metódus
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszatérési érték, ide kell írni!
	};
	public void SetConnection(DigitalObject ojectWhere, DigitalObject objectWhat){
	/* Leírás: Kapcsolatot teremt két DigitalObject között. 
	 * Beállítja a vezeték inputját (ojectWhere), illetve hozzáadja az output tömbjéhez 
	 * a DigitalObjectet (objectWhat).
	 */
		_TEST stack = new _TEST();	/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszatérési érték, ide kell írni!
	};
}

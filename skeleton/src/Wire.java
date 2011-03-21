/*
* Nev: 			Wire
* Tipus: 		Class
* Interfacek:	---
* Szulok		---
* 
*********** Leiras **********
 * Az aramkorben talalhato ertekek tarolasara szolgalo objektum. 
 * DigitalObject tipusu objektumok kozott teremt kapcsolatot.

*/

/*  IMPORTOK  */
import java.util.*;

public class Wire{
	/*  ATTRIBUTUMOK  */
	private static int WIRECounts;	// Statikus valtozo az egyedi ID ertekhez
		
	private String ID;
	/* Leiras: Egyedi karakteres azonosito, mely egyertelmuen meghataroz 
	 * egy Wire objektumot.
	*/
	
	@SuppressWarnings("unused")
	private int Value;
	/* Leiras: A Wire objektumok altal tarolt ertek. 
	 * A Gate objektumok ezek alapjan szamoljak ki kimenetuket
	*/
	
	@SuppressWarnings("unused")
	private List<DigitalObject> wireIn;
	/* Leiras: DigitalObject objektum-referencia, amely a vezetek 
	 * bemenetehez kapcsolodik.
	*/
	
	@SuppressWarnings("unused")
	private List<DigitalObject> wireOut;
	/* Leiras: DigitalObject objektum-referencia, amely a vezetek 
	 * kimenetehez kapcsolodik.
	*/
	
	/*  KONSTRUKTOR  */
	public Wire(){
		ID = "WIRE" + String.valueOf(WIRECounts++);
	}
	/*	METODUSOK	*/
	public String GetID(){
	// Leiras: A Wire egyedi azonositojanak lekerdezesere szolgalo metodus.
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszateresi ertek, ide kell irni!
		return null; 
	
	};
	public int GetValue(){
	// Leiras: A Wire objektum ertekenek lekerdezesere szolgalo metodus
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszateresi ertek, ide kell irni!
		return 0; 
	};
	public void SetValue(int NewValue){
	// Leiras: // Leiras: A Wire objektum ertekenek beallitasara szolgalo metodus
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszateresi ertek, ide kell irni!
	};
	public void SetConnection(DigitalObject ojectWhere, DigitalObject objectWhat){
	/* Leiras: Kapcsolatot teremt ket DigitalObject kozott. 
	 * Beallitja a vezetek inputjat (ojectWhere), illetve hozzaadja az output tombjehez 
	 * a DigitalObjectet (objectWhat).
	 */
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		// TODO: Ha vannak fuggvenyhivasok, el kell helyezni ide oket!
		stack.PrintTail(ID,"", ""); 	/* TEST */
		// TODO: Ha van visszateresi ertek, ide kell irni!
	};
}

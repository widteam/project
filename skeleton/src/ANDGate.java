import java.util.ArrayList;

/*
* Név: 			ANDGate
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai ÉS kaput megvalósító objektum. A bemeneteirõl beol-
* vasott értékekbõl kiszámolja és továbbadja a kimenetére az
* új értéket. Az új értékét az ÉS kapu igazságtáblája szerint
* számolja ki, mely két bemenet esetén a következõ:
* 
* kimenet   bemenet
*	A	B	A  AND B
*	0	0		0
*	0	1		0
*	1	0		0
*	1	1		1
*	X	0		0
*	X	1		X
*	0	X		0
*	1	X		X
*	X	X		X
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */
public class ANDGate extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int ANDCounts;
	/*  KONSTRUKTOR  */
	public ANDGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "AND" + ANDCounts++;
		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}
	/*	METÓDUSOK	*/
	public void Count(){
	// Leírás: Kiszámolja az ANDGate értékét az igazságtábla alapján	
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		return true;
	};

}

import java.util.ArrayList;

/*
* N�v: 			ANDGate
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Gate
* 
*********** Le�r�s **********
* Logikai �S kaput megval�s�t� objektum. A bemeneteir�l beol-
* vasott �rt�kekb�l kisz�molja �s tov�bbadja a kimenet�re az
* �j �rt�ket. Az �j �rt�k�t az �S kapu igazs�gt�bl�ja szerint
* sz�molja ki, mely k�t bemenet eset�n a k�vetkez�:
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
* Jelent�sek: 0: logikai HAMIS �rt�k | 1: logikai IGAZ �rt�k | X: don�t care

 */
public class ANDGate extends Gate{
	/*  ATTRIB�TUMOK  */
	private static int ANDCounts;
	/*  KONSTRUKTOR  */
	public ANDGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "AND" + ANDCounts++;
		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}
	/*	MET�DUSOK	*/
	public void Count(){
	// Le�r�s: Kisz�molja az ANDGate �rt�k�t az igazs�gt�bla alapj�n	
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		return true;
	};

}

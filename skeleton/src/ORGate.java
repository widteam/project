import java.util.ArrayList;

/*
* N�v: 			ORGate
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Gate
* 
*********** Le�r�s **********
* Logikai VAGY kaput megval�s�t� objektum. A bemeneteir�l beol-
* vasott �rt�kekb�l kisz�molja �s tov�bbadja a kimenet�re az
* �j �rt�ket. Az �j �rt�k�t a VAGY kapu igazs�gt�bl�ja szerint
* sz�molja ki, mely k�t bemenet eset�n a k�vetkez�:
* 
* kimenet   bemenet
*	A	B	A  OR B
*	0	0		0
*	0	1		1
*	1	0		1
*	1	1		1
*	X	0		X
*	X	1		1
*	0	X		X
*	1	X		1
*	X	X		X
* Jelent�sek: 0: logikai HAMIS �rt�k | 1: logikai IGAZ �rt�k | X: don�t care

 */
public class ORGate extends Gate{
	/*  ATTRIB�TUMOK  */
	private static int ORCounts;
	
	
	/*  KONSTRUKTOR  */
	public ORGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "OR" + ORCounts++;
		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}
	
	
	/*	MET�DUSOK	*/
	public void Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
	};		
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/
		return true;
	};

}
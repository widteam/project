import java.util.ArrayList;

/*
* Név: 			ORGate
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai VAGY kaput megvalósító objektum. A bemeneteirõl beol-
* vasott értékekbõl kiszámolja és továbbadja a kimenetére az
* új értéket. Az új értékét a VAGY kapu igazságtáblája szerint
* számolja ki, mely két bemenet esetén a következõ:
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
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */
public class ORGate extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int ORCounts;
	
	
	/*  KONSTRUKTOR  */
	public ORGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "OR" + ORCounts++;
		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}
	
	
	/*	METÓDUSOK	*/
	public void Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
	};		
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		return true;
	};

}
import java.util.ArrayList;

/*
* Név: 			INVERTER
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai invertert megvalósító objektum. Egyetlen egy bemenettel rendelkezik, 
* és kimenetére ennek inverzét adja, ha annak van értelme. 
* Igazságtáblája a következõ:
* 
* bemenet	kimenet
*	A		  NOT A
* 	0	  		1
*	1	  		0
*	X	  		X
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

 */
public class INVERTER extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int INVERTERCounts;
	
	
	/*  KONSTRUKTOR  */
	public INVERTER(Wire wirein1){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "INVERTER" + INVERTERCounts++;
		
		wireIn.add(wirein1);
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
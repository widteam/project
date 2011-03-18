import java.util.ArrayList;

/*
* N�v: 			INVERTER
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Gate
* 
*********** Le�r�s **********
* Logikai invertert megval�s�t� objektum. Egyetlen egy bemenettel rendelkezik, 
* �s kimenet�re ennek inverz�t adja, ha annak van �rtelme. 
* Igazs�gt�bl�ja a k�vetkez�:
* 
* bemenet	kimenet
*	A		  NOT A
* 	0	  		1
*	1	  		0
*	X	  		X
* Jelent�sek: 0: logikai HAMIS �rt�k | 1: logikai IGAZ �rt�k | X: don�t care

 */
public class INVERTER extends Gate{
	/*  ATTRIB�TUMOK  */
	private static int INVERTERCounts;
	
	
	/*  KONSTRUKTOR  */
	public INVERTER(Wire wirein1){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "INVERTER" + INVERTERCounts++;
		
		wireIn.add(wirein1);
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
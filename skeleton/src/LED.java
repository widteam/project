import java.util.ArrayList;

/*
* Név: 			GENERATOR
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Output
* 
*********** Leírás **********
* A bemenet megjelenítésére szolgáló objektum/osztály

*/
public class LED extends Output{
	/*  ATTRIBÚTUMOK  */
	private static int LEDCount;
	
	
	/*  KONSTRUKTOR  */
	public LED(Wire WireIn){
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
		ID = "LED" + LEDCount++;

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

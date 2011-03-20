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
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");

		/* Lekérdezzük a bemenetek értékeit */
		wireIn.get(0).GetValue();
		
		stack.PrintTail(ID,"","");
		return Value;
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		_TEST stack = new _TEST();
		stack.PrintHeader(ID,"","true:boolean");	
		Count();
		stack.PrintTail(ID,"","true:boolean");
		return true;
	};
}

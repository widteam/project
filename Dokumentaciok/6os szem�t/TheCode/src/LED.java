import java.util.ArrayList;

/*
* Nev: 			GENERATOR
* Tipus: 		Class
* Interfacek:	iComponent
* Szulok		DigitalObject-->Output
* 
*********** Leiras **********
* A bemenet megjelenitesere szolgalo objektum/osztaly

*/
public class LED extends Output{
	/*  ATTRIBuTUMOK  */
	private static int LEDCount;
	
	
	/*  KONSTRUKTOR  */
	public LED(Wire WireIn){
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
		ID = "LED" + LEDCount++;

	}

	
	
	/*	METoDUSOK	*/
	public int Count(){
	// Leiras: Kiszamolja egy DigitalObject erteket	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");

		/* Lekerdezzuk a bemenetek ertekeit */
		wireIn.get(0).GetValue();
		
		stack.PrintTail(ID,"","");
		return Value;
	};					
	public boolean Step(){
	/* Leiras: Feladata az adott elem ertekenek kiszamitasa, 
	 * ill. annak eldontese, hogy a DigitalObject stabil-e
	*/
		_TEST stack = new _TEST();
		stack.PrintHeader(ID,"","true:boolean");	
		Count();
		stack.PrintTail(ID,"","true:boolean");
		return true;
	};
}

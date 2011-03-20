import java.util.ArrayList;

/*
* N�v: 			GENERATOR
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Output
* 
*********** Le�r�s **********
* A bemenet megjelen�t�s�re szolg�l� objektum/oszt�ly

*/
public class LED extends Output{
	/*  ATTRIB�TUMOK  */
	private static int LEDCount;
	
	
	/*  KONSTRUKTOR  */
	public LED(Wire WireIn){
		wireOut = null;
		wireIn = new ArrayList<Wire>();
		wireIn.add(WireIn);
		ID = "LED" + LEDCount++;

	}

	
	
	/*	MET�DUSOK	*/
	public int Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");

		/* Lek�rdezz�k a bemenetek �rt�keit */
		wireIn.get(0).GetValue();
		
		stack.PrintTail(ID,"","");
		return Value;
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/
		_TEST stack = new _TEST();
		stack.PrintHeader(ID,"","true:boolean");	
		Count();
		stack.PrintTail(ID,"","true:boolean");
		return true;
	};
}

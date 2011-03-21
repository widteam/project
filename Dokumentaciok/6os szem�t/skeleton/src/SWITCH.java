import java.util.ArrayList;

/*
* N�v: 			SWITCH
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Input
* 
*********** Le�r�s **********
* Speci�lis Input objektum, mely a kimenet�t ciklikusan v�ltoztatja egy,
* felhaszn�l� �ltal szerkeszthet� szekvencia alapj�n. 
* Az objektum nyilv�ntartja a szekvenci�t, illetve az aktu�lis poz�ci�t
* a mint�ban. A gener�tor Reset utas�t�s�nak h�v�s�val a poz�ci� a 
* minta elej�re �ll�that�.

*/
public class SWITCH extends Input{
	/*  ATTRIB�TUMOK  */
	private static int SWITCHCount;
	
	
	/*  KONSTRUKTOR  */
	public SWITCH(Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		wireOut.add(WiresOut);
		ID = "SWITCH" + SWITCHCount++;
		
		Value = 0;
	}

	
	
	/*  MET�DUSOK  */
	public void Toggle(){
	// Le�r�s: A Value v�ltoz� �rt�k�t �ll�tja null�b�l egybe, egyb�l null�ba
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		/*if(Value == 0) Value = 1;
		else Value = 0;			*/
		stack.PrintTail(ID,"","");		/* TEST */
	};
	public int Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
	
		/* Az �SSZES kimenetre kiadjuk a kisz�m�tott eredm�nyt. Skeletonn�l csak egyre */
		/*for(Wire OutPut:wireOut){
			OutPut.SetValue(Value);	
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");		/* TEST */
		return Value;	
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	 */
			_TEST stack = new _TEST();				/* TEST */
			stack.PrintHeader(ID,"","");			/* TEST */		
			Count();								// MEgh�vja a Count met�dust
			stack.PrintTail(ID,"","true:boolean");	/* TEST */	
			return true;							// A SWITCH mindig igazzal t�r vissza
		};

}

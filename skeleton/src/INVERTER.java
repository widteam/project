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
		PreviousValue = -1;
		wireIn.add(wirein1);
	}
	
	
	/*	METÓDUSOK	*/
	public int Count(){
		// Leírás: Kiszámolja egy DigitalObject értékét	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lekérdezzük a bemenetek értékeit */
			wireIn.get(0).GetValue();
			
			// Eredmény kiszámítása
			//if(wireIn.get(0).GetValue() ==0)Result =1;
			//if(wireIn.get(0).GetValue() ==1)Result =0;
			//if(wireIn.get(0).GetValue() ==-1)Result =-1
			
			/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
			
			/*for(Wire OutPut:wireOut){
				OutPut.SetValue(Result);
			}
			*/
			Wire wire_out = new Wire();
			wire_out.SetValue(0);
			
			stack.PrintTail(ID,"",Result +":int");
			return Result;
		};		
		public boolean Step(){
			/* Leírás: Feladata az adott elem értékének kiszámítása, 
			 * ill. annak eldöntése, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A végsõ eredmény: Stabil-e az áramkör
				_TEST stack = new _TEST();					/* TEST */
				stack.PrintHeader(ID,"","true:boolean");	/* TEST */
				PreviousValue = Count();					// Megnézzük az elsõ futás erredményét
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem üres a Feedback tömb
					int NewValue ;						// Lokális változó
					for(DigitalObject obj:Feedbacks){	// Feedback össezs elemén végig
						obj.Count();
					}
					NewValue = Count();					// Megnézzük újól az eredményt
					Result = (PreviousValue==NewValue);	// Eltér-e a kettõ?( Prev és a mostani) 
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
				}
				stack.PrintTail(ID,"",Result + ":boolean");	/* TEST */
				return Result;
			};
}
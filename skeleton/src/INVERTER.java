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
		PreviousValue = -1;
		wireIn.add(wirein1);
	}
	
	
	/*	MET�DUSOK	*/
	public int Count(){
		// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lek�rdezz�k a bemenetek �rt�keit */
			wireIn.get(0).GetValue();
			
			// Eredm�ny kisz�m�t�sa
			//if(wireIn.get(0).GetValue() ==0)Result =1;
			//if(wireIn.get(0).GetValue() ==1)Result =0;
			//if(wireIn.get(0).GetValue() ==-1)Result =-1
			
			/* Az �SSZES kimenetre kiadjuk a kisz�m�tott eredm�nyt. Skeletonn�l csak egyre */
			
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
			/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
			 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A v�gs� eredm�ny: Stabil-e az �ramk�r
				_TEST stack = new _TEST();					/* TEST */
				stack.PrintHeader(ID,"","true:boolean");	/* TEST */
				PreviousValue = Count();					// Megn�zz�k az els� fut�s erredm�ny�t
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem �res a Feedback t�mb
					int NewValue ;						// Lok�lis v�ltoz�
					for(DigitalObject obj:Feedbacks){	// Feedback �ssezs elem�n v�gig
						obj.Count();
					}
					NewValue = Count();					// Megn�zz�k �j�l az eredm�nyt
					Result = (PreviousValue==NewValue);	// Elt�r-e a kett�?( Prev �s a mostani) 
					
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
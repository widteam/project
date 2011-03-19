import java.io.*;
public class main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line ="";
		Integer choice = -1;

		while(!line.contentEquals("exit")){	//M�g nem akar kil�pni a feslhaszn�l� nem l�p�nk ki			;

			System.out.println("TESZT SZAKASZ");
			System.out.println("K�rem v�lasszon a lehet�s�gek k�z�l!");
			System.out.println("  0 - Load Board (Egyszer�) ");
			System.out.println("  1 - Load Board (Visszacsatol�ssal) ");
			System.out.println("  2 - Load Board (Instabil) ");
			System.out.println("  3 - Toggle Switch");
			System.out.println("  4 - Set Sequence");
			System.out.println("  5 - Step Components(Egyszer�)");
			System.out.println("  6 - Step Components(Visszacsatol�ssal");
			System.out.println("  7 - Step Components(Visszacsatol�ssal");
			System.out.println("  8 - Count");
			System.out.println("  9 - Run");
			System.out.println("  10 - Pause");
			System.out.println("  11 - Stop");
			/* NEM LETT MEGVAL�S�TVA */ //System.out.println("  13 - Check Value");
			System.out.println("  exit - Kil�p�s");
			
			try{		// Sz�mm� alak�tjuk - ha tudjuk - a bevitt sz�veget
				choice  = Integer.decode(line);				
			}catch(Exception e){
				//line = "exit" ;
			}

			// Sz�tv�logatjuk az esaeteket
			switch (choice){
			case 0:	 // LoadBoard egyszer�
				// DigitalBoard l�trehoz�sa
				DigitalBoard o0	 = new DigitalBoard();
				o0.LoadBoard("0");					
				break;
			case 1:  // LoadBoard visszacsatol�ssal
				// DigitalBoard l�trehoz�sa
				DigitalBoard o1 = new DigitalBoard();
				o1.LoadBoard("1");
				break;
			case 2: // LoadBoard instabil
				// DigitalBoard l�trehoz�sa
				DigitalBoard o2 = new DigitalBoard();
				o2.LoadBoard("2");
				break;
			case 3: // Toggle switch
				// DigitalBoard l�trehoz�sa
				DigitalBoard o3 = new DigitalBoard();	
				// a Toggle(...)megh�v�sa egy tesztkapcsol�ra 
				o3.Toggle("SWITCH_to_toggle");
				break;
			case 4:	// SetSequence
				// DigitalBoard l�trehoz�sa
				DigitalBoard o4 = new DigitalBoard();	
				o4.SetSequence(5, "GEN_to_setsequence");
				break;
			case 8:	// Count
				SWITCH sw = new SWITCH(new Wire());
				System.out.println("SWITCH");
				sw.Count();
				GENERATOR gen = new GENERATOR(0,0,new Wire());
				System.out.println("GENERATOR");
				gen.Count();
				ORGate or = new ORGate(new Wire(),new Wire());
				or.AddOutput(new Wire());
				System.out.println("ORGATE");
				or.Count();
				ANDGate and = new ANDGate(new Wire(),new Wire());
				and.AddOutput(new Wire());
				System.out.println("AND GATE");
				and.Count();
				INVERTER inv = new INVERTER(new Wire());
				System.out.println("INVERTER");
				inv.Count();
				LED led = new LED(new Wire());
				System.out.println("LED");
				led.Count();
				break;	
			case 9:	// RUN
				// DigitalBoard l�trehoz�sa
				DigitalBoard o5 = new DigitalBoard();
				o5.Run();
				break;	
			case 10:	// Pause
				// DigitalBoard l�trehoz�sa
				DigitalBoard o6 = new DigitalBoard();
				o6.Pause();
				break;
			case 11:	// Stop
				// DigitalBoard l�trehoz�sa
				DigitalBoard o7 = new DigitalBoard();
				o7.Stop();
				break;
			case 5:		// Step Component, egyszer�
				DigitalBoard o8	 = new DigitalBoard();
				o8.LoadBoard("0");
				o8.StepComponents(0);
				break;
			case 6:		// Step Component, visszacsatol�s
				DigitalBoard o9	 = new DigitalBoard();
				o9.LoadBoard("1");
				o9.StepComponents(1);
				break;
			case 7:		// Step Component, visszacsatol�s, instabil
				DigitalBoard o10	 = new DigitalBoard();
				o10.LoadBoard("2");
				o10.StepComponents(2);
				break;
			default:
				break;
			}
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

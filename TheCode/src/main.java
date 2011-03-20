import java.io.*;
public class main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line ="";
		Integer choice = -1;

		// DigitalBoardok létrehozása
		DigitalBoard o0	 = new DigitalBoard();	// egyszerû
		DigitalBoard o1	 = new DigitalBoard();	// Stabil visszacsatolás
		DigitalBoard o2	 = new DigitalBoard();	// Instabil visszacsatolás

		while(!line.contentEquals("exit")){	//Míg nem akar kilépni a feslhasználó nem lépünk ki			;

			System.out.println("TESZT SZAKASZ");
			System.out.println("Kérem válasszon a lehetõségek közül!");
			System.out.println("  0 - Load Board (Egyszerû) ");
			System.out.println("  1 - Load Board (Visszacsatolással) ");
			System.out.println("  2 - Load Board (Instabil) ");
			System.out.println("  3 - Toggle Switch");
			System.out.println("  4 - Set Sequence");
			System.out.println("  5 - Step Components(Egyszerû)");
			System.out.println("  6 - Step Components(Visszacsatolással");
			System.out.println("  7 - Step Components(Visszacsatolással");
			System.out.println("  8 - Count");
			System.out.println("  9 - Run");
			System.out.println("  10 - Pause");
			System.out.println("  11 - Stop");
			/* NEM LETT MEGVALÓSÍTVA */ //System.out.println("  13 - Check Value");
			System.out.println("  exit - Kilépés");
			
			try{		// Számmá alakítjuk - ha tudjuk - a bevitt szöveget
				choice  = Integer.decode(line);				
			}catch(Exception e){
				//line = "exit" ;
			}

			// Szétválogatjuk az esaeteket
			switch (choice){
			case 0:	 // LoadBoard egyszerû
				o0.LoadBoard("0");					
				break;
			case 1:  // LoadBoard visszacsatolással
				o1.LoadBoard("1");
				break;
			case 2: // LoadBoard instabil
				o2.LoadBoard("2");
				break;
			case 3: // Toggle switch
				// a Toggle(...)meghívása egy tesztkapcsolóra 
				o0.Toggle("SWITCH_to_toggle");
				break;
			case 4:	// SetSequence
				o0.SetSequence(5, "GEN_to_setsequence");
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
				o0.Run();
				break;	
			case 10:	// Pause
				o0.Pause();
				break;
			case 11:	// Stop
				o0.Stop();
				break;
			case 5:		// Step Component, egyszerû
				o0.StepComponents(0);
				break;
			case 6:		// Step Component, visszacsatolós
				o1.StepComponents(1);
				break;
			case 7:		// Step Component, visszacsatolós, instabil
				o2.StepComponents(2);
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

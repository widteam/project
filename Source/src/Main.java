import java.io.*;
public class Main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line ="";
		Integer choice = -1;

		while(!line.contentEquals("exit")){	//Mig nem akar kilepni a feslhasznalo nem lepunk ki			;

			System.out.println("TESZT SZAKASZ");
			System.out.println("Kerem valasszon a lehetosegek kozul!");
			System.out.println("  0 - Csom�kteszt "); //Temporary, TODO �tcsin�lni!
			System.out.println("  1 - Load Board (Visszacsatolassal) ");
			System.out.println("  2 - Load Board (Instabil) ");
			System.out.println("  3 - Toggle Switch");
			System.out.println("  4 - Set Sequence");
			System.out.println("  5 - Step Components(Egyszeru)");
			System.out.println("  6 - Step Components(Visszacsatolassal");
			System.out.println("  7 - Step Components(Visszacsatolassal");
			System.out.println("  8 - Count");
			System.out.println("  9 - Run");
			System.out.println("  10 - Pause");
			System.out.println("  11 - Stop");
			/* NEM LETT MEGVALoSiTVA */ //System.out.println("  13 - Check Value");
			System.out.println("  exit - Kilepes");
			
			try{		// Szamma alakitjuk - ha tudjuk - a bevitt szoveget
				choice  = Integer.decode(line);				
			}catch(Exception e){
				//line = "exit" ;
			}

			// Szetvalogatjuk az esaeteket
			switch (choice){
			case 0:	 // LoadBoard egyszeru
				// DigitalBoard letrehozasa
				DigitalBoard o0	 = new DigitalBoard();
				o0.LoadBoard("0");					
				break;
			case 1:  // LoadBoard visszacsatolassal
				// DigitalBoard letrehozasa
				DigitalBoard o1 = new DigitalBoard();
				o1.LoadBoard("1");
				break;
			case 2: // LoadBoard instabil
				// DigitalBoard letrehozasa
				DigitalBoard o2 = new DigitalBoard();
				o2.LoadBoard("2");
				break;
			case 3: // Toggle switch
				// DigitalBoard letrehozasa
				DigitalBoard o3 = new DigitalBoard();	
				// a Toggle(...)meghivasa egy tesztkapcsolora 
				o3.Toggle("SWITCH_to_toggle");
				break;
			case 4:	// SetSequence
				// DigitalBoard letrehozasa
				DigitalBoard o4 = new DigitalBoard();	
				o4.SetSequence(5, "GEN_to_setsequence");
				break;
			case 8:	// Count
				SWITCH sw = new SWITCH("main",new Wire("main"));
				System.out.println("SWITCH");
				sw.Count();
				GENERATOR gen = new GENERATOR("main",0,0,new Wire("main"));
				System.out.println("GENERATOR");
				gen.Count();
				ORGate or = new ORGate("main",new Wire("main"),new Wire("main"));
				or.AddOutput(new Wire("main"));
				System.out.println("ORGATE");
				or.Count();
				ANDGate and = new ANDGate("main",new Wire("main"),new Wire("main"));
				and.AddOutput(new Wire("main"));
				System.out.println("AND GATE");
				and.Count();
				INVERTER inv = new INVERTER("main",new Wire("main"));
				System.out.println("INVERTER");
				inv.Count();
				LED led = new LED("main",new Wire("main"));
				System.out.println("LED");
				led.Count();
				break;	
			case 9:	// RUN
				// DigitalBoard letrehozasa
				DigitalBoard o5 = new DigitalBoard();
				o5.Run();
				break;	
			case 10:	// Pause
				// DigitalBoard letrehozasa
				DigitalBoard o6 = new DigitalBoard();
				o6.Pause();
				break;
			case 11:	// Stop
				// DigitalBoard letrehozasa
				DigitalBoard o7 = new DigitalBoard();
				o7.Stop();
				break;
			case 5:		// Step Component, egyszeru
				DigitalBoard o8	 = new DigitalBoard();
				o8.LoadBoard("0");
				o8.StepComponents();
				break;
			case 6:		// Step Component, visszacsatolos
				DigitalBoard o9	 = new DigitalBoard();
				o9.LoadBoard("1");
				o9.StepComponents();
				break;
			case 7:		// Step Component, visszacsatolos, instabil
				DigitalBoard o10	 = new DigitalBoard();
				o10.LoadBoard("2");
				o10.StepComponents();
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

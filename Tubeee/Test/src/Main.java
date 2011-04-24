import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Timer;

public class Main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line = "";

		DigitalBoard TheBoard = new DigitalBoard();
		Logger.logging_level = Logger.log_levels.MEDIUM;
		
		while (!line.contentEquals("exit")) {
			HandleUserCommand(TheBoard, reader);
		}
	}

	public static String[] GetCmdParams(String cmdLine) {

		Pattern regxp;
		Matcher match;

		String command = "";
		String param1 = "";
		String param2 = "";

		// Regularis Kifejezes
		regxp = Pattern.compile(".*? "); // Megkeressuk parancsot
		// Megkeressuk a talalatokat
		match = regxp.matcher(cmdLine);

		if (match.find()) {
			command = match.group();// Kimentjuk parancsot
		} else {
			regxp = Pattern.compile(".*");// Egyszavas parancs
			match = regxp.matcher(cmdLine);
			if (match.find()) {
				command = match.group();// Kimentjuk parancsot
			}
		}

		cmdLine = cmdLine.replace(command, "");// Kivesszuk talalatot,reszre
												// keresunk cska
		command = command.replace(" ", ""); // Kivesszuk spacet

		match = regxp.matcher(cmdLine);
		if (match.find()) {
			param1 = match.group();// Kimentjuk elso parametert
		} else {
			regxp = Pattern.compile(".*");// Egyszavas parancs
			match = regxp.matcher(cmdLine);
			if (match.find()) {
				param1 = match.group();// Kimentjuk parancsot
			}
		}

		cmdLine = cmdLine.replace(param1, "");// Kivesszuk talalatot,reszre
												// keresunk cska
		param1 = param1.replace(" ", ""); // Kivesszuk spacet

		regxp = Pattern.compile(".*");
		match = regxp.matcher(cmdLine);
		match.find();
		param2 = match.group();// Kimentjuk masodik parametert
		param2 = param2.replace(" ", ""); // Kivesszuk spacet

		return new String[] { command, param1, param2 };

	}

	public static void HandleUserCommand(final DigitalBoard Board,
			BufferedReader reader) {

		int TimerIntervalInMS = 5000;
		Timer BoardClock;

		String  boardpath = "";
		ActionListener TimerTask = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if(Board.GetStatus() == Status.RUNNING)			  			
				try {
					Board.StepComponents();	    	
				} catch (ExceptionElementHasNoInputs ehni) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: ElementHasNoInput: A megjelolt elemnek nincs bemenete! /Hiba itt: "
									+ ehni.TheObject.GetName() + "/");
				} catch (ExceptionElementNotConnected ehni) {
					Logger.Log(
							Logger.log_type.ERROR,
							"i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez!/Hiba itt: "
									+ ehni.TheObject.GetName() + "/");
				} catch (ExceptionUnstableCircuit instab) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato. /Hiba itt: "
									+ instab.TheObject.GetName() + "/");
				} catch (ExceptionWireHasMultipleInputs e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: WireHasMultipleInputs: Nem egyertelmu Wire bemenet! /Hiba itt: "
									+ e.TheObject.GetName() + "/");
				} catch (ExceptionElementInputSize e) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: UnstableCircuit: A megjelolt elem nem rendelkezik a megfelelo szamu bemenettel! /Hiba itt: "
									+ e.TheObject.GetName() + "/");
				} catch (ExceptionsWithConnection e) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: ErrorWithConnections: Meghatarozhattalan hiba tortent az aramkori kapcsolatok letrehozasakor!");
				} catch (Exception e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: UnnownError: Meghatarozhatatlan tortent!");
				}

			}
		};
		BoardClock = new Timer(TimerIntervalInMS, TimerTask);
		BoardClock.setCoalesce(false);
	
		System.out.println("Parancs         Parameterlista   Hatas");
		System.out.println("loadBoard       XY               Betolt egy BHDL fajlt");
		System.out.println("setFrequency    ELEM_ID   FREKV  Generator frekv.-t allitja");
		System.out.println("setSample       ELEM_ID  MERET   Oscilloscope mintajanak nagysagat allitja");
		System.out.println("stepComponents                   Egyet leptet az aramkoron");
		System.out.println("run                              Elinditja a szimulaciot.");
		System.out.println("pause                            felfuggeszti a szimulaciot.");
		System.out.println("stop                             megallitja a szimulaciot.");
		System.out.println("toggleSwitch    ELEM_ID          fel/le kapcsolja az adott elemet");
		System.out.println("setSequence     ELEM_ID  SZEKV   Beallitja az adott Generator szekvenciajat");
		System.out.println("\n");
		
		System.out.println("setOutput       MOD              0-fajlba ir,1 STDOUT,2 mindketto");
		/*System.out.println("setLogfile      FILE             beallitja a logolashoz hasznalt fajlt");*/
		/*System.out.println("setLogmode      MOD              0-3 logolasi szintek");*/
		System.out.println("setInterval     DELAY            A futattast mukodteto Timer delaye MSben");
		
		System.out.println("\n");
		System.out.println("exit                             Kilepes a tesztelesbol");
		System.out.println("\n");
		
		System.out.println("/*******Parancsok fogadasa*******/");

		String cmdLine = "";

		String command = "";
		String param1 = "";
		String param2 = "";

		while (!cmdLine.contentEquals("exit")) {
			try {
				// a bevitt szoveget
				cmdLine = reader.readLine();

				// parancs es parameterek meghatarozasa
				command = GetCmdParams(cmdLine)[0];
				param1 = GetCmdParams(cmdLine)[1];
				param2 = GetCmdParams(cmdLine)[2];
				//Logger.Log(Logger.log_type.INFO,command+" "+param1 + " "+param2);
				
				// setFrequency [sfreq]
				if (command.equalsIgnoreCase("setFrequency") || command.equalsIgnoreCase("sfreq")) {
					try {
						Board.SetFrequency(Integer.parseInt(param2), param1);
					} catch (NumberFormatException e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: Wrong Parameter: /" + param2 + "/");
					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Meghatarozhatatlan tortent!");
					}
				} 
				// setSample [ssampl]
				else if (command.equalsIgnoreCase("setSample") || command.equalsIgnoreCase("ssampl")) {
					try {
						Board.SetSample(Integer.parseInt(param2), param1);
					} catch (NumberFormatException e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: Wrong Parameter: /" + param2 + "/");
					} catch (ExceptionObjectNotFound e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e2.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Meghatarozhatatlan tortent!");
					}
				}
				// LoadBoard [lb]
				else if (command.equalsIgnoreCase("loadBoard") || command.equalsIgnoreCase("lb")) {
					try {
						Board.LoadBoard(param1);
						boardpath = param1;
						Logger.Log(Logger.log_type.INFO, param1 + " is loaded");
					} catch (IOException e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: FileNotFound: Nem olvashato a megadott bemeneti fajl.");
					} catch (ExceptionWrongBoard e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: WrongBoard: Rosszul formazott BHDL fajl!");
					} catch (Exception e2) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Meghatarozhatatlan tortent!");
					}	
				}
				
				// stepComponents [step]
				else if (command.equalsIgnoreCase("stepComponents") || command.equalsIgnoreCase("step")) {
					try {						
						if(Board.GetStatus() != Status.STOPPED)
					  		Board.StepComponents();						
						Logger.Log(Logger.log_type.INFO,
								"Board circuit has stepped");
					} catch (ExceptionElementHasNoInputs ehni) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: ElementHasNoInput: A megjelolt elemnek nincs bemenete! /Hiba itt: "
										+ ehni.TheObject.GetName() + "/");
					} catch (ExceptionElementNotConnected ehni) {
						Logger.Log(
								Logger.log_type.ERROR,
								"i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez!/Hiba itt: "
										+ ehni.TheObject.GetName() + "/");
					} catch (ExceptionUnstableCircuit instab) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato. /Hiba itt: "
										+ instab.TheObject.GetName() + "/");
					} catch (ExceptionWireHasMultipleInputs e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: WireHasMultipleInputs: Nem egyertelmu Wire bemenet! /Hiba itt: "
										+ e.TheObject.GetName() + "/");
					} catch (ExceptionElementInputSize e) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: UnstableCircuit: A megjelolt elem nem rendelkezik a megfelelo szamu bemenettel! /Hiba itt: "
										+ e.TheObject.GetName() + "/");
					} catch (ExceptionsWithConnection e) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: ErrorWithConnections: Meghatarozhattalan hiba tortent az aramkori kapcsolatok letrehozasakor!");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnnownError: Meghatarozhatatlan tortent!");
					}					
				} 
				// run
				else if (command.equalsIgnoreCase("run")) {
					try {						
						Board.Run();
						BoardClock.setDelay(TimerIntervalInMS);
						BoardClock.addActionListener(TimerTask);
						if(!BoardClock.isRunning())BoardClock.start();
						
						
						Logger.Log(Logger.log_type.INFO,
								"Simulation is running");

					} catch (ExceptionElementHasNoInputs ehni) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: ElementHasNoInput: A megjelolt elemnek nincs bemenete! /Hiba itt: "
										+ ehni.TheObject.GetName() + "/");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (ExceptionElementNotConnected ehni) {
						Logger.Log(
								Logger.log_type.ERROR,
								"i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez!/Hiba itt: "
										+ ehni.TheObject.GetName() + "/");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (ExceptionUnstableCircuit instab) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato. /Hiba itt: "
										+ instab.TheObject.GetName() + "/");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (ExceptionWireHasMultipleInputs e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: WireHasMultipleInputs: Nem egyertelmu Wire bemenet! /Hiba itt: "
										+ e.TheObject.GetName() + "/");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (ExceptionElementInputSize e) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: UnstableCircuit: A megjelolt elem nem rendelkezik a megfelelo szamu bemenettel! /Hiba itt: "
										+ e.TheObject.GetName() + "/");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (ExceptionsWithConnection e) {
						Logger.Log(
								Logger.log_type.ERROR,
								"x Error: ErrorWithConnections: Meghatarozhattalan hiba tortent az aramkori kapcsolatok letrehozasakor!");
						Board.Stop();
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnnownError: Meghatarozhatatlan tortent!");
						BoardClock.removeActionListener(TimerTask);
						BoardClock.stop();
					}
				} 
				
				// pause
				else if (command.equalsIgnoreCase("pause")) {
					Logger.Log(Logger.log_type.INFO,
							"Simulation is not running");
					Board.Pause();				
				} 
				// Stop
				else if (command.equalsIgnoreCase("stop")) {
					Logger.Log(Logger.log_type.INFO, "Simulation stopped");
					Board.LoadBoard(boardpath);
					BoardClock.stop();
				
				} 
				// setOutput
				else if (command.equalsIgnoreCase("setOutput")) {
					Logger.Log(Logger.log_type.INFO, "Output mode is set to "
							+ param1);
					Logger.log_mode = Integer.parseInt(param1);
				} 
				// setLogfile
				/*else if (command.equalsIgnoreCase("setLogfile")) {
					Logger.Log(Logger.log_type.INFO, "Log file is set to"
							+ param1);
					Logger.log_file = param1;
				
				}*/ 
				// setInterval [sint]
				else if (command.equals("setInterval") || command.equals("sint")) {
					Logger.Log(Logger.log_type.INFO,
							"Boards interval is set to " + param1);
					BoardClock.setDelay(Integer.parseInt(param1));
				
				// toggleSwitch [ts] [toggle]
				} else if (command.equalsIgnoreCase("toggleSwitch") || command.equalsIgnoreCase("toggle") || command.equalsIgnoreCase("ts")) {
					try {
						Board.Toggle(param1);

					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Meghatarozhatatlan tortent!");
					}
					
					

				// setSequence [sseq]
				} else if (command.equalsIgnoreCase("setSequence") || command.equalsIgnoreCase("sseq")) {
					try {
						Board.SetSequence(param2, param1);
					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Meghatarozhatatlan tortent!");
					}
					
					
				// exit
				} else if (command.equalsIgnoreCase("exit")) {
					System.exit(0);

				} /*else if (command.equalsIgnoreCase("setLogmode")) {
					if (Integer.parseInt(param1) == 0) {
						Logger.logging_level = Logger.log_levels.LOW;
						Logger.Log(Logger.log_type.INFO,
								"Log mode set to LOW (Essential)");
					} 
					else if (Integer.parseInt(param1) == 1) {
						Logger.logging_level = Logger.log_levels.MEDIUM;
						Logger.Log(Logger.log_type.INFO,
								"Log mode set to MEDIUM (User info)");
					} 
					else if (Integer.parseInt(param1) == 2) {
						Logger.logging_level = Logger.log_levels.HIGH;
						Logger.Log(Logger.log_type.INFO,
								"Log mode set to HIGH (Debug mode)");
					} 
					else if (Integer.parseInt(param1) == 3) {
						Logger.logging_level = Logger.log_levels.EXTREME;
						Logger.Log(Logger.log_type.INFO,
								"Log mode set to EXTREME (Show all function call)");
					}					
				} */else {
					System.out.println("Unknown command");
				}
			} catch (Exception e) {
				Logger.Log(Logger.log_type.INFO,e.toString());
			}
		}
	}
}

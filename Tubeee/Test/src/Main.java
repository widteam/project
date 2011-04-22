import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line = "";

		final DigitalBoard TheBoard = new DigitalBoard();
		/*********************************************/
		Wire.DebugMode = false;
		DigitalObject.DebugMode = false;
		DigitalBoard.DebugMode = false;
		bhdlParser.DebugMode = false;
		/*********************************************/

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

	public static void HandleUserCommand(DigitalBoard Board,
			BufferedReader reader) {
		System.out.println("Parancs         Parameterlista          \t Hatas");
		System.out.println("loadBoard       [XY]                    \t A megadott allomanyt betolti");
		System.out.println("setFrequency    [FREKVENCIA] [ELEM_ID]  \t Az adott ID-ju elem frekvenciajat allitja");
		System.out.println("setSample       [MINTAMERET] [ELEM_ID]  \t Oscilloscope mintajanak nagysagat allitja");
		System.out.println("stepComponents                          \t Egyet leptet az aramkoron");
		System.out.println("run                                     \t Elinditja a szimulaciot. (csak 3-at lep)");
		System.out.println("toggleSwitch    [ELEM_ID]               \t fel/le kapcsolja az adott elemet");
		System.out.println("setSequence     [SZEKVENCIA] [ELEM_ID]  \t Beallitja az adott Generator szekvenciajat");
		System.out.println("debug           [SZINT]                 \t 0 - kikapcsol, 4 - mindent mutat.");
		System.out.println("exit                                    \t Kilepes a tesztelesbol");
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

				// setFrequency
				if (command.equalsIgnoreCase("setFrequency")) {
					try {
						Board.SetFrequency(Integer.parseInt(param2), param1);
						System.out.println(param1 + "'s frequency is set to "
								+ param2);
					} catch (NullPointerException e) {
						System.out
								.println("x Error: Wrong Parameter: No object with id "
										+ param1);
						continue;
					} catch (Exception ex) {
						System.out
								.println("x Error: Wrong Parameter: Object is not Generator");
						continue;
					}

					// setSample
				} else if (command.equalsIgnoreCase("setSample")) {
					try {
						Board.SetSample(Integer.parseInt(param2), param1);
						System.out.println(param1 + "'s sample size is set to "
								+ param2);
					} catch (NullPointerException e) {
						System.out
								.println("x Error: Wrong Parameter: No object with id "
										+ param1);
						continue;
					} catch (Exception ex) {
						System.out
								.println("x Error: Wrong Parameter: Object is not Oscilloscope");
						continue;
					}
				}
				// LoadBoard
				else if (command.equalsIgnoreCase("loadBoard")) {
					try {
						Board.LoadBoard(param1);
						System.out.println(param1 + " is loaded");
					} catch (FileNotFoundException exc) {
						System.out
								.println("x Error: FileNotFound: Nem olvashato a megadott bemeneti fajl.");
						continue;
					} catch (Exception ex) {
						System.out
								.println("x Error: Wrong Parameter: Object is not Oscilloscope");
						continue;
					}

					// stepComponents
				} else if (command.equalsIgnoreCase("stepComponents")) {
					try {
						Board.StepComponents();
						System.out.println("Board circuit has stepped");
					} catch (ElementHasNoInputsException ehni) {
						System.out
								.println("x Error: ElementHasNoInput: A megjelolt elemnek (elemeknek) nincs bemenete");
						// MEG KENE ADNI MELYIK ELEMNEK
					} catch (ElementNotConnectedException ehni) {
						System.out
								.println("i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez");
						// MEG KENE ADNI MELYIK ELEMNEK

					} catch (UnstableCircuitException instab) {
						System.out
								.println("x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato.");
					}

				} else if (command.equalsIgnoreCase("run")) {
					try {
						Board.Run();
						System.out.println("Simulation is running");
					} catch (ElementHasNoInputsException ehni) {
						System.out
								.println("x Error: ElementHasNoInput: A megjelolt elemnek (elemeknek) nincs bemenete");
						// MEG KENE ADNI MELYIK ELEMNEK
					} catch (ElementNotConnectedException ehni) {
						System.out
								.println("i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez");
						// MEG KENE ADNI MELYIK ELEMNEK

					} catch (UnstableCircuitException instab) {
						System.out
								.println("x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato.");
					}

					// setOutput
				} else if (command.equalsIgnoreCase("setOutput")) {
					/**** EZ NINCS KESZ!!! ****/
					System.out.println("Output is set to " + param1);
					// setInterval
				} else if (command.equals("setInterval")) {
					/**** EZ NINCS KESZ!!! ****/
					System.out.println("Boards interval is set to " + param1);

					// toggleSwitch
				} else if (command.equalsIgnoreCase("toggleSwitch")) {
					try {
						Board.Toggle(param1);
					} catch (NullPointerException e) {
						System.out
								.println("x Error: Wrong Parameter: No object with id "
										+ param1);
						continue;
					} catch (Exception ex) {
						System.out
								.println("x Error: Wrong Parameter: Object is not Switch");
						continue;
					}

					// setSequence
				} else if (command.equalsIgnoreCase("setSequence")) {
					try {
						Board.SetSequence(param2, param1);
						System.out.println(param1 + "'s sequence is set to "
								+ param2);
					} catch (NullPointerException e) {
						System.out
								.println("x Error: Wrong Parameter: No object with id "
										+ param1);
						continue;
					} catch (Exception ex) {
						System.out
								.println("x Error: Wrong Parameter: Object is not Generator");
						continue;
					}

					// exit
				} else if (command.equalsIgnoreCase("exit")) {
					System.exit(0);
				} else if (command.equalsIgnoreCase("debug")) {
					if (Integer.parseInt(param1) == 0) {
						Wire.DebugMode = false;
						DigitalObject.DebugMode = false;
						DigitalBoard.DebugMode = false;
						bhdlParser.DebugMode = false;
					} else if (Integer.parseInt(param1) == 1) {
						Wire.DebugMode = false;
						DigitalObject.DebugMode = true;
						DigitalBoard.DebugMode = false;
						bhdlParser.DebugMode = false;
					} else if (Integer.parseInt(param1) == 2) {
						Wire.DebugMode = false;
						DigitalObject.DebugMode = true;
						DigitalBoard.DebugMode = false;
						bhdlParser.DebugMode = true;
					} else if (Integer.parseInt(param1) == 3) {
						Wire.DebugMode = false;
						DigitalObject.DebugMode = true;
						DigitalBoard.DebugMode = true;
						bhdlParser.DebugMode = true;
					} else if (Integer.parseInt(param1) == 4) {
						Wire.DebugMode = true;
						DigitalObject.DebugMode = true;
						DigitalBoard.DebugMode = true;
						bhdlParser.DebugMode = true;
					}
				} else {
					System.out.println("Unknown command");
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}

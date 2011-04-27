import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;




public class Controller implements ActionListener {

	private int maxX = 500;
	private int maxY = 500;

	/* a MODELL */
	/** A modellt tartalmazó változó. */
	DigitalBoard digitalboard;
	
	/* a VIEW */
	// ////////////

	/* Frame-k */
	/** Az ablak, amelyben fut az alkalmazásunk. */
	JFrame frame;

	/* Timerek */
	/** A Timer delay-e */
	int delay = 5000;
	/** A futtatás alatti idõzítésért felel. */
	Timer timer;

	/* Gombok */
	/** Rakattintva betölti a modellt (hálózatot). */
	JButton loadBoardButton;
	/** Rakattintva betölti a modellt (hálózatot). */
	JButton loadSpecBoardButton;
	/** Rakattintva futtatja a hálózatot. */
	JButton runButton;
	/** Rakattintva megállítja a hálózat futását. */
	JButton pauseButton;
	/** Rakattintva alaphelyzetbe állítja a hálózatot. */
	JButton stopButton;
	/** Rakattintva lepteti a halozatot */
	JButton stepButton;
	/** Rakattintva kilep a programbol */
	JButton exitButton;

	/* Listek */
	/**
	 * A program lefutása során elõforduló események jelzése a felhasználó felé.
	 */
	JList eventList;
	DefaultListModel listModel = new DefaultListModel();
	
	// ActionCommandek
	protected final static String LOAD_BOARD = "loadBoard";
	protected final static String LOAD_BOARD_1 = "loadBoard1";
	protected final static String RUN_SIMULATION = "run";
	protected final static String STOP_SIMULATION = "stop";
	protected final static String PAUSE_SIMULATION = "pause";
	protected final static String STEP = "stepComponents";
	protected final static String EXIT = "exit";
	protected final static String TICK = "tick";

	public Controller() {
		// A modell inicializálása
		digitalboard = new DigitalBoard();

		// Kepernyo meretenek lekerese
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		maxX = screenSize.width;
		maxY = screenSize.height;

		// Elemek letrehozasa
		/* Timer */
		timer = new Timer(delay, this);
		timer.setActionCommand(TICK);
		
		/* Load Board */
		loadBoardButton = new JButton("Load", new ImageIcon("__KEP__HELYE__"));
		loadBoardButton.setActionCommand(LOAD_BOARD);
		loadBoardButton.addActionListener(this);

		/* Teszthez by csomák.. debugot segiti, ha van egy gyorsgomb */
		loadSpecBoardButton = new JButton("Load1es", new ImageIcon("__KEP__HELYE__"));
		loadSpecBoardButton.setActionCommand(LOAD_BOARD_1);
		loadSpecBoardButton.addActionListener(this);
		
		/* Run */
		runButton = new JButton("Run", new ImageIcon("__KEP__HELYE__"));
		runButton.setActionCommand(RUN_SIMULATION);
		runButton.addActionListener(this);

		/* Pause */
		pauseButton = new JButton("Pause", new ImageIcon("__KEP__HELYE__"));
		pauseButton.setActionCommand(PAUSE_SIMULATION);
		pauseButton.addActionListener(this);

		/* Stop */
		stopButton = new JButton("Stop", new ImageIcon("__KEP__HELYE__"));
		stopButton.setActionCommand(STOP_SIMULATION);
		stopButton.addActionListener(this);

		/* Exit */
		exitButton = new JButton("Exit", new ImageIcon("__KEP__HELYE__"));
		exitButton.setActionCommand(EXIT);
		exitButton.addActionListener(this);
		
		/* Step Components */
		stepButton = new JButton("Step", new ImageIcon("__KEP__HELYE__"));
		stepButton.setActionCommand(STEP);
		stepButton.addActionListener(this);

		eventList = new JList(listModel); //data has type Object[]
		eventList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		eventList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		eventList.setVisibleRowCount(-1);



		// Panel letrehozasa
		JPanel pane = new JPanel();

		// panelhez hozzaadom az elemeket
		pane.add(loadBoardButton);
		pane.add(loadSpecBoardButton);
		pane.add(runButton);
		pane.add(pauseButton);
		pane.add(stopButton);
		pane.add(stopButton);			
		pane.add(exitButton);
		//pane.add(eventList);
		
		// letrehozom a frame-t
		frame = new JFrame("DigitalCircuit Simulator  - WID");
		// ha bezarjuk reagaljon ra
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// frame-hez a panelt
		frame.add(pane);

		frame.setSize(maxX, maxY);
		frame.setVisible(true);

		Logger.logging_level = Logger.log_levels.MEDIUM;
		Logger.listModel=listModel;
	}
	
	
	public void actionPerformed(ActionEvent event) {

		String command = event.getActionCommand();
		String path = "";

		/* FILTER */
		class BHDLFileFilter extends javax.swing.filechooser.FileFilter {
		    public boolean accept(java.io.File f) {
		        return f.isDirectory() || f.getName().toLowerCase().endsWith(".bhdl");
		    }
		    
		    public String getDescription() {
		        return "ButaHDL fajlok (*.bhdl)";
		    }
		}
		
		if (command.equalsIgnoreCase("loadBoard1")) {
			try {
				digitalboard.LoadBoard("teszt5.bhdl");
				Logger.Log(Logger.log_type.INFO, path + " is loaded");
				boardView bdv=new boardView(digitalboard);
				//bdv.setSize(500,500);
				frame.add(bdv);
				bdv.paintComponent(frame.getGraphics());
			} catch (IOException e2) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: FileNotFound: Nem olvashato a megadott bemeneti fajl.");
			} catch (ExceptionWrongBoard e2) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: WrongBoard: Rosszul formazott BHDL fajl!");
			} catch (Exception e) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"
								+ e.toString() + " File: " + path);
			}
		}
		
		if (command.equalsIgnoreCase("loadBoard")) {
			/* FileBrowser letrehozasa a palya meghatarozasara */
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new BHDLFileFilter());
			int rVal = chooser.showOpenDialog(frame);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				path = chooser.getSelectedFile().getPath();
				try {
					digitalboard.LoadBoard(path);
					Logger.Log(Logger.log_type.INFO, path + " is loaded");
					boardView bdv=new boardView(digitalboard);
					bdv.setSize(500,500);
					frame.add(bdv);
					bdv.paintComponent(frame.getGraphics());
				} catch (IOException e2) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: FileNotFound: Nem olvashato a megadott bemeneti fajl.");
				} catch (ExceptionWrongBoard e2) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: WrongBoard: Rosszul formazott BHDL fajl!");
				} catch (Exception e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"
									+ e.toString() + " File: " + path);
				}
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				path = "";
			}
		}// end loadboard
		// stepComponents [step]
		else if (command.equalsIgnoreCase("stepComponents")) {
			try {
				if (digitalboard.GetStatus() != Status.STOPPED)
					digitalboard.StepComponents();
				Logger.Log(Logger.log_type.INFO,
						"digitalboard circuit has stepped");
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
						"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"
								+ e.toString());
			}
		}// end stepComponents
		// run
		else if (command.equalsIgnoreCase("run")) {
			digitalboard.Run();
			timer.start();
			Logger.Log(Logger.log_type.INFO, "Simulation started.");
		}

		// pause
		else if (command.equalsIgnoreCase("pause")) {
			Logger.Log(Logger.log_type.INFO, "Simulation is not running");
			digitalboard.Pause();
		}
		// Stop
		else if (command.equalsIgnoreCase("stop")) {
			try {
				digitalboard.LoadBoard(path);
				Logger.Log(Logger.log_type.INFO, path + " is loaded");
			} catch (IOException e2) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: FileNotFound: Nem olvashato a megadott bemeneti fajl.");
			} catch (ExceptionWrongBoard e2) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: WrongBoard: Rosszul formazott BHDL fajl!");
			} catch (Exception e) {
				Logger.Log(Logger.log_type.ERROR,
						"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"
								+ e.toString() + " File: " + path);
			}
			timer.stop();
			Logger.Log(Logger.log_type.INFO, "Simulation stopped");
		}
		// tick
		else if (command.equalsIgnoreCase("tick")) {
			if (digitalboard.GetStatus() == Status.RUNNING){
				try {
					digitalboard.StepComponents();
				} catch (ExceptionElementHasNoInputs ehni) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: ElementHasNoInput: A megjelolt elemnek nincs bemenete! /Hiba itt: "
									+ ehni.TheObject.GetName() + "/");
					digitalboard.Stop();
					timer.stop();

				} catch (ExceptionElementNotConnected ehni) {
					Logger.Log(
							Logger.log_type.ERROR,
							"i Warning: ElementHasNoOutput: Egy kimenettel rendelkezo elem nem csatlakozik tovabbi aramkori elemhez!/Hiba itt: "
									+ ehni.TheObject.GetName() + "/");
					digitalboard.Stop();
					timer.stop();

				} catch (ExceptionUnstableCircuit instab) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: UnstableCircuit: Instabil aramkor, a szimulacio nem futtathato. /Hiba itt: "
									+ instab.TheObject.GetName() + "/");
					digitalboard.Stop();
					timer.stop();

				} catch (ExceptionWireHasMultipleInputs e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: WireHasMultipleInputs: Nem egyertelmu Wire bemenet! /Hiba itt: "
									+ e.TheObject.GetName() + "/");
					digitalboard.Stop();
					timer.stop();

				} catch (ExceptionElementInputSize e) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: UnstableCircuit: A megjelolt elem nem rendelkezik a megfelelo szamu bemenettel! /Hiba itt: "
									+ e.TheObject.GetName() + "/");
					digitalboard.Stop();
					timer.stop();

				} catch (ExceptionsWithConnection e) {
					Logger.Log(
							Logger.log_type.ERROR,
							"x Error: ErrorWithConnections: Meghatarozhattalan hiba tortent az aramkori kapcsolatok letrehozasakor!");
					digitalboard.Stop();
					timer.stop();

				} catch (NullPointerException e) {
					Logger.Log(Logger.log_type.ERROR,
							"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					timer.stop();

				} catch (Exception e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"
									+ e.toString());
					digitalboard.Stop();
					timer.stop();

				}//end try
			}//end if board==running
			else if(command.equalsIgnoreCase("exit")){
				System.exit(1);
			}
		}// end tick
	}
}
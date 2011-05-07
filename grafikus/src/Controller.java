
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Controller implements ActionListener {

    /* a MODELL */
    /** A modellt tartalmazo valtozo. */
    DigitalBoard digitalboard;

    /* a VIEW */
    private boardView BoardView;

    /* Frame-k */
    /** Az ablak, amelyben fut az alkalmazasunk. */
    JFrame frame;

    /* Timerek */
    /** A Timer delay-e */
    Integer delay = 1000;
    /** A futtatás alatti idozitesert felel */
    Timer timer;

    /* Gombok */
    /** Rakattintva betolti a modellt (halozatot). */
    JButton loadBoardButton;
    /** Rakattintva futtatja a halozatot. */
    JButton runButton;
    /** Rakattintva megallitja a halozat futasat. */
    JButton pauseButton;
    /** Rakattintva alaphelyzetbe allitja a halozatot. */
    JButton stopButton;
    /** Rakattintva lepteti a halozatot */
    JButton stepButton;
    /** Rakattintva kilep a programbol */
    JButton exitButton;
    /** Ertek beirasa utan ENTER-re beallitja a Timer erteket */
    JTextField timerValueField;
    JPanel pane;
    
    /* Listek */
    /**
     * A program lefutï¿½sa sorï¿½n elï¿½fordulï¿½ esemï¿½nyek jelzï¿½se a felhasznï¿½lï¿½ felï¿½.
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
    protected final static String TIMER = "timerChanged";
    protected final static String TICK = "tick";

    public Controller() {
        // A modell inicializalasa
        digitalboard = new DigitalBoard();

        // a view inicializalasa
        BoardView = new boardView(digitalboard);

        // letrehozom a frame-t
        frame = new JFrame("DigitalCircuit Simulator  - WID");
        // ha bezarjuk reagaljon ra
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.setSize(800, 700);
        frame.setResizable(false);

        // Elemek letrehozasa
        /* Timer */
        timer = new Timer(delay, this);

        timer.setActionCommand(TICK);
        // Gombok merete
        Dimension buttonSize = new Dimension(100, 30);

        /* Load Board */
        loadBoardButton = new JButton("Load", new ImageIcon("__KEP__HELYE__"));
        loadBoardButton.setActionCommand(LOAD_BOARD);
        loadBoardButton.setPreferredSize(buttonSize);
        loadBoardButton.addActionListener(
                this);

        /* Run */
        runButton = new JButton("Run", new ImageIcon("__KEP__HELYE__"));
        runButton.setActionCommand(RUN_SIMULATION);
        runButton.setPreferredSize(buttonSize);
        runButton.addActionListener(
                this);

        /* Pause */
        pauseButton = new JButton("Pause", new ImageIcon("__KEP__HELYE__"));
        pauseButton.setActionCommand(PAUSE_SIMULATION);
        pauseButton.setPreferredSize(buttonSize);
        pauseButton.addActionListener(this);

        /* Stop */
        stopButton = new JButton("Stop", new ImageIcon("__KEP__HELYE__"));
        stopButton.setActionCommand(STOP_SIMULATION);
        stopButton.setPreferredSize(buttonSize);
        stopButton.addActionListener(this);

        /* Exit */
        exitButton = new JButton("Exit", new ImageIcon("__KEP__HELYE__"));
        exitButton.setActionCommand(EXIT);
        exitButton.setPreferredSize(buttonSize);
        exitButton.addActionListener(this);

        /* Step Components */
        stepButton = new JButton("Step", new ImageIcon("__KEP__HELYE__"));
        stepButton.setActionCommand(STEP);
        stepButton.setPreferredSize(buttonSize);
        stepButton.addActionListener(this);

        /* Timer value */
        timerValueField = new JTextField();
        timerValueField.setActionCommand(TIMER);
        timerValueField.setPreferredSize(buttonSize);
        timerValueField.setText(delay.toString());
        timerValueField.addActionListener(this);
        timerValueField.setHorizontalAlignment(JTextField.CENTER);


        eventList = new JList(listModel); // data has type Object[]
        eventList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        eventList.setLayoutOrientation(JList.VERTICAL);
         eventList.setVisibleRowCount(5);
        //eventList.setPreferredSize(
        //        new Dimension(frame.getSize().width - 50, 100));
        eventList.setBackground(Color.LIGHT_GRAY);
        JScrollPane listScroller = new JScrollPane(eventList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        	      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setPreferredSize(new Dimension(frame.getSize().width - 50, 100));
        listScroller.setBorder(new LineBorder(Color.LIGHT_GRAY));
        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(
                new FlowLayout());
        buttonPanel.setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT);

        buttonPanel.setBackground(Color.LIGHT_GRAY);

        buttonPanel.add(loadBoardButton);
        //buttonPanel.add(loadSpecBoardButton);
        buttonPanel.add(runButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(timerValueField);

        frame.add(buttonPanel, BorderLayout.NORTH);

        BoardView.setBackground(Color.WHITE);

        frame.add(BoardView, BorderLayout.CENTER);
        JPanel eventPanel = new JPanel();

        eventPanel.add(listScroller);

        eventPanel.setBackground(Color.LIGHT_GRAY);

        frame.add(eventPanel, BorderLayout.SOUTH);

        frame.setVisible(
                true);

        Logger.logging_level = Logger.log_levels.MEDIUM;
        Logger.listModel = listModel;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        String path = "";

        /* FILTER */
        class BHDLFileFilter extends javax.swing.filechooser.FileFilter {

            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".bhdl");
            }

            @Override
            public String getDescription() {
                return "ButaHDL fajlok (*.bhdl)";
            }
        }

        if (command.equalsIgnoreCase("exit")) {
            System.exit(0);
        }


        if (command.equalsIgnoreCase("loadBoard1")) {
            try {
                digitalboard.LoadBoard("teszt4.bhdl");
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
            } finally {
                frame.repaint();
            }
        }

        if (command.equalsIgnoreCase("loadBoard")) {
            /* FileBrowser letrehozasa a palya meghatarozasara */
            JFileChooser chooser = new JFileChooser(new java.io.File("").getAbsolutePath());
            chooser.setFileFilter(new BHDLFileFilter());
            int rVal = chooser.showOpenDialog(frame);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                path = chooser.getSelectedFile().getPath();
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
                } finally {
                    frame.repaint();
                }
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                path = "";
            }
        }// end loadboard
        // stepComponents [step]
        else if (command.equalsIgnoreCase("stepComponents")) {
            try {
                if (digitalboard.GetStatus() != Status.STOPPED) {
                    digitalboard.StepComponents();
                }
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
            } finally {
                frame.repaint();
            }
        }// end stepComponents
        // run
        else if (command.equalsIgnoreCase("run")) {
            digitalboard.Run();
            timer.start();
            Logger.Log(Logger.log_type.INFO, "Simulation started.");
            frame.repaint();
        } // pause
        else if (command.equalsIgnoreCase("pause")) {
            Logger.Log(Logger.log_type.INFO, "Simulation is not running");
            digitalboard.Pause();
            frame.repaint();
        } // Stop
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
            frame.repaint();
        } else if (command.equalsIgnoreCase("timerChanged")) {
            
            try {
                timer.setDelay(Integer.parseInt(timerValueField.getText()));
            } catch (Exception e) {
                timer.setDelay(5000);
            }

        }// tick
        else if (command.equalsIgnoreCase("tick")) {
            if (digitalboard.GetStatus() == Status.RUNNING) {
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

                } finally {
                    frame.repaint();
                }// end try
            }
        }// end tick
    }
}
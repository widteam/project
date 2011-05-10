import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.border.LineBorder;

public class Controller implements ActionListener,MouseListener {

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
     * A program lefutasa soran elofordulo esemenyek jelzese a felhasznalo fele.
     */
    JList eventList;
    DefaultListModel listModel = new DefaultListModel();
    
    // ActionCommandek
    protected final static String LOAD_BOARD = "loadBoard";
    protected final static String RUN_SIMULATION = "run";
    protected final static String STOP_SIMULATION = "stop";
    protected final static String PAUSE_SIMULATION = "pause";
    protected final static String STEP = "stepComponents";
    protected final static String EXIT = "exit";
    protected final static String TIMER = "timerChanged";
    protected final static String TICK = "tick";


    // Gombok merete
    Dimension buttonSize = new Dimension(100, 30);
    
    public Controller() {
        // A modell inicializalasa
        digitalboard = new DigitalBoard();

        // a view inicializalasa
        BoardView = new boardView(digitalboard);
        BoardView.addMouseListener(this);
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

        if(digitalboard.getMainComposit()==null){
        	stopButton.setEnabled(false);
        	pauseButton.setEnabled(false);
        	stepButton.setEnabled(false);        	 
        	runButton.setEnabled(false);
        }
        else if(digitalboard.GetStatus() == Status.STOPPED){
        	stopButton.setEnabled(false);
        	pauseButton.setEnabled(false);
        	stepButton.setEnabled(false);        	 
        	runButton.setEnabled(true);
        }
        else if(digitalboard.GetStatus() == Status.PAUSED){        	
        	pauseButton.setEnabled(false);        	
        	stepButton.setEnabled(true);        	 
        	stopButton.setEnabled(true);
        	runButton.setEnabled(true);
        }
        else if(digitalboard.GetStatus() == Status.RUNNING){
        	
        	pauseButton.setEnabled(true);        	
        	stepButton.setEnabled(false);        	 
        	stopButton.setEnabled(true);
        	runButton.setEnabled(false);
        }
        eventList = new JList(listModel); // data has type Object[]
        eventList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        eventList.setLayoutOrientation(JList.VERTICAL);
        eventList.setVisibleRowCount(5);
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
        Logger.mainFrame=frame;
    }
    String path = "";
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        

        /* FILTER */
        class BHDLFileFilter extends javax.swing.filechooser.FileFilter {

            
            public boolean accept(java.io.File f) {
                return f.isDirectory()
                        || f.getName().toLowerCase().endsWith(".bhdl");
            }

           
            public String getDescription() {
                return "ButaHDL fajlok (*.bhdl)";
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
                	digitalboard.Stop();
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
        } 
        // pause
        else if (command.equalsIgnoreCase("pause")) {
            Logger.Log(Logger.log_type.INFO, "Simulation is not running");
            digitalboard.Pause();
            frame.repaint();
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
            digitalboard.Stop();
            Logger.Log(Logger.log_type.INFO, "Simulation stopped");
            frame.repaint();
        } 
        //timerValueChanged
        else if (command.equalsIgnoreCase("timerChanged")) {
            
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
        //exit
        else if (command.equalsIgnoreCase("exit")) {
            System.exit(0);
        }
        
        if(digitalboard.getMainComposit()==null){
        	stopButton.setEnabled(false);
        	pauseButton.setEnabled(false);
        	stepButton.setEnabled(false);        	 
        	runButton.setEnabled(false);
        }
        else if(digitalboard.GetStatus() == Status.STOPPED){
        	stopButton.setEnabled(false);
        	pauseButton.setEnabled(false);
        	stepButton.setEnabled(false);        	 
        	runButton.setEnabled(true);
        }
        else if(digitalboard.GetStatus() == Status.PAUSED){        	
        	pauseButton.setEnabled(false);        	
        	stepButton.setEnabled(true);        	 
        	stopButton.setEnabled(true);
        	runButton.setEnabled(true);
        }
        else if(digitalboard.GetStatus() == Status.RUNNING){
        	
        	pauseButton.setEnabled(true);        	
        	stepButton.setEnabled(false);        	 
        	stopButton.setEnabled(true);
        	runButton.setEnabled(false);
        }
    }

	/**
	 * Elkapjuk az eger esemenyeket. Azon belul jelenitunk meg popup windowokat...
	 * az egyes popup windowok-hoz sajat actionListener tartoziK!
	 */
	public void mouseClicked(MouseEvent arg0) {
		
		final String tmpID = arg0.getComponent().getName();
		if (tmpID != null && !tmpID.equalsIgnoreCase("null")) {
			String tipus = tmpID.split("#")[1].trim();
			
			/* toggle */
			if (tipus.equalsIgnoreCase("switch")) {
				try {
					digitalboard.Toggle(tmpID);
				} catch (ExceptionObjectNotFound e1) {
					Logger.Log(Logger.log_type.ERROR,
							"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
									+ e1.ItemID + "/");
				} catch (NullPointerException e) {
					Logger.Log(Logger.log_type.ERROR,
							"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
				} catch (Exception e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
				}
				frame.repaint();
			} 
			/*Oscilloscope: setSize , show */
			else if (tipus.equalsIgnoreCase("oscilloscope")) {

				/********************************************************
				 *  A grafikon 
				 ********************************************************/
				class Graph extends Canvas {
					private static final long serialVersionUID = 1L;
					private int num_of_samples = 10;
					public int canvas_width=480;
					public int canvas_height=80;
					int[] values;
					
					private int v_interval1  = canvas_width/num_of_samples/2;
					private int v_interval2  = canvas_width/num_of_samples;
					
					private int h_interval1  = canvas_height/2/2;
					private int h_interval2  = canvas_height/2;
					
					private final Color background = Color.WHITE;	
					private final Color outline = Color.BLACK;
					
					private final Color interval2 = new Color(0, 0, 40);
					private final Color interval1 = new Color(250, 128, 128);
					private final Color sign_color = new Color(40,150,20);
					
					public Graph(){
						this.setSize(canvas_width+10, canvas_height+10);
					}
					public void setNumOfSamples(int size){
						num_of_samples = size;
						v_interval1  = canvas_width/num_of_samples/2;
						v_interval2  = canvas_width/num_of_samples;
						
						h_interval1  = canvas_height/2/2;
						h_interval2  = canvas_height/2;
					}
					
					/*private final Font font = new Font(
							"Tiresias PCFont Z", Font.PLAIN, 18);
					 */
					
					private void paintGrids(Graphics g){
						g.setColor(interval1);
						for (int i = 0; i <= canvas_width; i += v_interval1) {
							g.drawLine(i, 0, i, canvas_height);
						}
						for (int i = 0; i <= canvas_height; i += h_interval1) {
							g.drawLine(0, i, canvas_width, i);
						}
						g.setColor(interval2);
						for (int i = 0; i <= canvas_width; i += v_interval2) {
							g.drawLine(i, 0, i, canvas_height);
						}
						for (int i = 0; i <= canvas_height; i += h_interval2) {
							g.drawLine(0, i, canvas_width, i);
						}
						g.setColor(outline);
						for (int i = 0; i <= canvas_width; i += canvas_width) {
							g.drawLine(i, 0, i, canvas_height);
						}
						for (int i = 0; i <= canvas_height; i += canvas_height) {
							g.drawLine(0, i, canvas_width, i);
						}
					}
					private void paintSign(Graphics g){
						Graphics2D g2d = (Graphics2D) g;
						g2d.setStroke(new BasicStroke(3F));
						g2d.setColor(sign_color);
						int y=canvas_height;
						int prev_y=canvas_height;
						 for(int i=0;i<values.length;i++){	
							 // hova rajzoljunk, mlyen magassagba
							 if(values[i]==1) y=h_interval2;
							 else y=canvas_height;
							 // vonal megrajzolasa
							 g2d.drawLine(i*v_interval2, y, (i+1)*v_interval2, y);
							 //most a fuggoleges vonal kell
							 if(y==prev_y){
								 ;
							 }
							 else{
								 g2d.drawLine((i)*v_interval2, prev_y, (i)*v_interval2, y);
							 }
							 prev_y=y;							 
						 }
					}
					public void paint(Graphics g) {
						super.paint(g);
						Graphics2D g2d = (Graphics2D) g;						
						g2d.addRenderingHints(new RenderingHints(
								RenderingHints.KEY_ANTIALIASING,
								RenderingHints.VALUE_ANTIALIAS_ON));					
						g2d.setRenderingHint(
								RenderingHints.KEY_TEXT_ANTIALIASING,
								RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
						
						
						g.setColor(background);
						//Rajzvaszon meretezese
						g.fillRect(0, 0, canvas_width, canvas_height);
						paintGrids(g);
						paintSign(g);
					}
				}
				/*************************************************************
				 * Grafikon vége
				 *************************************************************/
				// Dialogus ablak letrehozasa, modalitas beallitasa(legyen a foablak, focusban)
				final JDialog dialog=new JDialog(frame,"Oscilloscope Properites",Dialog.ModalityType.DOCUMENT_MODAL);
				dialog.setResizable(false);
				
				/* le kell catolnunk az oscilloscope-ot, hogy le tudjuk kerdezni az ertekeit*/
				final Oscilloscope theOsc = (Oscilloscope) digitalboard.GetElementByID(tmpID);
				
				/* a grafikon panelsavja */
				JPanel theGraphPanel = new JPanel();					
				final Graph g = new Graph();
				g.values=theOsc.getSamples();
				g.setNumOfSamples(theOsc.getSampleSize());				
				theGraphPanel.add(g);
				
				/* a felhasznaloi adatbevitelre felszolito elemek panelje */
				JLabel lblInput = new JLabel("Kerem adja meg a tarolando minta nagysagat!\n");
				final JTextField txtSampleSize = new JTextField(String.valueOf(theOsc.getSampleSize()), 10);
				JPanel theTextPanel = new JPanel();
				theTextPanel.setLayout(new BoxLayout(theTextPanel, BoxLayout.PAGE_AXIS));
				theTextPanel.add(lblInput);
				theTextPanel.add(txtSampleSize);
				
				/* a popup action listenere */
				ActionListener PopUpListener = new ActionListener(){
					public void actionPerformed(ActionEvent event){
						String command = event.getActionCommand();
				        if (command.equalsIgnoreCase("POPUP_OK")) {
				        	try {
								digitalboard.SetSample(Integer.parseInt(txtSampleSize.getText()), tmpID);
								g.setNumOfSamples(theOsc.getSampleSize());	
								g.repaint();
				        	} catch (ExceptionObjectNotFound e1) {
								Logger.Log(Logger.log_type.ERROR,
										"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
												+ e1.ItemID + "/");
							} catch (NullPointerException e) {
								Logger.Log(Logger.log_type.ERROR,
										"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
							} catch (Exception e) {
								Logger.Log(Logger.log_type.ERROR,
										"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
							}
							frame.repaint();
				        }
				        else if(command.equalsIgnoreCase("POPUP_CANCEL")){
				        	dialog.dispose();
				        }
				        else  if(command.equalsIgnoreCase("REFRESH")){				        	
				        	g.values=theOsc.getSamples();
				        	g.repaint();
				        }
					}
				};
				
			
				Timer PopUpUpdater = new Timer(this.delay,PopUpListener);
				PopUpUpdater.setActionCommand("REFRESH");
				PopUpUpdater.start();
				
				/* a gombok letrehozasa, beregisztralva a popup action listenerehez */
				JButton btnOK = new JButton("OK");
				btnOK.setActionCommand("POPUP_OK");
				btnOK.setPreferredSize(buttonSize);	
				btnOK.addActionListener(PopUpListener);
				
				JButton btnCancel = new JButton("Cancel"); 		      
				btnCancel.setActionCommand("POPUP_CANCEL");
				btnCancel.setPreferredSize(buttonSize);
				btnCancel.addActionListener(PopUpListener);	
				
				/* a gombok panelsavja */
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel,
                                                   BoxLayout.LINE_AXIS));
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(btnCancel);
                buttonPanel.add(btnOK);
                buttonPanel.setBorder(BorderFactory.
                    createEmptyBorder(0,0,5,5));				
                
				
				
				/* panel melyben benne minden ami nem grafikon */
                JPanel contentPane = new JPanel(new BorderLayout());
                contentPane.add(theGraphPanel, BorderLayout.PAGE_START);
                contentPane.add(theTextPanel, BorderLayout.CENTER);
                contentPane.add(buttonPanel, BorderLayout.PAGE_END);
                contentPane.setOpaque(true);
                dialog.setContentPane(contentPane);
                
                //Show it.
				dialog.setLocationRelativeTo(frame); 
				dialog.pack();
                dialog.setVisible(true);
                
				frame.repaint(); 
			} 
			/* generator: setfrequency, setsampl */
			else if (tipus.equalsIgnoreCase("generator")) {
		
				/* le kell castolnunk az oscilloscope-ot, hogy le tudjuk kerdezni az ertekeit*/
				GENERATOR theGen = (GENERATOR) digitalboard.GetElementByID(tmpID);
				
				/* a felhasznaloi adatbevitelre felszolito elemek panelje */
				JLabel lblInput1 = new JLabel("Kerem adja meg a frekvenciat!\n");
				final JTextField txtFrequency = new JTextField(String.valueOf(theGen.getFrequency()));
				JLabel lblInput2 = new JLabel("Kerem adja meg a szekvenciat!\n");
				JPanel theTextPanel = new JPanel();
				theTextPanel.setLayout(new BoxLayout(theTextPanel, BoxLayout.PAGE_AXIS));
				theTextPanel.add(lblInput1);
				theTextPanel.add(txtFrequency);
				theTextPanel.add(lblInput2);		
				
				String inputValue2 = (String) JOptionPane.showInputDialog(BoardView, theTextPanel,"Generator Properties",
						0, new ImageIcon(), null,theGen.getSequence());
				String inputValue1 = txtFrequency.getText();
			    try {
			    	if(inputValue2!=null && !inputValue2.isEmpty())
			    		digitalboard.SetSequence(inputValue2, tmpID);
				}  catch (ExceptionObjectNotFound e1) {
					Logger.Log(Logger.log_type.ERROR,
							"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
									+ e1.ItemID + "/");
				} catch (NullPointerException e) {
					Logger.Log(Logger.log_type.ERROR,
							"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
				} catch (Exception e) {
					Logger.Log(Logger.log_type.ERROR,
							"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
				}
				if(inputValue2!=null && inputValue1!=null && !inputValue1.isEmpty())
					try {
						digitalboard.SetFrequency(Integer.parseInt(inputValue1), tmpID);
					}catch (NumberFormatException e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: Wrong Parameter: /" + inputValue1 + "/");
					} catch (ExceptionObjectNotFound e1) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: ObjectNotFound: Nincs elem a megadott azonositoval! /"
										+ e1.ItemID + "/");
					}catch(ExceptionWrongParameter e){
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: WrongParameter: A frekvencianak pozitiv szamnak kell lennie!");
					} catch (NullPointerException e) {
						Logger.Log(Logger.log_type.ERROR,
								"x ERROR: NoBoard: Nincs betoltve a DigitalBoard!");
					} catch (Exception e) {
						Logger.Log(Logger.log_type.ERROR,
								"x Error: UnknownError: Ismeretlen hiba tortent! (Info: +"+e.toString());
					}
					frame.repaint();
			} 
			/* egyeb */
			else {
				;
			}

		}
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
package prototype;

/*  IMPORTOK  */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

/** 
 * <table border=0>
 * 	<tr align=left>
 * 		<th>Nev:</th>
 * 		<td width=30>&nbsp;&nbsp;DigitalBoard</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Tipus:</th>
 * 		<td>&nbsp;&nbsp;Class</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Interface: </th>
 * 		<td>&nbsp;&nbsp;---</td>
 * 	</tr>
 * 	<tr align=left>
 * 		<th>Szulok:</th>
 * 		<td>&nbsp;&nbsp;---</td>
 * </table> 
 *<br>
 * A digitalis aramkort nyilvantarto es a vezerlest  biztosito objektum.
 * Az aramkor osszes elemet es a koztuk levo kapcsolatokat letrehozza,
 * kezeli es tarolja. Uj aramkor megnyitasakor betolti az aramkort egy
 * fajlbol-ekozben ellenorzi a szintaktikai helyesseget,
 * letrehoz minden digitalis elemet, hierarchia szerint sorrendezi,
 * felfedezi a visszacsatolasokat.
 * Tovabbi feladata az aramkor szamitasait vezerelni,
 * igy ha a felhasznalo leptetest ker, a generatorokat lepteti,
 * es ujraszamolja az aramkor komponenseinek allapotat.
 * Ha a felhasznalo a futtatast valasztja, valtoztathato idokozonkent
 * lepteti a jelgeneratort, es a komponensek ertekeit ujraszamolja.
 */
public class DigitalBoard {
    /* ATTRIBUTUMOK */

    /**
     * Haromallapotu valtozo, amely a szimulacio aktualis allapotat tarolja
     */
    @SuppressWarnings("unused")
    private Status SimStatus;
    /**
     * Ez az attributum tarolja az osszes kaput, kimenetet, bemenetet
     * hierarchikus sorrendben Ez nem mas, mint egy listabol szervezett tomb. A
     * tomb indexe azonositja a hierarchia szintet (0-Forrasok, 1-a forrasokhoz
     * csatlakozo elemek, stb) az egyes szinteken pedig egy lista van az
     * elemekrol
     */
    private ArrayList<List<DigitalObject>> ComponentList;
    /**
     * Egyszeru lista a Wire objektumokbol
     */
    private List<Wire> WireList;

    /** KONSTRUKTOR */
    public DigitalBoard() {
        SimStatus = Status.STOPPED;
        ComponentList = new ArrayList<List<DigitalObject>>();
        WireList = new ArrayList<Wire>();
        // Composit MainComposit = new Composit("main");
    }

    /* METODUSOK */
    /**
     * Megkeres egy adott elemet egy Composit ComponentList listajaban
     */
    public DigitalObject GetElementByID(String ElementID) {
        if (ComponentList != null && !ComponentList.isEmpty()) {
            for (List<DigitalObject> sublist : ComponentList) {
                for (DigitalObject o : sublist) {
                    if (o.ID == ElementID) {
                        return (DigitalObject) o;
                    }
                }
            }
        }
        return null;
    }

    ;

    /**
     * A megfelelo parameterrel meghivja a ParseFile(String strFilePath)
     * metodust.
     *
     * @param strFilePath
     *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
     * @throws FileDoesNotExistException
     *             ha a fajl nem letezik vagy nem olvashato
     */
    public void LoadBoard(String strFilePath) {
        boolean exists = (new File(strFilePath)).exists();
        if (!exists) {
            // ParseFile(strFilePath);
            // throw FileDoesNotExistException;
        } else {
            ParseFile(strFilePath);
        }
    }

    ;

    /**
     * A megadott utvonalon talalhato fajlt olvassa be es soronkent ertelmezi az
     * allomanyt
     * MŰKÖDÉS:
     * 1, Beolvassuk a fájlt
     * 2, Composit-okra szedjük
     * 3, A Compositokat headerre és parancsokra szedjük
     * Ezt egy tömböket tároló tömbben tároljuk. Egy tömb egy composit, a tárolt tömbje pedig a header,parancsok
     * 4, A headerből kinyerjük a komposit nevét, be és kimenő vezetékeket(TODO), majd a parancsokat beparszoljuk és egy tömbben elmentjük. Ezek alapján hozzuk létre az objektumokat.
     * @param strFilePath
     *            Az aramkort leiro dokumentum (*.bhdl) eleresi utvonala
     */
    public void ParseFile(String strFilePath) {
        ArrayList<ArrayList<String>> foundComposites = new ArrayList<ArrayList<String>>();//Ebben tároljuk a parszolt struktúrát
        String strFileContents = "";//Beolvasott fájl tartalma
        ///////////////////////////FÁJLBÓL olvasás//////////////////////////////
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(strFilePath));
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
            strFileContents = text.toString();
        } catch (Exception e) {
            System.err.print(e.toString());
        }

        // Tisztogatas
        strFileContents = bhdlParser.remove("\n", strFileContents); //nincs sortores
        strFileContents = bhdlParser.remove("\t", strFileContents); //nincs tab

        //Amig nem üres a file
        int i = 0; //Ebben van eltárolva,hogy hányadik tömböt tároló tömböt érjük el. A tároló tömb a composit, egy eleme is tömb,ebben a parancsok vannak.
        while (!strFileContents.equals("")) {

            //Megkeressük composite-t
            String composit = bhdlParser.find_next_Composite(strFileContents.toString());

            //Kivesszük régiből
            strFileContents = strFileContents.replace(composit, "");

            //Hozzáadjuk a tömbhöz, egy tömb első elemeként
            ArrayList<String> tmp = new ArrayList<String>();
            tmp.add(composit);
            foundComposites.add(tmp);
        }

        System.out.println("\n Composit értelemzése \n");

        //Vannak már compositjaink egy tömbben
        i = 0; //i ujrahazsnálása
        while (i < foundComposites.size()) {//Végigmegyünk az összes compositon
            String current_composit = foundComposites.get(i).get(0);//Kivesszük a vizsgált elemet
            current_composit = current_composit.replace("composit ", "");//nem kell eleje
            current_composit = current_composit.replace("endcomposit;", "");//Sem vége

            //Header legyen az in,out
            String header = bhdlParser.find_header(current_composit);//pl. main( in, out) kiszedése
            current_composit = current_composit.replace(header, "");
            foundComposites.get(i).remove(0);//Kivesszük a régi header
            foundComposites.get(i).add(header);//Berakjuk headert

            while (!current_composit.equals("")) {//Régi teljes composittal dolgozunk
                String command = bhdlParser.find_next_Command(current_composit);//Keressük meg ebben a köv parancsot
                current_composit = current_composit.replace(command, "");//Kivesszük a cmpositból
                foundComposites.get(i).add(command);
            }
            i++;
        }
        //KIíratjuk a dolgokat,végig megyünk a tömbön,amiben tömbök vannak,és azok minden elemét kiírjuk
        i = 0;

        while (i < foundComposites.size()) {
            int j = 0;
            while (j < foundComposites.get(i).size()) {
                System.out.print((j == 0 ? "Fejléc: " : "Parancs: ") + foundComposites.get(i).get(j));
                j++;
                System.out.println();
            }
            i++;
        }

        System.out.println("\n Parancs értelemzése \n");

        //Most már megvan összes parancs, componensekhez rendelve, illetve a komponensek
        //Menjünk végig rajtuk és hozzuk létre az elemeket
        //Header alapján kompozit létrehozása
        i = 0;

        while (i < foundComposites.size()) {

            String header = foundComposites.get(i).get(0);//Szedjük, ki a headert, az adott komponens, első elemeként
            String name = "";
            Pattern regxp = Pattern.compile("^[a-zA-Z0-9]+$");//Kiszedjük a nevet
            Matcher match = regxp.matcher(header);
            if (match.find()) {
                name = match.group();
            }
            header = header.replace(name, "");

            //Egyelőre csak kompozit jön létre
            Composit comp = new Composit("", name);

            int j = 1;
            while (j < foundComposites.get(i).size()) {//Menjünk a composit többi részén is végig
                String command = "";
                String current_com = foundComposites.get(i).get(j);
                regxp = Pattern.compile(".+? ");//Kiszedjük a parancot
                match = regxp.matcher(current_com);
                if (match.find()) {
                    command = match.group();
                }
                current_com = current_com.replace(command, "");//Kiszedjük a parancot eredeti sztringből
                System.out.println("Parancs: " + command);

                ArrayList<String> params = new ArrayList<String>();//Adott paramétereit tároló tömb
                String param = "";

                while (!current_com.equals("")) {
                    regxp = Pattern.compile(".+?[ |&;=|]");//Paraméterválasztás
                    match = regxp.matcher(current_com);
                    if (match.find()) {
                        param = match.group();
                        if (!param.equals("")) {
                            params.add(param);//Hozzáadjuk a paraméterhez
                        }
                        System.out.println("Paraméter: " + param);
                    }
                    current_com = current_com.replace(param, "");//Kiszedjük parszolt paramétert
                }
                j++;
            }
            i++;
        }

        //Elemek létrehozása , bekötése

        /* Elkeszitem a WIre objektumokat, es a WireListet */
        List<DigitalObject> tmp_components = new ArrayList<DigitalObject>();
        /*****************************************************************
         * ************** TESZTARAMKOROK leirasa manualisan **************
         ******************************************************************/

        /*if (file.getName() == "teszt1.bhdl") {
        tmp_components = elementlist;
        }
        if (file.getName() == "teszt2.bhdl") {
        // SWITCH letrehozasa
        SWITCH sw01 = new SWITCH("main", null);
        tmp_components.add(sw01);

        // Oscilloscope letrehozasa
        Oscilloscope osc01 = new Oscilloscope("main", null, 10);
        tmp_components.add(osc01);

        Wire main_w02 = new Wire("main");
        WireList.add(main_w02);

        Wire main_w01 = new Wire("main");
        WireList.add(main_w01);
        sw01.wireOut.add(main_w01);
        main_w01.SetConnection(null, sw01);

        // letrehozzuk a kaput
        ANDGate main_ANDGate_0 = new ANDGate("main", main_w01, main_w02);
        tmp_components.add(main_ANDGate_0);

        // beallitjuk a drotokat
        main_w01.SetConnection(main_ANDGate_0, sw01);
        main_w02.SetConnection(main_ANDGate_0, main_ANDGate_0);
        // a kimentet meg sehova nem kotjuk
        // main_w02.SetConnection(null, main_ANDGate_0);
        main_ANDGate_0.AddOutput(main_w02);
        main_w02.SetConnection(osc01, null);
        osc01.wireIn.add(main_w02);
        }
        if (file.getName() == "teszt4.bhdl") {
        // SWITCH letrehozasa
        SWITCH sw01 = new SWITCH("main", null);
        tmp_components.add(sw01);
        Wire main_w1 = new Wire("main");
        WireList.add(main_w1);
        main_w1.SetConnection(null, sw01);
        sw01.wireOut.add(main_w1);

        // GENERATOR
        GENERATOR gen01 = new GENERATOR("main", 1000, 11001100);
        tmp_components.add(gen01);
        Wire main_w2 = new Wire("main");
        WireList.add(main_w2);
        main_w2.SetConnection(null, gen01);
        gen01.wireOut.add(main_w2);

        // LED
        LED led01 = new LED("main", null);
        tmp_components.add(led01);

        // Oscilloscope letrehozasa
        Oscilloscope osc01 = new Oscilloscope("main", null, 10);
        tmp_components.add(osc01);

        Wire feedback = new Wire("comp01");
        WireList.add(feedback);

        // letrehozzuk az inverter kaput
        INVERTER comp1_inv1 = new INVERTER("comp01", feedback);
        tmp_components.add(comp1_inv1);
        feedback.SetConnection(comp1_inv1, null);
        Wire comp1_w1 = new Wire("comp01");
        WireList.add(comp1_w1);
        comp1_w1.SetConnection(null, comp1_inv1);
        comp1_inv1.AddOutput(comp1_w1);

        // AND1 (!feedback-es)
        ANDGate main_and1 = new ANDGate("comp01", comp1_w1, main_w1);
        tmp_components.add(main_and1);
        comp1_w1.SetConnection(main_and1, null);
        main_w1.SetConnection(main_and1, null);
        Wire comp1_w2 = new Wire("comp01");
        WireList.add(comp1_w2);
        comp1_w2.SetConnection(null, main_and1);
        main_and1.AddOutput(comp1_w2);

        // AND2 (feedback-es)
        ANDGate main_and2 = new ANDGate("comp01", feedback, main_w2);
        tmp_components.add(main_and2);
        feedback.SetConnection(main_and2, null);
        main_w2.SetConnection(main_and2, null);
        Wire comp1_w3 = new Wire("comp01");
        WireList.add(comp1_w3);
        comp1_w3.SetConnection(null, main_and2);
        main_and2.AddOutput(comp1_w3);

        // OR1 (a ket es kapus)
        ORGate main_or1 = new ORGate("comp01", comp1_w2, comp1_w3);
        tmp_components.add(main_or1);
        comp1_w2.SetConnection(main_or1, null);
        comp1_w3.SetConnection(main_or1, null);
        // Wire comp1_w4 = new Wire("comp01");
        // WireList.add(comp1_w4);
        // comp1_w4.SetConnection(null, main_or1);
        main_or1.AddOutput(feedback);
        feedback.SetConnection(null, main_or1);

        // oszcilloszkop
        osc01.wireIn.add(comp1_w3);
        comp1_w3.SetConnection(osc01, null);

        // LED
        led01.wireIn.add(feedback);
        feedback.SetConnection(led01, null);

        }*/
        /*****************************************************************
         * ************** TESZTARAMKOROK leirasanak vege *************
         ******************************************************************/
        ComponentList.add(tmp_components);
        HierarchyCounter cntr = new HierarchyCounter();
        cntr.CountHierarchy(WireList, ComponentList);
        //Debug();
        run(strFilePath);
    }

    public void run(String filename) {
        System.out.println(filename + "'s Board is loaded");
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        try { // Szamma alakitjuk - ha tudjuk - a bevitt szoveget
            reader.readLine();//PETII, amit visszaad, azzal kezdj vmit
            //setSequence gen_name sequence(pl 01101010100) -> gen_name.setSequence(0x0110101010100) (binariba kell alakitani!!); count();
            //toggleSwitch sw_name -> sw_name.Toggle(); count();
            //step -> StepComponents();
        } catch (Exception e) {
            // line = "exit" ;
        }
    }

    public void Debug() {
        /* KIIRATAS, DEBUG */
        int szint = 0;
        for (List<DigitalObject> sublist : ComponentList) {
            System.out.println();
            System.out.print("Szint: ");
            System.out.print(szint++);
            System.out.print("\t");
            for (DigitalObject o : sublist) {
                System.out.print(o.GetID() + ", ");
                if (o.Feedbacks != null && !o.Feedbacks.isEmpty()) {
                    System.out.print("FEEDBACK [");
                    for (DigitalObject f_o : o.Feedbacks) {
                        System.out.print(" " + f_o.ID + ", ");
                    }
                    System.out.print("]");
                }
            }
        }
    }

    ;

    /**
     * metodus meghivja a SetStatus metodust RUNING parameterrel
     */
    public void Run() {
        SetStatus(Status.RUNNING);
    }

    ;

    /**
     * A metodus meghivja a SetStatus metodust PAUSED parameterrel
     */
    public void Pause() {
        SetStatus(Status.PAUSED);
    }

    ;

    /**
     * A metodus meghivja a SetStatus metodust STOPPED parameterrel
     */
    public void Stop() {
        SetStatus(Status.RUNNING);
    }

    ;

    /**
     * Atallitja a SimStatus attributumot a parameterben megadott ertekre
     *
     * @param NewStatus
     *            Az uj allapot
     */
    private void SetStatus(Status NewStatus) {
        SimStatus = NewStatus;
    }

    ;

    /**
     * A parameterben megadott azonositoju GENERATOR objektum frekvenciajat
     * modositja
     *
     * @param Frequency
     *            Az uj frekvencia
     * @param ElementID
     *            A modositani kivant GENERATOR IDja
     */
    public void SetFrequency(int Frequency, String ElementID) {
        GENERATOR tmp;
        tmp = (GENERATOR) GetElementByID(ElementID);
        tmp.SetFrequency(Frequency);
    }

    ;

    /**
     * A parameterben megadott azonositoju GENERATOR objektum szekvenciajat
     * modositja
     *
     * @param Sequence
     *            az uj szekvencia, minta
     * @param ElementID
     *            A modositani kivant GENERATOR IDja
     */
    public void SetSequence(int Sequence, String ElementID) {
        GENERATOR GEN_to_setsequence; /* Temporalis valtozo */
        GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);
        GEN_to_setsequence.SetSequence(Sequence);
    }

    ;

    /**
     * A parameterben megadott azonositoju SWITCH objektum erteket az
     * ellenkezore allitja azaltal, hogy meghivja az objektum hasonlo nevu
     * parameteret
     *
     * @param ElementID
     *            A SWITCH ID-ja
     */
    public void Toggle(String ElementID) {
        SWITCH SWITCH_to_toggle; /* Temporalis valtozo */
        SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);
        SWITCH_to_toggle.Toggle();
    }

    ;

    /**
     * Meghivja az osszes iComponent interfeszt megvalosito objektum Step()
     * metodusat.
     */
    public void StepComponents() {
        /*
         * Elvileg mar fel van epulve a hierarchia igy nekem eleg megkapnom a
         * ComponentListet
         */
        DigitalObject obj;
        for (List<DigitalObject> sublist : ComponentList) {
            for (DigitalObject o : sublist) {
                obj = (DigitalObject) o;
                obj.Step();
            }
        }
    }

    public boolean isMyObject(String obj_name) {
        boolean result = false;
        for (List<DigitalObject> sublist : ComponentList) {
            for (DigitalObject o : sublist) {
                if ((o.ID.split("#")[2]).trim() == obj_name) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean isMyWire(String wire_name) {
        boolean result = false;
        for (Wire w : WireList) {
            if ((w.GetID().split("#")[2]).trim() == wire_name) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
}

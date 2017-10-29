package com.spacetethers;

// Copyright (c) Vincent Cate 2002

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class SpaceTethers
    extends Frame
    implements ActionListener {
  boolean isStandalone = true;
  static boolean simulationStarted = false;
  static boolean simulationFinished = false;
  static boolean suspended = false;
  static String CurrentMessage = null;
 // static simtype SimObjects[] = new simtype[k.maxObjects];
  static Vector SimObjects = new Vector();

  public static List list;
  public static Button startButton;
  public static Button stopButton;
  public static Button startOverButton;
  public static TextArea textArea1 = new TextArea(25, 25);

  public static Thread thread1 = null;
  Label label1 = new Label();
  Label label2 = new Label();
  FlowLayout flowLayout1 = new FlowLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  String lastToken="";


  public SpaceTethers() {
    try {
      jbInit();
      this.start();
      this.setSize(1920,1080);
      setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }



  public void start() {

    if (simulationStarted) { // If we don't do this then when window is minimized
      return; //   we end up getting all the starting stuff again
    }

    CurrentMessage = "Waiting";
    k.g=this.getGraphics();

    sampleinput.init();
    for (int i = 0; i < sampleinput.sampleNames.length; i++) {
      list.add(sampleinput.sampleNames[i]);
    }
    list.add(k.outputName);
    //k.outputClear();

    Dimension mySize = this.getSize();
    k.setXPixels(mySize.width);
    k.setYPixels(mySize.height);

    thread1 = null;

    this.validate();

    try {
      thread1 = new Thread() {
        public void run() {
          startSimulation();
        }
      };
    }
    catch (Exception e1) {
      setMessage("Exception from thread " + e1.toString());
    }
    this.startButton.setEnabled(true);
//		 this.textArea1.setEnabled(true);
    this.doLayout();
    this.paintAll(this.getGraphics());
    java.lang.System.gc();
  }

  public static void doStop() {
    if (!getSimulationFinished()) {
      stopButton.setLabel("   Go   ");
      stopButton.setActionCommand("Go");
      setSuspended(true); // thread1.suspend();
    }
    startOverButton.setVisible(true);
    startOverButton.setEnabled(true);
  }

  public void actionPerformed(ActionEvent e) {
    int i;

    String meaning = e.getActionCommand();

    if (meaning.equals("Stop")) {
       doStop();
    }
    if (meaning.equals("Go")) {
      stopButton.setLabel("  Stop  ");
      stopButton.setActionCommand("Stop");
      startOverButton.setVisible(false);
      if (!getSimulationFinished()) {
        setSuspended(false); // thread1 . resume();
      }
    }

    if (meaning.equals("Start Simulation")) {
      this.removeAll();

      stopButton = new Button("  Stop  ");
      stopButton.setFont(new Font("Dialog", 1, 12));
      stopButton.setActionCommand("Stop");
      stopButton.addActionListener(this);
      stopButton.setBackground(SystemColor.control);
      stopButton.setForeground(SystemColor.controlText);
      startOverButton = new Button(" Start Over ");
      startOverButton.setActionCommand("Start Over");
      startOverButton.addActionListener(this);
      startOverButton.setFont(new Font("Dialog", 1, 12));
      startOverButton.setBackground(SystemColor.control);
      startOverButton.setForeground(SystemColor.controlText);
      startOverButton.setVisible(false);
      flowLayout1.setHgap(50);  // was 20
      flowLayout1.setVgap(50);  // was 10
      this.setLayout(flowLayout1);
      this.add(stopButton, null);
      this.add(startOverButton, null);

      setSimulationFinished(false);
      setSuspended(false);
      this.validate();
      thread1.start();
    }

    this.validate();
    this.paintAll(this.getGraphics());

    if (meaning.equals("Start Over")) {
      stopButton.setEnabled(false);
      startOverButton.setEnabled(false);
      setSimulationFinished(true); // thread1 will finish fast
      simulationStarted = false;
      double nowtime = java.lang.System.currentTimeMillis();
      while (java.lang.System.currentTimeMillis() < nowtime + 500) {
        try {
          Thread.currentThread().sleep(0);
        }
        catch (Exception e1) {
        }
      }
      stopButton.setLabel("  Stop  ");
      stopButton.setActionCommand("Stop");
      this.removeAll();
      try {
        jbInit();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      this.start();
    }
  }

  // This is here so there is no exception
  //   because a thread can not throw an exception.
  //  Get text input first
  //  Then do simulation
  public void startSimulation() {
    if (getSimulationFinished()) {
      return;
    }
    String s = textArea1.getText();
    simulationStarted = true;
    k.debug("startSimulation " + s);

    try {
      simulation(s);
      if (!getSimulationFinished()) { // If we were not made to stop
        setSimulationFinished(true);
        startOverButton.setVisible(true);
        startOverButton.setEnabled(true);
        stopButton.setVisible(false);
        this.paintAll(this.getGraphics());
      }
    }
    catch (Exception e1) {
      setMessage("startSim Exception " + e1.toString());
      k.outputAdd(CurrentMessage + "\n");
      setMessage("startSim Exception at token " + lastToken + " of " + e1.toString());
      k.outputAdd(CurrentMessage + "\n");
    }
  }

  private synchronized void setSimulationFinished(boolean value) {
    simulationFinished = value;
  }

  public static synchronized boolean getSimulationFinished() {
    return (simulationFinished);
  }

  public static synchronized void setSuspended(boolean value) {
    suspended = value;
  }

  private synchronized boolean getSuspended() {
    return (suspended);
  }

  // The GUI code got the input string and has given it to us.
  // We parse input string and make objects as instructed.
  // Then we loop and do simulation with those objects.

  private void simulation(String input) throws Exception {
    k.debug("simulation");

    Image offscreen = null;
    Graphics og = null;
    String nt;
    position posBigEnd = null, posSmallEnd = null, posMass = null;
    velocity velBigEnd = null, velSmallEnd = null, velMass = null;
    double bigKg = 0;
    double payloadKg = 0;
    double massKg = 0;
    double coefficientOfDrag = 0;
    double dragArea = 0;
    double massLiftOverDrag = 0;
    double tpsThermalConductivity = 0;
    double tpsThickness = 0;
    double tpsArea = 0;
    double tpsThermalMass = 0;
    double tpsSpecificHeat = 0;
    double massHeight = -1;
    double massVelocity = -1;
    double massVelAngle = -1;
    double totalTime = 8000;
    double restLength = 1000;
    double bigEndDiameter = 0.0;
    double smallEndDiameter = 0.0;
    double x = 0;
    double y = 0;
    double z = 0;
    double tensile = 3510000000.0;
    double density = 970;
    double elasticity = 0.03;
    int slices = 200;
    double timeMassToPayload = -1;
    double timePayloadRelease = -1;
    double stretch = -1;
    double thrustNewtons = 0;
    double exhaustVelocityMPS = 0;
    double rocketKg=0;
    double fuelFraction = 0; // converted to fuelKg
    double dryKg = 0;
    double fuelKg = 0;
    String stabilizationType="";
    double spinAngle = 0;
    boolean stageStaysAttached=false;
    double massNoseAngle = 0;
    double rocketParachuteCd = 0;
    double rocketParachuteArea = 0;
    double rocketParachuteOpenHeight = 0;
    double timeOfIgnition = 0;
    boolean retroThrust = false;
    double ispDegradedAtOneAtm=0;
    String errorMessage = " ";
    String label = "";
    int taperType=k.TaperLinear;
    double safetyFactor = 2;
    double blackBodyMass=0;
    double blackBodyArea=0;
    double blackBodySpecificHeat=0;
    double blackBodyStantonNumber=0;
    double blackBodyEmissivity=0.9;
    double meteorDensity=0;
    double meteorJoulesPerKg=0;
    double solarSailMetersOnASide=0;
    double gramsPerSqMeter=0;
    double solarSailEfficiency=0.90;
    double edtTotalLength=0;
    double edtCurrent=0;
    double edtDiameter=0;
    double maxGsTarget=100;
    double radKg=0;
    double radCharge=0;
    String radView="";
    vector3d radPosition=null;
    vector3d radVelocity=null;

    String labelCenter="";
    double distance=0;
    String labelTip="";
    double secondsPerRev=0;
    double secondsOffset=0;
    int direction=0;
    String tetherLabel="";
    double blastStdDev=0;
    double pusherStdDev=0;
    double ISP=0;
    double pusherLength=0;
    double pusherKg=0;

    clearSimObjects();
    k.globalReset();

    // tokenize aLine using default separators
    //      (white space)
    StringTokenizer parseLine = new StringTokenizer(input);
    boolean ntUsed = true;
    while (parseLine.hasMoreTokens()) {
      ntUsed = false;
      nt = parseLine.nextToken();
      k.debug("token " + nt);
      lastToken=nt;
      if (nt.equals("/*")) { // if got comment
        while (parseLine.hasMoreTokens() && !nt.equals("*/")) { //  discard till close comment
          nt = parseLine.nextToken();
        }
        continue;
      }
      if (k.isGlobal(nt)) {
        k.setGlobal(nt, parseLine.nextToken());
        ntUsed = true;
      }

      if (nt.equals("ispDegradedAtOneAtm")) {
        ispDegradedAtOneAtm = readDouble(parseLine.nextToken());
        ntUsed = true;
      }

      if (nt.equals("posBigEnd")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        posBigEnd = new position(x, y);
        ntUsed = true;
      }

      if (nt.equals("posSmallEnd")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        posSmallEnd = new position(x, y);
        ntUsed = true;
      }
      if (nt.equals("smallEndHeight")) {
        y = k.earthRadius + readDouble(parseLine.nextToken());
        x = 0;
        posSmallEnd = new position(x, y);
        ntUsed = true;
      }
      if (nt.equals("smallEndRadius")) {
        y = readDouble(parseLine.nextToken());
        x = 0;
        posSmallEnd = new position(x, y);
        ntUsed = true;
      }
      if (nt.equals("stretch")) {
        stretch = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("velBigEnd")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        velBigEnd = new velocity(x, y);
        ntUsed = true;
      }
      if (nt.equals("velSmallEnd")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        velSmallEnd = new velocity(x, y);
        ntUsed = true;
      }
      if (nt.equals("posMass")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        posMass = new position(x, y);
        ntUsed = true;
      }
      if (nt.equals("posMass3D")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        z = readDouble(parseLine.nextToken());
        posMass = new position(x, y, z);
        ntUsed = true;
      }
      if (nt.equals("velMass")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        velMass = new velocity(x, y);
        ntUsed = true;
      }
      if (nt.equals("velMass3D")) {
        x = readDouble(parseLine.nextToken());
        y = readDouble(parseLine.nextToken());
        z = readDouble(parseLine.nextToken());
        velMass = new velocity(x, y, z);
        ntUsed = true;
      }
      if (nt.equals("massHeight")) {
        y = k.earthRadius + readDouble(parseLine.nextToken());
        x = 0;
        posMass = new position(x, y);
        ntUsed = true;
      }
      if (nt.equals("massVelAndAngle")
          || nt.equals("surfaceVelAndAngle")
          || nt.equals("spaceVelAndAngle")) {
        massVelocity = readDouble(parseLine.nextToken());
        massVelAngle = readDouble(parseLine.nextToken());
        x = -massVelocity * java.lang.Math.cos(k.degreesToRadians(massVelAngle));
        y = massVelocity * java.lang.Math.sin(k.degreesToRadians(massVelAngle));
        if (nt.equals("surfaceVelAndAngle")) {
          x -= k.earthSpinSpeed;
        }
        if (nt.equals("spaceVelAndAngle")) {
          x = -x; // for space we want regular angles not from east horizon
        }
        velMass = new velocity(x, y);
        ntUsed = true;
      }
      if (nt.equals("massCircularOrbit") ||
          (nt.equals("massEccentricity"))) {
        double massEccentricity = 0; // default is ciruclar
        if (nt.equals("massEccentricity")) {
          massEccentricity = readDouble(parseLine.nextToken());
        }
        double r = java.lang.Math.sqrt(posMass.x * posMass.x +
                                       posMass.y * posMass.y);
        double v = java.lang.Math.sqrt(
            k.gravConst * k.earthMass * (massEccentricity + 1.0) / r);
        x = -v; // only right if posMass.x==0
        y = 0; //  dito
        velMass = new velocity(x, y);
        ntUsed = true;
      }

      if (nt.equals("bigKg")) {
        bigKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("massKg")) {
        massKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("massNoseAngle")) {
        massNoseAngle = readDouble(parseLine.nextToken());
        ntUsed = true;
      }

      if (nt.equals("payloadKg")) {
        payloadKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("totalTime")) {
        totalTime = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("timeMassToPayload")) {
        timeMassToPayload = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("timePayloadRelease")) {
        timePayloadRelease = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("restLength")) {
        restLength = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("bigEndDiameter")) {
        bigEndDiameter = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("smallEndDiameter")) {
        smallEndDiameter = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("density")) {
        density = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tensileGpa")) {
        tensile = 1000000000 * readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("elasticity")) {
        elasticity = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("slices")) {
        slices = (int) readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("coefficientOfDrag")) {
        coefficientOfDrag = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("dragArea")) {
        dragArea = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("massLiftOverDrag")) {
        massLiftOverDrag = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tpsThermalConductivity")) {
        tpsThermalConductivity = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tpsThickness")) {
        tpsThickness = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tpsArea")) {
        tpsArea = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tpsThermalMass")) {
        tpsThermalMass = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("tpsSpecificHeat")) {
        tpsSpecificHeat = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("rocketKg")) {
        rocketKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("rocketParachuteCd")) {
        rocketParachuteCd = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("rocketParachuteArea")) {
        rocketParachuteArea = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("rocketParachuteOpenHeight")) {
        rocketParachuteOpenHeight = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("thrustNewtons")) {
        thrustNewtons = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("ISP")) {
        ISP = readDouble(parseLine.nextToken());
        exhaustVelocityMPS = k.earthAcceleration * ISP;
        ntUsed = true;
      }
      if (nt.equals("exhaustVelocityMPS")) {
        exhaustVelocityMPS = readDouble(parseLine.nextToken());
        ISP = exhaustVelocityMPS / k.earthAcceleration;
        ntUsed = true;
      }
      if (nt.equals("fuelFraction")) {
        fuelFraction = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("fuelKg")) {
        fuelKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("dryKg")) {
        dryKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("stabilizationType")) {
        stabilizationType = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("stageStaysAttached")) {
        stageStaysAttached = "true".equals(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("spinAngle")) {
        spinAngle = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("timeOfIgnition")) {
        timeOfIgnition = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("retroThrust")) {
        retroThrust = "true".equals(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("blackBodyArea")) {
        blackBodyArea = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("blackBodyMass")) {
        blackBodyMass = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("blackBodySpecificHeat")) {
        blackBodySpecificHeat = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("blackBodyStantonNumber")) {
        blackBodyStantonNumber = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("blackBodyEmissivity")) {
        blackBodyEmissivity = readDouble(parseLine.nextToken());
        ntUsed = true;
      }

      if (nt.equals("taperType")) {
        String type = parseLine.nextToken();
        if (type.equals("Automatic")) {
          taperType = k.TaperAutomatic;
          ntUsed = true;
        }
        if (type.equals("Linear")) {
          taperType = k.TaperLinear;
          ntUsed = true;
        }
      }
      if (nt.equals("safetyFactor")) {
        safetyFactor = readDouble(parseLine.nextToken());
        stretch = 1 + (elasticity / safetyFactor);
        ntUsed = true;
      }
      if (nt.equals("label")) {
        label = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("meteorJoulesPerKg")) {
        meteorJoulesPerKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("meteorDensity")) {
        meteorDensity = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("solarSailMetersOnASide")) {
        solarSailMetersOnASide = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("gramsPerSqMeter")) {
        gramsPerSqMeter = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("solarSailEfficiency")) {
        solarSailEfficiency = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("edtTotalLength")) {
        edtTotalLength = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("edtCurrent")) {
        edtCurrent = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("edtDiameter")) {
        edtDiameter = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("maxGsTarget")) {
        maxGsTarget = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("radKg")) {
        radKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("radCharge")) {
        radCharge = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("radView")) {
        radView = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("radPosition")) {
        radPosition = new vector3d(
            readDouble(parseLine.nextToken()),
            readDouble(parseLine.nextToken()),
            readDouble(parseLine.nextToken()));
        ntUsed = true;
      }
      if (nt.equals("radLValue")) {
        double radLValue = readDouble(parseLine.nextToken());
        radPosition = new vector3d(radLValue * k.earthRadius, 0, 0);
        ntUsed = true;
      }
      if (nt.equals("radKeV")) {
        double radKeV = readDouble(parseLine.nextToken());
        double radE = radKeV / k.JtoKeV;
        // E = 0.5 * M * V^2  ->  V = sqrt(E/(0.5 * M));
        double radVel = Math.sqrt(radE / (0.5 * radKg));
        radVelocity = new vector3d(radVel, 0, 0);
        ntUsed = true;
      }
      if (nt.equals("radVelocity")) {
        radVelocity = new vector3d(
            readDouble(parseLine.nextToken()),
            readDouble(parseLine.nextToken()),
            readDouble(parseLine.nextToken()));
        ntUsed = true;
      }
      if (nt.equals("distance")) {
        distance = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("secondsPerRev")) {
        secondsPerRev = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("secondsOffset")) {
        secondsOffset = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("direction")) {
        direction = (int) readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("labelCenter")) {
        labelCenter = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("labelTip")) {
        labelTip = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("tetherLabel")) {
        tetherLabel = parseLine.nextToken();
        ntUsed = true;
      }
      if (nt.equals("blastStdDev")) {
        blastStdDev = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("pusherStdDev")) {
        pusherStdDev = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
       if (nt.equals("pusherLength")) {
        pusherLength = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
       if (nt.equals("pusherKg")) {
        pusherKg = readDouble(parseLine.nextToken());
        ntUsed = true;
      }
      if (nt.equals("MakeOrionBlast")) {
        addSimObject(new orionblast( label, tetherLabel, blastStdDev,
                        ISP, pusherLength, pusherStdDev, pusherKg));
        ntUsed = true;
      }

      if (nt.equals("MakeFixedDist")) {
        addSimObject(new fixeddist(label, labelCenter,  distance,
                   labelTip, secondsPerRev,
                    secondsOffset, direction));
        ntUsed = true;
      }

      if (nt.equals("MakeRadiation")) {
        addSimObject(new radiation( label, radKg, radCharge, radView,
                                    radPosition, radVelocity));
        ntUsed = true;
      }

      if (nt.equals("AirTest")) {
        double altitude = readDouble(parseLine.nextToken());
        double airDensity = air.Density(altitude);
        errorMessage += "Air density at " + k.dTSS(altitude)
                         + " meters is " + k.dTSS(airDensity)
                         + " kg/meter^3. "
                         + " Ignore -->   ||  ";
        ntUsed = false;  // Our message shows up as part of error message
      }
      if (nt.equals("MakeSmartReentry")) {
        addSimObject(new smartreentry(maxGsTarget, label));
        ntUsed = true;
      }
      if (nt.equals("MakeEDT")) {
        addSimObject(new edt( label, edtTotalLength,
                             edtCurrent, edtDiameter));
        ntUsed = true;
      }

      // SolarSail assumes a payload mass with label of current value of label
      if (nt.equals("MakeSolarSail")) {
        addSimObject(new solarsail(
                                         label, solarSailMetersOnASide, gramsPerSqMeter,
                                         solarSailEfficiency, retroThrust));
        ntUsed = true;
      }
      if (nt.equals("MakeMeteor")) {
        addSimObject(new meteor(  massKg,
                meteorJoulesPerKg,
                meteorDensity,
                coefficientOfDrag,  posMass,  velMass,
                label));
        ntUsed = true;
      }

      //  Real doc is in attime.class - but things like:
      //  AtTime 110 toss 5 from T1.Payload to m1
      if (nt.equals("AtTime")) {
          ntUsed = true;
          double time = k.readDouble(parseLine.nextToken());
          String command="";
          nt = parseLine.nextToken();
          while (nt.equals("EndAtTime")==false) {
              command += nt + " ";
              nt = parseLine.nextToken();
          }
          attime at1 = new attime(time, command);
          // addSimObject(at);         - don't do this any more since on attimeQueue
      }

      try {
        if (nt.equals("MakeTether")) {
          String labelBallast = parseLine.nextToken();
          String labelPayload = parseLine.nextToken();

          if (stretch > 0 && posSmallEnd != null) {
            x = 0;
            y = posSmallEnd.y + restLength * stretch;
            posBigEnd = new position(x, y);
          }
          // Next 2 if statements for backward compatibility with old input
          if (!findsimobject.labelExists(labelBallast)) {
            mass bigEnd = new mass(bigKg, posBigEnd, velBigEnd,
                                   labelBallast);
            addSimObject(bigEnd);
          }
          if (!findsimobject.labelExists(labelPayload)) {
            mass smallEnd = new mass(payloadKg, posSmallEnd, velSmallEnd,
                                     labelPayload);
            addSimObject(smallEnd);
          }
          addSimObject(new tether(restLength, bigEndDiameter,
                                  smallEndDiameter, density,
                                  tensile, elasticity,
                                  labelBallast, labelPayload,
                                  slices,
                                  taperType, safetyFactor, label));
          ntUsed = true;
        }
      } catch (Exception e1) {
        throw new Exception("SpaceTethers.simulate MakeTether " + e1.toString());
      }

      if (nt.equals("MakeMass")) {
        addSimObject(new mass(massKg, posMass,
                                             velMass, coefficientOfDrag, dragArea,
                                             massLiftOverDrag,
                                             label));
        ntUsed = true;
      }
      if (nt.equals("MakeSharpBody")) {
        addSimObject(new sharpbody(massKg, posMass,
                                             velMass, coefficientOfDrag, dragArea,
                                             massLiftOverDrag, massNoseAngle,
                                             tpsThermalConductivity,
                                             tpsThickness, tpsArea,
                                             tpsThermalMass,
                                             tpsSpecificHeat,
                                             label));
        ntUsed = true;
      }
      if (nt.equals("MakeBluntBody")) {
        if (!findsimobject.labelExists(label)) {
           mass m1 = new mass(massKg, posMass,
                             velMass, coefficientOfDrag, dragArea,
                             massLiftOverDrag,
                             label);
        }
        addSimObject(new bluntbody(blackBodyMass, blackBodyArea,
                                             blackBodySpecificHeat,
                                             blackBodyStantonNumber,
                                             blackBodyEmissivity,
                                             label));
        ntUsed = true;
      }
      if (nt.equals("MakeMoon")) {
        k.setMoonEarth();
        addSimObject(k.earth);
        addSimObject(k.moon);
        ntUsed = true;
      }

      // Rocket assumes a payload mass with label of current value of label
      if (nt.equals("MakeRocket")) {
        if (fuelKg == 0 && fuelFraction > 0) {
          fuelKg = fuelFraction * rocketKg;
          dryKg = (1.0 - fuelFraction) * rocketKg;
        }
        addSimObject(new rocket(
                        coefficientOfDrag, dragArea, massLiftOverDrag,
                        dryKg, fuelKg,
                        thrustNewtons,exhaustVelocityMPS,
                        rocketParachuteCd,
                        rocketParachuteArea,
                        rocketParachuteOpenHeight,
                        timeOfIgnition, retroThrust, ispDegradedAtOneAtm,
                        stabilizationType, spinAngle, stageStaysAttached,
                        label));

        ntUsed = true;
      }
      if (!ntUsed) {
        String errMsg = " ERROR:  Not recognized keyword: \n" + nt;
        setMessage(errMsg);
        throw new Exception(errMsg);
      }
    }
    // this.setSize(k.xPixels, k.yPixels);
    setStatusMessage();

    int o;
    double i, j, iloops, jloops;
    int stage;
    if (offscreen == null) {
        offscreen = createImage(getSize().width, getSize().height);
        og = offscreen.getGraphics();
        og.setClip(0, 0, getSize().width, getSize().height);
    }
    screen.clear(og);
    this.paint(og);
    this.getGraphics().drawImage(offscreen, 0, 0, null);

/*  Basic idea:
        1) Outer loop is once per display.
        2) Next loop is once per k.deltaT
        3) Next loop we do 3 passes over all the objects.
             simulate1() - masses initialize totalForce to just gravity
             simulate2() - all objects add in their forces to their masses
             simulate3() - masses figure out new velocities and positions
  */

    for (k.simTime = 0; !getSimulationFinished() && k.simTime < totalTime; ) {
      for (double nextDisplay=k.simTime+k.timePerDisplay;
           !getSimulationFinished() && nextDisplay>k.simTime;
           k.simTime += k.deltaT) {
          for (stage = 1; stage <= 3; stage++) {
            if (stage==1) {
              attime.simulate1(k.deltaT);
            }
            if (stage==3) {
              attime.simulate3(k.deltaT);
            }
            for(Enumeration c=SimObjects.elements();
                !getSimulationFinished() && c.hasMoreElements(); ) {
                simtype st = (simtype) c.nextElement();
                if (stage == 1) {
                  st.simulate1(k.deltaT);
                }
                if (stage == 2) {
                  st.simulate2(k.deltaT);
                }
                if (stage == 3) {
                  st.simulate3(k.deltaT);
                }
            }
          } // end of stage loop
        } // end of deltaT loop

      // Now to display - but first remove finished objects
      for(Enumeration c=SimObjects.elements(); c.hasMoreElements(); ) {
         simtype st = (simtype) c.nextElement();
         if (st.Finished) {
            removeSimObject(st);     // remove all finished sim objects
         }
      }

      while (!getSimulationFinished() && getSuspended()) {
        Thread.currentThread().sleep(50);
      }

      // We slowly draw everything to a buffer then blast onto real screen
      //  if (!getSimulationFinished()) {
      screen.clear(og);
      this.paint(og);
      this.getGraphics().drawImage(offscreen, 0, 0, null);
      //    }
      //java.lang.System.gc();
    } // loop for next set of computation

    setMessage("Done with simulation");
  }

  public static void addSimObject(simtype o) {
     if (o != null) {
       SimObjects.addElement(o);
     }
     findsimobject.add(o);
     setStatusMessage();
  }


  public static void removeSimObject(simtype o) throws Exception {
     SimObjects.removeElement(o);
     findsimobject.remove(o);
     setStatusMessage();
  }

  public static void setStatusMessage() {
    String statusMessage = "Simulating " + SimObjects.size() + " objects: ";
    int i=0;
    for(Enumeration c=SimObjects.elements();i<11 &&  c.hasMoreElements();i++ ) {
        simtype st = (simtype) c.nextElement();
        statusMessage += st.label + " ";
    }
    statusMessage += "  scaleYMeters " + k.dTS(k.scaleYMeters);
    setMessage(statusMessage);
  }

  public static int getNumSimObjects() {
    return(SimObjects.size());
  }

  public static void clearSimObjects() {
    SimObjects.removeAllElements();
    java.lang.System.gc();             // A good time to clean up
  }

  private String secsToTime(double totalInSecs) {
    double togo=totalInSecs;
    if (togo < 0.1) {
      return("Time in seconds " + k.dTSS10(togo));
    }
    int HOURS_PER_DAY = 24;
    int MINUTES_PER_HOUR = 60;
    int SECONDS_PER_MINUTE = 60;

    double seconds = (togo % SECONDS_PER_MINUTE);
    togo /= SECONDS_PER_MINUTE;

    int minutes = (int) (togo % MINUTES_PER_HOUR);
    togo /= MINUTES_PER_HOUR;

    int hours = (int) (togo % HOURS_PER_DAY);
    int days = (int) (togo / HOURS_PER_DAY);

    return ("Time: " + k.dTS3(totalInSecs) + " seconds = " + days + " days " + hours + " hours "
            + minutes + " mins " + k.dTS3(seconds) + " secs ");
  }

  public void paint(Graphics g) {
    screen.clear(g);
    super.paint(g); // paint buttons
    if (!simulationStarted) {
      return; // nothing more
    }
    screen.printMessage(g, CurrentMessage);

    screen.print(g, secsToTime( k.simTime));
    //screen.print(g, "Time " + k.dTS2(k.simTime));

      k.setOrigin();            // Set Origin before painting objects
      screen.drawEarthMoon(g);  // Draw after setOrigin determined
      for(Enumeration c=SimObjects.elements();c.hasMoreElements(); ) {
        simtype st = (simtype) c.nextElement();
        try {
          st.paint(g);
        } catch (Exception e1) {
          CurrentMessage = "Exception " + st.label + ".paint()  "
                             + e1.toString() + "  \n"
                             + e1.getLocalizedMessage() + " \n";
          k.outputAdd(k.simTime + " " + CurrentMessage);
          screen.printMessage(g, CurrentMessage);
        }
      }
      attime.paint(g);          // AtTime at end - all attime objects in queue
      findsimobject.paint(g);   // debug
  }

  public static double readDouble(String s) throws Exception {
    return(k.readDouble(s));
  }

  private static void setMessage(String message) {
    if (message != null) {
      CurrentMessage = message.toString();
    } else {
      CurrentMessage = "null at SetMessage";
    }
    k.debug(CurrentMessage);
    if (k.g != null) {
      screen.printMessage(k.g, CurrentMessage);
    }
  }

//Component initialization

  private void jbInit() throws Exception {

    list = new List();
    list.setForeground(SystemColor.textText);
    list.setBackground(new Color(240, 240, 240));
    list.setMultipleMode(false);
    list.addItemListener(new SpaceTethers_list_itemAdapter(this));

    startButton = new Button("Start Simulation");
    startButton.setActionCommand("Start Simulation");
    startButton.setFont(new Font("Dialog", 1, 12));
    startButton.addActionListener(new SpaceTethers_startButton_actionAdapter(this));
    startButton.addActionListener(this);
    startButton.setBackground(SystemColor.control);
    startButton.setForeground(SystemColor.controlText);

    textArea1.setForeground(SystemColor.textText);
    textArea1.setBackground(new Color(240, 240, 240));
    textArea1.setEditable(true);
    label1.setText("Samples (click one)");
    label1.setFont(new Font("Dialog", 1, 12));
    label1.setForeground(SystemColor.controlText);
    label2.setText("Input (you can edit this - copy ^C paste ^V)");
    label2.setFont(new Font("Dialog", 1, 12));
    label2.setForeground(SystemColor.controlText);

    this.setForeground(SystemColor.windowText);
    this.setBackground(SystemColor.window);
    this.setLayout(gridBagLayout1);

    this.add(label1,
             gbc(1, 1, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.SOUTHWEST,
                                    GridBagConstraints.NONE,
                                    new Insets(10, 20, 0, 3), 0, 0));
    this.add(list,
             gbc(1, 2, 1, 1, 0.5, 1.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH,
                                    new Insets(3, 20, 10, 3), 150, 250));
    this.add(label2,
             gbc(2, 1, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.SOUTHWEST,
                                    GridBagConstraints.NONE,
                                    new Insets(10, 3, 0, 3), 0, 0));
    this.add(textArea1,
             gbc(2, 2, 1, 1, 0.5, 1.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.BOTH,
                                    new Insets(3, 3, 10, 3), 200, 250));
    this.add(startButton,
             gbc(3, 1, 1, 2, 0.0, 0.0,
                                    GridBagConstraints.CENTER,
                                    GridBagConstraints.NONE,
                                    new Insets(3, 10, 3, 20), 7, 20));

  }

//Get app information

  public String getAppletInfo() {
    return "Application Information";
  }

//Get parameter info

  public String[][] getParameterInfo() {
    return null;
  }

  void startButton_actionPerformed(ActionEvent e) {

  }

// was meaning.equals("Get Selected Input")
  void list_itemStateChanged(ItemEvent e) {
    int i;

    i = SpaceTethers.list.getSelectedIndex();
    if (i >= 0 && i < sampleinput.numSamples) {
      SpaceTethers.textArea1.setText(sampleinput.samples[i]);
    }
    if (i == sampleinput.numSamples) {
       textArea1.setText(k.outputGet());
    }
  }

	private GridBagConstraints gbc(
		int gridx,
		int gridy,
		int gridheight,
		int gridwidth,
		double weightx,
		double weighty,
		int anchor,
		int fill,
		Insets insets,
		int ipadx,
		int ipady
	)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=gridx;
		c.gridy=gridy;
		c.gridwidth=gridwidth;
		c.gridheight=gridheight;
		c.weightx=weightx;
		c.weighty=weighty;
		c.anchor=anchor;
		c.fill=fill;
		c.insets=insets;
		c.ipadx=ipadx;
		c.ipady=ipady;
		return c;
	}
} // end class

class SpaceTethers_startButton_actionAdapter
    implements ActionListener {
  SpaceTethers adaptee;

  SpaceTethers_startButton_actionAdapter(SpaceTethers adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.startButton_actionPerformed(e);
  }
}

class SpaceTethers_list_itemAdapter
    implements ItemListener {
  SpaceTethers adaptee;

  SpaceTethers_list_itemAdapter(SpaceTethers adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.list_itemStateChanged(e);
  }
}

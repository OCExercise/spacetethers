 // Copyright (c) Vincent Cate 2002

package com.spacetethers;
import java.text.*;
import java.lang.String;
import java.awt.*;

public class k {
  public static final boolean sim3D=false;  // compile time flag - no spaces for script
  public static final boolean debugOn=false;   // for debug code
  public static int view3D=1;       // 1 means x,y->x,y
                                    // 2 means x,z->x,y
  public static mass tetherEnd3D=null;  // When doing Z vs lengths this is endpoint

  public static double pi = java.lang.Math.PI;
  public static double xPixels=700;   // comes from SpaceTethers.getSize()
  public static double yPixels=450;   // comes from SpaceTethers.getSize()
  public static double xOffset=350;   // set from xPixels
  public static double yOffset=225;   // set from yPixels
  public static double scaleYMeters=25000000.0;
  public static double gScale=scaleYMeters/yPixels;
  public static Graphics g=null;

  public static int EARTH=0;            // center at cetner of Earth - default
  public static int MASS=1;             // follow some mass
  public static int EARTHTOP=2;         // center at top of the Earth
  public static int MOON=3;             // center on moon
  public static int PAN=4;              // we can be moving between masses
  public static double gOrigin=EARTH;   // default is Earth
  public static mass   originMass=null;

  // Earth constants
  public static double secsPerDay = 3600.0*24.0;
  public static double earthSecsPerRev = secsPerDay; // NYC - not exact
  public static double earthRadius =  6378000;     // meters
  public static double earthSpinSpeed = 2 * earthRadius * pi / earthSecsPerRev;
  public static double maxHeightSpinningAir = 20000000; // Wild guess
  public static double earthMass =  5.972E24;      // Kg
                         // http://www.seds.org/nineplanets/nineplanets/earth.html
                         // http://news.bbc.co.uk/1/hi/sci/tech/732018.stm
  public static double moonSecsPerRotation = 3600*24*27.3; // Siderial or relative to stars
  public static double moonDistance = 384235000;   // meters to Earth center
  public static double moonRadius = 1738100;       // meters
  public static double moonMass = 7.349E22;        // Kg
                         // http://nssdc.gsfc.nasa.gov/planetary/factsheet/moonfact.html
  public static double gravConst =  6.67259E-11;      // Nm^2/Kg^2
                         // http://ssd.jpl.nasa.gov/astro_constants.html
                         // http://www.electrogravity.com/NEWG/NewGCnst.pdf
  public static double earthAcceleration = 9.80665;   // m/s^2
  public static double speedOfLight = 299792458;      // meters/sec
  public static double simTime=0;                  // How long simulation has been running
  public static double timePerDisplay=0.1;
  public static double deltaT = 0.001;
  public static double averagingSeconds = 0;       // default is none

  public static double KWHtoJ = 1000*3600;           // KWH to joules
  public static double JtoKWH = 1.0 / KWHtoJ;        //  other way
  public static double JtoBoilKgWater = 2500000;     // Latent heat of evaporation
  public static double JtoKeV = 6241506479963235.0;    // KeV is not much
  public static double bigNum = 1E50;
  public static int bigInt = 2000000000;

  // http://www.physchem.co.za/Heat/Specific.htm
  public static double airSpecificHeatRatio = 1.4; // http://www.taftan.com/thermodynamics/CP.HTM
  public static double shockGamma  = 1.4;          // for shockwave - same as above really
  public static double kcalToJoules = 4184;        // 1 kcal = 4184 Joules
// Specific Heat Capacity:  http://www.physchem.co.za/Heat/Specific.htm
//  public static double airSpecificHeat = 0.25 * kcalToJoules;
//  airSpecificHeat 1030  http://oceanworld.tamu.edu/resources/ocng_textbook/chapter05.htm
  public static double airSpecificHeat = 1024; // joules / Kg-K
// public static double airSpecificHeat = 1005; // http://www.weatherwise.org/qr/qry.diurnaltemp.html
// strange one  287;  // http://trc.ucdavis.edu/rainbow/A6001L9sup2.htm
 // public static double stantonNumber = 0.001;
  // black body http://spiff.rit.edu/classes/phys317/lectures/planck.html
  public static double airStandardAtmospher = 101325.01; // pascals
  public static double ispDegradedAtOneAtm = 0;  // default
  public static double stefanBoltzmann = 5.67E-8;  // joules/(s * m^2 * K^4)
  public static double gasConstant = 286;           // for mach
  private static double celsiusToKelvin = 273.15;
  public static double internalKelvin = 20 + celsiusToKelvin; // TPS heat flow

  private static double degreesToRadians = pi / 180.0;
  private static double radiansToDegress = 180.0 / pi;
  public static double shockErrorTolerance = 0.001;

  public static int TaperLinear=1;
  public static int TaperAutomatic=2;
  public static int taperConvergeLoops=30;   // loops to find center of mass
  public static boolean showXY=false;
  public static boolean showMassToTether=false;
  public static boolean showMassToMoon=false;
  public static boolean showEnergy=false;
  public static boolean showEarthRelative=true;
  public static boolean showMoonRelative=false;
  public static boolean showMomentum=false;
  public static boolean showTetherStatic=true;

  public static boolean logXY = false;  /* log show xy values */
  public static boolean logDrag = false;	/* log drag values */
  public static boolean logPerigee = false;	/* log Perigee and Apogee values */
  public static boolean logThrust = false;	/* log rocket thrust, max G */
  public static boolean rotatingAir=true;
  public static mass moon=null;                          // no default moon
  public static mass earth=null;                         // only used if moon used
  public static tether lastTether=null;           // Most recently made tether
  public static double emRatio = earthMass/moonMass;   // Earth moon mass ratio
  // http://www.cs.bsu.edu/homepages/dathomas/SpaceGrant/Orbit%20Xplorer/Activities/Orbit%20of%20the%20moon.htm
  public static double cmToEarth = moonDistance * (moonMass / (earthMass + moonMass));
  public static double cmToMoon  = moonDistance * (earthMass / (earthMass + moonMass));
  public static double moonVelocity = 1012.169;
        //  java.lang.Math.sqrt(k.gravConst * k.earthMass / moonDistance);
  public static String nowhere = "nowhere";
  public static double SqCMinSqMeter = 10000;
  public static double SqFtinSqMeter = 10.7639104;
  public static double JoulesPerBTU = 1054.3503;
  public static double JoulesPerMegaton = 4184000000000000.0;
  public static double OneAU = 150000000000.0; // 150 mil km
  public static double ElectronMass = 9.10938188E-31; // Kg
  public static double meTomp = 5.446170232E-4;       // mass electron / mass proton
  public static double ProtonMass = ElectronMass/meTomp;
  public static double ElectronCharge = -1.602E-19;
  public static double ProtonCharge = 1.602E-19;

  public k() {
  }

  public static void setShowXY(boolean val) {
     showXY = val;
  }
  public static void setLogXY(boolean val) {
     logXY = val;
  }
  public static void setLogDrag(boolean val) {
     logDrag = val;
  }
  public static void setLogPerigee(boolean val) {
     logPerigee = val;
  }
  public static void setLogThrust(boolean val) {
     logThrust = val;
  }

  public static void setShowMassToTether(boolean val) {
     showMassToTether = val;
  }
  public static void setShowMassToMoon(boolean val) {
     showMassToMoon = val;
  }
  public static void setShowEnergy(boolean val) {
     showEnergy = val;
  }
  public static void setXPixels(double x) {
     xPixels =  x;
     xOffset =  x/2;
  }

  public static void setYPixels(double y) {
     yPixels =  y;
     yOffset =  y/2;
  }

  public static void setGScale(double YMeters) {
     k.scaleYMeters = YMeters;
     gScale =  YMeters / k.yPixels;
  }

  // In SpaceTethers.java during paint() part of simulation loop for the objects
  // we have to set the screen origin k.gOrigin
  // Note that Earth and Moon are masses with those labels
  public static void setOrigin(String origin) throws Exception {
      if (origin.equals("EarthTop")) {
          k.gOrigin=EARTHTOP;
          return;
      }
      mass m1 = findsimobject.labelToMassOrDie(origin);
      if (m1 != null) {
          setOriginMass(m1);
          return;
      }
      throw new Exception("k.setOrigin did not find " + origin);
  }

  // really just for PAN
  public static void setOrigin(int origin) throws Exception {
      k.gOrigin = origin;
  }


  public static void setOriginMass(mass m1) {
    k.originMass = m1;
    k.gOrigin = MASS;
  }

// Used to do EARTH and MOON here but they are covered by MASS now
 public static void setOrigin() {
      if (k.gOrigin==EARTHTOP) {
          screen.setOrigin(0, k.earthRadius);
          return;
      }
      if (k.gOrigin==MASS) {
          k.originMass.setOrigin();
          return;
      }
      if (k.gOrigin==PAN) {
          attime.setOrigin();
          return;
      }
  }

  // When we make a moon the center of our coordinate system
  // is no longer the Earth, but the center of mass of the
  // Earth and moon together.  This means we have to also
  // make an Earth.
  public static void setMoonEarth() throws Exception {
     // Moon starts out straight up going left
     position posMass = new position(0, cmToMoon);
     velocity velMass=new velocity( -moonVelocity, 0);
     k.moon = new mass(k.moonMass, posMass, velMass, "Moon");

     // Already had an earth at 0,0 - now a moving earth.
     // Earth starts out down and going right
     k.earth.pos.set(0, -cmToEarth);
     k.earth.vel.set( moonVelocity/emRatio, 0);
  }

  // This should sort of be in screen and probably
  //   the only reason it is here is that k. takes up
  //   less screen space when looking at the code.
  // dTS = double To String
  public static String dTS(double input) {
      DecimalFormat fmt = new DecimalFormat("0");
      return(fmt.format(input));
  }
    // dTS = double To String
  public static String dTS2(double input) {
      DecimalFormat fmt = new DecimalFormat("0.00");
      return(fmt.format(input));
  }

  // dTS = double To String
  public static String dTS3(double input) {
      DecimalFormat fmt = new DecimalFormat("0.000");
      return(fmt.format(input));
  }
  // double to string scientific
  public static String dTSS(double input) {
      DecimalFormat fmt = new DecimalFormat("0.000E0");
      return(fmt.format(input));
  }

  // double to string scientific
  public static String dTSS10(double input) {
      DecimalFormat fmt = new DecimalFormat("0.000000000000E00");
      return(fmt.format(input));
  }
 // double to string scientific
  public static String dTSS25(double input) {
      DecimalFormat fmt = new DecimalFormat("0.00000000000000000000000000");
      return(fmt.format(input));
  }

  // Given height above moon surface calculate circular orbital speed
  public static double orbitMoonSpeed(double h) {
    double r = h + k.moonRadius;
    return(java.lang.Math.sqrt(k.gravConst * k.moonMass / r));
  }

  public static double readDouble(String s) throws Exception {
    try {
      return new Double(s).doubleValue();
    }
    catch (NumberFormatException e) {
      throw new Exception("Input " + s + " not a double, try again");
    }
  }

  public static double kelvinToCelsius(double kelvin) {
    return(kelvin - k.celsiusToKelvin);
  }
  public static double celsiusToKelvin(double celsius) {
    return(celsius + k.celsiusToKelvin);
  }
  public static double kelvinToFahrenheit(double kelvin) {
    double celsius = kelvinToCelsius(kelvin);
    double fahrenheit = ((212.0-32.0)/100.0)*celsius + 32.0;
    return(fahrenheit);
  }

  public static double degreesToRadians(double degrees) {
     return(degrees * degreesToRadians);
  }
  public static double radiansToDegrees(double radians) {
     return(radians / degreesToRadians);
  }

  public static String outputName="Output from run";
  private static int outputLines=0;
  private static int maxLines=100000;
  private static String[] outputArray= new String[maxLines];
  public static void outputClear() {
     int i;
     for (i=0; i<maxLines; i++) {
       outputArray[i]=null;
     }
     outputLines=0;
  }

  // We only save output for the first maxLines times
  public static void outputAdd(String s1) {
     if (outputLines < maxLines) {
       outputArray[outputLines] = s1;
       outputLines++;
     }
  }

  public static void debug(String message) {
    if (k.debugOn) {
      k.outputAdd(message + " \n");
    }
  }

  public static String outputGet() {
      return(arrayToString(outputArray));
  }

  // Given an array of strings return one big string.
  // Stop at first null string.
  public static String arrayToString(String[] a1)
  {
    StringBuffer  buffer = new StringBuffer();

    int i;
    for (i=0; i<a1.length && a1[i] != null; i++ ) {
      buffer.append(a1[i]);
    }
    return(buffer.toString());
  }

  // We can set globals at startup in SpaceTethers.java or
  //  at runtime from attime.java
  public static boolean isGlobal(String name) throws Exception {
    return(isGlobalAndSetGlobal(name, false, null));
  }
  // It is an exception if they give us a name we don't handle
  public static void setGlobal(String name, String value)
    throws Exception {
    boolean result = isGlobalAndSetGlobal(name, true, value);
    if (result == false) {
       throw new Exception("setGlobal failed with " + name + "  " + value);
    }
    return;
  }
  // Above are user friendly interfaces, below is just for us.
  // Want both in one so we don't get things out of sink.
  private static boolean isGlobalAndSetGlobal(String name, boolean alsoSet,
                                             String value)
                          throws Exception {

    if ("screenOrigin".equals(name)) {
        if (alsoSet) {
           setOrigin(value);
        }
        return(true);
    }
    if ("deltaT".equals(name)) {
        if (alsoSet) {
          deltaT = readDouble(value);
        }
        return(true);
    }
    if ("timePerDisplay".equals(name)) {
        if (alsoSet) {
          timePerDisplay = readDouble(value);
        }
        return(true);
    }
    if ("screenYScaleMeters".equals(name)) {
       if (alsoSet) {
          k.setGScale(readDouble(value));
       }
       return(true);
    }

    if ("showXY".equals(name)) {
      if (alsoSet) {
        setShowXY("true".equals(value));
      }
      return(true);
    }
    if ("showEarthRelative".equals(name)) {
      if (alsoSet) {
        showEarthRelative = "true".equals(value);
      }
      return(true);
    }
    if ("showMoonRelative".equals(name)) {
      if (alsoSet) {
        showMoonRelative ="true".equals(value);
      }
      return(true);
    }
    if ("showMomentum".equals(name)) {
      if (alsoSet) {
        showMomentum ="true".equals(value);
      }
      return(true);
    }
    if ("showTetherStatic".equals(name)) {
      if (alsoSet) {
        showTetherStatic ="true".equals(value);
      }
      return(true);
    }


    if ("logXY".equals(name)) {
      if (alsoSet) {
        setLogXY("true".equals(value));
      }
      return(true);
    }
    if ("logDrag".equals(name)) {
      if (alsoSet) {
        setLogDrag("true".equals(value));
      }
      return(true);
    }

    if ("logPerigee".equals(name)) {
      if (alsoSet) {
        setLogPerigee("true".equals(value));
      }
      return(true);
    }
    if ("logThrust".equals(name)) {
      if (alsoSet) {
        setLogThrust("true".equals(value));
      }
      return(true);
    }
    if ("showMassToTether".equals(name)) {
      if (alsoSet) {
        setShowMassToTether("true".equals(value));
      }
      return(true);
    }
    if ("showMassToMoon".equals(name)) {
      if (alsoSet) {
        setShowMassToMoon("true".equals(value));
      }
      return(true);
    }
    if ("showEnergy".equals(name)) {
      if (alsoSet) {
        setShowEnergy("true".equals(value));
      }
      return(true);
    }
    if ("rotatingAir".equals(name)) {
      if (alsoSet) {
        rotatingAir = "true".equals(value);
      }
      return(true);
    }
    if ("averagingSeconds".equals(name)) {
      if (alsoSet) {
        averagingSeconds = readDouble(value);
      }
      return(true);
    }

    if ("view3D".equals(name)) {
      if (alsoSet) {
        view3D = (int) readDouble(value);
      }
      return(true);
    }
    if ("tetherEnd3D".equals(name)) {
        if (alsoSet) {
           k.tetherEnd3D = findsimobject.labelToMassOrDie(value);
        }
        return(true);
    }
    return(false);  // did not match
  }

  // If user wants to start all over, we need to reset all global or static
  // variables anyplace in the code.
  // If we did not do this then something from previous input could affect this
  //   run and so make output dependent on history and not just current input.
  // May merge this in with above so test/set/initialize all together.
  public static void globalReset() throws Exception {
      k.debug("globalReset start");

      findsimobject.clearAll();
      attime.clearQueue();

      k.setShowXY(false);
      k.setLogXY(false);
      k.setLogDrag(false);
      k.setLogPerigee(false);
      k.setLogThrust(false);
      k.setShowMassToTether(false);
      k.setShowMassToMoon(false);
      k.setShowEnergy(false);
      k.moon = null;
      k.earth = new mass(k.earthMass, new position(0,0), new velocity (0,0), "Earth");
      findsimobject.add(k.earth);

      k.rotatingAir = true; // defaults to true
      k.outputClear();
      k.gOrigin=EARTH;
      k.simTime=0;                  // How long simulation has been running
      k.timePerDisplay=0.1;
      k.deltaT = 0.001;
      k.scaleYMeters=25000000.0;
      k.setGScale(k.scaleYMeters);   // sets k.gScale
      k.averagingSeconds=0;
      k.showEarthRelative=true;
      k.showMoonRelative=false;
      k.showMomentum=false;
      k.showTetherStatic=true;
      k.debug("globalReset done");
      k.view3D=1;
      k.tetherEnd3D=null;
    }
}

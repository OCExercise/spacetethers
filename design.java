// Copyright (c) Vincent Cate 2003
package spacetethers;
public class design {
  public static int Ignite;
public static String Spectra2000 = "density 970 \n " +
        "tensileGpa 4.0    elasticity 0.03 \n \n" ;
public static String Spectra1K = " density 909    \n" +
        " tensileGpa 2.9   elasticity 0.03 \n \n" ;
public static String TaperAuto2 = "taperType Automatic \n" +
        "safetyFactor   2 \n" ;
// Automatic SF 2 at 137 tons gives about this if linear taper tether
public static String LEO137T_SF2 = "bigEndDiameter   0.017058  " +
        "smallEndDiameter 0.008229 \n" ;
public static String LEOHead = "/* A tether with a payload speed of 5 km/s \n" +
          " Earth is about 0.5  So rocket needs about 4.5 km/s \n" +
          " but grav loss gets us back up to about 5  */ \n \n" ;

public static String LEOLength = "restLength    500000     " +
      "slices        501\n " ;

public static String LEOHeight = "smallEndHeight 100000 \n" ;

public static String LEOPay =
      "velSmallEnd   -5000    0     " +
      "payloadKg     4500   \n "  ;

public static String LEOBallast = "velBigEnd    -7900	  0    " +
      "bigKg       200000  \n " ;

public static String LEOEDT4T = "edtTotalLength 120000  \n" +
        "edtCurrent     100     " +
        "edtDiameter    0.0047  \n" +
        "MakeEDT           \n \n" ;
public static String LEOEDT2T = "edtTotalLength 100000  \n" +
            "edtCurrent     40      \n" +
            "edtDiameter    0.0033  \n" +
            "MakeEDT           \n \n" ;

public static String LEOtether = LEOHead + Spectra2000 +
        "label T4T       \n" +
        LEOHeight +
        LEOLength +
        LEOPay +
        LEOBallast +
        TaperAuto2 +
        "MakeTether  Ballast Payload  \n \n"  ;
public static String LEOEtether = LEOtether +
     LEOEDT4T;
    /*  This case (39)simulates the 4 ton tether with 137 ton ballast */
public static String LEO137B4T ="/* Use automatic taper for heavy ballast   \n"+
            " and payload but then loose most of mass.  \n" +
            " This simulates the first loads with the 4 ton tether   \n" +
            " At low ballast  */          \n" +
            design.Spectra2000 +
            "label T4T       \n" +
            design.LEOHeight +
            design.LEOLength +
            "velSmallEnd   -5085    0     " +
            "payloadKg     4500   " +
            "velBigEnd    -7983.2	  0    " +
            "bigKg       200000  \n " +
            design.TaperAuto2 +
            "MakeTether  Ballast Payload  \n \n" ;
public static String LEO4TlowBtoss =
    "AtTime 1 toss 300 from T4T.Payload to nowhere EndAtTime\n" +
    "AtTime 1 toss 63000 from T4T.Ballast to nowhere EndAtTime\n" +
    "AtTime 1440 toss 3700 from nowhere to T4T.Ballast EndAtTime\n \n" ;

public static String LEOSeed2T = Spectra2000 +
            "label T2T    \n" +
            LEOHeight +
            LEOLength +
            "velBigEnd     -7700    0  \n" +
            "bigKg       100000     \n " +
            "velSmallEnd   -5700    0  \n " +
            "payloadKg      2200       \n " +
            TaperAuto2 +
            "MakeTether   Ballast Payload  \n  \n " ;

   public static String TossLEO4Thdr =
  "/* Try rocket as Hall thruster, ISP 2020, fuel for 3420 seconds.*/ \n" +
  "/* AtTime 433 toss 4000 from T4T.Payload to M	minimum perigee 150 km*/ \n" +
  "/* Compare rocket to simple mass */ \n" ;

  public static String TossLEO4Tlog =       " \n" +
   "/* log from Sample 60 ­ Start at toss,   \n" +
  "M.logXY 440 velMass -8482 -1801 alt 729300 posMass -2913031 6482898\n" +
"M.logXY 450 velMass -8449 -1873 alt 747747.1 posMass -2997688 6464529\n" +
    "\n" +
"M.APOGEE 5430 velMass 3753 -1397 alt 8664613.41 posMass -5246872 ­14097892\n" +
   "\n" +
"M.PERIGEE 11000 velMass -8632 3260 alt 150058.2 posMass 2318422 6102496\n" +
"M.logXY  11010 velMass -8664 3172 alt 150061.6 posMass 2231939 6134659\n" ;

public static String Hall8Unit =
   "thrustNewtons 4.096  ISP 2020	/* 8 Hall @ .5012 each */  \n";
   public static String TossLEO4THall =
      "label R   " +
      "massKg 3979       \n" +
      "stageStaysAttached true   \n" +
      "dryKg 40.25  fuelKg 1 	\n" +
      "dragArea   328 /* 64 KW  */ coefficientOfDrag 0.5 \n" +
      design.Hall8Unit +
      "velMass -8482 -1801 posMass -2913031 6482898 \n" +
      "MakeMass MakeRocket  \n \n" ;

  public static String HallLEOraisePerigee =
  "/* simulated Hall thruster ­ raise Perigee 20 km, with minimal thrust */ \n" ;
  public static String HallLEOlowerApogee =
  "/* simulated Hall thruster ­ lower perigee with thrust  \n" +
  "   then ­ lower apogee, with minimal drag at 110 km altitude  \n" +
  "   After 5 days, start to raise perigee, with thrust */ \n" ;
public static String Display10Fast = "deltaT 0.05 \n" +
        "timePerDisplay 10 \n" ;
public static String Display10Norm = "deltaT 0.02 \n" +
        "timePerDisplay 10 \n" ;
public static String Display10FineRes = "deltaT 0.01 \n" +
        "timePerDisplay 10 \n" ;
public static String Display5FineRes = "deltaT 0.01 \n" +
        "timePerDisplay 5 \n" ;

public static String SimOrbit = "totalTime 6000 \n" ;
public static String SimDay = "totalTime 86400 \n" ;

   /* mostly when log Perigee, outside atmosphere, Mach info clutter */
    public static String logXY_Perigee = "rotatingAir false \n" +
    "logPerigee true \n" ;

}

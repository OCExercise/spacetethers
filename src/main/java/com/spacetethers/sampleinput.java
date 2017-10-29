
// Copyright (c) Vincent Cate 2003
package com.spacetethers;

import java.lang.Math;

public class sampleinput {


  public static int numSamples =102;

  public static String[] samples;
  public static String[] sampleNames;

  public sampleinput() {
  }


  public static String getName(int num) {
      return(sampleNames[num]);
  }

  public static String getSample(String name) {
     int i;
     for (i=0; i<numSamples; i++) {
        if (name.equals(sampleNames[i])) {
           return(samples[i]);
        }
     }
     return(" ");
  }

  public static boolean isSample(String name) {
     int i;
     for (i=0; i<numSamples; i++) {
        if (name.equals(sampleNames[i])) {
           return(true);
        }
     }
     return(false);
  }
  public static void init() {
    samples = new String[numSamples];
    sampleNames = new String[numSamples];

    sampleNames[0] = "0. Simulate Backwards";
    samples[0] =
        "/* We can run time backwards \n" +
        "  to see where a rocket would \n" +
        "  have to come from. */    \n  \n" +
        design.LEOSeed2T +
        "massKg 100  label M  \n " +
        "posMass     0 6480000  \n " +
        "velMass     -5700      0   \n " +
        "MakeMass    \n " +
        " \n" +
        "showXY true \n" +
        "screenYScaleMeters   1600000 \n" +
        "screenOrigin    T2T.C     \n " +
        " \n" +
        "deltaT           -0.01    \n " +
        "timePerDisplay  -1       \n " +
        "totalTime     -230     \n ";

    sampleNames[1] = "1. To tether";
    samples[1] =
        "/* Look at a rocket meeting \n" +
        "  a tether from Tether frame */ \n" +
        " \n" +
        "density 970              \n " +
        "tensileGpa 3.51          \n " +
        "elasticity 0.03           \n" +
        " \n" +
        "posBigEnd   1615485 6792550 \n" +
        "posSmallEnd  1248582 6446577\n" +
        "restLength    0500000 \n" +
        "velBigEnd     -7464 1800 \n" +
        "velSmallEnd   -6191 435\n " +
        "bigEndDiameter   0.005   \n " +
        "smallEndDiameter 0.003   \n " +
        "bigKg       50000     \n " +
        "payloadKg      200       \n " +
        "slices        100        \n " +
        "label T1    \n" +
        "MakeTether  Ballast Payload    \n " +
        " \n" +
        "massKg 250    \n " +
        "posMass     1194990 6266093  \n " +
        "velMass     -5509  2023\n " +
        "label M1    \n" +
        "MakeMass    \n " +
        "AtTime 212 toss 250 from M1 to T1.Payload EndAtTime \n" +
        "AtTime 1000 toss 250 from T1.Payload to M2 EndAtTime\n" +
        " \n" +
        "screenYScaleMeters   1600000 \n" +
        "screenOrigin    T1.C     \n " +
        "logXY true        \n" +
        " \n" +
        "deltaT           0.01    \n " +
        "timePerDisplay  1       \n " +
        "totalTime    1500     \n ";

    sampleNames[2] = "2. World View";
    samples[2] =
        "/* Look at things with whole \n" +
        "  Earth view.  */         \n" +
        " \n" +
        "density 970              \n " +
        "tensileGpa 3.51          \n " +
        "elasticity 0.03           \n" +
        " \n" +
        "posBigEnd   1615485 6792550 \n" +
        "posSmallEnd  1248582 6446577\n" +
        "restLength    0500000 \n" +
        "velBigEnd     -7464 1800 \n" +
        "velSmallEnd   -6191 435\n " +
        "bigEndDiameter   0.005   \n " +
        "smallEndDiameter 0.003   \n " +
        "bigKg       50000     \n " +
        "payloadKg      200       \n " +
        "slices        100        \n " +
        "label T1    \n" +
        "MakeTether Ballast Payload             \n " +
        " \n" +
        "massKg 250    \n " +
        "posMass     1194990 6266093  \n " +
        "velMass     -5509  2023\n " +
        "label M1    \n" +
        "MakeMass    \n " +
        "AtTime 212 toss 250 from M1 to T1.Payload EndAtTime \n" +
        "AtTime 1000 toss 250 from T1.Payload to M2 EndAtTime\n" +
        " \n" +
        "screenYScaleMeters   16000000 \n" +
        "screenOrigin    Earth      \n " +
        " showMassToTether true \n" +
        " \n" +
        "deltaT           0.01    \n " +
        "timePerDisplay  10       \n " +
        "totalTime     8400    \n ";

    sampleNames[3] = "3. Design point";
    samples[3] =
        design.LEOtether +
        design.LEOEDT4T +
        design.Display10Fast +
        "totalTime 8000        \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T4T.C       \n";

    sampleNames[4] = "4. Fast Tip Speed";
    samples[4] =
        "/* A tether with a high  \n" +
        " tip velocity and not  \n" +
        " enough safety margin.  */ \n " +
        " \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight 100000 \n" +
        "restLength    600000  \n" +
        "stretch       1.017   \n" +
        "velBigEnd     -7800    0 \n" +
        "velSmallEnd   -3600    0  \n" +
        "bigEndDiameter   0.01    \n" +
        "smallEndDiameter 0.0025 \n" +
        "bigKg       100000      \n" +
        "payloadKg     200       \n" +
        "slices        100      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        design.Display10FineRes +
        "totalTime 8000        \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T1.C              \n";

    sampleNames[5] = "5. Projectile 0km";
    samples[5] =
        "/* Look at air resistance \n" +
        " on some projectiles starting \n" +
        " at ground level.       */    \n" +
        " \n" +
        "screenYScaleMeters   600000   \n" +

        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     200      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "massHeight 0       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.2               \n" +
        "                         \n" +
        "label A55                \n" +
        "massVelAndAngle 4500 55  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A50                \n" +
        "massVelAndAngle 4500 50  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A45                \n" +
        "massVelAndAngle 4500 45  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A40                \n" +
        "massVelAndAngle 4500 40  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A35                \n" +
        "massVelAndAngle 4500 35  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A30                \n" +
        "massVelAndAngle 4500 30  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A25                \n" +
        "massVelAndAngle 4500 25  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A20                \n" +
        "massVelAndAngle 4500 20  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "screenOrigin    A35   \n";

    sampleNames[6] = "6. Projectile 20km";
    samples[6] =
        "/* Look at air resistance \n" +
        " on some projectiles starting \n" +
        " at 20,000 meters high.   \n" +
        " Do not slow down like    \n" +
        " ground launched ones. */ \n" +
        " \n" +
        "screenYScaleMeters   600000   \n" +
        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     200      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "massHeight 20000       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.2               \n" +
        "                         \n" +
        "label A30                \n" +
        "massVelAndAngle 4500 30  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A28                \n" +
        "massVelAndAngle 4500 28  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A26                \n" +
        "massVelAndAngle 4500 26  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A24                \n" +
        "massVelAndAngle 4500 24  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A22                \n" +
        "massVelAndAngle 4500 22  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A20                \n" +
        "massVelAndAngle 4500 20  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A18                \n" +
        "massVelAndAngle 4500 18  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label A16                \n" +
        "massVelAndAngle 4500 16  \n" +
        "MakeMass                \n" +
        "screenOrigin    A22   \n";

    sampleNames[7] = "7. Rocket 0km";
    samples[7] =
        "/* Look at air resistance \n" +
        " on a rocket starting \n" +
        " at 0 meters high.   \n" +
        " Slows down more than \n" +
        " 20km launched ones. */ \n" +
        " \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     300      \n" +
        "                       \n" +
        "massKg 200            \n" +
        "massHeight 1           \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.3           \n" +
        "                       \n" +
        "ISP 350                \n" +
        "thrustNewtons 70000     \n" +
        "dryKg  368.25         \n" +
        "fuelKg 1704.75        \n" +
        "                     \n" +
        "label A65          \n" +
        "surfaceVelAndAngle 150 65 \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A64          \n" +
        "surfaceVelAndAngle 150 64  \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A63          \n" +
        "surfaceVelAndAngle 150 63   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A62          \n" +
        "surfaceVelAndAngle 150 62   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A61          \n" +
        "surfaceVelAndAngle 150 61   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "screenOrigin    A64   \n";

    sampleNames[8] = "8. Rocket 20km";
    samples[8] =
        "/* Look at air resistance \n" +
        " a rocket starting \n" +
        " at 20 km high.   \n" +
        " Do not slow down like    \n" +
        " ground launched ones. */ \n" +
        " \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     250      \n" +
        "                       \n" +
        "massKg 0.1            \n" +
        "massHeight 20000       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3               \n" +
        "                         \n" +
        "ISP 350                  \n" +
        "thrustNewtons 70000     \n" +
        "rocketKg 2273        \n" +
        "fuelFraction 0.75            \n" +
        "stageStaysAttached true    \n" +
        "                         \n" +
        "label A54                  \n" +
        "surfaceVelAndAngle 150 54   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A53                  \n" +
        "surfaceVelAndAngle 150 53   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A52                 \n" +
        "surfaceVelAndAngle 150 52   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A51                  \n" +
        "surfaceVelAndAngle 150 51   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A50                  \n" +
        "surfaceVelAndAngle 150 50   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "screenOrigin    A52   \n";

    sampleNames[9] = "9. SSTO 0km";
    samples[9] =
        "/* Look at air resistance \n" +
        " on a SSTO rocket starting \n" +
        " at 0 meters high.   \n" +
        " */    \n" +
        " \n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth  \n" +
        "                        \n" +
        "deltaT           0.01  \n" +
        "timePerDisplay  10      \n" +
        "totalTime     12000      \n" +
        "                       \n" +
        "massKg 0.01            \n" +
        "rocketKg 2273          \n" +
        "massHeight 0           \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3              \n" +
        "                         \n" +
        "ISP 350                  \n" +
        "thrustNewtons 59000     \n" +
        "fuelFraction 0.96       \n" +
        "stageStaysAttached true    \n" +
        "                         \n" +
        "label A63.9               \n" +
        "surfaceVelAndAngle 150 63.9   \n" +
        "MakeMass            \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A63.8               \n" +
        "surfaceVelAndAngle 150 63.8  \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A63.7               \n" +
        "surfaceVelAndAngle 150 63.7   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A63.6               \n" +
        "surfaceVelAndAngle 150 63.6   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A63.5               \n" +
        "surfaceVelAndAngle 150 63.5   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n";

    sampleNames[10] = "10. SSTO from 20km";
    samples[10] =
        "/* Look at air resistance \n" +
        " a rocket starting \n" +
        " at 20 km high   \n" +
        " going to orbital velocity \n" +
        " */ \n" +
        " \n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           0.01  \n" +
        "timePerDisplay  10     \n" +
        "totalTime     12000     \n" +
        "                       \n" +
        "massKg 0.01            \n" +
        "rocketKg 2273            \n" +
        "massHeight 20000       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3               \n" +
        "                         \n" +
        "ISP 350                  \n" +
        "thrustNewtons 70000     \n" +
        "fuelFraction 0.94       \n" +
        "stageStaysAttached true  \n" +
        "                         \n" +
        "label A51               \n" +
        "surfaceVelAndAngle 150 51   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A50.5               \n" +
        "surfaceVelAndAngle 150 50.5   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A50               \n" +
        "surfaceVelAndAngle 150 50   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A49.5               \n" +
        "surfaceVelAndAngle 150 49.5   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A49               \n" +
        "surfaceVelAndAngle 150 49   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n";

    sampleNames[11] = "11. Circular Orbits - Fast";
    samples[11] =
        "/* Some masses in circular orbits.\n" +
        " See        \n" +
        " http://www.astro.lsa.umich.edu/Course/Aller160/a160mech.htm \n " +
        " http://hyperphysics.phy-astr.gsu.edu/hbase/orbv3.html  \n" +
        " V = sqrt( MASSEarth * GravConstEarth / R) */ \n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  10      \n" +
        "totalTime     6000      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "                       \n" +
        "massHeight 100000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 200000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 300000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 400000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 500000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 600000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 700000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 800000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 900000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1000000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1100000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1200000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n";

    sampleNames[12] = "12. Circular Orbits - Slow";
    samples[12] =
        "/* Some masses in circular orbits.\n" +
        " See        \n" +
        " http://www.astro.lsa.umich.edu/Course/Aller160/a160mech.htm \n " +
        " http://hyperphysics.phy-astr.gsu.edu/hbase/orbv3.html  \n" +
        " V = sqrt( MASSEarth * GravConstEarth / R) */ \n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     6000      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "                       \n" +
        "massHeight 100000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 200000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 300000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 400000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 500000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 600000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 700000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 800000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 900000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1000000       \n" +
        "massCircularOrbit        \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1100000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "massHeight 1200000       \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n";

    sampleNames[13] = "13. VASMIR Engine";
    samples[13] =
        "/* Low thrust high ISP \n" +
        " starting in LEO.  \n" +
        " http://gps.csr.utexas.edu/~gaylor/gaylor_pub/pluto-project.pdf\n" +
        " */ \n" +
        "screenYScaleMeters   100000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           1      \n" +
        "timePerDisplay  100000  \n" +
        "totalTime    489302077  \n" +
        " /* 10000 sec * 1100 lbs fuel / 0.022481 lbs thrust \n" +
        " 15.52 years of thrust - wow  \n" +
        " Escape velocity after 600/4893 of fuel */ \n" +
        "label VASMIR           \n" +
        "massKg 1               \n" +
        "rocketKg 1000          \n" +
        "massHeight 100000      \n" +
        "massCircularOrbit        \n" +
        "ISP 10000              \n" +
        "thrustNewtons 0.1      \n" +
        "fuelFraction 0.5       \n" +
        "MakeMass               \n" +
        "MakeRocket             \n";

    sampleNames[14] = "14. Deep Space 1";
    samples[14] =
        "/* Low thrust high ISP \n" +
        " starting in LEO.  \n" +
        " http://www.boeing.com/defense-space/space/bss/factsheets/xips/nstar/ionengine.html\n" +
        " http://nssdc.gsfc.nasa.gov/database/MasterCatalog?sc=1998-061A \n" +
        " */ \n" +
        "screenYScaleMeters   100000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "label DS1               \n" +
        "deltaT           1      \n" +
        "timePerDisplay  100000  \n" +
        "totalTime    100000000  \n" +
        "massKg 0.3              \n" +
        "rocketKg 486             \n" +
        "massHeight 100000      \n" +
        "massCircularOrbit      \n" +
        "ISP 3100              \n" +
        "thrustNewtons 0.092    \n" +
        "fuelFraction 0.167592  \n" +
        " /* 81.5 Kg xenon 486.3 Kg total */ \n" +
        "MakeMass               \n" +
        "MakeRocket             \n";

    sampleNames[15] = "15. Project Orion";
    samples[15] =
        "/* High thrust high ISP \n" +
        " See  \n" +
        " http://www.amazon.com/exec/obidos/ASIN/0805059857/offshoreinformat\n" +
        " */ \n" +
        "screenYScaleMeters   4000000   \n" +
        "                        \n" +
        "deltaT           0.01      \n" +
        "timePerDisplay  1  \n" +
        "totalTime    500  \n" +
        "label Orion       \n" +
        "massKg 1          \n" +
        "rocketKg 4000000         \n" +
        "massHeight 0      \n" +
        "surfaceVelAndAngle 200 90  \n" +
        "ISP 6000                \n" +
        "thrustNewtons 80000000  \n" +
        "fuelFraction 0.5  \n" +
        "MakeMass          \n" +
        "MakeRocket             \n" +
        "screenOrigin    Orion   \n";

    sampleNames[16] = "16. Light Slow Reentry";
    samples[16] =
        "/* Looking at drag and \n" +
        " heat of reentry when empty.  \n" +
        " */ \n" +
        "screenYScaleMeters   500000   \n" +
        "                        \n" +
        "deltaT           0.0005  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     250      \n" +
        "                       \n" +
        "massKg 295             \n" +
        "massHeight 100000       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3               \n" +
        "massNoseAngle 10        \n" +
        "massVelAndAngle 5000 0  \n" +
        "label 5km-0.3m^2           \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 5km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 5000 0  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 4.5km-0.3m^2           \n" +
        "dragArea 0.3               \n" +
        "massVelAndAngle 4500 0  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 4.5km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 4500 0  \n" +
        "MakeSharpBody             \n" +
        "                        \n" +
        "label 4km-0.3m^2           \n" +
        "dragArea 0.3               \n" +
        "massVelAndAngle 4000 0  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 4km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 4000 0  \n" +
        "MakeSharpBody              \n" +
        "screenOrigin    4.5km-1m^2   \n";

    sampleNames[17] = "17. Heavy Slow Reentry";
    samples[17] =
        "/* Looking at drag and \n" +
        " heat of reentry when empty.  \n" +
        " */ \n" +
        "screenYScaleMeters   500000   \n" +
        "                        \n" +
        "deltaT           0.0005  \n" +
        "timePerDisplay  1        \n" +
        "totalTime     210       \n" +
        "                        \n" +
        "massHeight 100000       \n" +
        "massKg 2273             \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3               \n" +
        "massNoseAngle 10        \n" +
        "massVelAndAngle 5000 0  \n" +
        "MakeSharpBody                \n" +
        "                        \n" +
        "label 5km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 5000 0  \n" +
        "MakeSharpBody                \n" +
        "                        \n" +
        "label 4.5km-0.3m^2           \n" +
        "dragArea 0.3               \n" +
        "massVelAndAngle 4500 0  \n" +
        "MakeSharpBody                \n" +
        "                        \n" +
        "label 4.5km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 4500 0  \n" +
        "MakeSharpBody                \n" +
        "                        \n" +
        "label 4km-0.3m^2           \n" +
        "dragArea 0.3               \n" +
        "massVelAndAngle 4000 0  \n" +
        "MakeSharpBody                \n" +
        "                        \n" +
        "label 4km-1m^2           \n" +
        "dragArea 1                 \n" +
        "massVelAndAngle 4000 0  \n" +
        "MakeSharpBody                \n" +
        "screenOrigin   4.5km-1m^2   \n";

    sampleNames[18] = "18. Heavy Fast Reentry";
    samples[18] =
        "/* Looking at drag and \n" +
        " heat of reentry from full  \n" +
        " orbital speed */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.0005  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    440       \n" +
        "                        \n" +
        "massHeight 100000       \n" +
        "massKg 2273             \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3               \n" +
        "massNoseAngle 10        \n" +
        "label 8km-1m^2           \n" +
        "massVelAndAngle 8000 -2  \n" +
        "MakeSharpBody              \n" +
        "                         \n" +
        "label 8km-1m^2           \n" +
        "dragArea 1                  \n" +
        "massVelAndAngle 8000 -2  \n" +
        "MakeSharpBody                 \n" +
        "                          \n" +
        "label 7.5km-0.3m^2           \n" +
        "dragArea 0.3                 \n" +
        "massVelAndAngle 7500 -1  \n" +
        "MakeSharpBody                \n" +
        "                          \n" +
        "label 7.5km-1m^2           \n" +
        "dragArea 1                   \n" +
        "massVelAndAngle 7500 -1  \n" +
        "MakeSharpBody                \n" +
        "                          \n" +
        "label 7km-0.3m^2           \n" +
        "dragArea 0.3                 \n" +
        "massVelAndAngle 7000 0  \n" +
        "MakeSharpBody               \n" +
        "                          \n" +
        "label 7km-1m^2           \n" +
        "dragArea 1                   \n" +
        "massVelAndAngle 7000 0  \n" +
        "MakeSharpBody                \n" +
        "screenOrigin    7.5km-1m^2   \n";

    sampleNames[19] = "19. XPrize";
    samples[19] =
        "/* Three possible XPrize rockets. \n" +
        " Note less than half fuel. */ \n" +
        " \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     300      \n" +
        "                       \n" +
        "massKg 1               \n" +
        "rocketKg 3000          \n" +
        "stageStaysAttached true \n" +
        "massHeight 0           \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.3              \n" +
        "                         \n" +
        "rocketParachuteOpenHeight 5000 \n" +
        "rocketParachuteArea   50 \n" +
        "rocketParachuteCd     0.5 \n" +
        "ISP 350                  \n" +
        "thrustNewtons 70000     \n" +
        "fuelFraction 0.45        \n" +
        "                         \n" +
        "label A80                \n" +
        "surfaceVelAndAngle 150 80   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A85                \n" +
        "surfaceVelAndAngle 150 85  \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A90                \n" +
        "surfaceVelAndAngle 150 90   \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "screenOrigin    A80  \n";

    sampleNames[20] = "20. GEO-Sync Orbit";
    samples[20] =
        "/* To show GEO orbit. \n" +
        " Mass stays above same spot on Earth.  \n" +
        " Note a few meters of error in height \n" +
        " out of 35 million.  Can eliminate  \n" +
        " this error with smaller deltaT. */ \n" +
        " \n" +
        "screenYScaleMeters   100000000   \n" +
        "screenOrigin    Earth    \n" +
        "                         \n" +
        "label GEO                \n" +
        "deltaT           0.005   \n" +
        "timePerDisplay  100      \n" +
        "totalTime     86400      \n" +
        "showXY true              \n" +
        "massKg 3000              \n" +
        "massHeight 35785000      \n" +
        "massCircularOrbit        \n" +
        "MakeMass                 \n";

    sampleNames[21] = "21. Geo-stationary Tether";
    samples[21] =
        "/* Note that this is huge and not something that can \n" +
        "  really be built yet.  This is hundreds of times \n" +
        "  more mass than everything humans have ever launched \n" +
        "  put together.  We are also using some imaginary material \n" +
        "  that is 10 times stronger than Spectra 2000. */ \n" +
        "density 970              \n " +
        "tensileGpa 35.1          \n " +
        "elasticity 0.03           \n" +
        " \n" +
        "label Elevator          \n" +
        "smallEndHeight 50000\n" +
        "restLength    36840000 \n" +
        "stretch 1.01 \n" +
        "velBigEnd    -3205 0 \n" +
        "velSmallEnd   -470 0 \n " +
        "bigEndDiameter   0.1   \n " +
        "smallEndDiameter 0.01   \n " +
        "bigKg       500000000     \n " +
        "payloadKg      10000       \n " +
        "slices        1000        \n " +
        "MakeTether   Ballast Payload              \n " +
        " \n" +
        " \n" +
        "screenYScaleMeters   120000000 \n" +
        "screenOrigin    Earth     \n " +
        " \n" +
        "deltaT           0.01    \n " +
        "timePerDisplay  1      \n " +
        "totalTime     900     \n ";

    sampleNames[22] = "22. Orbits with Drag/Thrust";
    samples[22] =
        "/* Have some drag and some thrust. \n" +
        " With 2 sq meters area and thrust  \n" +
        " of 0.5 Newtons we need to be at  \n" +
        " 200 km to stay in orbit. \n" +
        " */ \n" +
        "screenYScaleMeters   1000000   \n" +
        "                       \n" +
        "deltaT           0.001 \n" +
        "timePerDisplay  1      \n" +
        "totalTime    1000      \n" +
        "massKg 1               \n" +
        "rocketKg 500           \n" +
        "coefficientOfDrag 0.5             \n" +
        "dragArea 2                \n" +
        "ISP 3100               \n" +
        "thrustNewtons 0.5      \n" +
        "fuelFraction 0.2       \n" +
        "                       \n" +
        "label 100km            \n" +
        "massHeight 100000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 120km            \n" +
        "massHeight 120000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 140km            \n" +
        "massHeight 140000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 160km            \n" +
        "massHeight 160000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 180km            \n" +
        "massHeight 180000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 200km            \n" +
        "massHeight 200000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 220km            \n" +
        "massHeight 220000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "label 240km            \n" +
        "massHeight 240000      \n" +
        "massCircularOrbit      \n" +
        "MakeMass               \n" +
        "MakeRocket             \n" +
        "screenOrigin    200km \n";

    sampleNames[23] = "23. Rendezvous";
    samples[23] =
        "/* Look at 2 seconds before/after rendezvous. \n" +
        "  Mass is at center of screen. \n" +
        "  It is 50 meters from mass to top of screen.  \n" +
        "  Just a few seconds for tether to harpoon payload. */ \n" +
        " \n" +
        "density 970              \n " +
        "tensileGpa 3.51          \n " +
        "elasticity 0.03           \n" +
        " \n" +
        "label catchMe             \n" +
        "posBigEnd   15400 6984983 \n" +
        "posSmallEnd  11440 6480027\n" +
        "restLength    0500000 \n" +
        "velBigEnd     -7700 17 \n" +
        "velSmallEnd   -5720 -24\n " +
        "bigEndDiameter   0.005   \n " +
        "smallEndDiameter 0.003   \n " +
        "bigKg       50000     \n " +
        "payloadKg      200       \n " +
        "slices        100        \n " +
        "MakeTether   Ballast Payload              \n " +
        " \n" +
        "label M1  \n" +
        "massKg 250    \n " +
        "posMass     11400 6479981  \n " +
        "velMass     -5700  19\n " +
        "MakeMass    \n " +
        " \n" +
        "screenYScaleMeters   100 \n" +
        "screenOrigin    M1     \n " +
        " showMassToTether true \n" +
        " \n" +
        "deltaT           0.0001    \n " +
        "timePerDisplay  0.1       \n " +
        "totalTime     4     \n ";

    sampleNames[24] = "24. Moon Flyby 1";
    samples[24] =
        "/* Gravity assist flyby of moon. \n" +
        "  Masses start out at the same height as the moon \n" +
        "  and 2200 m/sec and after passing will be higher and faster. \n " +
        "  http://nssdc.gsfc.nasa.gov/planetary/factsheet/moonfact.html */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .5      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     500000  \n" +
        "posMass     384400000 0   \n" +
        "massKg 1000             \n" +
        "label m1                       \n" +
        "massVelAndAngle 2200 39.7 \n" +
        "MakeMass                \n" +
        "label m2                       \n" +
        "massVelAndAngle 2200 39.75 \n" +
        "MakeMass                \n" +
        "label m3                        \n" +
        "massVelAndAngle 2200 39.8  \n" +
        "MakeMass                \n" +
        "label m4                        \n" +
        "massVelAndAngle 2200 39.85 \n" +
        "MakeMass                \n" +
        "label m5                        \n" +
        "massVelAndAngle 2200 39.9  \n" +
        "MakeMass                \n" +
        "label m6                         \n" +
        "massVelAndAngle 2200 39.95 \n" +
        "MakeMass                \n" +
        "label m7                         \n" +
        "massVelAndAngle 2200 40  \n" +
        "MakeMass                \n" +
        "label m8                         \n" +
        "massVelAndAngle 2200 40.05  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[25] = "25. Moon Flyby 2";
    samples[25] =
        "/* Gravity assist flyby of moon. \n" +
        "  Masses start out at the same height as the moon \n" +
        "  and 1200 m/sec and after passing will be higher and faster. \n " +
        "  http://nssdc.gsfc.nasa.gov/planetary/factsheet/moonfact.html */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .3      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     1500000  \n" +
        "posMass     271811846 271811846   \n" +
        "massKg 1000             \n" +
        "                        \n" +
        "label m1                       \n" +
        "massVelAndAngle 1200 24.2 \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m2                       \n" +
        "massVelAndAngle 1200 24.3 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m3                       \n" +
        "massVelAndAngle 1200 24.4  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m4                       \n" +
        "massVelAndAngle 1200 24.5 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m5                       \n" +
        "massVelAndAngle 1200 24.6  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m6                       \n" +
        "massVelAndAngle 1200 24.7 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m7                       \n" +
        "massVelAndAngle 1200 24.8 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m8                       \n" +
        "massVelAndAngle 1200 24.9  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[26] = "26. Moon Flyby 3";
    samples[26] =
        "/* Gravity assist flyby of moon. \n" +
        "  Masses start out at the same height as the moon \n" +
        "  and 1200 m/sec and after passing will be higher and faster. \n " +
        "  http://nssdc.gsfc.nasa.gov/planetary/factsheet/moonfact.html */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .3      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     1500000  \n" +
        "posMass     271811846 271811846   \n" +
        "massKg 1000             \n" +
        "                        \n" +
        "label m1                       \n" +
        "massVelAndAngle 1500 18.5 \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m2                       \n" +
        "massVelAndAngle 1500 19 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m3                       \n" +
        "massVelAndAngle 1500 19.5  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m4                       \n" +
        "massVelAndAngle 1500 20 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m5                       \n" +
        "massVelAndAngle 1500 20.5  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m6                      \n" +
        "massVelAndAngle 1500 21 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m7                       \n" +
        "massVelAndAngle 1500 21.5 \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label m8                       \n" +
        "massVelAndAngle 1500 22  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[27] = "27. Earth Moon Lagrange Orbits";
    samples[27] =
        "/* L1, L2, L3, L4, L5 orbits for Earth moon system. \n" +
        " These numbers are not exact, but good enough to  \n" +
        " see the ideas.  After 20 days or so L1 and L2 will \n" +
        " not be in position, but they are very unstable orbits.  */ \n" +
        " \n" +
        "screenYScaleMeters   900000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .1      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     2419200  \n" +
        "massKg 1000             \n" +
        "                        \n" +
        "label L1                \n" +
        "posMass   0  321561610   \n" +
        "velMass  " +
        k.dTS2( - (k.moonVelocity * (321561610 / k.cmToMoon)))
        + " 0    \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label L2                \n" +
        "posMass   0  444062000   \n" +
        "velMass " +
        k.dTS2( - (k.moonVelocity * (444062000 / k.cmToMoon)))
        + " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label L3                \n" +
        "posMass   0  -386180000   \n" +
        "velMass " +
        k.dTS2(k.moonVelocity * (386180000 / k.cmToMoon))
        + " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label L4                \n" +
        "posMass " +
        k.dTS2( - (java.lang.Math.cos(k.degreesToRadians(30)) * k.moonDistance))
        + " " +
        k.dTS2(java.lang.Math.sin(k.degreesToRadians(30)) * k.moonDistance
               - k.cmToEarth)
        + " \n" +
        "velMass " +
        k.dTS2( - (java.lang.Math.sin(k.degreesToRadians(30)) * 1.00621 *
                   k.moonVelocity))
        + " " +
        k.dTS2( - (java.lang.Math.cos(k.degreesToRadians(30)) * 1.00621 *
                   k.moonVelocity))
        + " \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label L5                \n" +
        "posMass " +
        k.dTS2(java.lang.Math.cos(k.degreesToRadians(30)) * k.moonDistance)
        + " " +
        k.dTS2(java.lang.Math.sin(k.degreesToRadians(30)) * k.moonDistance
               - k.cmToEarth)
        + " \n" +
        "velMass " +
        k.dTS2( - (java.lang.Math.sin(k.degreesToRadians(30)) * 1.00621 *
                   k.moonVelocity))
        + " " +
        k.dTS2(java.lang.Math.cos(k.degreesToRadians(30)) * 1.00621 *
               k.moonVelocity)
        + " \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[28] = "28. Non-spinning tether 1000 km";
    samples[28] =
        "/*    \n" +
        "  \n" +
        "  \n" +
        "  \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight 100000 \n" +
        "restLength    1000000  \n" +
        "stretch       1.005   \n" +
        "velBigEnd     -7470    0 \n" +
        "velSmallEnd   -6550    0  \n" +
        "bigEndDiameter   0.0075 \n" +
        "smallEndDiameter 0.0025 \n" +
        "bigKg       50000      \n" +
        "payloadKg     300       \n" +
        "slices        100      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        design.Display10FineRes +
        "totalTime 8000        \n" +
        "screenYScaleMeters 3000000 \n" +
        "screenOrigin T1.C              \n";

    sampleNames[29] = "29. Non-spinning tether 2000 km";
    samples[29] =
        "/*    \n \n \n \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight 100000 \n" +
        "restLength    2000000  \n" +
        "stretch       1.01   \n" +
        "velBigEnd     -7330    0 \n" +
        "velSmallEnd   -5530    0  \n" +
        "bigEndDiameter   0.0075 \n" +
        "smallEndDiameter 0.0025 \n" +
        "bigKg       50000      \n" +
        "payloadKg     300       \n" +
        "slices        100      \n" +
        "MakeTether  Ballast Payload             \n" +
        "                      \n" +
        design.Display10FineRes +
        "totalTime 8000        \n" +
        "screenYScaleMeters 6000000 \n" +
        "screenOrigin T1.C              \n";

    sampleNames[30] = "30. Reentry with Lift and heat";
    samples[30] =
        "/* Looking at reentry with different lift/drag ratios. \n" +
        " Note LD0 has high G load and others low G load. \n" +
        " LD4 skips along atmosphere like Sanger rocket-bomber. \n" +
        " For some real shapes and real L/D ratios see: \n" +
        "    http://www.islandone.org/APC/Aero/00.html */ \n" +
        "screenYScaleMeters   500000   \n" +
        "                        \n" +
        "deltaT           0.005  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     3000      \n" +
        "                       \n" +
        "massKg 295             \n" +
        "massHeight 100000       \n" +
        "massNoseAngle 10        \n" +
        "massVelAndAngle 5000 0  \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 1                 \n" +
        "tpsThermalConductivity 0.3 \n" +
        "tpsThickness          0.05  \n" +
        "tpsArea               10  \n" +
        "tpsThermalMass    100 \n" +
        "tpsSpecificHeat      520  \n" +
        "                       \n" +
        "label LD0.0             \n" +
        "massLiftOverDrag 0      \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD0.25             \n" +
        "massLiftOverDrag 0.25    \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD0.5             \n" +
        "massLiftOverDrag 0.5   \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD0.75              \n" +
        "massLiftOverDrag 0.75    \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD1               \n" +
        "massLiftOverDrag 1.0    \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD2               \n" +
        "massLiftOverDrag 2.0    \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD3               \n" +
        "massLiftOverDrag 3.0    \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label LD4               \n" +
        "massLiftOverDrag 4.0    \n" +
        "MakeSharpBody           \n" +
        "screenOrigin    LD2     \n";

    sampleNames[31] = "31. Barely rotating tether - toss & catch";
    samples[31] =
        " /* Joe Carroll barely rotating tether.  \n" +
        " At about 2 hours 15 min a released mass \n" +
        " gets close to the ballast or center of mass.  \n" +
        " A small ion-drive could go from ballast and get it. */ \n" +
        " density 909    \n" +
        " tensileGpa 2.9      \n" +
        " elasticity 0.03\n" +
        " \n" +
        " posBigEnd             0   6785000  \n" +
        " posSmallEnd  -290000 6785000 \n" +
        " velBigEnd       -7662  20 \n" +
        " velSmallEnd   -7675  -460 \n" +
        " restLength   290000  \n" +
        " bigEndDiameter   0.00519    \n" +
        " smallEndDiameter 0.00462  \n" +
        "  bigKg       180000     \n" +
        "  payloadKg      6000  \n" +
        "  slices        100  \n" +
        "  label T1    \n" +
        "  MakeTether  Ballast Payload  \n" +
        "  \n" +
        " AtTime 2777 toss 5000 from T1.Payload to M1 EndAtTime\n" +
        " showMassToTether true \n" +
        " showXY false \n" +
        " screenYScaleMeters 1000000  \n" +
        design.Display10FineRes +
        " totalTime   8400  \n" +
        " screenOrigin T1.C  \n";

    sampleNames[32] = "32. Reentry with different insulation";
    samples[32] =
        "/* Looking at a range of insulation values. \n" +
        " \n" +
        " */ \n" +
        "screenYScaleMeters   500000   \n" +
        "                        \n" +
        "deltaT           0.005  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     540      \n" +
        "                       \n" +
        "massKg 295             \n" +
        "massHeight 100000       \n" +
        "massNoseAngle 10        \n" +
        "massVelAndAngle 5000 0  \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 1                 \n" +
        "tpsThermalConductivity 0.3 \n" +
        "tpsArea               10  \n" +
        "tpsThermalMass    100 \n" +
        "tpsSpecificHeat      520  \n" +
        "massLiftOverDrag 0.4   \n" +
        "                       \n" +
        "label 1cm             \n" +
        "tpsThickness          0.01  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 2cm             \n" +
        "tpsThickness          0.02  \n" +
        "MakeSharpBody             \n" +
        "                        \n" +
        "label 3cm             \n" +
        "tpsThickness          0.03  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 4cm              \n" +
        "tpsThickness          0.04  \n" +
        "MakeSharpBody              \n" +
        "                        \n" +
        "label 5cm               \n" +
        "tpsThickness          0.05  \n" +
        "MakeSharpBody              \n" +
        "screenOrigin    3cm   \n";

    sampleNames[33] = "33. Moon Tether";
    samples[33] =
        "/* Looking at tether around moon \n" +
        "  */ \n" +
        "                      \n" +

        design.Spectra2000 +
        "bigEndDiameter   0.01 \n" +
        "smallEndDiameter 0.005 \n" +
        "bigKg       50000     \n" +
        "payloadKg     550     \n" +
        "slices        100      \n" +
        "                       \n" +
        "label T1               \n" +
        "posBigEnd 0 " + k.dTS(k.cmToMoon + k.moonRadius + 100000) + "  \n" +
        "posSmallEnd 0 " + k.dTS(k.cmToMoon + k.moonRadius + 100) + "  \n" +
        "velBigEnd " + k.dTS( - (75 + k.orbitMoonSpeed(100000) + k.moonVelocity)) +
        " 0  \n" +
        "velSmallEnd " + k.dTS( -k.moonVelocity) + " 0 \n " +
        "restLength          99500  \n" +
        "MakeTether   Ballast Payload           \n" +
        "                     \n" +
        "MakeMoon             \n" +
        "deltaT .01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 800000        \n" +
        "screenYScaleMeters 800000 \n" +
        "screenOrigin T1.C          \n" +
        " /* screenYScaleMeters 1000000000 \n" +
        "screenOrigin Earth        */   \n";

    sampleNames[34] = "34. Moon orbits";
    samples[34] =
        "/* Looking at how masses orbit the moon.  \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   8000000   \n" +
        "screenOrigin    Moon    \n" +
        "                        \n" +
        "deltaT           0.01   \n" +
        "timePerDisplay  100       \n" +
        "totalTime     20000     \n" +
        "massKg 1000             \n" +
        "                        \n" +
        "label m1                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 100000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(100000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m2                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 200000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(200000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m3                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 300000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(300000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m4                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 400000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(400000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m5                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 500000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(500000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m6                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 600000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(600000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m7                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 700000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(700000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label m8                \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 800000) + "  \n" +
        "massVelAndAngle " + k.dTS(k.orbitMoonSpeed(800000) + k.moonVelocity) +
        " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[35] = "35. Design point - Automatic Taper";
    samples[35] =
        design.LEOtether +
        design.LEOEDT4T +
        design.Display5FineRes +
        "totalTime 8000        \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T4T.C             \n";

    sampleNames[36] = "36. Geo-stationary Tether - smaller";
    samples[36] =
        "/*  We are also using some imaginary material \n" +
        "  that is 10 times stronger than Spectra 2000. \n" +
        " You can try it with a 3.51 Gpa and tether mass is huge. */ \n" +
        "density 970              \n " +
        "tensileGpa 35.1          \n " +
        "elasticity 0.03           \n" +
        " \n" +
        "label GEO-unobtainium    \n" +
        "smallEndHeight 50000\n" +
        "restLength    36840000 \n" +
        design.TaperAuto2 +
        "velBigEnd    -3205 0 \n" +
        "velSmallEnd   -470 0 \n " +
        "bigKg       5000000     \n " +
        "payloadKg     250     \n " +
        "slices        1000        \n " +
        "MakeTether  Ballast Payload               \n " +
        " \n" +
        " \n" +
        "screenYScaleMeters   120000000 \n" +
        "screenOrigin    Earth     \n " +
        " \n" +
        "deltaT           0.01    \n " +
        "timePerDisplay  1      \n " +
        "totalTime     900     \n ";

    sampleNames[37] = "37. Taper where ballast equals payload";
    samples[37] =
        "/* These samples/demos/examples are also tests.  \n" +
        " An automatic taper with 100 Kg at each end should \n" +
        " have about the same diameter at each end.  Also, \n" +
        " when simulation starts springs should be about equal. \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight 100000 \n" +
        "restLength    600000  \n" +
        design.TaperAuto2 +
        "velBigEnd     -10340    0 \n" +
        "velSmallEnd   -5000    0  \n" +
        "bigKg       100      \n" +
        "payloadKg     100       \n" +
        "slices        601      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        "deltaT 0.05           \n" +
        "timePerDisplay 10     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T1.C              \n";

    sampleNames[38] = "38. Multiple toss to GEO";
    samples[38] =
        "/* Tossing at different times to find toss  \n" +
        " time to go to GTO.   \n" +
        "  */          \n" +
        design.LEOtether +
        "AtTime 550 toss 600 from T4T.Payload to M550 EndAtTime\n" +
        "AtTime 555 toss 700 from T4T.Payload to M555 EndAtTime\n" +
        "AtTime 558 toss 700 from T4T.Payload to M558 EndAtTime\n" +
        "AtTime 560 toss 700 from T4T.Payload to M560 EndAtTime\n" +
        "AtTime 565 toss 700 from T4T.Payload to M565 EndAtTime\n" +
        "AtTime 570 toss 600 from T4T.Payload to M570 EndAtTime\n" +
        "             \n" +
        "label GEO1    \n" +
        "massKg 3000  \n" +
        /*     "massHeight 35785000  \n" +
             "massCircularOrbit  \n" +  */
        "posMass     -36514229 -21081500 \n" +
        "velMass     1537.15     -2662.4 \n" +
        "MakeMass  \n" +
        "                      \n" +
        "deltaT 0.05 \n" +
        "timePerDisplay 5 \n" +
        "totalTime 86400          \n" +
        "screenYScaleMeters 3000000    \n" +
        "screenOrigin T4T.C              \n" +
        "AtTime 700 deltaT 0.05 EndAtTime \n" +
        "AtTime 700 timePerDisplay 20 EndAtTime \n" +
        "AtTime 800 panNewmassTime M560 100 EndAtTime \n" +
        "AtTime 800 zoomNewscaleTime 90000000 1000 EndAtTime \n" +
        "AtTime 901 remove T4T EndAtTime \n" +
        "AtTime 901.1 remove Ballast EndAtTime \n" +
        "AtTime 901.2 remove Payload EndAtTime \n" +
        "AtTime 902 deltaT 0.25 EndAtTime \n" +
        "AtTime 10000 panNewmassTime GEO1 800 EndAtTime \n" +
        "AtTime 10000 zoomNewscaleTime 9000000 2000 EndAtTime \n";

    sampleNames[39] = "39. Build for Heavy fly light ";
    samples[39] =
        design.LEO137B4T +
        design.LEO4TlowBtoss +
        design.Display5FineRes +
        "totalTime 8000        \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T4T.C              \n";

    sampleNames[40] = "40. Moon flyby 4 ";
    samples[40] =
        "/*  */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .5      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     5000000  \n" +
        "posMass     39019369 -15975230   \n" +
        "massKg 1000             \n" +
        "label m1                       \n" +
        "massVelAndAngle 4200 112.265 \n" +
        "MakeMass                \n" +
        "label m2                       \n" +
        "massVelAndAngle 4220 112.265 \n" +
        "MakeMass                \n" +
        "label m3                        \n" +
        "massVelAndAngle 4240 112.265  \n" +
        "MakeMass                \n" +
        "label m4                        \n" +
        "massVelAndAngle 4260 112.265 \n" +
        "MakeMass                \n" +
        "label m5                        \n" +
        "massVelAndAngle 4280 112.265  \n" +
        "MakeMass                \n" +
        "label m6                         \n" +
        "massVelAndAngle 4300 112.265 \n" +
        "MakeMass                \n" +
        "label m7                         \n" +
        "massVelAndAngle 4320 112.265  \n" +
        "MakeMass                \n" +
        "label m8                         \n" +
        "massVelAndAngle 4340 112.265 \n" +
        "MakeMass                \n" +
        "                        \n" +
        "MakeMoon                \n";

    sampleNames[41] = "41. GEO to Moon Safe Return Orbit ";
    samples[41] =
        "/* Similar to Apollo safe return orbit \n" +
        " but starting in GEO. */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .5      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     5000000  \n" +
        // 0     "posMass     42162998 0   \n" +
        // 5 low   "posMass     42002555 -3674747   \n" +
        // 10.1 high "posMass     41501632 -7430123   \n" +
        // 8 next since 10 seemed closer
        "posMass     41752670.6 -5867955   \n" +
        "massKg 1000             \n" +
        "label m1                       \n" +
        "spaceVelAndAngle 4169 82 \n" +
        "MakeMass                \n" +
        "label m2                       \n" +
        "spaceVelAndAngle 4168.5  82\n" +
        "MakeMass                \n" +
        "label m3                        \n" +
        "spaceVelAndAngle 4168  82  \n" +
        "MakeMass                \n" +
        "label m4                        \n" +
        "spaceVelAndAngle 4167.5  82\n" +
        "MakeMass                \n" +
        "label m5                        \n" +
        "spaceVelAndAngle 4167  82  \n" +
        "MakeMass                \n" +
        "label m6                         \n" +
        "spaceVelAndAngle 4166.5  82 \n" +
        "MakeMass                \n" +
        "label m7                         \n" +
        "spaceVelAndAngle 4166  82  \n" +
        "MakeMass                \n" +
        "label m8                         \n" +
        "spaceVelAndAngle 4165.5  82 \n" +
        "MakeMass                \n" +
        "label GEO1              \n" +
        "massHeight 35785000     \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "showMassToMoon true     \n" +
        "MakeMoon                \n";

    sampleNames[42] = "42. GEO to Moon Safe Return Orbit ";
    samples[42] =
        "/* Similar to Apollo safe return orbit \n" +
        " but starting in GEO. */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .5      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     5000000  \n" +
        "posMass     41501632 -7430123   \n" +
        "massKg 1000             \n" +
        "label m1                       \n" +
        "spaceVelAndAngle 4183 79.85 \n" +
        "MakeMass                \n" +
        "label m2                       \n" +
        "spaceVelAndAngle 4182.5 79.85 \n" +
        "MakeMass                \n" +
        "label m3                        \n" +
        "spaceVelAndAngle 4182 79.85  \n" +
        "MakeMass                \n" +
        "label m4                        \n" +
        "spaceVelAndAngle 4181.5 79.85 \n" +
        "MakeMass                \n" +
        "label m5                        \n" +
        "spaceVelAndAngle 4181 79.85  \n" +
        "MakeMass                \n" +
        "label m6                         \n" +
        "spaceVelAndAngle 4180.5 79.85 \n" +
        "MakeMass                \n" +
        "label m7                         \n" +
        "spaceVelAndAngle 4180 79.85  \n" +
        "MakeMass                \n" +
        "label m8                         \n" +
        "spaceVelAndAngle 4179.5 79.85 \n" +
        "MakeMass                \n" +
        "label GEO1              \n" +
        "massHeight 35785000     \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "showMassToMoon true     \n" +
        "MakeMoon                \n";

    sampleNames[43] = "43. Projectile 0km with rotating air";
    samples[43] =
        "/* Look at air resistance \n" +
        " on some projectiles starting \n" +
        " at ground level.       */    \n" +
        " \n" +
        "screenYScaleMeters   600000   \n" +
        "rotatingAir true         \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     200      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "massHeight 000       \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 0.2               \n" +
        "                         \n" +
        "label M1                 \n" +
        "surfaceVelAndAngle 4500 55  \n" +
        "MakeMass                 \n" +
        "                         \n" +
        "label M2                 \n" +
        "surfaceVelAndAngle 4500 55  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M3                 \n" +
        "surfaceVelAndAngle 4500 50  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M4                 \n" +
        "surfaceVelAndAngle 4500 45  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M5                 \n" +
        "surfaceVelAndAngle 4500 40  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M6                 \n" +
        "surfaceVelAndAngle 4500 35  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M7                 \n" +
        "surfaceVelAndAngle 4500 30  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M8                 \n" +
        "surfaceVelAndAngle 4500 25  \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label M9                 \n" +
        "surfaceVelAndAngle 4500 20  \n" +
        "MakeMass                \n" +
        "screenOrigin    M4   \n";

    sampleNames[44] = "44. Hotels at GEO - 200 km 1/10th G";
    samples[44] =
        "/* Two hotels at ends of 200 km tether.   \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight " + (35785000 - 100000) + " \n" +
        "restLength    200000  \n" +
        design.TaperAuto2 +
        "velBigEnd     " + ( -3074 - 320) + " 0 \n" +
        "velSmallEnd   " + ( -3074 + 320) + " 0 \n" +
        "bigKg        200000      \n" +
        "payloadKg    200000       \n" +
        "slices        100      \n" +
        "MakeTether    Hilton Marriott           \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 1000000 \n" +
        "screenOrigin T1.C              \n";

    sampleNames[45] = "45. Hotels at GEO - 200 km 1/6th G";
    samples[45] =
        "/* Two hotels at ends of 200 km tether.   \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label Hotels         \n" +
        "smallEndHeight " + (35785000 - 100000) + " \n" +
        "restLength    200000  \n" +
        design.TaperAuto2 +
        "velBigEnd     " + ( -3074 - 412) + " 0 \n" +
        "velSmallEnd   " + ( -3074 + 412) + " 0 \n" +
        "bigKg        1000000      \n" +
        "payloadKg    1000000       \n" +
        "slices        100      \n" +
        "MakeTether   Hilton Marriott           \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 500000 \n" +
        "screenOrigin Hotels.C              \n";

    sampleNames[46] = "46. Hotels at GEO - 20 km 1/6th G";
    samples[46] =
        "/* Two hotels at ends of 20 km tether.   \n" +
        "  Each is 1 mil Kg and sping for 1/6 G. */          \n" +
        design.Spectra2000 +
        "label Hotels         \n" +
        "smallEndHeight " + (35785000 - 10000) + " \n" +
        "restLength    20000  \n" +
        design.TaperAuto2 +
        "velBigEnd     " + ( -3074 - 131) + " 0 \n" +
        "velSmallEnd   " + ( -3074 + 131) + " 0 \n" +
        "bigKg        1000000      \n" +
        "payloadKg    1000000       \n" +
        "slices        100      \n" +
        "MakeTether   Hilton Marriott            \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 50000 \n" +
        "screenOrigin Hotels.C              \n";

    sampleNames[47] = "47. Hotels with pickup tether ";
    samples[47] =
        "/* Treat 2 hotels as single 400,000 Kg ballast at CM.   \n" +
        "  */          \n" +
        design.Spectra2000 +
        "label Hotels         \n" +
        "smallEndHeight " + (35785000 - 100000) + " \n" +
        "restLength    100000  \n" +
        design.TaperAuto2 +
        "velBigEnd     " + ( -3074 - 655) + " 0 \n" +
        "velSmallEnd   " + ( -3074 + 655) + " 0 \n" +
        "bigKg        2000000      \n" +
        "payloadKg    10000       \n" +
        "slices        100      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 500000 \n" +
        "screenOrigin Hotels.C     \n";

    sampleNames[48] = "48. Hastol - Rotovator ";
    samples[48] =
        "/* The mass is close but my   \n" +
        "  diameters are bigger than in paper. */ \n" +
        design.Spectra2000 +
        "label Hastol         \n" +
        "smallEndHeight  100000 \n" +
        "restLength    600000  \n" +
        design.TaperAuto2 +
        "velBigEnd     " + ( -7550 - 560) + " 0 \n" +
        "velSmallEnd   " + ( -7550 + 3400) + " 0 \n" +
        "bigKg        1650000      \n" +
        "payloadKg    20000       \n" +
        "slices        100      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 1000000 \n" +
        "screenOrigin Hastol.C      \n";

    sampleNames[49] = "49. SpaceShipOne  ";
    samples[49] =
        "/* We simulate bent mode on way down         \n" +
        " as parachute opening.  Trying to do:   \n" +
        " http://www.scaled.com/projects/tierone/     \n" +
        " Don't have any real weights, drag, ISP      \n" +
        " just guessing to get in ballpark for now.   \n" +
        " http://www.aviationnow.com/avnow/news/channel_awst_story.jsp?id=news/04213top.xml \n" +
        " 3-4 Gs     65 sec burn   \n" +
        " Over 4 Gs for 20 sec - peak a bit over 5 Gs \n" +
        " Wing area 160 sq feet  (wingspan 16.4 feet) \n" +
        " L/D  0.7  \n" +
        " Ballistic coefficient of 12 psf \n " +
        " Stagnation temp at peak 1,100 F \n " +
        " http://www.popsci.com/popsci/aviation/article/0,12543,444888,00.html \n " +
        "  84 degree angle at burnout \n" +
        " */ \n" +
        "screenYScaleMeters   300000   \n" +
        "                         \n" +
        "deltaT           0.001   \n" +
        "timePerDisplay  1        \n" +
        "totalTime     600        \n" +
        "                         \n" +
        "label SS1                \n" +
        "massKg 0.1               \n" +
        "massHeight 15000         \n" +
        "coefficientOfDrag 0.5    \n" +
        "dragArea 2               \n" +
        "massLiftOverDrag 0.7   /* really no lift on way up so some error here */  \n" +
        "surfaceVelAndAngle 150 89.8 /* PS says 84 at burnout */  \n" +
        "MakeMass                 \n" +
        "                         \n" +
        "blackBodyArea    0.3     \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "massNoseAngle 10         \n" +
        "/* MakeSharpBody    */        \n" +
        "                         \n" +
        "ISP 300                  \n" +
        "rocketKg 2700            \n" +
        "fuelFraction 0.425       \n" +
        "thrustNewtons 51000      \n" +
        "stageStaysAttached true  \n" +
        "rocketParachuteOpenHeight 100000 \n" +
        "rocketParachuteArea   18     \n" +
        "rocketParachuteCd     0.5    \n" +
        "MakeRocket                   \n" +
        "screenOrigin   SS1      \n";

    sampleNames[50] = "50. Space Shuttle Reentry";
    samples[50] =
        "/* Have real info on Space Shuttle \n" +
        " so we can see if simulator gets   \n" +
        " similar results.  */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.0005  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    4000       \n" +
        "                        \n" +
        "massHeight 200000       \n" +
        "massKg 207000           \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 231               \n" + // 360 sq meters at 40 degrees
        "massLiftOverDrag 1       \n" +
        "blackBodyMass        50     \n" +
        "blackBodyArea         5     \n" +
        "blackBodySpecificHeat 500   \n" +
        "label SS                 \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody             \n" +
        "screenOrigin    SS   \n";

    sampleNames[51] = "51. 7.7 km/sec Beryllium heatsink ";
    samples[51] =
        "/* dif ballistic coefficients */ \n" +
        "/* Takes about 25% of mass for heatsink */ \n" +
        "/* */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    4000       \n" +
        "                        \n" +
        "massHeight 200000       \n" +
        "massKg 4000           \n" +
        "coefficientOfDrag 1             \n" +
        "massLiftOverDrag 0.4       \n" +
        "blackBodyMass        1000     \n" +
        "blackBodySpecificHeat 1885   \n" + // Beryllium
        "blackBodyEmissivity 0.9      \n" +
        "                        \n" +
        "label 6sqm               \n" +
        "dragArea 6                  \n" +
        "blackBodyArea         6     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                        \n" +
        "label 12sqm              \n" +
        "dragArea 12                 \n" +
        "blackBodyArea         12     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 24sqm              \n" +
        "dragArea 24                 \n" +
        "blackBodyArea         24     \n" +
        "massVelAndAngle 7700 0       \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "screenOrigin    12sqm   \n";

    sampleNames[52] = "52. 5 km/sec Beryllium heatsink   ";
    samples[52] =
        "/* dif ballistic coefficients  */ \n" +
        "/* Takes about 5% of mass for heatsink */ \n" +
        "/* */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    800       \n" +
        "                        \n" +
        "massHeight 100000       \n" +
        "massKg 4000           \n" +
        "coefficientOfDrag 1              \n" +
        "massLiftOverDrag 0.4       \n" +
        "blackBodyMass        200     \n" +
        "blackBodySpecificHeat 1885   \n" + //  Beryllium
        "blackBodyEmissivity 0.9      \n" +
        "                        \n" +
        "label 6sqm                  \n" +
        "dragArea 6                     \n" +
        "blackBodyArea         6     \n" +
        "massVelAndAngle 5000 0      \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 12.57sqm              \n" +
        "dragArea 12.57                 \n" +
        "blackBodyArea         12.57 \n" +
        "massVelAndAngle 5000 0      \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 25sqm                 \n" +
        "dragArea 25                    \n" +
        "blackBodyArea         25    \n" +
        "massVelAndAngle 5000 0      \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "screenOrigin    6sqm   \n";

    sampleNames[53] = "53. GEO to L1 ";
    samples[53] =
        "/* Going from GEO orbit to L1 inside Moon's orbit \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   800000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           .5      \n" +
        "timePerDisplay  1000   \n" +
        "totalTime     5000000  \n" +
        "posMass     41501632 -7430123   \n" +
        "massKg 1000             \n" +
        "label m1                       \n" +
        "spaceVelAndAngle 4110 73.5 \n" +
        "MakeMass                \n" +
        "label m2                       \n" +
        "spaceVelAndAngle 4115 74 \n" +
        "MakeMass                \n" +
        "label m3                        \n" +
        "spaceVelAndAngle 4120 74.5  \n" +
        "MakeMass                \n" +
        "label m4                        \n" +
        "spaceVelAndAngle 4125 75 \n" +
        "MakeMass                \n" +
        "label m5                        \n" +
        "spaceVelAndAngle 4130 75.5 \n" +
        "MakeMass                \n" +
        "label m6                         \n" +
        "spaceVelAndAngle 4135 76 \n" +
        "MakeMass                \n" +
        "label m7                         \n" +
        "spaceVelAndAngle 4140 76.5  \n" +
        "MakeMass                \n" +
        "label m8                         \n" +
        "spaceVelAndAngle 4145 77 \n" +
        "MakeMass                \n" +
        "label GEO1              \n" +
        "massHeight 35785000     \n" +
        "massCircularOrbit       \n" +
        "MakeMass                \n" +
        "showMassToMoon true     \n" +
        "MakeMoon                \n";

    sampleNames[54] = "54. Tether LEO-GTO  ";
    samples[54] =
        "/* Checking that we get the same results as in \n" +
        " http://www.tethers.com/papers/JPC00LEOGTO.pdf  \n" +
        "  I get about twice the radius in the paper.    \n" +
        "  Checked with author and I am correct. */ \n" +
        design.Spectra2000 +
        "label T1             \n" +
        "smallEndHeight  100000 \n" +
        "restLength    100000  \n" +
        "taperType    Automatic \n" +
        "safetyFactor 3.5        \n" +
        "velBigEnd     " + ( -7550 - 420) + " 0 \n" +
        "velSmallEnd   " + ( -7550 + 1200) + " 0 \n" +
        "bigKg        15004      \n" +
        "payloadKg    3150       \n" +
        "slices        100      \n" +
        "MakeTether   Ballast Payload            \n" +
        "                      \n" +
        "deltaT 0.01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 8000        \n" +
        "screenYScaleMeters 1000000 \n" +
        "screenOrigin T1.C      \n";

    sampleNames[55] = "55. 7.7 km/sec and fixed 1 sq-meter nose";
    samples[55] =
        "/*  */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05  \n" +
        "timePerDisplay  10       \n" +
        "totalTime    4000       \n" +
        "                        \n" +
        "massHeight 200000       \n" +
        "massKg 10000           \n" +
        "coefficientOfDrag 0.5              \n" +
        "massLiftOverDrag 0.5       \n" +
        "blackBodyMass        0     \n" +
        "blackBodySpecificHeat 0   \n" +
        "blackBodyStantonNumber 0 \n" +
        "                        \n" +
        "label 5sqm               \n" +
        "dragArea 5                  \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 10sqm              \n" +
        "dragArea 10                 \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 20sqm              \n" +
        "dragArea 20                 \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 40sqm              \n" +
        "dragArea 40                 \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 80sqm              \n" +
        "dragArea 80                 \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 160sqm             \n" +
        "dragArea 160                \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 320sqm              \n" +
        "dragArea 320                 \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "                        \n" +
        "label 640sqm             \n" +
        "dragArea 640                \n" +
        "blackBodyArea         1     \n" +
        "massVelAndAngle 7700 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody               \n" +
        "screenOrigin    80sqm  \n";

    sampleNames[56] = "56. 5 km/sec and fixed 1 sq-meter nose";
    samples[56] =
        "/*  */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05   \n" +
        "timePerDisplay  1       \n" +
        "totalTime    800        \n" +
        "                         \n" +
        "massHeight 150000        \n" +
        "massKg 10000             \n" +
        "coefficientOfDrag 0.5    \n" +
        "massLiftOverDrag 0.5     \n" +
        "                         \n" +
        "label 5sqm               \n" +
        "dragArea 5               \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 10sqm              \n" +
        "dragArea 10              \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 20sqm              \n" +
        "dragArea 20              \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 40sqm              \n" +
        "dragArea 40              \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 80sqm              \n" +
        "dragArea 80              \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 160sqm             \n" +
        "dragArea 160             \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 320sqm             \n" +
        "dragArea 320             \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                         \n" +
        "label 640sqm             \n" +
        "dragArea 640             \n" +
        "blackBodyArea         1  \n" +
        "massVelAndAngle 5000 0   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "screenOrigin    10sqm   \n";

    double payload, fuel, rocket, total;
    sampleNames[57] = "57. 4 tons from 0km to 100km & 5 km/s";
    payload = 4000;
    fuel = 42700;
    rocket = 0.15 * (fuel + payload);
    total = payload + fuel + rocket;
    samples[57] =
        "/* Assume dry rocket is 15% of fuel+payload \n" +
        "  Assume payload is 4000 Kg */ \n" +
        " \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     300      \n" +
        "                       \n" +
        "massKg " + k.dTS2(payload) + " \n" +
        "dryKg " + k.dTS2(rocket) + " \n" +
        "fuelKg " + k.dTS2(fuel) + " \n" +
        "massHeight 0           \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 8             \n" +
        "stageStaysAttached false  \n" +
        "                         \n" +
        "ISP 360                  \n" +
        "ispDegradedAtOneAtm 0.12 \n" +
        "thrustNewtons " + k.dTS2(19.02 * total) + " \n" +
        "                         \n" +
        "label A78.41              \n" +
        "surfaceVelAndAngle 150 78.41  \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A78.43              \n" +
        "surfaceVelAndAngle 150 78.43  \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A78.45              \n" +
        "surfaceVelAndAngle 150 78.45 \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A78.47              \n" +
        "surfaceVelAndAngle 150 78.47 \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A78.49              \n" +
        "surfaceVelAndAngle 150 78.49 \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "screenOrigin    A78.47   \n";

    sampleNames[58] = "58. 4 tons from 5.9km to 100km & 5 km/s";
    payload = 4000;
    fuel = 43600;
    rocket = 0.15 * (fuel + payload);
    total = payload + fuel + rocket;
    samples[58] =
        "/* Assume dry rocket is 15% of fuel+payload \n" +
        "  Assume payload is 4000 Kg \n" +
        "  This is to simulate mountain top launch. \n" +
        "  Altitude 16,000 feet = 4877 meters */ \n" +
        " \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     300      \n" +
        "                       \n" +
        "massKg " + k.dTS(payload) + " \n" +
        "dryKg " + k.dTS2(rocket) + " \n" +
        "fuelKg " + k.dTS2(fuel) + " \n" +
        "massHeight 4877           \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 8             \n" +
        "                         \n" +
        "ISP 360                  \n" +
        "ispDegradedAtOneAtm 0.12 \n" +
        "thrustNewtons " + k.dTS2(20.6763 * total) + " \n" +
        "stageStaysAttached false  \n" +
        "                         \n" +
        "label A88.362             \n" +
        "surfaceVelAndAngle 10 88.362  \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A88.364              \n" +
        "surfaceVelAndAngle 10 88.364\n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A88.366             \n" +
        "surfaceVelAndAngle 10 88.366\n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A88.368              \n" +
        "surfaceVelAndAngle 10 88.368\n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "                         \n" +
        "label A88.37              \n" +
        "surfaceVelAndAngle 10 88.37 \n" +
        "MakeMass                 \n" +
        "MakeRocket               \n" +
        "screenOrigin    A88.366   \n";

    sampleNames[59] = "59. 4 tons from 15km to 100km & 5 km/s";
    payload = 4000;
    fuel = 28100;
    rocket = 0.15 * (fuel + payload);
    total = payload + fuel + rocket;
    samples[59] =
        "/* Assume dry rocket is 15% of fuel+payload \n" +
        "  Assume payload is 4000 Kg \n" +
        "  To simulate an air launch at 50,000 feet */ \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     300      \n" +
        " \n" +
        "massKg " + k.dTS2(payload) + " \n" +
        "fuelKg " + k.dTS(fuel) + " \n" +
        "dryKg " + k.dTS(rocket) + " \n" +
        "massHeight 15240        \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 8                 \n" +
        " \n" +
        "ISP 360  \n" +
        "ispDegradedAtOneAtm 0.12 \n" +
        "thrustNewtons " + k.dTS2(24.1848 * total) + " \n" +
        "stageStaysAttached false  \n" +
        " \n" +
        "label A62.4              \n" +
        "surfaceVelAndAngle 150 62.4   \n" +
        "MakeMass                 \n" +
        "MakeRocket  \n" +
        "  \n" +
        "label A62.3              \n" +
        "surfaceVelAndAngle 150 62.3   \n" +
        "MakeMass                 \n" +
        "MakeRocket \n" +
        "  \n" +
        "label A62.2              \n" +
        "surfaceVelAndAngle 150 62.2   \n" +
        "MakeMass                 \n" +
        "MakeRocket \n" +
        "  \n" +
        "label A62.1               \n" +
        "surfaceVelAndAngle 150 62.1  \n" +
        "MakeMass                 \n" +
        "MakeRocket \n" +
        "  \n" +
        "label A62             \n" +
        "surfaceVelAndAngle 150 62   \n" +
        "MakeMass                 \n" +
        "MakeRocket  \n" +
        "screenOrigin    A62.2   \n";

    sampleNames[60] = "60. 4 ton toss to ballast transfer orbit";
    samples[60] =
        design.LEOEtether +
        " \n" +
        "AtTime 433 toss 4000 from T4T.Payload to M EndAtTime\n" +
        " \n" +
        design.Display10Norm +
        "totalTime 12000   \n" +
        "screenYScaleMeters 4000000 \n" +
        "screenOrigin T4T.C \n \n" +
        "showXY false  rotatingAir false \n" +
        "logPerigee true\n" +
        "logXY   true\n" +
        "/* gives   \n" +
        "M.logXY 440 velMass -8482 -1801 alt 729300 posMass -2913031 6482898\n" +
        "M.logXY 450 velMass -8449 -1873 alt 747747.1 posMass -2997688 6464529\n" +
        "\n" +
        "M.APOGEE 5430 velMass 3753 -1397 alt 8664613.41 posMass -5246872 -14097892\n" +
        "\n" +
        "M.PERIGEE 11000 velMass -8632 3260 alt 150058.2 posMass 2318422 6102496\n" +
        "M.logXY  11010 velMass -8664 3172 alt 150061.6 posMass 2231939 6134659\n" +
        "\n" +
        "Hall testing uses \n" +
        "velMass -8482 -1801 posMass -2913031 6482898\n" +
        "*/";

    sampleNames[61] = "61. Raising perigee with Hall thruster";
    samples[61] =
        design.TossLEO4Thdr +
        "/* simulated Hall thruster - raise Perigee 20 km, with minimal thrust */ \n" +
        "/* This gives difference in Apogee & Perigee due to 1 kg of fuel, \n" +
        "set of 8 hall thrusters  */ \n" +
        design.TossLEO4THall +
        "massKg 4000 \n" +
        "label M		MakeMass \n \n" +
        "screenYScaleMeters   16000000    \n" +
        "screenOrigin    R    \n" +
        " \n" +
        design.Display10FineRes +
        "totalTime     10840     /* near  perigee */  \n" +
        design.logXY_Perigee +
        design.TossLEO4Tlog;

    sampleNames[62] = "62. Lower apogee - aerobreaking/Hall-thruster";
    samples[62] = design.TossLEO4Thdr +
        "label R   massKg 3978      \n" +
        "stageStaysAttached true   \n" +
        "dryKg 1.25  fuelKg 1 	\n" +
        "dragArea   328 /* 64 KW  */ coefficientOfDrag 2.0 \n" +
        design.Hall8Unit +
        "velMass -8482 -1801 posMass -2913031 6482898 \n" +
        "timeOfIgnition  476640 " +
        "MakeMass MakeRocket  \n \n" +
        "dryKg 1.3 fuelKg 0.7 \n" +
        "timeOfIgnition 469860 MakeRocket \n" +
        "timeOfIgnition 462900 MakeRocket \n" +
        "timeOfIgnition 456030 MakeRocket \n" +
        "timeOfIgnition 449130 MakeRocket \n" +
        "timeOfIgnition 442290 MakeRocket \n" +
        "timeOfIgnition 435420 MakeRocket \n" +
        "timeOfIgnition 428550 MakeRocket \n" +
        "timeOfIgnition 421710 MakeRocket \n" +
        "timeOfIgnition 414870 MakeRocket \n" +
        "retroThrust true   dryKg 0   fuelKg 0.75 \n" +
        "timeOfIgnition 14148 " + "MakeRocket \n" +
        "fuelKg 1 \n" +
        "timeOfIgnition 2530 " + "MakeRocket \n" +
        " \n" +
        "screenYScaleMeters   30000000   \n" +
        "screenOrigin    Earth  \n" +
        design.Display10FineRes +
        "totalTime     " + k.dTS(6.0 * 86400.0) +
        "     /* 6 days to lower apogee near rendezvous apogee of 2570 */  \n" +
        "  \n" +
        "AtTime 162000 screenYScaleMeters 26000000 EndAtTime \n " +
        "AtTime 240000 screenYScaleMeters 24000000 EndAtTime \n " +
        "AtTime 305000 screenYScaleMeters 22000000 EndAtTime \n " +
        "AtTime 366000 screenYScaleMeters 20000000 EndAtTime \n " +
        "AtTime 418000 screenYScaleMeters 18000000 EndAtTime \n " +
        design.logXY_Perigee +
        design.TossLEO4Tlog;

    sampleNames[63] = "63. Decay of Circular Orbits";
    samples[63] =
        "/* Comparing orbital decay for this simulator to that in books.\n" +
        " Graph we have is Fig 28a from page 252 of Design Guide to Orbital Flight \n" +
        " Also graph in Fig II C-24 on page II-52 of Space Planners Guide \n " +
        " 16.1 lbs/sq-foot  for w/CD*A                 \n" +
        " 7.302838 Kg/0.092903 sq-meters  for w/CD*A   \n" +
        " 78.61  Kg/sq-meter  for w/CD*A               \n" +
        " */\n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           1  \n" +
        "timePerDisplay  1000      \n" +
        "totalTime     86400000      \n" + // 1000 years
        "                       \n" +
        "massKg 78.61            \n" +
        "coefficientOfDrag 1               \n" +
        "dragArea 1                \n" +
        "                       \n" +
        "label 100miles         \n" +
        "massHeight " + k.dTS(100 * 1.6093472 * 1000) + " \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 150miles         \n" +
        "massHeight " + k.dTS(150 * 1.6093472 * 1000) + "       \n" +
        "massEccentricity  0      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 200miles         \n" +
        "massHeight " + k.dTS(200 * 1.6093472 * 1000) + "       \n" +
        "massEccentricity  0      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 250miles         \n" +
        "massHeight " + k.dTS(250 * 1.6093472 * 1000) + "       \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 300miles         \n" +
        "massHeight " + k.dTS(300 * 1.6093472 * 1000) + "       \n" +
        "massEccentricity  0      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 350miles         \n" +
        "massHeight " + k.dTS(350 * 1.6093472 * 1000) + " \n" +
        "massEccentricity  0      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 400miles         \n" +
        "massHeight " + k.dTS(400 * 1.6093472 * 1000) + " \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n";

    sampleNames[64] = "64. Decay of Eccentric 160 km Orbits";
    samples[64] =
        "/* Comparing orbital decay for this simulator to that in books.\n" +
        " Graph we have is Fig 28a from page 252 of Design Guide to Orbital Flight \n" +
        " Also graph in Fig II C-24 on page II-52 of Space Planners Guide \n " +
        " 16.1 lbs/sq-foot  for w/CD*A                 \n" +
        " 7.302838 Kg/0.092903 sq-meters  for w/CD*A   \n" +
        " 78.61  Kg/sq-meter  for w/CD*A               \n" +
        " */\n" +
        "screenYScaleMeters   20000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           1  \n" +
        "timePerDisplay  1000      \n" +
        "totalTime     86400000      \n" + // 1000 years
        "                       \n" +
        "massKg 78.61            \n" +
        "coefficientOfDrag 1               \n" +
        "dragArea 1                \n" +
        "                       \n" +
        "label 0Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.005Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.005       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.01Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.01       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.02Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.02     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.03Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.03       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.04Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.04       \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.06Ecc         \n" +
        "massHeight 160934      \n" +
        "massEccentricity 0.06      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.08Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.08      \n" +
        "MakeMass                \n";

    sampleNames[65] = "65. Decay of very Eccentric -  160 km Orbits";
    samples[65] =
        "/* Comparing orbital decay for this simulator to that in books.\n" +
        " Graph we have is Fig 28a from page 252 of Design Guide to Orbital Flight \n" +
        " Also graph in Fig II C-24 on page II-52 of Space Planners Guide \n " +
        " 16.1 lbs/sq-foot  for w/CD*A                 \n" +
        " 7.302838 Kg/0.092903 sq-meters  for w/CD*A   \n" +
        " 78.61  Kg/sq-meter  for w/CD*A               \n" +
        " */\n" +
        "screenYScaleMeters   50000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           5  \n" +
        "timePerDisplay  10000      \n" +
        "totalTime     86400000      \n" + // 1000 years
        "                       \n" +
        "massKg 78.61            \n" +
        "coefficientOfDrag 1               \n" +
        "dragArea 1                \n" +
        "                       \n" +
        "label 0.1Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.10      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.15Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.15      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.2Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.2      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.3Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.3      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.4Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.4     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.5Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.5      \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 0.7Ecc         \n" +
        "massHeight 160934       \n" +
        "massEccentricity 0.7     \n" +
        "MakeMass                \n";

    sampleNames[66] = "66. Decay test SMAD data ";
    samples[66] =
        "/* From SMAD 3rd edition in last few pages.  \n" +
        "         \n" +
        "   Estimated Orbital Lifetimes in days \n" +
        " for 2 sample  mass/( Cd * Area)  \n" +
        "          50 Kg/m^2   200 kg/m^2 \n" +
        "            Solar      Solar     \n" +
        " Altitude  Min Max   Min Max \n" +
        " 100 km   .06  .06  .06  .06 \n" +
        "  \n" +
        " 150 km   .24  .18  .54  .48 \n" +
        "\n" +
        " 200 km  1.65  1.03  5.99  3.6\n" +
        " \n" +
        " 250 km  10.06 3.82  40.21  14.98*/ \n" +
        " */\n" +
        "screenYScaleMeters   16000000   \n" +
        "screenOrigin    Earth   \n" +
        "                        \n" +
        "deltaT           1  \n" +
        "timePerDisplay  1000      \n" +
        "totalTime     2000000      \n" +
        "                       \n" +

        "coefficientOfDrag 1               \n" +
        "dragArea 1                \n" +
        "                       \n" +
        "label 50Kg100km        \n" +
        "massKg 50              \n" +
        "massHeight  100000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 50Kg150km        \n" +
        "massKg 50              \n" +
        "massHeight  150000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 50Kg200km        \n" +
        "massKg 50              \n" +
        "massHeight  200000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 50Kg150km        \n" +
        "massKg 50              \n" +
        "massHeight  250000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 200Kg100km        \n" +
        "massKg 200              \n" +
        "massHeight  100000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 200Kg150km        \n" +
        "massKg 200             \n" +
        "massHeight  150000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 200Kg200km        \n" +
        "massKg 200              \n" +
        "massHeight  200000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label 200Kg250km        \n" +
        "massKg 200              \n" +
        "massHeight  250000      \n" +
        "massEccentricity  0     \n" +
        "MakeMass                \n" +
        "                         \n";

    sampleNames[67] = "67. Apollo reentry over/nominal/under  ";
    samples[67] =
        "/* Not working right yet.  */ \n" +
        "/* */ \n" +
        "/* */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    4000       \n" +

        "massHeight 100000           \n" +
        "massKg 4309                 \n" +
        "coefficientOfDrag .5        \n" +
        "massLiftOverDrag 0.5        \n" +
        "blackBodyMass        2500   \n" +
        "blackBodySpecificHeat 1885  \n" + // Beryllium
        "dragArea 8.3                \n" +
        "blackBodyArea         8.3   \n" +
        "                            \n" +
        "label overshoot             \n" +
        "massVelAndAngle 11300 -7.5  \n" +
        "MakeMass                    \n" +
        "MakeBluntBody               \n" +
        "                            \n" +
        "label nominal               \n" +
        "massVelAndAngle 11300 -8    \n" +
        "MakeMass                    \n" +
        "MakeBluntBody               \n" +
        "                            \n" +
        "label undershoot            \n" +
        "massVelAndAngle 11300 -8.5  \n" +
        "MakeMass                    \n" +
        "MakeBluntBody               \n" +
        "screenOrigin    nominal   \n";

    sampleNames[68] = "68. Meteors - Test data  ";
    samples[68] =
        "/* These test cases come from: */ \n" +
        "/*    http://www.science.gmu.edu/~edenniso/test_results_4.html  */ \n" +
        "/*    Note this works for first and  */ \n" +
        "/*      second set of 4 cases */ \n" +
        "/* They model conduction which we don't, */ \n" +
        "/*  so our heat of ablation is different. */ \n" +
        "/* But results agree reasonably well. */ \n" +
        "                        \n" +
        "screenYScaleMeters   500000   \n" +
        "screenOrigin    EarthTop  \n" +
        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay   0.01       \n" +
        " /* in 15 seconds we have either impacted */ \n" +
        " /*    or are subsonic for no big crater */ \n" +
        "totalTime        15      \n" +
        "                        \n" +
        "massHeight 80000         \n" +
        "massKg 100                \n" +
        "coefficientOfDrag 1                   \n" +
        "meteorDensity   2200       \n" +
        "                           \n" +
        "meteorJoulesPerKg 3000000 /* guessed to fit */   \n" +
        "                           \n" +
        "label 90deg20kps              \n" +
        "surfaceVelAndAngle 20000 -90   \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label 45deg20kps              \n" +
        "surfaceVelAndAngle 20000 -45   \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label 90deg40kps              \n" +
        "surfaceVelAndAngle 40000 -90   \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label 45deg40kps              \n" +
        "surfaceVelAndAngle 40000 -45   \n" +
        "MakeMeteor                  \n";

    sampleNames[69] = "69. Meteors - 20 kps - Range of sizes ";
    samples[69] =
        "/* Looking at wide range of meteor sizes.  */ \n" +
        "/* Can see how much hits the ground,   */ \n" +
        "/*   how much energy it has in megatons,  */ \n" +
        "/*   and what sized craters they would make. */ \n" +
        "                        \n" +
        "screenYScaleMeters   500000   \n" +
        "screenOrigin    EarthTop  \n" +
        "                        \n" +
        "deltaT           0.01  \n" +
        "timePerDisplay   0.01       \n" +
        " /* in 15 seconds we have either impacted */ \n" +
        " /*    or are subsonic for no big crater */ \n" +
        "totalTime        15      \n" +
        "                        \n" +
        "massHeight 90000          \n" +
        "coefficientOfDrag 1                   \n" +
        "meteorDensity   3500       \n" +
        "                           \n" +
        "meteorJoulesPerKg 3000000 /* From test data */   \n" +
        "surfaceVelAndAngle 20000 -45   \n" +
        "                           \n" +
        "label TenKg              \n" +
        "massKg 10                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label HundKg              \n" +
        "massKg 100                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label ThouKg              \n" +
        "massKg 1000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label MilKg              \n" +
        "massKg 1000000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label BilKg               \n" +
        "massKg 1000000000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label TrilKg               \n" +
        "massKg 1000000000000               \n" +
        "MakeMeteor                  \n";

    sampleNames[70] = "70. Meteors - 40 kps - Range of sizes ";
    samples[70] =
        "/* Looking at wide range of meteor sizes.  */ \n" +
        "/* Can see how much hits the ground,   */ \n" +
        "/*   how much energy it has in megatons,  */ \n" +
        "/*   and what sized craters they would make. */ \n" +
        "                        \n" +
        "screenYScaleMeters   500000   \n" +
        "screenOrigin    EarthTop  \n" +
        "                        \n" +
        "deltaT           0.01  \n" +
        "timePerDisplay   0.01       \n" +
        " /* in 15 seconds we have either impacted */ \n" +
        " /*    or are subsonic for no big crater */ \n" +
        "totalTime        15      \n" +
        "                        \n" +
        "massHeight 90000          \n" +
        "coefficientOfDrag 1                   \n" +
        "meteorDensity   3500       \n" +
        "                           \n" +
        "meteorJoulesPerKg 3000000 /* From test data */   \n" +
        "surfaceVelAndAngle 40000 -45   \n" +
        "                           \n" +
        "label TenKg              \n" +
        "massKg 10                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label HundKg              \n" +
        "massKg 100                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label ThouKg              \n" +
        "massKg 1000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label MilKg              \n" +
        "massKg 1000000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label BilKg               \n" +
        "massKg 1000000000                \n" +
        "MakeMeteor                  \n" +
        "                           \n" +
        "label TrilKg               \n" +
        "massKg 1000000000000               \n" +
        "MakeMeteor                  \n";

    sampleNames[71] = "71. Nitrous air-launch - high thrust";
    payload = 4000;
    fuel = 45000;
    rocket = 0.10 * fuel;
    total = payload + fuel + rocket;
    samples[71] =
        "/* Assume dry rocket is 10% of fuel \n" +
        "  Assume payload is 4000 Kg \n" +
        "  To simulate an air launch at 50,000 feet \n" +
        "  Assume 4 meter diameter (12.57 sq meter area) \n" +
        "  Using ISP of 300 for nitrous-oxide/propane  */ \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     250      \n" +
        " \n" +
        "massKg " + k.dTS2(payload) + " \n" +
        "fuelKg " + k.dTS2(fuel) + " \n" +
        "dryKg " + k.dTS2(rocket) + " \n" +
        "massHeight 15240        \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 12.57                \n" +
        " \n" +
        "ISP 300  \n" +
        "ispDegradedAtOneAtm 0.12 \n" +
        "thrustNewtons " + k.dTS(1.65 * total * k.earthAcceleration) + "  \n" +
        " \n" +
        "label A \n" +
        "surfaceVelAndAngle 30 85.6   \n" +
        "MakeMass   \n" +
        "MakeRocket  \n" +
        "  \n" +
        "label B \n" +
        "surfaceVelAndAngle 30 85.8   \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label C \n" +
        "surfaceVelAndAngle 30 86   \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label D \n" +
        "surfaceVelAndAngle 30 86.2  \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label E \n" +
        "surfaceVelAndAngle 30 86.4   \n" +
        "MakeMass   \n" +
        "MakeRocket  \n" +
        "screenOrigin    C   \n";

    sampleNames[72] = "72. Nitrous air-launch - low thrust ";
    payload = 4000;
    fuel = 56000;
    rocket = 0.10 * fuel;
    total = payload + fuel + rocket;
    samples[72] =
        "/* Assume dry rocket is 10% of fuel \n" +
        "  Assume payload is 4000 Kg \n" +
        "  To simulate an air launch at 50,000 feet \n" +
        "  Assume 4 meter diameter (12.57 sq meter area) \n" +
        "  Using ISP of 300 for nitrous-oxide/propane  */ \n" +
        "screenYScaleMeters   800000   \n" +
        "                        \n" +
        "deltaT           0.001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     330      \n" +
        " \n" +
        "massKg " + k.dTS2(payload) + " \n" +
        "fuelKg " + k.dTS2(fuel) + " \n" +
        "dryKg " + k.dTS2(rocket) + " \n" +
        "massHeight 15240        \n" +
        "coefficientOfDrag 0.5              \n" +
        "dragArea 12.57                \n" +
        " \n" +
        "ISP 300  \n" +
        "ispDegradedAtOneAtm 0.12 \n" +
        "thrustNewtons " + k.dTS(1.3 * total * k.earthAcceleration) + "  \n" +
        " \n" +
        "label A \n" +
        "surfaceVelAndAngle 30 87.736   \n" +
        "MakeMass   \n" +
        "MakeRocket  \n" +
        "  \n" +
        "label B \n" +
        "surfaceVelAndAngle 30 87.7365  \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label C \n" +
        "surfaceVelAndAngle 30 87.737   \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label D \n" +
        "surfaceVelAndAngle 30 87.7375  \n" +
        "MakeMass   \n" +
        "MakeRocket \n" +
        "  \n" +
        "label E \n" +
        "surfaceVelAndAngle 30 87.738   \n" +
        "MakeMass   \n" +
        "MakeRocket  \n" +
        "screenOrigin    C   \n";

    sampleNames[73] = "73. Solar Sail - Huge for visualization ";
    samples[73] =
        "/* Solar Sail starting in GEO orbit  \n" +
        "   and climbing from there.  */ \n" +
        " \n" +
        "screenYScaleMeters   100000000   \n" +
        "screenOrigin    Earth    \n" +
        "                         \n" +
        "deltaT           1       \n" +
        "timePerDisplay  100      \n" +
        "totalTime     8640000    \n" +
        "solarSailMetersOnASide 5000000   \n" +
        "solarSailEfficiency 0.90 \n" +
        "massKg   0               \n" +
        "gramsPerSqMeter 4        \n" +
        "coefficientOfDrag  1     \n" +
        "massHeight 35785000      \n" +
        "massCircularOrbit        \n" +
        "label BigSail            \n" +
        "MakeMass                 \n" +
        "MakeSolarSail            \n";

    sampleNames[74] = "74. Solar Sail at GEO";
    samples[74] =
        "/* Solar Sail starting in GEO orbit  \n" +
        "   and climbing from there.  */ \n" +
        " \n" +
        "screenYScaleMeters   200000000   \n" +
        "screenOrigin    Earth    \n" +
        "                         \n" +
        "deltaT           1       \n" +
        "timePerDisplay  1000      \n" +
        "totalTime     8640000      \n" +
        "solarSailMetersOnASide 1000       \n" +
        "solarSailEfficiency 0.90 \n" +
        "massKg   4000            \n" +
        "gramsPerSqMeter 4        \n" +
        "coefficientOfDrag  1     \n" +
        "massHeight 35785000      \n" +
        "massCircularOrbit        \n" +
        "label GeoSail            \n" +
        "MakeMass                 \n" +
        "MakeSolarSail            \n";

    sampleNames[75] = "75. Connection test";
    samples[75] =
        "/* Make 2 masses and then connect them later. \n" +
        " */ \n" +
        "screenYScaleMeters   90000000   \n" +
        "screenOrigin    EarthTop   \n" +
        "                        \n" +
        "deltaT           0.1  \n" +
        "timePerDisplay  100      \n" +
        "totalTime     6000      \n" +
        "                       \n" +
        "massKg 1000            \n" +
        "                       \n" +
        "label HigherMass          \n" +
        "massHeight 41000000       \n" +
        "velMass -3200 0           \n" +
        "MakeMass                \n" +
        "                         \n" +
        "label LowerMass           \n" +
        "massHeight 31000000       \n" +
        "velMass -3000 0           \n" +
        "MakeMass                \n" +
        "                         \n" +
        "AtTime 500 connect       \n" +
        " HigherMass to LowerMass  \n" +
        "  fromDiameter 0.5 toDiameter 0.5     \n" +
        "EndAtTime                        \n" +
        "                            \n" +
        "showXY  true               \n";

    sampleNames[76] = "76. Geo Tether and Solar Sail";
    samples[76] =
        "/* Hanging tether with solar sail on it  \n" +
        "   \n" +
        "  */ \n" +
        "   \n" +
        "label HangingTether     \n" +
        design.Spectra2000 +
        "restLength    12400000    \n" +
        "smallEndRadius 30600000 /* fromEarth center */  \n" +
        design.TaperAuto2 +
        "velBigEnd    -3200 0      \n" +
        "velSmallEnd   -2385 0     \n " +
        "bigKg       2000000       \n " +
        "payloadKg     100000       \n " +
        "slices        100         \n " +
        "MakeTether   Ballast Payload               \n " +
        " \n" +
        "label HangingTether.CenterOfMass   \n" +
        "solarSailMetersOnASide 5000       \n" +
        "solarSailEfficiency 0.90 \n" +
        "gramsPerSqMeter 4        \n" +
        "MakeSolarSail            \n" +
        "                         \n" +
        "screenOrigin HangingTether.C   \n" +
        "screenYScaleMeters   30000   \n" +
        "deltaT           1     \n " +
        "timePerDisplay  100       \n " +
        "totalTime     9000000    \n ";

    sampleNames[77] = "77. Geo Tether and Solar Sail Tug";
    samples[77] =
        "/* Hanging tether with solar sail pulling it.  \n" +
        "   There is a control problem that we have not \n" +
        "   delt with, so solar sail is not always out  \n" +
        "   in front and pulling the right way.      */ \n" +
        "   \n" +
        "label HangingTether     \n" +
        design.Spectra2000 +
        "restLength    12400000    \n" +
        "smallEndRadius 30600000 /* fromEarth center */  \n" +
        design.TaperAuto2 +
        "velBigEnd    -3148 0      \n" +
        "velSmallEnd   -2230 0     \n " +
        "bigKg       2000000       \n " +
        "payloadKg     97010       \n " +
        "slices        500         \n " +
        "MakeTether   Ballast Payload               \n " +
        " \n" +
        "label SolarSailTug         \n" +
        "solarSailMetersOnASide 5000       \n" +
        "solarSailEfficiency 0.90 \n" +
        "massKg                   500 \n" +
        "gramsPerSqMeter 4        \n" +
        "posMass -25000 42162986  \n" +
        "velMass -3074 -2         \n" +
        "MakeMass                 \n" +
        "MakeSolarSail            \n" +
        "screenOrigin HangingTether.C   \n" +
        "                         \n" +
        "AtTime 800 connect  \n" +
        " SolarSailTug to HangingTether.C  \n" +
        "  fromDiameter 0.2 toDiameter 0.2  \n" +
        "EndAtTime                        \n" +

        "screenYScaleMeters   50000   \n" +
        "deltaT           0.1     \n " +
        "timePerDisplay  100       \n " +
        "totalTime     9000000    \n ";

    sampleNames[78] = "78. X-prize to orbit - SSTO Spin Stabilized";
    samples[78] =
        "/* John Carmack posted about launching \n" +
        " a spin Stabilized frocket from an X-prize \n" +
        " rocket on its way up to 100 km into orbit.  \n" +
        " This sample is with a single liquid stage.  \n" +
        " In all 4 samples we start at 90 km while    \n" +
        " going up fast enough to coast to 100 km.    \n" +
        " In these samples we don't count the x-prize \n" +
        " vehicle as a stage. \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.01 \n" +
        "timePerDisplay  10      \n" +
        "totalTime     200000    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "massKg 4           \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.01          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "ISP 350                \n" +
        "/* SSTO - only stage */      \n" +
        "thrustNewtons 6000    \n" +
        "dryKg  20              \n" +
        "fuelKg 200             \n" +
        "stabilizationType spin  \n" +
        "stageStaysAttached true \n" +
        "spinAngle  179         \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[79] = "79. X-prize to orbit - second with guidance";
    samples[79] =
        "/* Here we have 2 solid stanges where the second \n" +
        " stage has guidance. \n" +
        "   \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.01 \n" +
        "timePerDisplay  10      \n" +
        "totalTime     30000    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "massKg 4           \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.01          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "ISP 269                \n" +
        "/* upper stage */      \n" +
        "thrustNewtons 500      \n" +
        "dryKg  2             \n" +
        "fuelKg 20             \n" + // 20/6 = 3.33
        "stabilizationType fin  \n" +
        "retroThrust false       \n" +
        "timeOfIgnition 300       \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "/* lower stage */      \n" +
        "thrustNewtons 3000    \n" +
        "dryKg  20              \n" +
        "fuelKg 200             \n" + // 200/46  = 4.43
        "stabilizationType spin  \n" +
        "retroThrust false      \n" +
        "spinAngle  150         \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[80] = "80. X-prize to orbit - 180 degrees off ";
    samples[80] =
        "/* Continuing from previous sample.   \n" +
        " Henry Cate pointed out that if you had  \n" +
        " two solid rockets facing oposite ways on \n" +
        " opposite sides of your payload you could \n" +
        " spin Stabilize the whole thing and fire \n" +
        " one to start and the other to circularize \n" +
        " at the far side of Earth like 45 mins later. \n" +
        " We also use 2 solids here to get going.  \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.01 \n" +
        "timePerDisplay  10      \n" +
        "totalTime     30000    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "/* payload and starting */ \n" +
        "massKg 4               \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.01          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "ISP 269                \n" +
        "/* upper stage - Apogee kick */      \n" +
        "thrustNewtons 500      \n" +
        "dryKg  0.05            \n" +
        "fuelKg 0.5               \n" +
        "stabilizationType spin \n" +
        "spinAngle  -3         \n" +
        "timeOfIgnition 2760    \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "/*  stage 2 */    \n" +
        "thrustNewtons 1000    \n" +
        "dryKg  2.5             \n" +
        "fuelKg 25             \n" +
        "stabilizationType spin \n" +
        "spinAngle  177        \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "/* lower stage / stage 1 */      \n" +
        "thrustNewtons 6000    \n" +
        "dryKg  13              \n" +
        "fuelKg 130             \n" +
        "stabilizationType spin \n" +
        "spinAngle  177         \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[81] = "81. X-prize to orbit - thruster ";
    samples[81] =
        "/* 2 spin Stabilized solids then thruster \n" +
        " with guidance to circularize the orbit.  \n" +
        "   \n" +
        " At URL: \n" +
        " http://www.asi.org/adb/04/03/09/01/thiokol.html#09-19 \n" +
        "   it seems expensive solid rockets can \n" +
        "   get to 92%, 93%, and even 94% fuel.  \n" +
        "   Going to guess that cheap can do 90%. \n" +
        " There is some heat, but a small amount \n" +
        " of ablative paint should handle it.    \n" +
        " Probably want a smaller thruster than \n" +
        " we have here as 0.9 Kg is a big fraction \n" +
        " of our total mass. \n" +
        " For the thruster we use: \n" +
        " http://www.busek.com/low_power.htm  \n" +
        " URL has 0.9 Kg, 50 to 200 watts,    \n" +
        "  0.0124 Newtons, 1300 sec ISP.      \n" +
        " This probably does not include power conditioning. \n" +
        " Ion drives with ISP of 2000+ can be made down to 50 watts:  \n" +
        "    http://www.islandone.org/APC/Electric/06.html \n" +
        " Arcjets can be very small   \n" +
        "   http://www.islandone.org/APC/Electric/03.html \n" +
        "   but with ISP in the 400 to 1300 range \n" +
        " */ \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.01 \n" +
        "timePerDisplay  10      \n" +
        "totalTime     30000    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "/* payload and starting */ \n" +
        "massKg 3.6               \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.01          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "/* thruster upper stage */ \n" +
        "ISP 1300               \n" +
        "thrustNewtons 0.0124   \n" +
        "dryKg  0.9             \n" +
        "fuelKg 0.5             \n" +
        "stabilizationType fin  \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "/* middle  stage */    \n" +
        "ISP 269                \n" +
        "thrustNewtons 4000    \n" +
        "dryKg  2.5            \n" +
        "fuelKg 25             \n" +
        "stabilizationType spin \n" +
        "spinAngle  181        \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "/* lower stage */      \n" +
        "ISP 269                \n" +
        "thrustNewtons 30000    \n" +
        "dryKg  15              \n" +
        "fuelKg 150             \n" +
        "stabilizationType spin \n" +
        "spinAngle  181        \n" +
        "MakeRocket             \n" +
        "                       \n" +
        "blackBodyMass        0.5     \n" +
        "blackBodyArea         0.01   \n" +
        "blackBodySpecificHeat 896   /* Aluminum */  \n" +
        "blackBodyEmissivity 0.9     \n" +
        "MakeBluntBody               \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[82] = "82. EDT - Electro-Dynamic Tether ";
    samples[82] =
        design.LEOEtether +
        design.Display10Fast +
        "totalTime 8000  \n" +
        "screenYScaleMeters 2000000 \n" +
        "screenOrigin T4T.C      \n";

    sampleNames[83] = "83. SmartReentry 150 km ";
    samples[83] =
        "/* Want to see how Gs can be reduced  \n" +
        " by smartly adjusting angle of attack \n" +
        " on the capsule. */ \n" +
        "screenYScaleMeters   500000   \n" +
        "                        \n" +
        "deltaT           0.0001  \n" +
        "timePerDisplay  1      \n" +
        "totalTime     3000      \n" +
        "                       \n" +
        "massKg 4000             \n" +
        "massHeight 150000       \n" +
        "massVelAndAngle 5000 0  \n" +
        "                        \n" +
        "label FixedLD0.35              \n" +
        "coefficientOfDrag 1.04   \n" +
        "dragArea 9.62          \n" +
        "massLiftOverDrag 0.35    \n" +
        "MakeMass                \n" +
        "                        \n" +
        "dragArea 12.57          \n" +
        "label SmartReentry      \n" +
        "MakeMass                \n" +
        "maxGsTarget   6.1        \n" +
        "MakeSmartReentry        \n" +
        "screenOrigin    SmartReentry   \n";

    sampleNames[84] = "84. Tinny high speed capsule  ";
    samples[84] =
        "/* Small 20 Kg capsule coming in fast  */ \n" +
        "/*  */ \n" +
        "/* */ \n" +
        "screenYScaleMeters   1500000   \n" +
        "                        \n" +
        "deltaT           0.05  \n" +
        "timePerDisplay  1       \n" +
        "totalTime    4000       \n" +
        "                        \n" +
        "massHeight 200000       \n" +
        "massKg 20          \n" +
        "coefficientOfDrag 1             \n" +
        "massLiftOverDrag 0      \n" +
        "blackBodyMass        2     \n" +
        "blackBodySpecificHeat 1885   \n" + // Beryllium
        "blackBodyEmissivity 0.9      \n" +
        "                        \n" +
        "label 0.3sqm               \n" +
        "dragArea 0.3                 \n" +
        "blackBodyArea         0.3      \n" +
        "spaceVelAndAngle 11000 -90   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "                        \n" +
        "label 0.6sqm               \n" +
        "dragArea 0.6                 \n" +
        "blackBodyArea         0.6      \n" +
        "spaceVelAndAngle 11000 -90   \n" +
        "MakeMass                 \n" +
        "MakeBluntBody            \n" +
        "screenOrigin    0.3sqm   \n";

    String tmps =
        "/* Simulate trajectory of a single proton particle. */ \n" +
        "/* */ \n" +
        "deltaT          0.000002  \n" +
        "timePerDisplay  0.001       \n" +
        "  \n" +
        "label RefMass \n" +
        "massKg 1 \n" +
        "posMass " + (k.earthRadius * 1.5) + " 0 \n" +
        "velMass 0 0 \n" +
        "MakeMass    \n" +
        "  \n" +
        "label P0 \n" +
        " /* Non scientific numbers for microsoft java compatibility */ \n" +
        "radKg " + k.ProtonMass + " \n" +
        "radCharge  " + k.ProtonCharge + " \n" +
        "radVelocity  107000000 0 30000000 \n" +
        "radLValue 2.5        \n" +
        "MakeRadiation        \n";

    sampleNames[85] = "85. Radiation in Van Allen Belt - single proton - side";
    samples[85] =
        "/* This looks like it is getting closer to  \n" +
        "  the Earth but really it is going around behind. */ \n" +
        "radView      side       \n" +
        tmps +
        "totalTime    2      \n" +
        "screenOrigin RefMass  \n" +
        "screenYScaleMeters " + (k.earthRadius * 2.0) + "\n";

    sampleNames[86] = "86. Radiation in Van Allen Belt - single proton - top ";
    samples[86] =
        "radView      top       \n" +
        tmps +
        "totalTime    20       \n" +
        "screenOrigin Earth  \n" +
        "screenYScaleMeters " + (k.earthRadius * 6.0) + "\n";

    tmps = "/* A simulating a range of protons bouncing around. */ \n" +
        "/* */ \n" +
        "                        \n" +
        "deltaT          0.000001  \n" +
        "timePerDisplay  0.001       \n" +
        "  \n" +
        "label RefMass \n" +
        "massKg 1 \n" +
        "posMass " + (k.earthRadius * 1.5) + " 0 \n" +
        "velMass 0 0 \n" +
        "MakeMass    \n" +
        "  \n" +
        "label P0 \n" +
        "radKg " + k.ProtonMass + " \n" +
        "radCharge  " + k.ProtonCharge + " \n" +
        "radVelocity  107000000 0 30000000 \n" +
        "label P1    radLValue 2.0    MakeRadiation \n" +
        "label P2    radLValue 2.1    MakeRadiation \n" +
        "label P3    radLValue 2.2    MakeRadiation \n" +
        "label P4    radLValue 2.3    MakeRadiation \n" +
        "label P5    radLValue 2.4    MakeRadiation \n" +
        "label P6    radLValue 2.5    MakeRadiation \n" +
        "label P7    radLValue 2.6    MakeRadiation \n" +
        "label P8    radLValue 2.7    MakeRadiation \n";

    sampleNames[87] =
        "87. Radiation in Van Allen Belt - range of LValues - side";
    samples[87] =
        "radView      side       \n" +
        tmps +
        "totalTime    2      \n" +
        "screenOrigin RefMass  \n" +
        "screenYScaleMeters " + (k.earthRadius * 2.0) + "\n";

    sampleNames[88] =
        "88. Radiation in Van Allen Belt - range of LValues - top";
    samples[88] =
        "radView      top       \n" +
        tmps +
        "totalTime    20       \n" +
        "screenOrigin Earth  \n" +
        "screenYScaleMeters " + (k.earthRadius * 6.0) + "\n" +
        " AtTime 0 showText RefMass false EndAtTime \n" +
        " AtTime 0.1 showText P1 false EndAtTime \n" +
        " AtTime 0.1 showText P2 false EndAtTime \n" +
        " AtTime 0.1 showText P3 false EndAtTime \n" +
        " AtTime 0.1 showText P4 false EndAtTime \n" +
        " AtTime 0.1 showText P5 false EndAtTime \n" +
        " AtTime 0.1 showText P6 false EndAtTime \n" +
        " AtTime 0.1 showText P7 false EndAtTime \n" +
        " AtTime 0.1 showText P8 false EndAtTime \n";

    sampleNames[89] = "89. Moon Tether with winch";
    samples[89] =
        "/* Looking at tether around moon \n" +
        "  */ \n" +
        "                      \n" +

        design.Spectra2000 +
        "bigEndDiameter   0.002 \n" +
        "smallEndDiameter 0.002 \n" +
        "bigKg       4000     \n" +
        "payloadKg     1      \n" +
        "slices        100      \n" +
        "                       \n" +
        "label T1               \n" +
        "posBigEnd 0 " + k.dTS(k.cmToMoon + k.moonRadius + 20000) + "  \n" +
        "posSmallEnd 0 " + k.dTS(k.cmToMoon + k.moonRadius + 10) + "  \n" +
        "velBigEnd " + k.dTS( - (10 + k.orbitMoonSpeed(20000) + k.moonVelocity)) +
        " 0  \n" +
        "velSmallEnd " + k.dTS( -k.moonVelocity) + " 0 \n " +
        "restLength         19900  \n" +
        "MakeTether   Ballast Payload           \n" +
        "                     \n" +
        "MakeMoon             \n" +
        "deltaT .005           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 2700      \n" +
        "totalTime 132500        \n" +
        "screenYScaleMeters 200000 \n" +
        "screenOrigin T1.Ballast     \n" +
        "averagingSeconds 75         \n" +
        "showEarthRelative false     \n" +
        "showMoonRelative true      \n" +
        "showMomentum true           \n" +
        "AtTime 10 showText Moon false EndAtTime \n" +
        "AtTime 20 showText Earth false EndAtTime \n" +
        "AtTime 70 : This sample input is to show that the balOrbAngMomen goes up if we pickup, spin up, and toss some regolith. EndAtTime \n" +
        "AtTime 120 : First 2 rotations to show starting 1 Kg tip speed and ballast orbital momentum. EndAtTime \n" +
        "AtTime 150 toss 4 from nowhere to T1.Payload EndAtTime \n" +
        "AtTime 200 : Now a rotation after picking up 4 Kg EndAtTime \n" +
        "AtTime 224 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 4000  10000  1 50000 1025 120 50 EndAtTime \n" +
        "AtTime 1200 : Now winching in till toss at 1270 . EndAtTime \n" +
        "AtTime 1277 toss 4 from T1.Payload to M1 EndAtTime \n" +
        "AtTime 1320 panNewmassTime M1 30 EndAtTime \n" +
        "AtTime 1360 : We pan over to watch the mass tossed backward EndAtTime \n" +
        "AtTime 1365 panNewmassTime T1.Ballast 60 EndAtTime \n" +
        "AtTime 1371 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 4000  0  -1 50000 2000 90 50 EndAtTime \n" +
        "AtTime 1500 : Now winching back out. EndAtTime \n" +
        "AtTime 2700 : Gained bit over 0.0001E13 momentum. This is about right for 4 Kg at 260 m/sec EndAtTime \n"
        ;

    sampleNames[90] = "90. Winch in/out to increase spin/tip-speed";
    samples[90] =
        " density 970    \n" +
        " tensileGpa 4.0      \n" +
        " elasticity 0.03\n" +
        " \n" +
        " posBigEnd             0   6785000  \n" +
        " posSmallEnd  -290000 6785000 \n" +
        " velBigEnd       -7662  20 \n" +
        " velSmallEnd   -7675  -460 \n" +
        " restLength   290000  \n" +
        " bigEndDiameter   0.00519    \n" +
        " smallEndDiameter 0.00462  \n" +
        "  bigKg       180000     \n" +
        "  payloadKg      3000  \n" +
        "  slices        100  \n" +
        "  label T1    \n" +
        "  MakeTether  Ballast Payload  \n" +
        "  \n" +
        " screenYScaleMeters 1000000  \n" +
        "deltaT .02           \n" +
        "timePerDisplay 10     \n" +
        " totalTime   80400  \n" +
        " screenOrigin T1.C  \n" +
        "AtTime 1213 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  24000  12 10000 400 300 50 EndAtTime \n" +
        "AtTime 2150 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  4500  -12 10000 700 300 50 EndAtTime \n" +
        "AtTime 3300 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  22000  12 10000 400 300 50 EndAtTime \n" +
        "AtTime 4250 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  6000  -12 10000 700 300 50 EndAtTime \n" +
        "AtTime 5200 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  21000  12 10000 400 300 50 EndAtTime \n" +
        "AtTime 6050 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  7000  -12 10000 700 300 50 EndAtTime \n" +
        "AtTime 6900 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  20000  12 10000 400 300 50 EndAtTime \n" +
        "AtTime 7700 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 500000  8000  -12 10000 700 300 50 EndAtTime \n" +
        "AtTime 20000 : This tether started out like sample #3 at 455 m/sec tip speed but is now faster.  EndAtTime \n"
        ;

    sampleNames[91] = "91. Sounds wave speed check";
    samples[91] =
        "/* Tossing at different times to find toss  \n" +
        " time to go to GTO.   \n" +
        "  */          \n" +
        design.LEOtether +
        "AtTime 10 toss 2000 from nowhere to T4T.Payload EndAtTime\n" +
        "AtTime 11 toss 2000 from T4T.Payload to nowhere EndAtTime\n" +
        "AtTime 1000 : CurrentMaxStretch/CurrentMinStretch show wave. Each slice is 1 km in ~0.1 sec. About 11 km/sec is right. EndAtTime \n" +
        "             \n" +
        "                      \n" +
        "deltaT 0.001 \n" +
        "timePerDisplay 0.1 \n" +
        "totalTime 60         \n" +
        "screenYScaleMeters 1500000    \n" +
        "screenOrigin T4T.C              \n";

    sampleNames[92] = "92. Winching 2 tethers ";
    samples[92] =
        "/* Let one out while other comes in \n" +
        "  */ \n" +
        "                      \n" +
        design.Spectra2000 +
        "bigEndDiameter   0.002 \n" +
        "smallEndDiameter 0.001 \n" +
        "slices        100      \n" +
        "restLength         19900  \n" +
        "                       \n" +
        "label Ballast               \n" +
        "massKg       100     \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 20000) + "  \n" +
        "velMass " + k.dTS( - (200 + k.orbitMoonSpeed(20000) + k.moonVelocity)) +
        " 0  \n" +
        "MakeMass                \n" +
        "label Payload1          \n" +
        "massKg       5          \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 10) + "  \n" +
        "velMass " + k.dTS( -k.moonVelocity) + " 0 \n " +
        "MakeMass                \n" +
        "label T1                \n" +
        "MakeTether   Ballast Payload1           \n" +
        "                      \n" +
        "label Payload2          \n" +
        "posMass 0 " + k.dTS(k.cmToMoon + k.moonRadius + 2 * 20000) + "  \n" +
        "velMass " +
        k.dTS( - (10 + 2 * k.orbitMoonSpeed(20000) + k.moonVelocity)) +
        " 0  \n" +
        "MakeMass                \n" +
        "label T2              \n" +
        "MakeTether   Ballast Payload2          \n" +
        "                     \n" +
        "MakeMoon             \n" +
        "deltaT .01           \n" +
        "timePerDisplay 1     \n" +
        "totalTime 7000        \n" +
        "screenYScaleMeters 1000 \n" +
        "screenOrigin Ballast     \n" +
        "showEarthRelative false     \n" +
        "showMoonRelative true      \n" +
        "showTetherStatic false     \n" +
        "averagingSeconds 20         \n" +
        "AtTime 0.1 showText Moon false EndAtTime \n" +
        "AtTime 0.2 showText Earth false EndAtTime \n" +
        "AtTime 0.3 winchAllIn T2 EndAtTime  \n" +
        "AtTime 0.4  winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T1 7000 100000  15 20000 10000 105 50 EndAtTime \n" +
        "AtTime 0.5  winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "                T2 7000 100000 -15 20000 10000 95 50 EndAtTime \n" +
        "AtTime 50 zoomNewscaleTime 100000 2000 EndAtTime \n" +
        "AtTime 7000 : Zooming out while T1 going in and T2 going out.  Will finish in about 2 hours. EndAtTime \n"
        ;

    sampleNames[93] = "93. X-prize to tether - 5 km/sec ";
    samples[93] =
        "/* \n" +
        " This sample is with a single liquid stage.  \n" +
        " In all 4 samples we start at 90 km while    \n" +
        " going up fast enough to coast to 100 km.    \n" +
        " In these samples we don't count the x-prize \n" +
        " vehicle as a stage. \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.001 \n" +
        "timePerDisplay  1      \n" +
        "totalTime     130    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "massKg 50           \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.1          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "ISP 350                \n" +
        "/* SSTO - only stage */      \n" +
        "thrustNewtons 6000    \n" +
        "dryKg  20              \n" +
        "fuelKg 200             \n" +
        "stabilizationType spin  \n" +
        "stageStaysAttached true \n" +
        "spinAngle  175         \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[94] = "94. X-prize to tether - 4 km/sec";
    samples[94] =
        "/* \n" +
        " This sample is with a single liquid stage.  \n" +
        " In all 4 samples we start at 90 km while    \n" +
        " going up fast enough to coast to 100 km.    \n" +
        " In these samples we don't count the x-prize \n" +
        " vehicle as a stage. \n" +
        "  */ \n" +
        " \n" +
        "screenYScaleMeters   2400000   \n" +
        "deltaT           0.001 \n" +
        "timePerDisplay  1      \n" +
        "totalTime     120    \n" +
        "                       \n" +
        "label rocket1          \n" +
        "massKg 91           \n" +
        "massHeight 90000      \n" +
        "/* Going up fast enough to coast to 100 km */ \n" +
        "surfaceVelAndAngle 442.66 90 \n" +
        "coefficientOfDrag 0.5  \n" +
        "dragArea 0.1          \n" +
        "MakeMass               \n" +
        "                       \n" +
        "ISP 350                \n" +
        "/* SSTO - only stage */      \n" +
        "thrustNewtons 9000    \n" +
        "dryKg  20              \n" +
        "fuelKg 200             \n" +
        "stabilizationType spin  \n" +
        "stageStaysAttached true \n" +
        "spinAngle  176.5         \n" +
        "timeOfIgnition 0       \n" +
        "MakeRocket             \n" +
        "screenOrigin    rocket1 \n";

    sampleNames[95] = "95. Moon-L1 Space Elevator";
    samples[95] =
        "/*  */ \n" +
        " \n" +
        "MakeMoon                \n" +
        "                        \n" +
        "deltaT           .01      \n" +
        "timePerDisplay  1   \n" +
        "totalTime     2419200  \n" +
        "                        \n" +
        "label L1                \n" +
        "massKg 1000             \n" +
        "posMass   0  321561610   \n" +
        "velMass  " +
        k.dTS2( - (k.moonVelocity * (321561610 / k.cmToMoon)))
        + " 0    \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label Surface1           \n" +
        "massKg 1000             \n" +
        "posMass   0 0   \n" +
        "velMass  0 0  \n" +
        "MakeMass                \n" +
        "label Surface2          \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label FD1                \n" +
        "labelCenter Moon        \n" +
        "distance " + k.moonRadius + "\n" +
        "labelTip Surface1       \n" +
        "secondsPerRev " + k.moonSecsPerRotation + " \n" +
        "secondsOffset " + k.moonSecsPerRotation * 0.70 + " \n" +
        "direction 1             \n" +
        "MakeFixedDist           \n" +
        "                        \n" +
        "label FD2                \n" +
        "labelTip Surface2       \n" +
        "secondsOffset " + k.moonSecsPerRotation * 0.80 + " \n" +
        "MakeFixedDist           \n" +
        "                        \n" +
        "label L1Ballast         \n" +
        "massKg 500000         \n" +
        "posMass   0  235062000.0 \n" +
        "velMass " +
        k.dTS2( - (k.moonVelocity * (235062000 / k.cmToMoon)))
        + " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label MoonElevator1      \n" +
        design.Spectra2000 +
        "taperType Automatic     \n" +
        "safetyFactor 2          \n" +
        "slices 200              \n" +
        "MakeTether L1Ballast Surface1 \n" +
        "                        \n" +
        "label MoonElevator2      \n" +
        "MakeTether L1Ballast Surface2 \n" +
        "                        \n" +
        "screenYScaleMeters  160000000   \n" +
        "screenOrigin    MoonElevator1.100  \n";

    sampleNames[96] = "96. Moon-L2 Space Elevator";
    samples[96] =
        "/*  */ \n" +
        " \n" +
        "MakeMoon                \n" +
        "                        \n" +
        "label L2                \n" +
        "massKg 1000             \n" +
        "posMass   0  444062000   \n" +
        "velMass " +
        k.dTS2( - (k.moonVelocity * (444062000 / k.cmToMoon)))
        + " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "deltaT           .01      \n" +
        "timePerDisplay  1   \n" +
        "totalTime     2419200  \n" +
        "                        \n" +
        "label Surface1           \n" +
        "massKg 1000             \n" +
        "posMass   0 0   \n" +
        "velMass  0 0  \n" +
        "MakeMass                \n" +
        "label Surface2          \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label FD1                \n" +
        "labelCenter Moon        \n" +
        "distance " + k.moonRadius + "\n" +
        "labelTip Surface1       \n" +
        "secondsPerRev " + k.moonSecsPerRotation + " \n" +
        "secondsOffset " + k.moonSecsPerRotation * 0.30 + " \n" +
        "direction 1             \n" +
        "MakeFixedDist           \n" +
        "                        \n" +
        "label FD2                \n" +
        "labelTip Surface2       \n" +
        "secondsOffset " + k.moonSecsPerRotation * 0.20 + " \n" +
        "MakeFixedDist           \n" +
        "                        \n" +
        "label L2Ballast         \n" +
        "massKg 700000         \n" + // 5 to small 10 too big - maybe 6
        "posMass   0  605062000.0 \n" +
        "velMass " +
        k.dTS2( - (k.moonVelocity * (605062000 / k.cmToMoon)))
        + " 0  \n" +
        "MakeMass                \n" +
        "                        \n" +
        "label MoonElevator1      \n" +
        design.Spectra2000 +
        "taperType Automatic     \n" +
        "safetyFactor 2          \n" +
        "slices 200              \n" +
        "MakeTether L2Ballast Surface1 \n" +
        "                        \n" +
        "label MoonElevator2      \n" +
        "MakeTether L2Ballast Surface2 \n" +
        "                        \n" +
        "screenYScaleMeters  250000000   \n" +
        "screenOrigin    MoonElevator1.100  \n";

    sampleNames[97] = "97. Test 3D  ";
    samples[97] =
        "label M1    \n" +
        "massKg 250    \n " +
        "posMass3D   0 0 " + k.dTS(k.earthRadius+200000) + " \n " +
        "velMass3D   7783 0 0  \n " +
        "MakeMass    \n " +
        "view3D    2  \n" +
        "deltaT           .01      \n" +
        "timePerDisplay  1   \n" +
        "totalTime     5000  \n" +
        "AtTime 3600 view3D 1 EndAtTime \n";

    double joinRadius=2000;  // 4 km total for midsection
    double sideLength=20000; // 10 km each on sides
    double totalRadius = joinRadius + sideLength;
    double safetyFactor = 10;
    double edgeThickness= safetyFactor * 0.02;   // thickness at hotels
    double joinThickness= safetyFactor * 0.02;   // Where side tethers join mid section
    double midThickness= safetyFactor * 0.02;   // Around pusher plate
    double sideRestLength = (1 - 0.03/safetyFactor) * sideLength;
    double midRestLength = (1 - 0.03/safetyFactor) * 2.0 * joinRadius;
    // Used duel hotel simulation 45 to get thicknesses
            // V^2 / R = 1/6 * G
            // V = sqrt (R*G/6)
    double edgeVelocity = Math.sqrt(totalRadius*k.earthAcceleration/6.0);
    double joinVelocity = edgeVelocity * joinRadius/totalRadius;
    double yDistance = k.cmToMoon;


    sampleNames[98] = "98. Orion Tether with winches ";
    samples[98] =
        "/* Simulate as 3 tethers with puser plate on middle one \n" +
        "  */ \n" +
        "label LeftCrew     \n" +
        "massKg    1000000     \n" +
        "posMass " + k.dTS2(-totalRadius) + " " + k.dTS2(yDistance) + "  \n" +
        "velMass 0 " + k.dTS2(-edgeVelocity) + " \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label RightCrew     \n" +
        "massKg    1000000     \n" +
        "posMass " + k.dTS2(totalRadius) + " " + k.dTS2(yDistance) + "  \n" +
        "velMass 0 " + k.dTS2(edgeVelocity) + " \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label LeftJoin     \n" +
        "massKg    10     \n" +
        "posMass " + k.dTS2(-joinRadius) + " " + k.dTS2(yDistance) + "  \n" +
        "velMass 0 " + k.dTS2(-joinVelocity) + " \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label RightJoin     \n" +
        "massKg    10     \n" +
        "posMass " + k.dTS2(joinRadius) + " " + k.dTS2(yDistance) + "  \n" +
        "velMass 0 " + k.dTS2(joinVelocity) + " \n" +
        "MakeMass                \n" +
        "                      \n" +
         design.Spectra2000 +
        "taperType Linear      \n" +
        "bigEndDiameter   " + k.dTS3(joinThickness) + " \n" +
        "smallEndDiameter " + k.dTS3(edgeThickness) + " \n" +
        "slices        200      \n" +
        "restLength      " + k.dTS2(sideRestLength) + " \n" +
        "                       \n" +
        "label LeftTether                \n" +
        "MakeTether   LeftJoin LeftCrew \n" +
        "                      \n" +
        "label RightTether                \n" +
        "MakeTether   RightJoin RightCrew  \n" +
        "                      \n" +
        "label MidTether                \n" +
        "bigEndDiameter   " + k.dTS3(midThickness) + " \n" +
        "smallEndDiameter " + k.dTS3(midThickness) + " \n" +
        "restLength      " + k.dTS(midRestLength) + " \n" +
        "slices        50      \n" +
        "MakeTether   LeftJoin RightJoin  \n" +
        "                      \n" +
        "deltaT .0005          \n" +
        "timePerDisplay 0.1     \n" +
        "totalTime 7000        \n" +
        "screenYScaleMeters " + totalRadius*2.1 + " \n" +
        "screenOrigin MidTether.C     \n" +
        "tetherEnd3D LeftCrew       \n" +
        "                           \n" +
        "label OB1                  \n" +
        "tetherLabel MidTether     \n" +
        "pusherStdDev 500.0        \n" +
        "blastStdDev 500.0          \n" +
        "ISP           100000.0       \n" +
        "pusherLength   " + k.dTS2(2.0* joinRadius) + "       \n" +
        "pusherKg      1000000.0     \n" +
        "MakeOrionBlast \n" +
        "averagingSeconds 10        \n" +
 //       "AtTime 0.3 deltaT .0015 EndAtTime  \n" +
        "AtTime 0.1 winchIn LeftTether 1000 EndAtTime \n" +
        "AtTime 0.1 winchIn RightTether 1000 EndAtTime \n" +
        "AtTime 0.2 showText LeftTether false EndAtTime \n" +
        "AtTime 0.2 showText RightTether false EndAtTime \n" +
        "AtTime 0.3 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "            LeftTether 2000000000  20000000  -50 600 100 150 200 EndAtTime \n" +
        "AtTime 0.3 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "            RightTether 2000000000  20000000  -50 600 100 150 200 EndAtTime \n" +
        "AtTime 0.39 timePerDisplay 0.001 EndAtTime \n" +
        "AtTime 0.4 orionBlast OB1 300 EndAtTime \n" +
        "AtTime 0.5 timePerDisplay 0.1 EndAtTime \n" +
       "AtTime 101 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "            LeftTether 1000000000  20000000  50 600 100 90 200 EndAtTime \n" +
        "AtTime 101 winchLabelPowerForceSpeedDistTimePavgPspring \n" +
        "            RightTether 1000000000  20000000  50 600 100 90 200 EndAtTime \n"
  //      "AtTime 150 orionBlast OB1 50 EndAtTime \n" +
  //      "AtTime 2000 orionBlast OB1 50 EndAtTime \n"
        ;


    totalRadius = 50000;
    edgeVelocity = Math.sqrt(totalRadius*k.earthAcceleration/2.0);
    sampleNames[99] = "99. Orion Tether - No winch";
    samples[99] =
        "/* Simulate  tether with pusher plate  \n" +
        "  */ \n" +
        "label TopCrew     \n" +
        "massKg    1000000     \n" +
        "posMass 0 " + k.dTS2(yDistance + totalRadius) + "  \n" +
        "velMass " + k.dTS2(-edgeVelocity) + " 0 \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label BottomCrew     \n" +
        "massKg    1000000     \n" +
        "posMass 0 " +  k.dTS2(yDistance-totalRadius) + "  \n" +
        "velMass " + k.dTS2(edgeVelocity) + " 0 \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label MidTether                \n" +
        "density 970           \n " +
        "tensileGpa 4.0        \n " +
        "elasticity 0.03        \n" +
        "taperType Automatic   \n" +
        "safetyFactor 10      \n" +
        "slices        500     \n" +
        "MakeTether   TopCrew BottomCrew  \n" +
        "                      \n" +
        "deltaT .003           \n" +
        "timePerDisplay 1    \n" +
        "totalTime 7000        \n" +
        "screenYScaleMeters " + joinRadius*2.1 + " \n" +
        "screenOrigin MidTether.C     \n" +
        "                           \n" +
        "label OB1                  \n" +
        "tetherLabel MidTether     \n" +
        "pusherStdDev 500.0        \n" +
        "blastStdDev 500.0          \n" +
        "ISP           100000.0       \n" +
        "pusherLength " + k.dTS2(joinRadius*2) + " \n" +
        "pusherKg      2000000.0     \n" +
        "MakeOrionBlast \n" +
        "AtTime 1 orionBlast OB1 555 EndAtTime \n" +
        "AtTime 5 zoomNewscaleTime " + k.dTS(totalRadius*2.1) + " 30 EndAtTime \n" +
       "AtTime 215 orionBlast OB1 555 EndAtTime \n";


    totalRadius = 150000;
    edgeVelocity = Math.sqrt(totalRadius*k.earthAcceleration/0.75);
    sampleNames[100] = "100. Orion Tether - Spectra-2050 ";
    samples[100] =
        "/* Simulate  tether with pusher plate  \n" +
        "  */ \n" +
        "label TopCrew     \n" +
        "massKg    1000000     \n" +
        "posMass 0 " + k.dTS2(yDistance + totalRadius) + "  \n" +
        "velMass " + k.dTS2(-edgeVelocity) + " 0 \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label BottomCrew     \n" +
        "massKg    1000000     \n" +
        "posMass 0 " +  k.dTS2(yDistance-totalRadius) + "  \n" +
        "velMass " + k.dTS2(edgeVelocity) + " 0 \n" +
        "MakeMass                \n" +
        "                      \n" +
        "label MidTether                \n" +
        "density 970           \n " +
        "tensileGpa 40.0        \n " +
        "elasticity 0.03        \n" +
        "taperType Automatic   \n" +
        "safetyFactor 20       \n" +
        "slices        2000     \n" +
        "MakeTether   TopCrew BottomCrew  \n" +
        "                      \n" +
        "deltaT .001           \n" +
        "timePerDisplay 1    \n" +
        "totalTime 7000        \n" +
        "screenYScaleMeters " + joinRadius*2.1 + " \n" +
        "screenOrigin MidTether.C     \n" +
        "                           \n" +
        "label OB1                  \n" +
        "tetherLabel MidTether     \n" +
        "pusherStdDev 1000.0        \n" +
        "blastStdDev 1000.0          \n" +
        "ISP           100000.0       \n" +
        "pusherLength " + k.dTS2(joinRadius*2) + " \n" +
        "pusherKg      1000000.0     \n" +
        "MakeOrionBlast \n" +
        "AtTime 1 orionBlast OB1 1000 EndAtTime \n" +
        "AtTime 2 zoomNewscaleTime " + k.dTS(totalRadius*2.1) + " 10 EndAtTime \n";
 //      "AtTime 577 orionBlast OB1 750 EndAtTime \n";



   sampleNames[numSamples-1] = "Clear text input window";
    samples[numSamples-1] = "\n";


  }
}

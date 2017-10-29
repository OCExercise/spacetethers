
// Copyright (c) Vincent Cate 2002,2003

// Started with model at
//    http://www.grc.nasa.gov/WWW/K-12/airplane/atmosmet.html
// Then got density from:
//    Fundamentals of Astrodynamics and Applications by David A Vallado, 2001
//    Equation 8-34 on page 532 and table 8-4 on page 534
//
// Most other objects only really need density, so really could just
//   call static method.  Originally I was interested in temperature
//   after shock angle (TPS variables) which wanted starting temp.
//   Now I just like the blunt body stuff (BlackBody*).

package spacetethers;

import java.lang.Math;

// Everything metric

public class air {
  public double height;
  public double pressureKpa;    // K-Pascals
  public double temperature; // celsius
  public double density;     // Kg/meter^3
  public velocity vel;
  public position pos;
  private static double valladoTable[][] =
      {{0, 25, 0, 1.225, 7.249},
      {25, 30, 25, 3.899E-2, 6.349},
      {30, 40, 30, 1.774E-2, 6.682},
      {40, 50, 40, 3.972E-3, 7.554},
      {50, 60, 50, 1.057E-3, 8.382},
      {60, 70, 60, 3.206E-4, 7.714},
      {70, 80, 70, 8.770E-5, 6.549},
      {80, 90, 80, 1.905E-5, 5.799},
      {90, 100, 90, 3.396E-6, 5.382},
      {100, 110, 100, 5.297E-7, 5.877},
      {110, 120, 110, 9.661E-8, 7.263},
      {120, 130, 120, 2.438E-8, 9.473},
      {130, 140, 130, 8.484E-9, 12.636},
      {140, 150, 140, 3.845E-9, 16.149},
      {150, 180, 150, 2.070E-9, 22.523},
      {180, 200, 180, 5.464E-10, 29.740},
      {200, 250, 200, 2.789E-10, 37.105},
      {250, 300, 250, 7.248E-11, 45.546},
      {300, 350, 300, 2.418E-11, 53.628},
      {350, 400, 350, 9.158E-12, 53.298},
      {400, 450, 400, 3.725E-12, 58.515},
      {450, 500, 450, 1.585E-12, 60.828},
      {500, 600, 500, 6.967E-13, 63.822},
      {600, 700, 600, 1.454E-13, 71.835},
      {700, 800, 700, 3.614E-14, 88.667},
      {800, 900, 800, 1.170E-14, 124.64},
      {900, 1000, 900, 5.245E-15, 181.05},
      {1000, 1005, 1000, 3.019E-15, 268.00}};

  //  We use the above table to fill out ourTable where each
  //  index in ourTable is for 5 km heigher so we can
  //  just read out the numbers we need without searching.
  private static int LastStart=1000;
  private static int TableRows=201;
  private static double ourTable[][];
  private static boolean tableInitialized=false;
  private static int IndexStartRange=0;
  private static int IndexEndRange=1;
  private static int IndexBaseAltitude=2;
  private static int IndexNominalDensity=3;
  private static int IndexScaleHeight=4;

  private static void initializeTable() {
    ourTable = new double [TableRows][5];
    int index=0;
    int j;
    boolean done=false;
    while (!done) {
        int StartRange = (int) valladoTable[index][IndexStartRange];
        int EndRange = (int) valladoTable[index][IndexEndRange];
        int BaseAltitude = (int) valladoTable[index][IndexBaseAltitude];
        double NominalDensity = valladoTable[index][IndexNominalDensity];
        double ScaleHeight = valladoTable[index][IndexScaleHeight];
        int ourStart = (int) StartRange/5;
        int ourEnd = (int) EndRange/5;
        for (j=ourStart; j<ourEnd; j++) {
          ourTable[j][IndexStartRange]=StartRange;
          ourTable[j][IndexEndRange]=EndRange;
          ourTable[j][IndexBaseAltitude]=BaseAltitude;
          ourTable[j][IndexNominalDensity]=NominalDensity;
          ourTable[j][IndexScaleHeight]=ScaleHeight;
        }
        if (StartRange == LastStart) {
          done=true;
        }
        index++;
    }
    tableInitialized=true;
  }

  public void setDensity() {
     this.density = Density(this.pos.height());
  }
  public void setDensity(double height) {
     this.density = Density(height);
  }


  // From Fundamentals of Astrodynamics and Applications by David A Vallado, 2001
  //   Equation 8-34 on page 532 and table 8-4 on page 534
  public static double Density(double altitudeMeters) {
    if (!tableInitialized) {
      initializeTable();
    }
    double altitudeKM = altitudeMeters / 1000.0;
    int index = (int) altitudeKM/5;
    if (index < 0) {
      index = 0;                    // better safe than sorry
      altitudeKM = 0;
    }
    if (index > TableRows-1) {
      index = TableRows-1;          // We have to use 1000 km for any above that
    }
    double BaseAltitude = ourTable[index][IndexBaseAltitude];
    double NominalDensity = ourTable[index][IndexNominalDensity];
    double ScaleHeight = ourTable[index][IndexScaleHeight];
    double resultDensity = NominalDensity
                    * Math.exp(- (altitudeKM - BaseAltitude)/ScaleHeight);
    return(resultDensity);
  }


  // http://www.grc.nasa.gov/WWW/K-12/airplane/atmosmet.html
  public air(position pos) {
     this.setValues(pos);
  }

  public void setValues(position pos) {
     this.height = pos.height();
     this.pos = new position(pos);

     if (height <= 11000) {                         // troposphere
             temperature = 15.04 - .00649 * height;
             pressureKpa = 101.29 * java.lang.Math.pow(((temperature + 273.1) / 288.08), 5.256);
     }
     if ((height > 11000) && (height <= 25000)) {    // lower stratosphere
             temperature = -56.46;
             pressureKpa = 22.65 * java.lang.Math.exp (1.73 - 0.000157 * height);
     }
     if (height > 25000) {                           // upper statosphere
             temperature = -131.21 + .00299 * height;
             pressureKpa = 2.488 * java.lang.Math.pow(((temperature + 273.1) / 216.6), -11.388);
     }

     // Nasa
     // density = pressureKpa / (0.2869 * (temperature + 273.1));
     this.setDensity(height);
     this.vel = calcVelocity(this.pos);

  }

  // returns a velocity representing air at some hight
  static public velocity calcVelocity(position p1) {
     double height = p1.height();
     velocity v1 = new velocity(0,0);
     if (height > k.maxHeightSpinningAir) {
       return(v1);
     }
     double speed = k.earthSpinSpeed; // supposed to decrease with altitude
                                      // but don't know how much
/*
     double r = p1.magnitude();
     double xvec = p1.x/r;   // unit vector
     double yvec = p1.y/r;
     double xsign = 1;
     double ysign = 1;
     if (p1.x < 0) { ysign = -1; }    // on left going down
     if (p1.y > 0) { xsign = -1; }    // above going left
     v1.y = ysign * java.lang.Math.abs(speed * xvec); // xvec since 90 degrees off
     v1.x = xsign * java.lang.Math.abs(speed * yvec); // yvec since 90 off
*/

     double angle = Math.atan2(p1.y, p1.x);
     v1.x = speed * Math.cos(angle+ k.pi/2.0);
     v1.y = speed * Math.sin(angle+ k.pi/2.0);

     return(v1);
  }

  // http://www.grc.nasa.gov/WWW/K-12/airplane/sound.html
  public double getMachSpeed() {
      double speed;
      speed=java.lang.Math.sqrt(1.4 * k.gasConstant * getTempKelvin());
      return(speed);
  }

  public double getTempKelvin() {
      return(k.celsiusToKelvin(this.temperature));
  }
}

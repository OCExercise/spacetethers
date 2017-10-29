

package spacetethers;


import java.awt.*;

// http://www.grc.nasa.gov/WWW/wind/valid/wedge/wedge.html
  // http://www.grc.nasa.gov/WWW/wind/valid/wedge/oblshk.f
   /*
c        Iterates the oblique shock relations to determine the
c        properties behind an oblique shock. Assumptions: perfect gas.
c        Written by John W. Slater, NASA Lewis Research Center.
c
c-----------------------------------------------------------------------
   */

public class shockwave {
  public double mach1, wedgeAngleRad;
  public double mach2, shockAngleRad, p2p1, t2t1, r2r1;


  public shockwave(double mach1, double wedgeAngleDegrees) {
      this.wedgeAngleRad = k.degreesToRadians(wedgeAngleDegrees);
      this.mach1=mach1;

      sckobl();
  }

  // Passed in air characteristics before shockwave
  public double paint(Graphics g, double temperature, double pressure, double density) {
      double tempKelvin=0;

      String line1= " ShockAng= " + k.dTS2(k.radiansToDegrees(shockAngleRad));

      tempKelvin = t2t1*(k.celsiusToKelvin(temperature));
      if (t2t1 > 0) {
           line1 += " Press= " + k.dTS2(p2p1*pressure)
                  + " Temp= " +  k.dTS(k.kelvinToCelsius(tempKelvin)) + " C";
      }
      screen.print(g, line1);
      // screen.print(g,"Density ratio      = " + k.dTS2(r2r1) + " " + k.dTS2(r2r1*density));
      return(tempKelvin);
  }

/*
 *  Iterates the oblique shock relations to determine properties
 *  behind an oblique shock.  Assumptions: perfect gas.
 */
   private void sckobl () {
      double delt1, delt2, thet1, thet2, thet;
      double rmn1, rmn2, gp1, gm1, t1, t2;
      double error;

      thet1 = 1.2 * wedgeAngleRad;  // Choose an initial shock angle to be increment of wedge.
      delt1 = tbm ( mach1, thet1, k.shockGamma);
      thet2 = 1.02 * thet1;

      error = 1000 * k.shockErrorTolerance;
      while (error > k.shockErrorTolerance) {   // use secant iteration
        delt2 = tbm ( mach1, thet2, k.shockGamma );

        error = java.lang.Math.abs((delt2-java.lang.Math.tan(wedgeAngleRad))/java.lang.Math.tan(wedgeAngleRad));
        if (error > k.shockErrorTolerance ) {
          thet  = thet2 - (java.lang.Math.tan(wedgeAngleRad)-delt2)*(thet2-thet1)/(delt1-delt2);
          thet1 = thet2;
          delt1 = delt2;
          thet2 = thet;
        }
      }

      shockAngleRad = thet2;

     // Compute properties behind shock based on perfect gas.               .

      rmn1  = mach1 * java.lang.Math.sin( shockAngleRad );

      gp1   = k.shockGamma + 1.0;
      gm1   = k.shockGamma - 1.0;

      double rmn1sq = rmn1*rmn1;
      r2r1  = gp1 * rmn1sq / ( gm1 * rmn1sq + 2.0 );

      p2p1  = 1.0 + 2.0 * k.shockGamma * ( rmn1sq - 1.0 ) / gp1;

      t1    = ( rmn1sq + 2.0 / gm1 );
      t2    = ( 2.0 * k.shockGamma * rmn1sq / gm1 - 1.0 );
      rmn2  = java.lang.Math.sqrt( t1 / t2 );

      t2t1  = p2p1 / r2r1;

      mach2 = rmn2 / java.lang.Math.sin( shockAngleRad - wedgeAngleRad );
  }

/*
 *       Evaluates shockAngleRad-beta-mach relation for an oblique shock.
 */
   public double tbm ( double mach, double beta, double gammalocal) {
      double  c1, c2;

      c1  = mach * mach * java.lang.Math.sin(beta) * java.lang.Math.sin(beta) - 1.0;
      c2  = mach * mach * ( gammalocal + java.lang.Math.cos(2.0*beta) ) + 2.0;
      return( 2.0 * (1.0/java.lang.Math.tan(beta)) * ( c1 / c2 ));
   }
}

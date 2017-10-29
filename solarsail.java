package spacetethers;

import java.awt.*;


//  Checked that there is agreement between:
//      time of max and min acceleration
//      angle of sail
//      increasing apogee is opposite max thrust (so on side going toward sun)
//      sun angle
//      sun x,y position
//
//  The max acceleration fits with my book.
//  The spiral times also seem to fit.
//  Drag gets bad below 1000 km which matches wisdom from books.
//
//  Two good books for solar sailing are:
//   "Solar Sailing : Technology, Dynamics and Mission Applications"
//       by McInnes, 1999
//   "Space Sailing" by Jerome L. Wright
//
//  Appendix of "Space Sailing" has more detailed thrust formula,
//    but we are not using that (yet?).
//

public class solarsail extends simtype {
  mass solarsailMass=null;
  double solarSailArea;      // area of solar sail
  double gramsPerSqMeter;    // grams/m^2
  double totalKg;            // total Kg
  boolean retroThrust;       // true means lowering orbit
  double solarSailEfficiency;
  double solarSailKg;        // just sail


  // Rest of this is here so that paint() can use it
  double velocityAngle=0;     // testing
  double solarAngle=0;        // testing
  double solarSailKmOnSide=0; // We will assume square sail
  double sailWidth=0;
  double sailNormalAngle=0;         // relative to sun
  double thrustNewtons=0;           // current
  double maxAcceleration=0;         //
  double impulseNewtonSeconds=0;   // total change in momentum so far


  public solarsail(String label,
                double solarSailMetersOnASide, double gramsPerSqMeter,
                double solarSailEfficiency,
                boolean retroThrust) throws Exception {
     this.solarSailKmOnSide=solarSailKmOnSide;
     this.sailWidth =solarSailMetersOnASide;          // assume square sail
     this.solarSailArea = Math.pow(this.sailWidth, 2);
     this.gramsPerSqMeter = gramsPerSqMeter;
     this.solarSailKg = this.solarSailArea * gramsPerSqMeter / 1000.0;
     this.solarSailEfficiency = solarSailEfficiency;
     this.label = label + ".solarsail";
     solarsailMass=findsimobject.labelToMassOrDie(label);

     solarsailMass.kg += solarSailKg;
     solarsailMass.dragArea= solarSailArea;
     solarsailMass.liftOverDrag=0;
  }

  public void simulate1(double deltaT)  {
      solarsailMass.simulate1(deltaT);
  }
  public void simulate2(double deltaT){
      solarsailMass.simulate2(deltaT);
      addSolarSailForce(deltaT);         // sets thrust force and drag area
  }
  public void simulate3(double deltaT) throws Exception {
     if (solarsailMass.pos.height() > 0) {
       solarsailMass.simulate3(deltaT);   // if on the ground don't move
     }
  }



  // goes to mass.totalForce
  private void addSolarSailForce(double deltaT) {
    thrustNewtons = 0.0;

   // atan2 in Java has arguments (y,x) which is backwards from
   //  anyone else who thinks in (x,y) coordinates.
    velocityAngle = Math.atan2(solarsailMass.vel.y,
                               solarsailMass.vel.x);
    this.solarAngle = Math.atan2( 0.0, -1.0 * k.OneAU); // Sun far to left

    // "Solar Sailing : Technology, Dynamics and Mission Applications"
    //  by McInnes, 1999, page 157, eq. 4.100
    this.sailNormalAngle = 0.5 *
        (velocityAngle - Math.asin( (Math.sin(velocityAngle) / 3.0)));

    //  "Space Sailing" by Jerome L. Wright, Page 2 for next formula.
    //  If not 1 AU from sun would need to divid by distance in AU squared
    double uNewtonsPerSqMeter = 9.126 * Math.pow(Math.cos(sailNormalAngle), 2.0);

    thrustNewtons = this.solarSailArea * uNewtonsPerSqMeter / 1000000.0;
    if (retroThrust) {
      thrustNewtons = -thrustNewtons;
    }
    if (solarsailMass.inShadow()) {
      thrustNewtons=0;
    }
    thrustNewtons = thrustNewtons * this.solarSailEfficiency;
    solarsailMass.totalForce.addAtAngle(thrustNewtons, sailNormalAngle);

    // Want effective width in direction of velocity for drag area
    // Not sure this is exactly right, but I don't plan on sailing where
    //   drag matters.
    double alignmentFactor = Math.cos(velocityAngle - sailNormalAngle);
    this.solarsailMass.dragArea = this.solarSailArea
                                         * (0.1 + 0.9 * alignmentFactor);

    // Estimate of total useful thrust so far
    this.impulseNewtonSeconds += thrustNewtons  * alignmentFactor * deltaT;
  }


  public void paint(Graphics g) {
   //  solarsailMass.paint(g);
     screen.drawAngledLine(g, solarsailMass.pos.x, solarsailMass.pos.y,
                           sailWidth, 0.5*k.pi + sailNormalAngle);
     screen.print(g, label + " : ");
  //   if (this.solarSailArea > 1) {
   //    return;
   //  }

//     screen.print(g, "sailNormalAngle " +    k.dTS2(k.radiansToDegrees(sailNormalAngle)));
//    screen.print(g, "sailWidth " +    k.dTS2(sailWidth));
//     screen.print(g, "sail Kg " +       k.dTS2(this.solarSailKg));
//     screen.print(g, "payload Kg " +    k.dTS2(this.solarSailPayloadKg));
//     screen.print(g, "totalKg " +       k.dTS2(this.totalKg));
//     screen.print(g, "solarSailArea " + k.dTS2(this.solarSailArea));
//     screen.print(g, "gramsPerSqMeter " + k.dTS2(this.gramsPerSqMeter));
//     screen.print(g, "velocityAngle " + k.dTS2(k.radiansToDegrees(this.velocityAngle)));
//     screen.print(g, "Sun is left: solarAngle " + k.dTS2(k.radiansToDegrees(this.solarAngle)));
     screen.print(g, "thrust Newtons " +       k.dTS2(this.thrustNewtons));
     screen.print(g, "impulseNewtonSec " +     k.dTS2(this.impulseNewtonSeconds));

     double acceleration = thrustNewtons/solarsailMass.kg;
     if (acceleration > maxAcceleration) {
        maxAcceleration = acceleration;
     }

     screen.print(g, "Acceleration in mm/sec^2:  " + k.dTS3(acceleration*1000.0));
     screen.print(g, "maxAccleration  mm/sec^2:  " + k.dTS3(maxAcceleration*1000.0));
     if (k.logThrust) {
       k.outputAdd(this.label +
          " time "  + k.dTS2(k.simTime)  +
          " mmPerSec2 " + k.dTS3(acceleration*1000.0) +
          " Thrust " + k.dTS3(thrustNewtons) +
         "\n");
     }
   }

}

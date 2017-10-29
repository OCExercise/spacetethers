package com.spacetethers;

import java.awt.*;
/**
 * WARNING - not finished/trusted code
 *
 We change the L/D and dragArea on a mass to simulate a
 capsule tilting to reduce peak G load.

 From initial plung we pull out to level flight then
 gradual desent.

 If Gs are too high we reduce angle of attack.

 If we are going to fast and are in danger of skipping out
 of the atmosphere we could make the lift negative (Apollo
 did this) but not doing so yet.

For a single stage to tether it would be nice if the
 bottom of the tether was high enough that we did not
 have to worry about the atmosphere hurting the tether.
 At 150 km is high enough, but then simple fixed angle
 reentry has too high a G load.  But with smartreentry
 we can reduce the G loads to where 150 km is ok.

 We also use this to do something like the Apollo reentry where
it minimizes Gs.  After initial plung you want to do constant altitude.
Then you get onto the ideal glide path.


Somewhere on the net it said that "Spaceflight Dynamics"
 by William E. Wiesel, p.p. 230-232 said:
 >Since the Apollo capsule could not change its effective angle
 >of attack, it always produced lift.  So, once the landing point
 >was correctly targeted, it was necessary to place the capsule
 >in a slow roll about the velocity vector to average out the now
 >undesired lift.

 This would make the drag the same, just that the lift
 was no longer making it go up.

 A smartreentry.java is around mass.java

 */

public class smartreentry extends simtype {
  mass ourMass;
  double maxDragArea;        // from mass
  double maxGsTarget;

  double attackAngle=0;      // 0 to pi/2 - angle of attack
  double maxLODangle;        // we search for this
  double maxCdAngle = k.pi/2.0;  // On side with max drag
  double liftOverDrag;
  double dragArea;
  double Cd;
  double dragGs;           // mass will calculate and report this in paint
  double lastHeight;       // From last deltaT to see if going up or down

  public smartreentry(double maxGsTarget, String label) throws Exception {
     this.maxGsTarget = maxGsTarget;
     this.label = label + ".sr";
     ourMass = findsimobject.labelToMassOrDie(label);
     maxDragArea = ourMass.dragArea;

     double a, maxLOD;
     maxLOD=-1;
     for (a=0; a<k.pi/2.0; a+= k.degreesToRadians(0.1)) {
        setForAngle(a);
        if (this.liftOverDrag > maxLOD) {
          maxLOD = this.liftOverDrag;
          maxLODangle = a;
        }
     }
  }


  // Page 132 and 133 of Re-entry and Planetary Entry I
  // Mass.java sets liftAndDrag force in simulate2 so we use in simulate3
  public void simulate3(double deltaT) {
     double dragF = ourMass.liftAndDrag.magnitude();
     dragGs = dragF/ourMass.kg/k.earthAcceleration;  // ourMass.paint() also calculates
     if (dragGs > this.maxGsTarget) {
       attackAngle -= k.degreesToRadians(0.1);     // take off 1/10th degree
     } else {
       attackAngle += k.degreesToRadians(0.1);
     }

     if (attackAngle < k.degreesToRadians(30)) {
       attackAngle=k.degreesToRadians(30);
     }
     if (attackAngle > this.maxLODangle) {
       attackAngle = this.maxLODangle;
     }
     setForAngle(attackAngle);

      ourMass.dragArea = dragArea;
      ourMass.liftOverDrag = liftOverDrag;
      ourMass.Cd = Cd;
  }

  private void setForAngle(double a) {
     double Cdmax = 1.7;          // page 133

     double Cdo = 0.4 * Cdmax;    // 0.3 estimate from Fig 2-38 on page 134

     double b = Cdo / (Cdmax - Cdo);  // check book - I put in () after
     double sinA = Math.sin(a);
     double sinA2 = sinA * sinA;
     double sinA3 = sinA * sinA2;
     Cd = Cdo + (Cdmax - Cdo)*sinA3;   // page 132
     liftOverDrag = sinA2 * Math.cos(a) / (b + sinA3);
     dragArea = maxDragArea * (0.2 + 0.8*sinA);  // can go down to 20%
     dragArea = maxDragArea;
  }

  public void paint(Graphics g) {
     // rocketMass.paint(g);

     String line1=  "SmartReentry  "
              + " Angle of Attack " + k.dTS2(k.radiansToDegrees(attackAngle))
              + " L/D " + k.dTS2(liftOverDrag)
              + " CD " + k.dTS2(Cd)
              + " dragArea  " + k.dTS2(dragArea)
              + " dragGs " + k.dTS2(dragGs);

     screen.print(g, line1);


  }




  public void setOrigin() {
     //this.centerOfMass.setOrigin();
  }
}

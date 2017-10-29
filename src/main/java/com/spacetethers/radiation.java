package com.spacetethers;

import java.awt.*;
import java.lang.Math;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class radiation extends simtype {
  double radKg;           // mass of the particle
  double radCharge;       // charge of the particle
  vector3d position;
  vector3d velocity;
  String radView;
  double initVmag;

   // to hold the vectors without allocating each time
  vector3d cross= new vector3d(0,0,0);
  vector3d acceleration= new vector3d(0,0,0);
  vector3d magneticField= new vector3d(0,0,0);
  vector3d force= new vector3d(0,0,0);
  vector3d estimatedMidPosition = new vector3d(0,0,0);
  vector3d estimatedVelocity = new vector3d(0,0,0);

  public radiation(String label, double radKg, double radCharge,
                   String radView,
                   vector3d position, vector3d velocity) throws Exception {
    boolean posBad = (position==null);
    boolean velBad = (velocity==null);
    if (posBad || velBad) {
      throw new Exception("radiation needs good positon and velocity "
                          + " pos " + posBad
                          + " vel " + velBad);
    }
    this.label=label;
    this.radKg=radKg;
    this.radCharge=radCharge;
    this.radView=radView;
    this.position = new vector3d(position);
    this.velocity = new vector3d(velocity);
    this.initVmag=velocity.magnitude();
 }

  //  We had to estimate a mid position and velocity to get an
  //    accurate enough magnetic field, cross product, and
  //    acceleration to get reasonable results.  The accelerations
  //    are huge (like 9 billion Gs) and so the usual simple
  //    time slice approach is not good enough.  You end up
  //    increasing the velocity/energy of the particles that way.
  public void simulate2(double deltaT) {
    // First we go halfway to get get our estimatedMidPosition
    estimatedMidPosition.set(position);
    estimatedVelocity.set(velocity);
    geomagnetic.set(magneticField, position); // set magneticField
    cross.setCross(velocity, magneticField);  // cross = VxB
    estimatedVelocity.addScaled(cross, 0.5*deltaT*radCharge/radKg);
    estimatedMidPosition.addScaled(estimatedVelocity, 0.5*deltaT);     // P+=V*T

    // Then we use that position and velocity to get acceleration used
    geomagnetic.set(magneticField, estimatedMidPosition); // set magneticField
    cross.setCross(estimatedVelocity, magneticField);  // cross = VxB

//    force.setScaled(cross, radCharge);        // F = Charge * VxB
//    acceleration.setScaled(force, 1.0/radKg); // F=M*A  A=F/M
//    velocity.addScaled(acceleration, deltaT); // V+=A*T

    velocity.addScaled(cross, deltaT*radCharge/radKg); // above 3 lines
    position.addScaled(velocity, deltaT);     // P+=V*T
  }


  public void paint(Graphics g) {
    if (radView.equals("side")) {
        screen.drawDot(g, position.x, position.z);
        screen.drawString(g, label, position.x, position.z);
    }
    if (radView.equals("top")) {
        screen.drawDot(g, position.x, position.y);
        screen.drawString(g, label, position.x, position.y);
    }
    if (!showText) {
      return;
    }
    double vmag = velocity.magnitude();
    double pmag = position.magnitude();
 //   double mmag = magneticField.magnitude();
 //   double amag = acceleration.magnitude();
 //   double cmag = cross.magnitude();

    screen.print(g, label + " (" + radView +") "
               + " Position radius " + k.dTSS10(pmag)
  //             + " Position Lvalue: " + position.toString(k.earthRadius)
               + " Position meters: " + position.toString()
 //              + " mag-f " + k.dTSS(mmag)
               );
//  screen.print(g,
 //              " deltaV " + k.dTSS10(initVmag - vmag)
 //              + " acc in Gs " + k.dTS2(amag/k.earthAcceleration)
 //              + " angle V-A "
 //              + k.dTSS10(lastVelocity.angleDegrees(acceleration))
 //     );

    double E = 0.5 * radKg * vmag * vmag; // E = 1/2 M V^2
    double KeV = E * k.JtoKeV;
    screen.print(g, " KeV: " + k.dTSS(KeV)
 //                + " Vel  C-mag "
 //                + k.dTSS(vmag/k.speedOfLight) + "  C: "
 //                + velocity.toString(k.speedOfLight)
                 + "  speed m/s "  + k.dTSS10(vmag)
                 + "  velocity m/s: " + velocity.toString());
  }
}

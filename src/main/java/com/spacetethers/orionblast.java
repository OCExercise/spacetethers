package com.spacetethers;

import java.awt.*;

/**
 * User should have 3 tethers spinning around in line.
 * Add mass for a pusher plate/parachute to a special strong tether in middle.
 * Then when called by AtTime we add an impulse to this plate in Z direction.
 */

public class orionblast extends simtype {
  double pusherStdDev;  // length along tether of pusher area
  double blastStdDev; // distance below center of mass on z axis
  double ISP;           // effective ISP
  double pusherLength;
  double pusherKg;      // Mass of pusher plate etc added to tether
  tether ourTether;     // tether we are part of

  double maxRelV=0;
  double minRelV=100000;
  double timeMaxRelV=0;
  double timeMinRelV=0;

  mass pusherMasses[] = new mass[5000]; // all the masses in the Pusher Plate
  double pusherShares[] = new double[5000]; // share each slice of mass or impulse
  int numMasses = 0;

  position blastPosition = new position(0, 0);
  double totalImpulse; // Newton-Seconds for all time

  // Input effective ISP, blastDistance, pusherMass
/*
      Add the impulse to the tether mid section and see what happens.
      Easy except we are 3D.
      I would have the thrust against the plane of the spin.
      Makes me want to do
    3D
      tethers.
      Any mass that can handle 100Gs goes in middle.Humans
      out at the ends of the tether.
*/
      public orionblast(String label, String tetherLabel,
                       double blastStdDev, double ISP, double pusherLength,
                        double pusherStdDev, double pusherKg
                        ) throws Exception {
    this.label = label;
    this.ourTether = (tether) findsimobject.labelToSimObjectOrDie(tetherLabel);
    this.blastStdDev=blastStdDev;
    this.ISP=ISP;
    this.pusherLength=pusherLength;
    this.pusherStdDev=pusherStdDev;
    this.pusherKg=pusherKg;


    // NYC need to center pusher plate on tether
    numMasses=0;
    ourTether.setCMIndex();
    position cmp = ourTether.centerOfMassMass.pos;
    for (int i = 0; i < ourTether.numMasses; i++) {
      if (ourTether.tetherMass[i].pos.distance(cmp) < pusherLength/2.0){
            this.pusherMasses[numMasses] = ourTether.tetherMass[i];
            numMasses++;
      }
    }

    calcShares(pusherStdDev);


     // Add in weight of Pusher Plate wire
    for (int i = 0; i < numMasses; i++) {
      double nextKg;
      nextKg = pusherKg * pusherShares[i];
//      nextKg = pusherKg / numMasses;
      pusherMasses[i].kg += nextKg;
      k.debug(" pusherMass " + i + " = " + k.dTS3(nextKg));
    }

    ourTether.setCMIndex();
    calcShares(blastStdDev);
  }

  public void simulate1(double deltaT) {
  }

  private void calcShares(double StdDev) throws Exception {
    int i;
    double totalRelative=0;

    double stdDev = numMasses / StdDev;
    double midWay = numMasses / 2.0;
    double stdDev22 = 2 * stdDev * stdDev;
    for (i = 0; i < numMasses; i++) {
      double diff= i - midWay;
      double diff2 = diff*diff;
      double y = Math.exp( -1 * diff2/stdDev22);
      pusherShares[i] = y;
      totalRelative += y;
    }
    // Normalize
    for (i = 0; i < numMasses; i++) {
      pusherShares[i] = pusherShares[i] / totalRelative;
    }
  }
  // called from AtTime do add blast impulse
  //   during simulate1() time
  //  Blast is always fixed distance from center of mass
  //    down in the Z direction
  //  Share of total force depends on distance and angle from blast
  //  We figure a relative number for each slices share
  //     We add these all up and then knowing the total impulse
  //       from ISP we can compute impulse for this slice.
  public void doBlast(double Kg) {
     double totalImpulse;
     double impulse, deltaV;

     totalImpulse = Kg * ISP * k.earthAcceleration;
     for (int i = 0; i < numMasses; i++) {
        impulse = pusherShares[i] * totalImpulse;
        deltaV = impulse / pusherMasses[i].kg;
        pusherMasses[i].vel.z += deltaV;
     }
  }


 // NYC could draw tether using dist(mass[0]),Z but centered on center of mass
 //  This way we can see how much it is flexing
  public void paint(Graphics g) {
     double totalZmomentum=0;
     double totalMass=0;
     double totalZenergy=0;
     for (int i = 0; i < ourTether.numMasses; i++) {
       double kg = ourTether.tetherMass[i].kg;
       double v  = ourTether.tetherMass[i].vel.z;
       totalZmomentum += kg * v;
       totalZenergy += 0.5 * kg * v*v;
       totalMass += kg;
     }
     double averageVelz = totalZmomentum/totalMass;
     double pushVel = ourTether.centerOfMassMass.vel.z;
     double pushRelV = pushVel-averageVelz;
     if (pushRelV > maxRelV) {
       maxRelV = pushRelV;
       timeMaxRelV = k.simTime;
     }
     if (pushRelV < minRelV) {
       minRelV = pushRelV;
       timeMinRelV = k.simTime;
     }
     String line1=  label + " : pusher velocity "
                  + k.dTS3(pushVel)
            //      + " totalZmomentum " + k.dTS(totalZmomentum)
                  + " velz " + k.dTS3(averageVelz)
                  + " pushRelV " + k.dTS3(pushRelV)
           //       + " MTenergy " + k.dTSS10(totalZenergy/k.JoulesPerMegaton)
                  + "  numMasses " + numMasses
                + " pusherStdDev " + pusherStdDev;
    screen.print(g, line1);
        line1=  " timeMaxRelV " + k.dTS2(timeMaxRelV)
               + " maxRelV " + k.dTS2(maxRelV)
               + " timeMinRelV " + k.dTS2(timeMinRelV)
               + " minRelV " + k.dTS2(minRelV);

    screen.print(g, line1);
    line1 =  " blastStdDev " + blastStdDev
                + " ISP " + ISP
                + " pusherKg " + pusherKg
                + " ourTether " + ourTether.label;
     screen.print(g, line1);

     int mins = ourTether.minSpring;
     int maxs = ourTether.maxSpring;
     double f1 = ourTether.tetherSpring[mins].tension;
     double f2 = ourTether.tetherSpring[maxs].tension;
     line1 = " ballast stretch " + k.dTS2(ourTether.tetherSpring[mins].stretch)
             + " force " + k.dTS(f1)
             + " Gs " + k.dTS2((f1/ourTether.ballast.kg)/k.earthAcceleration)
             + " payload stretch " + k.dTS2(ourTether.tetherSpring[maxs].stretch)
             + " force " + k.dTS(f2)
             + " Gs " + k.dTS2((f2/ourTether.payload.kg)/k.earthAcceleration);
     screen.print(g, line1);

     if (k.simTime < 1.0) {
       line1 = " Showing pusherShares distribution - 10000x";
       screen.print(g, line1);

       //  Draw the shape of pusherShares curve
       mass sm = ourTether.ballast;
       mass cm = ourTether.centerOfMassMass;
       double halfx = cm.pos.distance(sm);
       // We plot distance along tether vs Z coordinate centered on screen
       double omx = k.originMass.pos.x;
       double omy = k.originMass.pos.y;
       for (int i = 0; i < numMasses; i++) {
         double x = pusherMasses[i].pos.distance(sm) - halfx;
         double y = pusherShares[i] * k.scaleYMeters;
         screen.drawDot(g, x + omx, y + omy);
       }
     }
  }
}

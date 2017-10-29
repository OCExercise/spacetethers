package com.spacetethers;

import java.awt.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 * starting solar watts/kg and kg
 * starting tether mass
 * starting other mass
 * solar watts/kg
 * tether capacity
 * tether mass
 * tethers we are building - list
 *     total mass
 *     payload capacity
 * payload
 *     total mass
 *     what it is - how much tether and how much solar
 * minimum solar - for course correction
 * time from toss to catch
 * total deltaV  for first and second catch and aerobreaking
 *
 * could specify the deltaV and have us  calculate the tether Mass.
 *
 * Fixed amount for electronics and a percentage for coarse correction.
 *
 * old tether is part of new ballast
 *    could specify target ballast and just have it keep lifting
 *    solar till we get there.
 *
 *
 * AtTime Bootstrap  - just sends the rest of the string to us
 *    We generate strings to put into queue
 *    Could put objects on there - maybe better.
 */

public class bootstrap extends simtype {
  double tetherMaxPayloadKg;   // most tether can hold at designed velocity
  double minVelForCatch;       // must be moving at least this fast to catch
  double totalSolarWatts;      // total solar watts in ballast

  double targetSolarKg;        // solarKg in this target
  double targetTetherKg;       // tetherKg in this target
  double targetTotalKg;        // totalKg in this target

  double newSolarKg;           // solarKg in this target done
  double newTetherKg;          // tetherKg in this target done
  double newTotalKg;           // totalKg in this target done

  double orbitSolarKg;         // solar in flight or at ballast
  double orbitTetherKg;        // tether in flight or at ballast
  double orbitTotalKg;         // total in flight or at ballast

  double timeLastLaunch=0;
  double totalBallastKg;       // all mass at ballast
  double tetherNormAngle;
  double tetherBackAngle;
  double wattsPerKg;
  mass ourMass;                 // All mass simulated as single mass
  boottarget currentTarget;     // What we are working on now
  Vector targetQueue = new Vector();  // All of targets
  double currentAmps;          // There should be an EDT using this

  public bootstrap(String label) {
    this.label=label;
  }

  // Called from attime
  // A previously tossed payload has arrived
  // Form is:
  //   JustIn  Solar xx Tether yy Other zz
  public void handleEvent(String event) throws Exception {
     StringTokenizer parseLine = new StringTokenizer(event);
     String JustIn    = parseLine.nextToken();
     String Solar     = parseLine.nextToken();
     double SolarKg   = k.readDouble(parseLine.nextToken());
     String Tether    = parseLine.nextToken();
     double TetherKg  = k.readDouble(parseLine.nextToken());
     String Other     = parseLine.nextToken();
     double OtherKg   = k.readDouble(parseLine.nextToken());
     String EndAtTime = parseLine.nextToken();
     if (!JustIn.equals("JustIn") ||
         !Solar.equals("Solar") ||
         !Tether.equals("Tether") ||
         !Other.equals("Other") ||
         !EndAtTime.equals("EndAtTime")) {
       throw new Exception("bootstrap.handleEvent got non parsable string");
     }

     this.totalSolarWatts += SolarKg * this.wattsPerKg;
     this.newSolarKg += SolarKg;
     this.newTetherKg += TetherKg;
     double newKg = SolarKg + TetherKg + OtherKg;
     this.newTotalKg += newKg;
     this.ourMass.kg += newKg;
     this.totalBallastKg += newKg;  // When finished the new tether will
                                    // come out of ballast and old in.

     checkIfAtTarget();
     // NYC  need to adjust momentum
  }

  public void checkIfAtTarget() {
     if ((newSolarKg  < targetSolarKg) &&
        (newTetherKg  < targetTetherKg) &&
        (newTotalKg   < targetTotalKg)) {
      return;  // Not done yet
    }
    // NYC need to put in new tether and adjust ballasts
  }

  // These are the targets for different tether/solar situations.
  public void addTarget (boottarget nt) {
     targetQueue.addElement(nt);
  }

  // We look to see if we are ready to accept a new payload
  //  and if so act like others new this ahead of time and launched a rocket
  //  so we can catch it just now.
 /*
 *
 *
 * rocket equation to compute the payload velocity
 *     assume tip-speed is that velocity - should allow for energy?
 *
 * momentumDeficit = deltaV * payloadMass
 * EdtThrust comes from edt.java but could have been
 *   approximated wtih 0.89 Newtons / currentSolarKW - sun out 63% of time
 *
 * NYC:
 * Launch/Catch point is relative to a rotating Earth.  Only
 * want to catch when sun is out so we can power EDT.
 */
  public void simulate1(double deltaT) {

      if (timeLastLaunch > (k.simTime - 2000.0)) {
         return;    //  Don't launch if just fired one off
      }

      if (ourMass.vel.magnitude() < minVelForCatch) {
        return;     // Don't launch if ballast does not have momentum
      }

      double nextPayload=0;
      if (totalBallastKg / 15.0 < 0.5 * tetherMaxPayloadKg) {
        nextPayload = totalBallastKg/15.0;
      }
      if (totalBallastKg / 30.0 >=  tetherMaxPayloadKg) {
        nextPayload = tetherMaxPayloadKg;
      }
      if (nextPayload == 0) {
        return;
      }

      double nextSolar, nextTether, nextOther;
      nextSolar=0;
      if (orbitSolarKg < currentTarget.solarWatts/wattsPerKg) {
         nextSolar = 0;
      }
      if ((nextSolar < nextPayload)){

      }


  }

}

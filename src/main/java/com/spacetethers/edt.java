package com.spacetethers;

import java.awt.*;

/**

  We only thrust within 45 of vertical and only when in Ballast in sunlight.
  We add our force to the masses on an existing tether.
  We always start at ballast end and go specified length.
  We assume that EDT is 5 parts shielded and 1 part at each end unshielded.
  When we find we just need to keep finding more till
    we are as long as requested.

  Shifting center of mass during construction could be a problem for EDT,
  but can compensate as half the time we thrust the other way.

           diameter of wire - resistance and voltage
                   page 424 of space mission analysis and design
                       power - wiring up a space station
                       cable mass vs current

   Wire is always aluminum
    Calculate weight

   Future:
       Model electron density - only in day for us -
       Input watts and we figure out current and needed wire size

    ttpaper.pdf  - terminator tether - current, voltages, etc.
                   Comes up with slightly different answers.

    In our design we only thrust during the day because we don't
 want to have to carry lots of batteries, and because we can arrange
 our pickups so that we only need thrust on the daylight side.  It
 turns out that EDTs designed to work at night need longer bare
 wires, maybe 4 times as long, so this is another reason to like
 the idea of only thrusting during the day.

They used a ribbon, 10 mm wide by 0.6 mm thick (page 4, 5 in PDF counting).
  They were working on a 5 kW-10 kW power supply, to produce 0.5-0.8 N
 of thrust (to keep up the ISS).  They mention 10 amp currents a couple
 of times, not clear how serious that number is.  On page 3 (PDF 4)
 there is a mention that a 2.5 km with a 2mm radius, could collect 10 amp
 current with a bias voltage of 100 V.  I've mentally used that yardstick.
 My translation is that we could expect 4 amps per km with 100 V or
 more bias voltage.  (This is for a particular set of conditions - just
 useful for back of the envelope calculations.)

They make the assertion, on page 3 (4 in PDF counting) that current
 varies as Lb^(3/2), where Lb is the length of the bare part of the
 * wire.  That implies that the current collection above is not linear,
 * but more effective where the voltage is higher.  I hacked a simple
 * Excel spread sheet.  Extending the 10 amp for 2.5 km number above,
 * I think we can collect 40 amps with only 6.3 km of bare tether.
 * (I'm willing to use 10 km if we want some safety margin.  Not sure
 * where the bare tether hangs out.  For simulation purposes, I don't care.)

The bare tether will provide some thrust.  The first km will provide
 almost full thrust, the last km will have very low current, and
 provide little or no thrust.  If the current dropped off linearly,
 half the bare length would be counted as the length of the EDT tether
 used in thrust calculations.


 */

public class edt extends simtype {

  mass edtArray[] = new mass[2000];        // all the masses in the EDT
  int numMasses=0;
  double totalLength;
  double current;
  double diameter;
  boolean TetherNotBoot=true;
  tether ourTether;       // tether we are part of
  bootstrap ourBoot;      // part of a bootstrap simulation
  mass firstMass;         // first of EDT
  mass lastMass;          // last of EDT
  mass centerOfMass;      // for whole spectra tether
  double totalNewtons;    // for last deltaT
  double maxNewtons;      // best we have seen
  double totalImpulse;    // Newton-Seconds for all time
  double totalKg;         // For tether system

  public edt(String label, double totalLength, double current,
              double diameter) throws Exception {
    this.label=label + ".edt";
    simtype ourObject = findsimobject.labelToSimObjectOrDie(label);
//    if (ourObject.getClass().equals(this.ourTether)) {
      this.ourTether= (tether) ourObject;
      this.ourBoot=null;
      this.TetherNotBoot=true;
//    } else {
//      this.ourBoot = (bootstrap) ourObject;
//      this.ourTether=null;
//      this.TetherNotBoot=false;
//    }
    this.totalLength=totalLength;
    this.current=current;
    this.diameter=diameter;


    mass m1;
    boolean done=false;

    if (TetherNotBoot) {
      for (int i = 0; !done && i < 2000 && i < ourTether.maxMass; i++) {
        this.edtArray[i] = ourTether.tetherMass[i];
        if (edtArray[0].pos.distance(edtArray[i].pos) >= totalLength) {
          done = true;
          this.numMasses = i + 1;
        }
      }
    }

    double Kg = calcKg(diameter, totalLength);
    if (!TetherNotBoot){
      ourBoot.ourMass.kg += Kg;
      this.firstMass = ourBoot.ourMass;
      this.centerOfMass = ourBoot.ourMass;
    }

    if(TetherNotBoot) {
      double sliceKg = Kg / numMasses;
      for (int i = 0; i < numMasses; i++) {
        edtArray[i].kg += sliceKg; // Add in weight of EDT wire
      }

      ourTether.setCMIndex();
      this.totalKg = ourTether.grandTotalKg;

      this.firstMass = edtArray[0];
      this.lastMass = edtArray[numMasses - 1];
      this.centerOfMass = findsimobject.labelToMassOrDie(label +
           ".CenterOfMass");
    }

  }

  public void simulate1(double deltaT) {

  }

  // Page 184 of Tethers in Space Handbook, 3rd edition, dec 1997
  public void simulate2(double deltaT) {
    double tetherNormAngle = 0;
    double tetherBackAngle = 0;

    boolean normPower=false;
    boolean backPower=false;
    double dL = totalLength/numMasses;   // approximate for now

    if (TetherNotBoot) {
      tetherNormAngle = Math.atan2(firstMass.pos.y - lastMass.pos.y,
                                          firstMass.pos.x - lastMass.pos.x);
      tetherBackAngle = Math.atan2(lastMass.pos.y - firstMass.pos.y,
                                          lastMass.pos.x - firstMass.pos.x);
    } else {
      tetherNormAngle=ourBoot.tetherNormAngle;
      tetherBackAngle=ourBoot.tetherBackAngle;
    }
    double clockAngle = Math.atan2(centerOfMass.pos.y,
                                   centerOfMass.pos.x);

    double offVerticalAngle = clockAngle - tetherNormAngle;
    normPower = (Math.abs(offVerticalAngle) < k.pi/4.0);
    if (!normPower) {
      offVerticalAngle = clockAngle - tetherBackAngle;
      backPower = (Math.abs(offVerticalAngle) < k.pi / 4.0);
    }
    boolean inSunlight = !firstMass.inShadow();
    totalNewtons=0;
    if (inSunlight && (normPower || backPower)) {     // power range
      double velocityAngle = clockAngle + k.pi / 2.0;
      if (TetherNotBoot) {
        double effectiveDl = dL * Math.cos(offVerticalAngle);
        int i;
        for (i = 0; i < numMasses; i++) {
          double radius = edtArray[i].pos.magnitude();
          double goodForce = Math.abs(current * magneticField(radius) *
                                      effectiveDl);
          edtArray[i].totalForce.addAtAngle(goodForce, velocityAngle);
          totalNewtons += goodForce;
        }
      }
      if (!TetherNotBoot) {
        double effectiveLength = totalLength * Math.cos(offVerticalAngle);
        double radius = ourBoot.ourMass.pos.magnitude();
        double goodForce = Math.abs(ourBoot.currentAmps
                                    * magneticField(radius)
                                    * effectiveLength);
        ourBoot.ourMass.totalForce.addAtAngle(goodForce, velocityAngle);
        totalNewtons += goodForce;
      }
      if (totalNewtons > maxNewtons) {
        maxNewtons=totalNewtons;
      }
    }

  }

  // All EDT wire is Aluminum for now and maybe forever
  private double calcKg(double diameter, double length) {
    double densityAl=2707;   // Aluminum is Kg/meter^2
    double radius = diameter/2.0;
    double area  =  k.pi * radius * radius;
    double Kg = area * length * densityAl;
    return(Kg);
  }

  public void simulate3(double deltaT) {
    this.totalImpulse += this.totalNewtons * deltaT;
  }

  // Average magnetic field over Equator
  // Input is radius in meters from center of earth.
  // Formula comes from page 184 of Tethers In Space, 3rd edition, Dec 1997
  //
  private static double magneticField(double radius) {
     double MicroTeslas = 29 * Math.pow((k.earthRadius/radius), 3.0);
     double Teslas = MicroTeslas / 1000000.0;
     return(Teslas);
  }

  public void paint(Graphics g) {

     // F=MA    so A = F/M
     double acceleration = totalNewtons / totalKg;
     // Converting from meters/sec+^2 to meters/sec/day
     double accPerDay = acceleration * k.secsPerDay;
     String line1=  label + " : "
                  + " TotalImpulse "   + k.dTS2(this.totalImpulse)
                  + " acc " + k.dTS2(accPerDay) + " meters/sec/day "
                  + " MaxNewtons "     + k.dTS2(this.maxNewtons)
                  + " CurrentNewtons " + k.dTS2(this.totalNewtons);

     screen.print(g, line1);

  }

}

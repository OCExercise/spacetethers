

package com.spacetethers;

import java.awt.*;
/*

T = mC
T = Thrust in Newtons
m = propellant mass flow in kg
C = exhaust velocity in m/s
Impulse = Thrust * time
ISP = Impulse / (dM * g)
    = seconds a pound of propellant can produce a pount of thrust

I recommend section 14.1, "Rocket Science", pages 533 to 539, in the book
"Understanding Space - And Introduction to Astronautics", 2nd Edition,
 Jerry Jon Sellers, 2000.


Rocket object is going to have a mass object that already exists and
   is also getting called by simulation loop.

  */

public class rocket extends simtype {
  mass rocketMass=null;
  double Cd;
  double dragArea;
  double liftOverDrag;
  double exhaustVelocityMPS; // instead of ISP use meters per second
  double dryKg;
  double fuelKg;         // Kg
  double massFlowRate;     // Kg/second
  double parachuteCd;
  double parachuteArea;
  double parachuteHeight;
  double timeOfIgnition;
  double thrust=0;         // not local so can use in paint()
  double Gs=0;
  double maxGs=0;          // for paint()
  boolean retroThrust;
  int stabilizationType;
  double spinAngle;         // only if spin stabalized
  boolean stageStaysAttached=false;
  double ispDegradedAtOneAtm;

  public int stage;
  private int largestStage=0;
  static int FIN=1;
  static int SPIN=2;
  static int CIRC=3;

 // rocket nextStage=null;  // we could just become the next stage...

  public rocket(double Cd, double dragArea, double liftOverDrag,
                double dryKg, double fuelKg,
                double thrust, double exhaustVelocityMPS,
                double parachuteCd, double parachuteArea,
                double parachuteHeight, double timeOfIgnition,
                boolean retroThrust, double ispDegradedAtOneAtm,
                String stabilizationType,
                double spinAngle, boolean stageStaysAttached,
                String label) throws Exception {
     rocketMass=findsimobject.labelToMass(label);
     if (rocketMass == null) {
       throw new Exception("Rocket needs a mass to exist first, but "
                           + label + " does not exist.");
     }
     this.Cd=Cd;
     this.dragArea=dragArea;
     this.liftOverDrag=liftOverDrag;
     this.dryKg=dryKg;
     this.fuelKg=fuelKg;
     this.massFlowRate = thrust/exhaustVelocityMPS;
     this.exhaustVelocityMPS= exhaustVelocityMPS;
     this.parachuteCd=parachuteCd;
     this.parachuteArea=parachuteArea;
     this.parachuteHeight=parachuteHeight;
     this.timeOfIgnition=timeOfIgnition;
     this.retroThrust=retroThrust;
     this.stabilizationType = FIN;         // default type
     if (stabilizationType.equals("spin")) {
       this.stabilizationType=SPIN;
     }
     if (stabilizationType.equals("circ")) {
       this.stabilizationType=CIRC;
     }
     this.ispDegradedAtOneAtm=ispDegradedAtOneAtm;
     this.spinAngle=k.degreesToRadians(spinAngle);
     this.stageStaysAttached=stageStaysAttached;

     rocketMass.kg += dryKg + fuelKg;
     rocketMass.stage++;                  // we number backwards
     this.stage=rocketMass.stage;         // remember what stage we are
     this.label = label + ".backstage." + stage;
  }


 public void simulate1(double deltaT) throws Exception  {
    if (this.largestStage == 0) {        // only look first time
        this.largestStage = rocketMass.stage;
    }
 }

  public void simulate2(double deltaT) throws Exception   {
    if (this.stage == rocketMass.stage) {
      rocketMass.Cd=this.Cd;
      rocketMass.dragArea=this.dragArea;
      addRocketForce(deltaT); // uses rocketMass.ourAir
      checkParachute(); // could change Cd for next round
    }
  }
  public void simulate3(double deltaT) throws Exception {

  }




  // Only check parachute after fuel gone and on way down
  private void checkParachute() {
     if ((rocketMass.vel.y < 0 && fuelKg < 1)
        && (parachuteCd > 0)
        && (rocketMass.getHeight() < parachuteHeight)) {
        rocketMass.Cd = parachuteCd;
        rocketMass.dragArea = parachuteArea;
     }
  }

  // goes to mass.totalForce
  private void addRocketForce(double deltaT) {
    double v, fuelUsed;
    velocity airRelativeVelocity;
    thrust = 0;
    if (k.simTime < timeOfIgnition) {
      return;                           // If not time yet do nothing
    }

    fuelUsed = massFlowRate * deltaT;
    if (this.fuelKg - fuelUsed <= 0) {
      if (!stageStaysAttached) {
        rocketMass.kg -= this.dryKg; // stage separation
      }
      rocketMass.stage--;          // we number stages backwards
      return;
    }
    thrust = massFlowRate * exhaustVelocityMPS;

    this.fuelKg -= fuelUsed;
    this.rocketMass.kg -= fuelUsed;
    if (retroThrust) {
      thrust = -thrust;
    }
    if (k.rotatingAir) {
      velocity airVelocity = air.calcVelocity(rocketMass.pos); // mass could save this
      airRelativeVelocity = new velocity(rocketMass.vel, airVelocity, -1);
    } else {
      airRelativeVelocity = rocketMass.vel; // then not really relative
    }
    if (ispDegradedAtOneAtm > 0) {
      double fracSeaLevelPressure =
          rocketMass.ourAir.pressureKpa * 1000.0 / k.airStandardAtmospher;
      thrust -= thrust * fracSeaLevelPressure * ispDegradedAtOneAtm;
    }
    if (this.stabilizationType == FIN) {
      v = airRelativeVelocity.magnitude();
      if (v > 0) {
        rocketMass.totalForce.add( thrust * airRelativeVelocity.x / v,
                                   thrust * airRelativeVelocity.y / v);
      }
    }
    if (this.stabilizationType == SPIN) {
      rocketMass.totalForce.addAtAngle(thrust, spinAngle);
    }
    if (this.stabilizationType == CIRC) {
      double circAngle = Math.atan2(rocketMass.pos.y, rocketMass.pos.y)
                          + k.pi/2;
      rocketMass.totalForce.addAtAngle(thrust, circAngle);
    }
     Gs = (thrust/rocketMass.kg)/k.earthAcceleration;
     if (Gs > maxGs) {
        maxGs = Gs;
     }
  }


  public void paint(Graphics g) {
      String line1=  "Stage " + (1 + this.largestStage - this.stage)
                  + "  Total Kg=" + k.dTS2(rocketMass.kg)
                  + "  Fuel Kg=" + k.dTS2(fuelKg);
     if (Math.abs(maxGs) > .001) {
       line1 += "  Rocket Gs " + k.dTS3(Gs) + " max Gs " + k.dTS3(maxGs);
     } else if (Math.abs(Gs) > 0.0000001) {
       line1 += "  Milli Gs " + k.dTS3(Gs*1000.0) + " max MGs "
               + k.dTS3(maxGs*1000.0);
     }
     if (rocketMass.showText) {
       screen.print(g, line1);
     }

     if (k.logThrust && thrust != 0) {
       k.outputAdd(this.label +
          " time "  + k.dTS2(k.simTime)  +
          " MilliGs " + k.dTS3(Gs*1000.0) +
          " Thrust " + k.dTS3(thrust) +
         "\n");
     }
  }

}

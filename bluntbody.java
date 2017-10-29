package spacetethers;


import java.awt.*;
import java.lang.Math;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class bluntbody extends simtype {

     // blunt body stuff.
   // Could be separate object, but then rocket, tether, etc would not get it.
   public mass ourMass;
   public double blackBodyMass=0;
   public double blackBodyArea=0;
   public double blackBodySpecificHeat=0;
   public double blackBodyStantonNumber=0;
   public double blackBodyEmissivity=1;
   public double blackBodyTemp=k.celsiusToKelvin(20.0);

   public double blackBodyMaxTemp = 0;
   public double blackBodyTotalJoules=0;
   public double wattsPerCm2=0;
   public double joulesPerCm2=0;

  public bluntbody(bluntbody bb) {
     this.blackBodyArea=bb.blackBodyArea;
     this.blackBodyMass=bb.blackBodyMass;
     this.blackBodyTemp=bb.blackBodyTemp;
     this.blackBodySpecificHeat=bb.blackBodySpecificHeat;
     this.blackBodyStantonNumber=bb.blackBodyStantonNumber;
     this.blackBodyEmissivity=bb.blackBodyEmissivity;
     this.blackBodyMaxTemp = bb.blackBodyMaxTemp;
  }

  public bluntbody(
               double blackBodyMass, double blackBodyArea,
               double blackBodySpecificHeat, double blackBodyStantonNumber,
               double blackBodyEmissivity,
               String label) throws Exception {
     this.ourMass = findsimobject.labelToMassOrDie(label);
     this.label = label + ".bb";
     this.blackBodyMass= blackBodyMass;
     this.blackBodyArea= blackBodyArea;
     this.blackBodySpecificHeat = blackBodySpecificHeat;
     this.blackBodyStantonNumber = blackBodyStantonNumber;
     this.blackBodyEmissivity = blackBodyEmissivity;
     this.blackBodyMaxTemp = 0;
  }
  public void simulate1(double deltaT) {
     ourMass.simulate1(deltaT);
  }
  public void simulate2(double deltaT) {
     ourMass.setAirRelativeVelocity();      // We want more drag precision
     ourMass.simulate2(deltaT);
  }
  public void simulate3(double deltaT) throws Exception {
     ourMass.simulate3(deltaT);

        // NYC - using shockwave temp - not normal
        //   double maxheating = ourShock.p2p1 * ourAir.density * this.A * this.vel.magnitude() * tempKelvin;
        double airMassPerSec = ourMass.ourAir.density
                  * this.blackBodyArea
                  * ourMass.airRelativeVelocity.magnitude();
        double heatFromAir=0;
        if (this.blackBodyStantonNumber > 0) {
           heatFromAir = deltaT * this.blackBodyStantonNumber *
              (ourMass.stagTemp - this.blackBodyTemp)
              * airMassPerSec * k.airSpecificHeat;
        } else {
          // no stanton number needed - page 256 of Hypersonic Aerothermodynamics
                             // A = Pi * r^2 ,   r = sqrt(A/Pi)
           double blackBodyRadius = Math.sqrt(blackBodyArea/k.pi);
           double heatRatePerCC = 18300.0 * Math.pow(ourMass.ourAir.density, 0.5)
                             * Math.pow(ourMass.airRelativeVelocity.magnitude()/10000.0, 3.05) /
                             Math.sqrt(blackBodyRadius);
           heatFromAir = deltaT * heatRatePerCC * k.SqCMinSqMeter * blackBodyArea;
        }


//   Can simulate where has thermal mass - but for nose surface mass is very small

        if (heatFromAir < 0) {
          heatFromAir=0;
        }
        if (blackBodyMass > 0 && blackBodySpecificHeat > 0) {
          double blackBodyHeatLoss = deltaT * this.blackBodyEmissivity
                            * this.blackBodyArea * k.stefanBoltzmann *
                              Math.pow(blackBodyTemp, 4.0);
          double deltaHeat =  heatFromAir - blackBodyHeatLoss;
          this.blackBodyTemp += deltaHeat/
                          (this.blackBodyMass * this.blackBodySpecificHeat);
        } else {
          // Can look at equilibrium temp - as if no thermal mass inertia
          //  maxheating = emissivity * A * stefanBoltzmann * T ^ 4
          //  T = (maxheating / (emissivity * A * stefanBoltzmann))^1/4
          double surfacetemp4 = heatFromAir/
                  (deltaT *
                  (this.blackBodyEmissivity  * this.blackBodyArea * k.stefanBoltzmann));
          this.blackBodyTemp = Math.pow(surfacetemp4, 1.0 / 4.0);
        }



        this.blackBodyTotalJoules += heatFromAir;  // for deltaT

        this.wattsPerCm2 = heatFromAir/blackBodyArea/k.SqCMinSqMeter/
                                deltaT;
        this.joulesPerCm2 += heatFromAir/blackBodyArea/k.SqCMinSqMeter;

  }


  public void paint(Graphics g) {
       // ourMass.paint(g);            // does a setAirRelativeVelocity()
        String line1="";

        double blackBodyCelsius = k.kelvinToCelsius(this.blackBodyTemp);
        if (blackBodyCelsius > this.blackBodyMaxTemp) {
          this.blackBodyMaxTemp = blackBodyCelsius;
        }

        double blackBodyWater =  blackBodyTotalJoules / k.JtoBoilKgWater;

        line1 += " BBH2O " + k.dTS2(blackBodyWater);

        line1 += /* " airHeat " + k.dTS(airHeat)
                + */ " black-body " + k.dTS(blackBodyCelsius) + " C "
                + " max " + k.dTS(blackBodyMaxTemp) + " C ";
        screen.print(g, line1);
        line1="";

        k.outputAdd(this.ourMass.label +
                     " mass " +
                     " x " + k.dTS2(this.ourMass.pos.x) +
                     " y " + k.dTS2(this.ourMass.pos.y) +
                     " time "  + k.dTS2(k.simTime)  +
                     " vel "   + k.dTS2(ourMass.airRelativeVelocity.magnitude()/1000.0)  +
                     " alt "   + k.dTS2(this.ourMass.pos.height()/1000) +
                     " tempC " + k.dTS2(blackBodyCelsius)   +
                     " watts/cm2 " + k.dTS2(wattsPerCm2) +
                     " totalJoules/cm2 " + k.dTS2(joulesPerCm2) +
                     " waterKg " + k.dTS2(blackBodyWater) +
                      "\n");

  }

}
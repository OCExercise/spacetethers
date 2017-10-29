package com.spacetethers;


import java.awt.*;
  /*


  */

public class meteor extends simtype {
  bluntbody meteorBody=null;
  double joulesPerKg;      // how fast we ablate
  double density;
  double initialKg;
  double lastVelocity=0;
  double lastEnergy=0;
  double initialEnergy=0;
  mass ourMass=null;

  public meteor(double meteorInitialKg, double meteorJoulesPerKg,
                double meteorDensity,  double meteorCd,
                position pos, velocity vel,
                String label) throws Exception {
     this.joulesPerKg = meteorJoulesPerKg;
     this.density = meteorDensity;
     this.initialKg = meteorInitialKg;
     this.label=label;

     double liftOverDrag=0;
      double blackBodyMass=0;  // this mass only if heat sink, not ablation
      double blackBodyArea=1; // going to change after mass is created
      double blackBodySpecificHeat=0;
      double blackBodyStantonNumber=0;
      double blackBodyEmissivity=1;
      double Area=1; // going to change after mass is created
      this.ourMass= new mass(initialKg, pos, vel, meteorCd,
                             Area, liftOverDrag, label);
      meteorBody= new bluntbody(
                         blackBodyMass, blackBodyArea,
                         blackBodySpecificHeat, blackBodyStantonNumber,
                         blackBodyEmissivity, label);
      this.updateMass();

  }

  //  Set area and mass for drag and heat
  //
  public void updateMass() {

     double massLost = meteorBody.blackBodyTotalJoules / this.joulesPerKg;
     double currentKg = this.initialKg - massLost;
     if (currentKg < 0) {
       currentKg = 0;
     }

     double volume=currentKg/this.density;   // mass = volume * density
     // v = 4/3 * pi * r^3
     // r = (v / (4/3 * pi) ^ 1/3
     double radius=Math.pow(volume / (4.0/3.0 * k.pi) , 1.0/3.0);
     double area= k.pi * radius * radius;

     this.ourMass.kg=currentKg;         // also MetorBody.ourMass
     this.ourMass.dragArea=area;
     this.meteorBody.blackBodyMass=0;    // Not currentKg since not heatsink
     this.meteorBody.blackBodyArea=area;
  }


  public void simulate1(double deltaT)  {
     meteorBody.simulate1(deltaT);
  }

  public void simulate2(double deltaT)  {
     meteorBody.simulate2(deltaT);
  }

  public void simulate3(double deltaT) throws Exception {
     meteorBody.simulate3(deltaT);
  }

  public void paint(Graphics g) {
     this.updateMass();
     meteorBody.ourMass.paint(g);   // We skip the bluntBody.paint() stuff
     double radius = Math.sqrt(meteorBody.ourMass.dragArea/k.pi);  //  r = sqrt(A/Pi),   A = Pi * r^2
     double diameter = 2.0 * radius;
     double v = meteorBody.ourMass.vel.magnitude();
     boolean onGround=false;

     if (v < 0.1 * this.lastVelocity ) {
        v = this.lastVelocity;
        onGround = true;
     } else {
        this.lastVelocity = v;
     }
     double ke = 0.5 * meteorBody.ourMass.kg * v * v;
     double pe = meteorBody.ourMass.kg * k.earthAcceleration
               * meteorBody.ourMass.pos.height();
     double energy = ke + pe;

     if (this.initialEnergy == 0) {
       this.initialEnergy = energy;
     }

     double airHeat = this.initialEnergy - energy
                         -meteorBody.blackBodyTotalJoules;
     double bodyFraction = meteorBody.blackBodyTotalJoules/airHeat;
     double megatonsImpact = ke / k.JoulesPerMegaton;
     String line1 = " Mass " + k.dTS3(meteorBody.ourMass.kg) +
 //                    "  Area " + k.dTS3(meteorMass.Area) +
                     "  Diameter " + k.dTS3(diameter) +
                     "  MegatonsAir " + k.dTS3(airHeat / k.JoulesPerMegaton) +
                     "  BodyFraction "  + k.dTS3(bodyFraction) +
                     "  MegatonsImpact " + k.dTS3(megatonsImpact);
     if (onGround) {
       line1 += "  CraterDiameter " + k.dTS3(craterDiameter(megatonsImpact));
     }
     screen.print(g, line1);
   }

  //  http://www.faqs.org/faqs/space/math/
   public double craterDiameter(double megatons) {
      double kilotons = 1000 * megatons;
      double pa = 1800;  // kg/m^3   for density of reference impact site
      double pt = 2600;  // kg/m^3   for average rock
      double Sp = Math.pow((pa/pt),(1.0/3.4));

     // Kn = Empirically determined scaling factor from bomb yield to crater
     // 	diameter at Jangle U.
      double Kn = 74;    // 1 kiloton is 74 meters at Jangle U.

      double diameter =  Sp * Kn * Math.pow(kilotons, (1.0/3.4));

      // 	Crater diameter, meters. On Earth, if D > 3 km, the crater is
      //  assumed to collapse by a factor of 1.3 due to gravity.
      if (diameter > 3000) {
         diameter = diameter / 1.3;
      }
      return(diameter);
   }

}

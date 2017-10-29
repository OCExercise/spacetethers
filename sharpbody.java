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

public class sharpbody extends simtype {

   public mass   ourMass;
   public double noseAngle=0; // degrees
   public double tpsThermalConductivity = 0;
   public double tpsThickness = 0;
   public double tpsArea = 0;
   public double tpsThermalMass = 0;
   public double tpsSpecificHeat = 0;
   public double joules = 0;


  public sharpbody(sharpbody sb) {
     this.ourMass = new mass(sb.ourMass);

     this.noseAngle=sb.noseAngle; // degrees
     this.tpsThermalConductivity = sb.tpsThermalConductivity;
     this.tpsThickness = sb.tpsThickness;
     this.tpsArea = sb.tpsArea;
     this.tpsThermalMass = sb.tpsThermalMass;
     this.tpsSpecificHeat = sb.tpsSpecificHeat;
     this.joules = sb.joules;
  }

  public sharpbody(double kg, position p1, velocity v1, double Cd,
               double dragArea, double liftOverDrag,
               double noseAngle,
               double tpsThermalConductivity, double tpsThickness,
               double tpsArea, double tpsThermalMass,
               double tpsSpecificHeat,
               String label) {
     this.ourMass = new mass(kg, p1, v1, Cd, dragArea, liftOverDrag, label);
     this.noseAngle = noseAngle;
     this.tpsThermalConductivity = tpsThermalConductivity;
     this.tpsThickness = tpsThickness;
     this.tpsArea = tpsArea;
     this.tpsThermalMass= tpsThermalMass;
     this.tpsSpecificHeat = tpsSpecificHeat;
     this.label = label + ".sb";
  }


  public void simulate1(double deltaT) {
     ourMass.simulate1(deltaT);
  }

  public void simulate2(double deltaT)  {
     ourMass.simulate2(deltaT);
  }
  public void simulate3(double deltaT) throws Exception  {
     ourMass.simulate3(deltaT);
  }

  public void paint(Graphics g) {
    this.ourMass.paint(g);
    if (this.noseAngle > 0) {
      double tempKelvin = 0;
      if (ourMass.Mach > 1.5) {
        shockwave ourShock = new shockwave(this.ourMass.Mach,
                                           this.noseAngle);
        if (ourShock.shockAngleRad > 0 && ourShock.shockAngleRad < k.pi / 2) {
          tempKelvin = ourShock.paint(g, ourMass.ourAir.temperature,
                                      ourMass.ourAir.pressureKpa,
                                      ourMass.ourAir.density); // NYC if *1000
        }

        double watts = this.tpsThermalConductivity
            * (tempKelvin - k.internalKelvin) * this.tpsArea /
            this.tpsThickness;
        if (watts > 0) {
          this.joules += watts * k.timePerDisplay;
        }
      }
      if (this.joules > 0) {
        double tempInc = this.joules /
            (this.tpsThermalMass * this.tpsSpecificHeat);
        screen.print(g, " KW Hours=" + k.dTS2(this.joules * k.JtoKWH)
                     + " TempInc=" + k.dTS(tempInc)
                     + " H20 Kg=" + k.dTS2(this.joules / k.JtoBoilKgWater)
                     );
        // + " KW=" + k.dTS(watts/1000));
      }
    }
  }

}
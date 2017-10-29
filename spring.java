// Copyright (c) Vincent Cate 2002

package spacetethers;

import java.awt.*;

// Springs are connected to masses but they don't have mass.
//   In simulate2() they apply their force to the 2 masses they are connected to

public class spring extends simtype {
  public double restLength;   // length when no tension on spring
  private double springK;      // newtons/meter of stretch (pos num)
  public double strength;      // max newtons before it breaks
  private mass m1;             // mass on side toward ballast
  private mass m2;             // mass on side toward payload

  public double length;        // current length
  public double stretch;       // tether uses to for max/min for reporting
  public double tension;     // tension of current force in newtons
  public force f1 = new force(0,0);
  private String label;        // for reporting
  private position deltaPos = new position(0,0);

  public spring() {

  }


  public spring(double restLength, double springK,
                double strength, String label) {
     this.restLength=restLength;
     this.springK=springK;
     this.strength=strength;

     this.label=label;
  }

  // When tether.java makes this spring it had not made both masses
  //   so it needed to be able to set them later (but only once).
  public void setMasses(mass m1, mass m2) throws Exception {
    if (m1==null || m2==null) {
      throw new Exception("spring.setMasses given null mass");
    }
    this.m1 = m1;
    this.m2 = m2;
  }
  public void setBalSideMass(mass m1) {
    this.m1 = m1;
  }
  // When we get a new length we calculate and check new force
  // Force should be right for mass at p1 and reversed for p2
  // Returns number of meters spring is stretched
  public void simulate2 (double deltaT) throws Exception  {
    // We dont just use positon.distance(pos) because we want dX and dY below
     deltaPos.setDiff(m1.pos, m2.pos);
     length=deltaPos.magnitude();

//     double dX = m1.pos.x - m2.pos.x;
//     double dY = m1.pos.y - m2.pos.y;
//     length=Math.sqrt(dX*dX + dY*dY);


     stretch = length - restLength;   // how much spring is stretched
     if (stretch < 0) {
        // throw new Exception("Can not compress a rope" +
        //      String.valueOf(length) + " < " + String.valueOf(restLength) + "\n");
        stretch=0;
     }

     if (restLength > 0) {
//       tension = springK * (stretch  / restLength);     // force linear with stretch
       tension = springK * stretch;     // force linear with stretch
     } else {
       tension = 0;   // Should not really have 0 length springs
     }

     if (tension > strength) {
        throw new Exception("Snap  " + this.label
    //       + " x1=" + k.dTS(m1.pos.x) + " y1=" + k.dTS(m1.pos.y)
    //       + "   x2=" + k.dTS(m2.pos.x) + " y2=" + k.dTS(m2.pos.y)
           + " tension " + k.dTS(tension) + " > " + k.dTS(strength));
     //         + " Str=" +  String.valueOf(stretch)
     //         + " sK=" + String.valueOf(springK)
     //         + " ln=" + String.valueOf(length));
     }

     // dX/length makes unit vector the tension makes right length
     if (length == 0) {
       f1.set(0,0);
     } else {
       if (tension == 0) {
         f1.set(m1.totalForce);
         f1.subtract(m2.totalForce);
         tension = f1.magnitude()/2.0;  // NYC not right since only gravity
         tension = 0;
       }
       f1.set(deltaPos, tension / length);
//       f1.set(tension * dX / length, tension * dY / length);
//      f1.set(deltaPos.x * tension / length,
//            deltaPos.y * tension / length,
//             deltaPos.z * tension / length);
     }

     // Add force to masses
     m1.totalForce.subtract(f1);
     m2.totalForce.add(f1);
  }

  // winch.java uses to make springs restLength longer or shorter
  // The length and stretch are recalculated every time.
  // Can pass a negative amountShorter to make longer.
  public void shortenLength(double amountShorter) {
    restLength -= amountShorter;
  }

  public double length() {
    return(m1.pos.distance(m2.pos));
  }

   public void paint (Graphics g) {
       screen.print(g, label
                    + " from " + m1.label + " to " + m2.label
                    + " tension " + k.dTS2(tension)
                    + " stretch " + k.dTS2(100.0 * stretch/restLength) + "%"
                    + " length " + k.dTS2(length));
   }


}

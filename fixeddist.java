package spacetethers;

import java.awt.*;
/**
 * <p>Title: </p>
 *    Like a mass at the end of a clock arm.  It rotates over time
 *    but keeps a fixed distance from some other mass.
 *
 *    Can allow free movement sideways, just not toward or away.
 *      It just take and eliminate force that is along line of bar.
 *      Then adjust position to make sure right distance.
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 */

public class fixeddist extends simtype {
  double distance;
  double secondsPerRev;
  double secondsOffset;
  int direction;    // -1=clockwise, 0=no movement, 1=counterclockwise
  mass   centerMass;
  mass   tipMass;

  public fixeddist(String label, String labelCenter, double distance,
                   String labelTip, double secondsPerRev,
                   double secondsOffset, int direction) throws Exception {
     this.label=label;
     this.distance=distance;
     this.secondsPerRev=secondsPerRev;
     this.secondsOffset=secondsOffset;
     this.direction=direction;
     this.centerMass=findsimobject.labelToMassOrDie(labelCenter);
     this.tipMass=findsimobject.labelToMassOrDie(labelTip);

     this.simulate1(1.0);
  }

  public void simulate1(double deltaT) throws Exception {
     if (direction != 0 && secondsPerRev != 0) {
 //        double lastx = tipMass.pos.x;
 //        double lasty = tipMass.pos.y;
         double dx= distance*Math.cos(direction*2*k.pi
                          *(secondsOffset+k.simTime)/secondsPerRev);
         double dy= distance*Math.sin(direction*2*k.pi
                          *(secondsOffset+k.simTime)/secondsPerRev);
         tipMass.pos.x = centerMass.pos.x + dx;
         tipMass.pos.y = centerMass.pos.y + dy;
         tipMass.vel.x = centerMass.vel.x;  // NYC - not right yet
         tipMass.vel.y = centerMass.vel.y;

     } else {
         double currentDistance = centerMass.pos.distance(tipMass.pos);
         double fixRatio = distance/currentDistance;
         double dx = fixRatio * (tipMass.pos.x - centerMass.pos.x);
         double dy = fixRatio * (tipMass.pos.y - centerMass.pos.y);
         tipMass.pos.x = centerMass.pos.x + dx;
         tipMass.pos.y = centerMass.pos.y + dy;
    }
  }

  public void simulate2(double deltaT) throws Exception {

  }

  public void simulate3(double deltaT) throws Exception {

  }

  public void paint(Graphics g) throws Exception {
     if (this.showText) {
       screen.drawLine(g, centerMass.pos.x, centerMass.pos.y,
                       tipMass.pos.x, tipMass.pos.y);
     }
  }
}

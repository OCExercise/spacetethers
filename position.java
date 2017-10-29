 // Copyright (c) Vincent Cate 2002,2003
package spacetethers;

public class position extends coordinate {
     public position () {
     }
     public position(double x, double y) {
       super(x,y);
     }
     public position(double x, double y, double z) {
       super(x,y,z);
     }     // Make a copy
     public position(position p2) {
       super(p2);
     }
     // Add mult*delta to base
     public position(position base, position delta, int mult) {
       super(base, delta, mult);
     }


     // Distance from center of Earth which is center of coordinate system
     //   at least when we don't have moon around
     public double distance () {
         return(super.magnitude());
     }

     // Height from surface of Earth
     public double height() {
//         return(distanceEarth() - k.earthRadius);  can't do
         return(this.magnitude() - k.earthRadius);
     }

     public double distanceEarth () {
         return(distance(k.earth.pos));
     }

     public double distance (position p2) {
         return(super.magnitudeDiff(p2));
     }
     public double distance2D (position p2) {
         double dx = this.x - p2.x;
         double dy = this.y - p2.y;
         return(Math.sqrt(dx*dx + dy*dy));
     }
     public double distance2D (mass m1) {
        return(distance2D(m1.pos));
     }
     public double distance (mass m1) {
         return(distance(m1.pos));
     }
}

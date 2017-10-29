
// Copyright (c) Vincent Cate 2002

package spacetethers;
//  F=MA
//  A=F/M
//  deltaVelocity = deltaT * F / M

public class force extends coordinate {

  public force(double x, double y) {
     super(x,y);
  }

  public void reset() {
     if (k.sim3D) {
       super.set(0,0,0);
     } else {
       super.set(0,0);
     }
  }

  public void addAtAngle(double thrustNewtons, double angle) {
    this.x += thrustNewtons * Math.cos(angle);
    this.y += thrustNewtons * Math.sin(angle);
  }

  // This is special for the spring method
  public void set(position v1, double k1) {
    super.set(v1, k1);
  }
}

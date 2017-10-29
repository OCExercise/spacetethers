package com.spacetethers;

import java.lang.Math;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 * For some of this I used:
 *    "SpaceFlight Dynamics" by Dr. William E. Wiesel,
 *          Appendix A, Vectors and Matrices.
 */

public class vector3d {
  double x, y, z;

  // totaly new vector
  public vector3d(double x, double y, double z) {
    this.x=x;
    this.y=y;
    this.z=z;
  }

  // new vector as clone of existing one
  public vector3d(vector3d v1) {
    this.x=v1.x;
    this.y=v1.y;
    this.z=v1.z;
  }

  // to convert from 2D type to 3D type
  public vector3d(coordinate c1) {
    this.x=c1.x;
    this.y=c1.y;
    this.z=0;
  }

  public void set(double x, double y, double z) {
    this.x=x;
    this.y=y;
    this.z=z;
  }

  public void set(vector3d v1) {
    this.x=v1.x;
    this.y=v1.y;
    this.z=v1.z;
  }

  // set from cross of 2 other vectors
  // See  http://www.netcomuk.co.uk/~jenolive/vect8.html
  //      http://physics.syr.edu/courses/java-suite/crosspro.html
  public void setCross(vector3d a, vector3d b) {
    this.x = a.y*b.z - a.z*b.y;
    this.y = a.z*b.x - a.x*b.z;
    this.z = a.x*b.y - a.y*b.x;
  }

  public double dot(vector3d b) {
    return(this.x*b.x + this.y*b.y + this.z*b.z);
  }

  public double angle(vector3d b) {
    return(Math.acos(this.dot(b)/(this.magnitude() * b.magnitude())));
  }

  public double angleDegrees(vector3d b) {
    return(k.radiansToDegrees(this.angle(b)));
  }

  // add scaled version of another vector to this
  public void addScaled(vector3d v1, double s) {
    this.x += v1.x * s;
    this.y += v1.y * s;
    this.z += v1.z * s;
  }

  // set to scale of another vector
  public void setScaled(vector3d v1, double s) {
    this.x = v1.x * s;
    this.y = v1.y * s;
    this.z = v1.z * s;
  }

  // toString but reduced by some amount
  public String toString(double divide) {
    return( "(" + k.dTSS(x/divide) + ","
                  + k.dTSS(y/divide) + ","
                  + k.dTSS(z/divide) + ")");
  }
  public String toString() {
    return( "(" + k.dTSS(x) + ","
                  + k.dTSS(y) + ","
                  + k.dTSS(z) + ")");
  }
  public double magnitude() {
    return(Math.sqrt(x*x + y*y + z*z));
  }

}

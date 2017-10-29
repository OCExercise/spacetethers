package com.spacetethers;
import java.lang.Math;

/**
 *  Extended by:
 *     Position
 *     Velocity
 *     Force
 *     No need for Acceleration as mass does this in couple lines
 */

public class coordinate {

  public double x=0;
  public double y=0;
  public double z=0; // use is optional at compile time with k.sim3D

  public coordinate() {
  }

  public coordinate(coordinate p1) {
    this.x = p1.x;
    this.y = p1.y;
    if (k.sim3D) {
      this.z = p1.z;
    }
  }

  public coordinate(double x, double y) {
    this.x = x;
    this.y = y;
    if (k.sim3D) {
      this.z = 0;
    }
  }
  public coordinate(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  // Construct from coordinate and delta * multiplier
  public coordinate(coordinate p1, coordinate p2, int mult) {
    this.x = p1.x + p2.x * mult;
    this.y = p1.y + p2.y * mult;
    if (k.sim3D) {
      this.z = p1.z + p2.z * mult;
    }
  }

  // set from coordinate and delta * multiplier
  public void setDiff(coordinate p1, coordinate p2) {
    this.x = p1.x - p2.x;
    this.y = p1.y - p2.y;
    if (k.sim3D) {
      this.z = p1.z - p2.z;
    }
  }

  public void copy(coordinate c1) {
    if (c1 == null) {
      this.x = 0;
      this.y = 0;
      if (k.sim3D) {
         this.z = 0;
      }
    }
    else {
      this.x = c1.x;
      this.y = c1.y;
      if (k.sim3D) {
         this.z = c1.z;
      }
    }
  }

  public void set(coordinate c1) {
    copy(c1);
  }

  public void set(coordinate c1, double r) {
    this.x = c1.x * r;
    this.y = c1.y * r;
    if (k.sim3D) {
      this.z = c1.z * r;
    }
  }

// magnitude of this vector
  public double magnitude() {
    if (k.sim3D) {
      return (Math.sqrt(x * x + y * y + z * z));
    } else {
      return (Math.sqrt(x * x + y * y));
    }
  }

  public double magnitudeDiff(coordinate p2) {
    double dx, dy, dz;

    dx = x - p2.x;
    dy = y - p2.y;
    if (k.sim3D) {
       dz = z - p2.z;
       return (Math.sqrt(dx * dx + dy * dy + dz * dz));
    } else {
       return (Math.sqrt(dx * dx + dy * dy));
    }
  }

 // frac of way from p1 to p2
  public void partway(coordinate p1, coordinate p2, double frac) {
    double dx, dy, dz;

    dx = p2.x - p1.x;
    dy = p2.y - p1.y;
    this.x = p1.x + frac*dx;
    this.y = p1.y + frac*dy;

    if (k.sim3D) {
      dz = p2.z - p1.z;
      this.z = p1.z + frac*dz;
    }
  }

  public void subtract(coordinate p2) {
    x -= p2.x;
    y -= p2.y;
    if (k.sim3D) {
      z -= p2.z;
    }
  }

  public void set(double x, double y) {
    this.x = x;
    this.y = y;
    if (k.sim3D) {
      this.z=0;
    }
  }
  public void set(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void add(coordinate p2) {
    x += p2.x;
    y += p2.y;
    if (k.sim3D) {
      z += p2.z;
    }
  }

  public void add(double dx, double dy) {
    x += dx;
    y += dy;
  }
  public void add(double dx, double dy, double dz) {
    x += dx;
    y += dy;
    z += dz;
  }

  public void divide(double d) {
    x = x / d;
    y = y / d;
    if (k.sim3D) {
      z = z / d;
    }
  }
}

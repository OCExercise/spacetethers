// Copyright (c) Vincent Cate 2002

package spacetethers;

public class velocity extends coordinate {
  public velocity() {
  }

  public velocity(velocity v1) {
     super(v1);
  }

  public velocity(double x, double y) {
     super(x,y);
  }
  public velocity(double x, double y, double z) {
     super(x,y,z);
  }
  // Construct from velocity and delta * multiplier
  public velocity(velocity v1, velocity v2, int mult) {
     super(v1, v2, mult);
  }
}
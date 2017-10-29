package com.spacetethers;

import java.awt.*;
/**
 * <p>Title: </p>
 * <p>Description: This type is extended by all simulation objects.
 *       This way the main simulation loop does not need to know
 *       the individual types. </p>
 *
 *    All simulation objects can have these 4 methods:
 *       In simulate1() mass objects initialize their totalForce
 *           variable with the force of gravity.
 *       In simulate2() all objects add in their forces to the masses
 *           they are involved with.
 *       In simulate3() mass objects apply the totalForce to
 *           compute an accleration, new velocity, and new position.
 *
 *       The paint() method is called to display stuff on the screen.
 *           This is usually much less often than the simulate methods.
 *
 *    Note that an object may not implement a simulate1() or
 *    simulate2() in which case nothing is done during that stage.
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 */

public class simtype {
  public String label="";           // All objects should have a label
  public boolean Finished=false;    // When finished object will be removed
  public boolean showText=true;     // User can decide if they want text info

  public simtype() {
  }

  public void simulate1(double deltaT) throws Exception {

  }

  public void simulate2(double deltaT) throws Exception {

  }

  public void simulate3(double deltaT) throws Exception {

  }

  public void paint(Graphics g) throws Exception {

  }
}

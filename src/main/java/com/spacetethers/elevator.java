package com.spacetethers;

import java.awt.*;
/**

   This is to simulate and elevator climbing up a tether.
   Going down is easy, so we only simulate going up.

   Inputs:
      label for this (all objects need unique labels)
      label for tether
      label for elevator mass (including any payload mass as one mass)
      power available
      maximum speed
      max distance
      max time

   Assume we have steady speed and acceleration force can be ignored

   Operation:
      Use findsimobject to find the tether we are going to climb.
      Put Kg from elevator mass on tether slice above
        see difference in force on that slice and one below
        the share of that difference that our mass is is our share of force
      See how far we can climb with the power we have in deltaT
      Calculate new position as distance between two tether masses

    Paint current position.
 */

public class elevator extends simtype {
  tether ourTether;
  mass ourMass;
  double maxPower;
  double maxSpeed;
  double maxDistance;
  double maxTime;

  public elevator(String label, String tetherLabel, String massLabel,
                  double maxPower, double maxSpeed, double maxDistance,
                  double maxTime) throws Exception {
      this.label=label;
      ourTether = (tether) findsimobject.labelToSimObjectOrDie(tetherLabel);
      ourMass = findsimobject.labelToMassOrDie(massLabel);
      this.maxPower = maxPower;
      this.maxSpeed = maxSpeed;
      this.maxDistance = maxDistance;
      this.maxTime = maxTime;
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

package com.spacetethers;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 * In bootstrap we work towards a boottarget and then we are either
 *   finished or work towards a new boottarget.
 *
 * Target can be in terms of payload, tip speed and watts.
 * In bootstrap we figure out the mass of that type of tether
 *   and that much solar.
 */

public class boottarget {
  double solarWatts;
  double payloadKg;
  double tipSpeed;
  double totalKg;   // including previous tethers no longer used

  public boottarget(String label, double solarWatts, double payloadKg,
                    double tipSpeed, double totalKg) throws Exception {
    bootstrap ourBoot = (bootstrap) findsimobject.labelToSimObjectOrDie(label);
    this.solarWatts=solarWatts;
    this.payloadKg=payloadKg;
    this.tipSpeed=tipSpeed;
    this.totalKg=totalKg;
    ourBoot.addTarget(this);
  }

}

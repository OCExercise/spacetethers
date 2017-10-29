package com.spacetethers;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 * http://www.spenvis.oma.be/spenvis/ecss/ecss05/ecss05.html#_TOC541
 * The total field strength in an ideal dipole is given by

       B = M R^-3 [ 1+3*sin^2(lambda)]^(1/2)
where lambda is the magnetic latitude and R is the radial co-ordinate
 (R, lambda constituting a polar co-ordinate system). M is the magnetic
 dipolar moment. In a dipole approximation for the Earth's field,
 M currently has a value of about 7.9x1030 nT.cm3 or 30400 nT.RE3
 where RE is the mean radius of the Earth. The dipole which approximates
 the Earth's field is both tilted and offset with respect to the Earth's
 rotation axis, so that the geomagnetic poles do not coincide with the
 geographic poles and the field strength is not independent of longitude.
 This configuration is called an eccentric dipole

 *    Year    ME (nT.RE^3)      M (nT.cm^3)     M (T.m^3)
      1995      30207.7        7.8123E30        7.8123E15

   I (Vince) figured difference from nT.cm^3 to T.m^3 as follows:
        nT  -> T   : E9  - nano is billionth
       cm^3 -> m^2 : E6  - 100^3
        Total diff : E15

 ... in Cartesian coordinates by:
      Bx  = 3xz M R^-5
      By  = 3yz M R^-5
      Bz  = (3z^2 - R^2) M R^-5

where M can be taken from the table above,
 and R is the radius of the location in units consistent
 with the units of M. The z axis is along the dipole axis.

 */

public class geomagnetic {
  public geomagnetic() {
  }

  // Set this to be value for magnetic field at position p1
  // Tilt should be 11 degrees - not yet
  // Offset should be 436 km - not done.
  public static void set(vector3d result, vector3d p1) {
    double R = p1.magnitude();
    double x = p1.x;
    double y = p1.y;
    double z = p1.z;
    double M = 7.8123E15;        // version in T-meters^3
    double R5 = Math.pow(R, 5.0);

    result.x = 3.0 * x * z * M / R5;
    result.y = 3.0 * y * z * M / R5;
    result.z = (3.0 * z * z  - (R * R)) * M / R5;

  }
}

package spacetethers;

/*
    Input is:
       label of mass to connect from
       label of mass to connect to
       diameter of tether

   From this we make a fixed diameter tether between
       these 2 masses

   This is called from attime.java
*/

public class connect {
  public connect() {

  }

  public static tether masses(String fromLabel, String toLabel,
                           double bigEndDiameter, double smallEndDiameter)
         throws Exception {

     mass fromMass = findsimobject.labelToMassOrDie(fromLabel);
     mass toMass = findsimobject.labelToMassOrDie(toLabel);
     double restLength = fromMass.pos.distance(toMass.pos);
     double density=970;           // spectra-2000
     double tensile=3510000000.0;  // spectra-2000
     double elasticity=0.50;       // more stretch than spectra's 0.03
     int slices=100;
     int taperType= k.TaperLinear;
     double safetyFactor=2;         // really only for automatic taper
     String label=toMass.label + ".connect";  // so name unique
     return(new tether( restLength, bigEndDiameter,
                       smallEndDiameter, density,
                       tensile, elasticity,
                       fromLabel, toLabel,
                       slices,
                       taperType, safetyFactor, label));

  }
}
// Copyright (c) Vincent Cate 2002

package com.spacetethers;

import java.awt.*;
import java.lang.Math;

// first spring connects ballast to first mass in array.
// one more springs than masses
// last spring connects last mass to payload

public class tether extends simtype {
  String CurrentMessage=" ";
  boolean Initialized=false;
  public mass ballast=null;
  public mass payload=null;
  public mass centerOfMassMass=null;
  public mass tetherMass[] = null;
  public spring tetherSpring[] = null;
  public double tetherOnlyKg;    // just tether - not payload or ballast
  private int indexCM=0;
  private double cmDistance;
  public int minSpring;
  public int minMass;
  public int slices;
  public int numMasses;
  public int numTetherMasses;  // This is numMasses-2
  public int numSprings;
  public int maxMass;
  public int maxSpring;

  private double length;
  public double restLength;
  private double density;
  private double tensile;
  private double elasticity;

  private double minPayH=k.bigNum;
  private double minBalH=k.bigNum;
  private double minPayV=k.bigNum;
  private double minBalV=k.bigNum;
  private double minCMH=k.bigNum;
  private double minCMV=k.bigNum;
  private double maxCMH=0;
  private double lastCMH=0;
  private double lastCX=0;
  private double lastCY=0;
  private double lastCVX=0;
  private double lastCVY=0;
  public double lastTime=k.simTime;
  private int climb;


  private double maxStretch=0;
  private int maxStretchIndex=0;
  private double maxStretchNow;
  private int maxStretchIndexNow;
  private double minStretch;
  private int minStretchIndex=0;
  private double minStretchNow;
  private int minStretchIndexNow;


  // Next for calculating taper
  private double taperTension=0;
  private double taperCMfrac=0;
  private mass taperCM=new mass();
  private double payloadTetherKg=0;   // what tether was before payload added
  public double ballastTetherKg=0;   // what tether was before ballast added
  public double grandTotalKg=0;

  private double lastReport=0;
  boolean tetherDrag=false;
  rollingaverage ourAverage=null;
  double averageMassKg=0;

  public mass winchMassMemory[]=null;      // used by winch only


  // We make tethers that
  //     start in straight line
  //     diameter varies linearly
  //     position varies linearly
  //     velocity varies linearly
  //     density is Kg/meter^3
  //  distances,position,length in meters
  //  velocity in meters/second
  //  Or with automatic taper using safetyFactor
  public tether(double restLength,
         double diameterBigEnd, double diameterSmallEnd,
         double density, double tensile, double elasticity,
         String labelBallast, String labelPayload,
         int slices,
         int taperType, double safetyFactor, String label) throws Exception {

    mass bigEnd = findsimobject.labelToMassOrDie(labelBallast);
    mass smallEnd = findsimobject.labelToMassOrDie(labelPayload);


     this.slices = slices;

     numSprings = slices;    // Every spring is a slice
     maxSpring = slices-1;
     minSpring = 0;

     numMasses = slices+1;   // One more mass than number of springs
     maxMass = slices;       //    but that is counting ballast and payload
     minMass = 0;


     tetherMass = new mass[numMasses];
     tetherSpring = new spring[numSprings];

     tetherMass[minMass] = bigEnd;
     ballast = bigEnd;
     tetherMass[maxMass] = smallEnd;
     payload= smallEnd;
     this.length = bigEnd.pos.distance(smallEnd.pos);

     if (taperType == k.TaperAutomatic) {
       this.restLength = (1 - elasticity/safetyFactor) * this.length;
       restLength=this.restLength;
     } else {
       this.restLength = restLength;
     }
     this.density = density;
     this.tensile = tensile;
     this.elasticity = elasticity;
     this.minStretch = restLength; // bigger than when it would break
     this.label=label;
     if (length > (1 + elasticity) * restLength) {
        throw new Exception("tether.tether stretched too much in initial "
                   + String.valueOf(length) + " > "
                   + String.valueOf(elasticity) + " * "
                   + String.valueOf(restLength));
     }
     if (length < restLength) {
        throw new Exception("tether.tether compressing rope in initial "
                   + String.valueOf(length) + " < "
                   + String.valueOf(restLength)
                   + "Big Y = " + String.valueOf(bigEnd.pos.y)
                   + "Small Y = " + String.valueOf(smallEnd.pos.y));
     }

     position deltaPos = new position(smallEnd.pos);
     deltaPos.subtract(bigEnd.pos);
     deltaPos.divide(numSprings);

     double deltaLength = deltaPos.distance();
     double deltaLength2 = length / (numSprings);
     if (java.lang.Math.abs( (int) (deltaLength - deltaLength2)) > 0.0001) {
        throw new Exception("tether.tether deltaLength and 2 "
                   + String.valueOf(deltaLength) + "  " +
                   String.valueOf(deltaLength2));
     }

     // SpaceTethers.setMessage("deltaLength = " + String.valueOf(deltaLength));

     velocity deltaVel = new velocity(smallEnd.vel);
     deltaVel.subtract(bigEnd.vel);
     deltaVel.divide(numMasses);

     double radiusBigEnd = diameterBigEnd / 2;
     double radiusSmallEnd = diameterSmallEnd / 2;

     double deltaRestLength = restLength / numSprings;

     // setMessage("deltaRestLength = " + String.valueOf(deltaRestLength), 200);

     double nextStrength, nextRadius, nextAvgRadius;
     double nextArea, nextKg, nextSpringK;
     position nextPosition;
     velocity nextVelocity;

     double nextLength = this.length / numSprings;

     int tapertry;

     this.taperCMfrac = 0.5;  // first guess
     int loops=1;
     if (taperType == k.TaperAutomatic) {
       loops = k.taperConvergeLoops;
     }
     taperCM.pos=new position(0,0);
     taperCM.vel=new velocity(0,0);
     for (tapertry=0; tapertry<loops; tapertry++) {
        this.taperCM.pos.partway(bigEnd.pos, smallEnd.pos, taperCMfrac);
        this.taperCM.vel.partway(bigEnd.vel, smallEnd.vel, taperCMfrac);
        int i;
        // Taper function may need us to start at the payload end
        this.taperTension = 0;
        for (i=maxMass-1; i>minMass; i--) {
           nextPosition = new position(bigEnd.pos, deltaPos, i);
           nextVelocity = new velocity(bigEnd.vel, deltaVel, i);

           try {
             nextAvgRadius = taperRadius(nextPosition, nextVelocity,
                                         radiusBigEnd, radiusSmallEnd,
                                         i, taperType, safetyFactor);
           } catch (Exception e1) {
             throw new Exception("tether.taperRadius Exception " + e1.toString());
           }

           nextArea =  k.pi * nextAvgRadius * nextAvgRadius;
           nextKg = nextArea * deltaRestLength * density;

           {
             double dragArea;

             double Cd;
             if (tetherDrag) {
               dragArea = 2 * nextAvgRadius * deltaRestLength;
               Cd=2.2;
             } else {
               dragArea = 0;
               Cd=2.2;
             }
             double liftOverDrag = 0;
             if (tetherMass[i] != null) {
               findsimobject.remove(tetherMass[i]);  // Making over
             }
             if (i == minMass+1 || i==maxMass-1) {
               nextKg = nextKg * 1.5;  // Add 1/2 slice that should be at end
             }
             tetherMass[i]= new mass( nextKg, nextPosition, nextVelocity,
                                      Cd, dragArea, liftOverDrag,
                                      this.label+ "." + i);


           }
           nextStrength=0;
           if (i < numSprings) {
              nextStrength=tensile*nextArea;
              nextSpringK=java.lang.Math.abs(nextStrength/(elasticity*deltaRestLength));
              tetherSpring[i]= new spring(deltaRestLength,
                                   nextSpringK, nextStrength,
                                   this.label + ".spring." + i);
              if( i == minMass+1) {
                   tetherSpring[minMass]= new spring(deltaRestLength,
                                   nextSpringK, nextStrength,
                                   this.label + ".spring." + minMass);
              }
           }
           if (tapertry == k.taperConvergeLoops-1) {
              k.outputAdd(this.label +
                          " BuildTether " +
                          " index "  + i +
                          " distKm "   + k.dTS2((i*deltaRestLength)/1000.0) +
                          " diameterCm " + k.dTS3(2.0*nextAvgRadius*100.0) +
                          " mass "   + k.dTS2(nextKg) +
                          " area "   + k.dTS2(nextArea) +
                          " strength " + k.dTS2(nextStrength) +
                          "\n");
           }
        }
        for (i=minSpring; i < numSprings; i++) {
           tetherSpring[i].setMasses(tetherMass[i],tetherMass[i+1]);
        }

        setCMIndex();

        // next time try halfway between last and what was just computed
        this.taperCMfrac = (this.taperCMfrac + this.cmDistance/this.length )/2.0;
     }  // we loop each time trying new CM

     this.winchMassMemory = new mass[numMasses];
     for (int i=0; i<numMasses; i++) {
        this.winchMassMemory[i]=tetherMass[i];
     }
     findsimobject.addAlias(this.label + ".Ballast", ballast.label);
     findsimobject.addAlias(this.label + ".0", ballast.label);
     findsimobject.addAlias(this.label + ".CenterOfMass", this.label + "." + indexCM);
     findsimobject.addAlias(this.label + ".Payload", payload.label);
     findsimobject.addAlias(this.label + "." + maxMass, payload.label);
     findsimobject.addAlias(this.label + ".C", this.label + "." + indexCM);
            // C = mass that started as center of mass
            //   is used screenOrigin
  //   findsimobject.add(this);
     k.lastTether=this;
     java.lang.System.gc();  // we generate a lot of garbage
     Initialized=true;
  }


  // convert from a spring strength to a tether radius for this tether
  private double strengthToDiameter(double strength) {
    double area = strength / this.tensile;
       // area = pi * r^2
       // r = sqrt(area/pi)
    double radius = java.lang.Math.sqrt(area/k.pi);
    return(2.0 * radius);
  }

  // Returns average radius of next part of tether
  private double taperRadius(position p1, velocity v1,
                       double radiusBigEnd, double radiusSmallEnd,
                       int i, int TType, double safetyFactor)
                       throws Exception {
     if (TType == k.TaperLinear) {
        double deltaRadius = (radiusSmallEnd-radiusBigEnd)/numSprings;
        double nextRadius = radiusBigEnd + i*deltaRadius;  // taper function
        double nextAvgRadius = nextRadius + 0.5*deltaRadius;         // use halfway radius
        return(nextAvgRadius);
     }
     if (TType == k.TaperAutomatic) {
        try {
          this.taperTension += weight(this.tetherMass[i + 1].kg, p1, v1);
        } catch (Exception e1) {
          throw new Exception("tether.weight() Exception " + e1.toString());
        }
        double nextArea = this.taperTension / tensile;
        nextArea = nextArea * safetyFactor;
        double nextAvgRadius = java.lang.Math.sqrt(nextArea/k.pi); // A = pi * r^2
        return(nextAvgRadius);
     }
     return(0);  // Not a valid taper type
  }

  //  Assume we are starting vertical
  //    look at gravity less centripital from earth and tether circles
  //  Note that can be negative as we get near ballast.
  //  Assume center of mass is orbiting at stable height?
  //  Can do gravity difference between end and center!!!
  private double weight(double Kg, position p1, velocity v1) {
       double result=0;

       // force from Earth gravity
       double rcm = taperCM.pos.distanceEarth();
       double re = p1.distanceEarth();
       double grav = Kg * k.earth.kg * k.gravConst / (re * re);  // Earth Grav
             grav -= Kg * k.earth.kg * k.gravConst / (rcm * rcm);

       if (k.moon != null) {
          // force from Moons gravity
          rcm = taperCM.pos.distance(k.moon);
          double rm = p1.distance(k.moon);
          grav += Kg * k.moon.kg * k.gravConst / (rm * rm);  // Earth Grav
          grav -= Kg * k.moon.kg * k.gravConst / (rcm * rcm);
       }

       // tension from spin
       double rs = taperCM.pos.distance(p1);  // distance to CM
       velocity vs = new velocity(v1, taperCM.vel, -1);  // spin velocity
       double vsm = vs.magnitude();
       double centTether = Kg * vsm*vsm/rs;

       double taperCMv = vsm;
       // tension from earth centripital
       // double vo = v1.magnitude();  // very bad
      //double vo = this.taperCMv;     // not counting sping velocity
           // next is best so far
       double vo = taperCMv * (re / rcm); // not counting sping velocity
       // double vo = java.lang.Math.abs(
       //              java.lang.Math.abs(v1.x) - vs); // orbitl not counting sping velocity
       double centEarth = Kg * vo*vo/re;   // centripital for Earth lessens tension
       centEarth -= Kg * taperCMv*taperCMv/rcm;

       // I can see being off by the fact that there is one extra
       //   revolution every orbit - 90 mil
       if (re == taperCM.pos.distanceEarth()) {
         return(result);          // zero weight at center - easy case
       }
       // below CM as we go up tether next part has to handle our extra tension
       if (re < taperCM.pos.distanceEarth()) {        // if below the center of mass
         result += grav;          // adds to weight/tension when below CM
         result += centEarth;     // reduces tension when below
         result += centTether;    // adds to tension
         return(result);
       }
       // above CM is tricky conceptual case
       // If there was no ballast tether shape would just mirror
       // if there is ballast I think it is a cut off mirror// else                    // we are above - tricky case to understand
         result += grav;          // reduces when above CM
         result += centEarth;     // adds when above
         result -= centTether;    // reduces

      return(result);
  }

  public mass getBallast () {
     return (ballast);
  }

  public mass getPayload () {
     return (payload);
  }


  // Called by this.paint()
  // We find the index of the mass nearest the center of mass
  public void setCMIndex () throws Exception {
     int guess;
     boolean done=false;
     double ballastTorque=0;

    position end = tetherMass[minMass].pos;
    grandTotalKg=0;
    for (int i = minMass; i <= maxMass; i++) {
      if (tetherMass[i] == null) {
        throw new Exception("setCMIndex found null mass at index " + i);
      }
      grandTotalKg += tetherMass[i].kg;
      ballastTorque += tetherMass[i].kg
                     * (end.distance(tetherMass[i].pos));
    }
    cmDistance = ballastTorque/grandTotalKg;

     int low=minMass;
     int high=numMasses-1;
     while (low < high-1) {
        guess = (low + high) / 2;
        if (end.distance(tetherMass[guess].pos) < cmDistance) {
           low=guess;
        } else {
           high=guess;
        }
     }

     // Which ever was closer, low or high, use that
     if (Math.abs(end.distance(tetherMass[low].pos) - cmDistance) <
         Math.abs(end.distance(tetherMass[high].pos) - cmDistance)){
       indexCM = low;
     } else {
       indexCM = high;
     }

     this.centerOfMassMass=tetherMass[indexCM];

     tetherOnlyKg = grandTotalKg - payload.kg - ballast.kg;
  }


  private void setMessage (String s) {
     CurrentMessage=s.toString();
  }

  public void simulate1(double deltaT) throws Exception {
    for (int i = minMass; i < numMasses; i++) {
      tetherMass[i].simulate1(deltaT);
    }
  }


  // We call spring.simulate2() for each of the springs.
  //  This computes and adds in the spring forces for the tether masses.
  public void simulate2(double deltaT) throws Exception {
     int i=0;
     double thisStretch;

     maxStretchNow=0;
     maxStretchIndexNow=0;
     minStretchNow=restLength+1;  // so fake minimum must be bigger than any real stretch
     minStretchIndexNow=-1;
     for (i=minSpring; i<=maxSpring; i++) {
        tetherSpring[i].simulate2(deltaT);
        thisStretch=tetherSpring[i].stretch;
        if (thisStretch > maxStretchNow) {
           maxStretchNow=thisStretch;
           maxStretchIndexNow=i;
        }
        if (thisStretch < minStretchNow) {
           minStretchNow=thisStretch;
           minStretchIndexNow=i;
        }
     }
     if (maxStretchNow > maxStretch) {
        maxStretch=maxStretchNow;
        maxStretchIndex = maxStretchIndexNow;
     }
     if (minStretchNow < minStretch) {
        minStretch=minStretchNow;
        minStretchIndex = minStretchIndexNow;
     }
  }

  // Call mass.simulate3() for each tether mass to add in
  //  mass.totalForce and update velocity and position.
  public void simulate3(double deltaT) throws Exception {
    int i;
    for (i = minMass+1; i < numMasses-1; i++) {
      tetherMass[i].simulate3(deltaT);
    }
  }

  // Paint tether and fun facts
  public void paint (Graphics g) throws Exception {
     int i;
     if (!Initialized){
        return;
     }
     setCMIndex();

     // We set the position of masses wound on on winch spool to be
     // that of ballast just in case screenOrigin is using one of these.
     for (i=0; i<minMass; i++) {
        winchMassMemory[i].pos.copy(ballast.pos);
        winchMassMemory[i].vel.copy(ballast.vel);
     }

     mass pay = getPayload();
     mass bal = getBallast();
     mass cm = tetherMass[indexCM];

     findsimobject.removeAlias(this.label + ".CenterOfMass");
     findsimobject.addAlias(this.label + ".CenterOfMass", this.label + "." + indexCM);

     double pv = pay.vel.magnitude();
     double bv = bal.vel.magnitude();
     double ph = pay.pos.height();
     double bh = bal.pos.height();
     double cmh = cm.pos.height();
     double cmv = cm.vel.magnitude();
     velocity tipRelVelocity = new velocity(cm.vel, pay.vel, -1);
     double tipSpeed = tipRelVelocity.magnitude();

     if (pv < minPayV) {minPayV=pv;};
     if (bv < minBalV) {minBalV=bv;};
     if (ph < minPayH) {minPayH=ph;};
     if (bh < minBalH) {minBalH=bh;};
     if (cmh < minCMH) {minCMH=cmh;};
     if (cmh > maxCMH) {maxCMH=cmh;};
     if (cmv < minCMV) {minCMV=cmv;};
     if (k.logPerigee) {
        if (cmh> lastCMH) {
           if (climb < 1) {
               climb = 1;
               logVelPos("PRG ");
           }
        }  else  {   /* assumed if (cmh <= lastCMH) */
         if (climb > 0 ) {
           climb =0;
           logVelPos ("APG ");
         }
       }
     }
     lastCMH = cmh;
     lastCVX = cm.vel.x;
     lastCVY = cm.vel.y;
     lastCX = cm.pos.x;
     lastCY = cm.pos.y;
     lastTime=k.simTime;

     if (k.logXY ) {
           logVelPos(" log ");
     }


     if ((ph < 0) || (bh < 0)) {
       throw new Exception("Tether Crashed into the Earth");
     }

     bal.paintDot(g);
     cm.paintDot(g);


     //  Draw the tether
     for (i=minMass; i<(numMasses-1); i++) {
          screen.drawLine(g, tetherMass[i].pos.x, tetherMass[i].pos.y,
                 tetherMass[i+1].pos.x, tetherMass[i+1].pos.y);
     }

     if (k.sim3D) {       //  Draw the tether if sim3D
       mass endPoint;
       if (k.tetherEnd3D!=null) {
         endPoint=k.tetherEnd3D;
       } else {
         endPoint = ballast;
       }
       position left;
       position right;
       if (endPoint.pos.distance2D(ballast) < endPoint.pos.distance2D(payload)) {
         left=ballast.pos;
         right=payload.pos;
       } else {
         left=payload.pos;
         right=ballast.pos;
       }
       double xoffset = endPoint.pos.distance2D(left)-endPoint.pos.distance2D(k.originMass);
       double xscale = ballast.pos.distance2D(payload);
       // We plot distance along tether vs Z coordinate centered on screen
       double omx = k.originMass.pos.x;
       double omy = k.originMass.pos.y;
       double zoffset = endPoint.pos.z;
       for (i = 0; i < numMasses; i++) {
         double leftdist = left.distance2D(tetherMass[i]);
         double rightdist = right.distance2D(tetherMass[i]);
         double x = omx + xoffset + (leftdist/ (leftdist+rightdist)) * xscale;
//        double x = omx + tetherMass[i].pos.distance(endPoint) - halfx;
//        double x = tetherMass[i].pos.x;
//        double x = tetherMass[i].pos.y - omy + omx;
          double y = omy + tetherMass[i].pos.z - zoffset; // * 100.0;
         screen.drawDot(g, x, y);
       }
     }

     if (k.logXY) {
       k.outputAdd(this.label +
                     " tether " +
                     " time " + k.dTS2(k.simTime) +
                     " balx " + k.dTS2(bal.pos.x) +
                     " baly " + k.dTS2(bal.pos.y) +
                     " cmx " + k.dTS2(cm.pos.x) +
                     " cmy " + k.dTS2(cm.pos.y) +
                     " payx " + k.dTS2(pay.pos.x) +
                     " payy " + k.dTS2(pay.pos.y) +
                     "\n");
     }

     if (!this.showText) {
       return;
     }

     cm.paintText(g, this.label);
   /*
     tetherSpring[minSpring].paint(g);
     tetherSpring[minSpring+1].paint(g);
     tetherSpring[minSpring+2].paint(g);
     tetherSpring[minSpring+3].paint(g);

     tetherSpring[maxSpring-3].paint(g);
     tetherSpring[maxSpring-2].paint(g);
     tetherSpring[maxSpring-1].paint(g);
     tetherSpring[maxSpring].paint(g);
     */

     if (k.showEarthRelative) {
       screen.print(g,
                    this.label + ": CM vel " + k.dTS(cmv) + " min " + k.dTS(minCMV) +
                    " ht  " + k.dTS(cmh) + " min " + k.dTS(minCMH)
                    + " max " + k.dTS(maxCMH));
     }

     if (k.moon != null && k.showMoonRelative) {
       velocity moonRelVel = new velocity(k.moon.vel, cm.vel, -1);
       position moonRadius = new position(k.moon.pos, cm.pos, -1);
       position tipRadius = new position(k.moon.pos, pay.pos, -1);
       vector3d angularMomentum = new vector3d(0,0,0);
       vector3d moonRadius3d = new vector3d(moonRadius);
       vector3d moonRelVel3d = new vector3d(moonRelVel);
       angularMomentum.setCross(moonRadius3d, moonRelVel3d);
       double balOrbAngMomen = averageMassKg * angularMomentum.magnitude();
       if (averageMassKg == 0 || ourAverage==null) {
         int numDataPoints = (int) (k.averagingSeconds / k.timePerDisplay);
         ourAverage= new rollingaverage(numDataPoints, balOrbAngMomen);
         averageMassKg = bal.kg;
       }

       double avgLunarAngMom=ourAverage.timeAverage(balOrbAngMomen);


       screen.print(g,  this.label + " Moon relative:  velocity " +
                        k.dTS2(moonRelVel.magnitude()) +
                        " radius " +
                        k.dTS2(moonRadius.magnitude()) +
                        " tip height " +
                        k.dTS2(tipRadius.magnitude() - k.moonRadius ));
       if (k.showMomentum) {
         screen.print(g, " Ballast lunar orbital momentum - balOrbAngMomen " +
                      k.dTSS10(balOrbAngMomen) +
                      " rolling average " +
                      k.dTSS10(avgLunarAngMom)
                      );
       }

//       screen.print(g, " bal.kg " + k.dTS2(bal.kg) +
//                       " rad3d " + k.dTS2(moonRadius3d.magnitude()) +
//                       " vel3d " + k.dTS2(moonRelVel3d.magnitude()) +
//                       " cross " + k.dTS2(angularMomentum.magnitude()));
     }

     if (k.showEnergy) {
       double grandTotalKg = getBallast().kg + tetherOnlyKg + getPayload().kg;
       double potentialEnergy = -k.gravConst * grandTotalKg * k.earthMass /
           (cmh + k.earthRadius);
       double kineticEnergy = 0.5 * grandTotalKg * cmv * cmv;
       double totalEnergy = potentialEnergy + kineticEnergy;

       screen.print(g, " Energy " + k.dTS(totalEnergy)
                    + " KE " + k.dTS(kineticEnergy)
                    + " PE " + k.dTS(potentialEnergy));

       bal.showEnergy(g, "Ballast");
       cm.showEnergy(g, "CM");
       pay.showEnergy(g, "Payload");
     }
/*
     screen.print(g, "Pay vel " + k.dTS(pv) + " min " +  k.dTS(minPayV)+
                     "Pay ht  " + k.dTS(ph) + " min " +  k.dTS(minPayH));
     if (k.showXY) {
        screen.print(g, "      X=" + k.dTS(pay.vel.x) + "  Y=" +
            k.dTS(pay.vel.y)+
            " pos  X=" + k.dTS(pay.pos.x) + "  Y=" + k.dTS(pay.pos.y));
     }
     screen.print(g, "Bal vel " + k.dTS(bv) + " min " +  k.dTS(minBalV)+
                     "Bal ht  " + k.dTS(bh) + " min " +  k.dTS(minBalH));
     if (k.showXY) {
         screen.print(g, "      X=" + k.dTS(bal.vel.x) + "  Y=" +
                  k.dTS(bal.vel.y)+
                  " pos  X=" + k.dTS(bal.pos.x) + "  Y=" + k.dTS(bal.pos.y));
     }
*/

     position tipdeltapos = new position(cm.pos, pay.pos, -1);          // subtract
     double tiplen = tipdeltapos.distance();
     double centrip=0;
     if (tiplen > 0) {
        centrip = (tipSpeed*tipSpeed/tiplen)/k.earthAcceleration;
     }
     screen.print(g, "Tip speed " + k.dTS(tipSpeed)
            + " Tip Centripetal " + k.dTS3(centrip)
            + " Gs ");
     if (k.showTetherStatic) {
       screen.print(g, "Ballast " + k.dTS2(getBallast().kg) + " Kg " +
                    "Tether " + k.dTS2(tetherOnlyKg) + " Kg " +
                    "Payload " + k.dTS2(getPayload().kg) + " Kg");
       screen.print(g, "Diameters in cm : Ballast="
                    + k.dTS3(100.0 * strengthToDiameter(tetherSpring[minMass].strength))
                    + " CM="
                    + k.dTS3(100.0 * strengthToDiameter(tetherSpring[indexCM].strength))
                    + " Pay="
                    + k.dTS3(100.0 * strengthToDiameter(tetherSpring[maxSpring].strength)));
     }

     double curLength = bal.pos.distance(pay.pos);
     double cmDist = bal.pos.distance(cm.pos);
     screen.print(g, "Length " + k.dTS(curLength) +
                     " CMIndex " + this.indexCM +
                     "  CMI  " + k.dTS2(100*cmDist/curLength) + "% len" +
                     "  CenMas  " + k.dTS2(100*cmDistance/curLength) + "% len" +
                     "  CMF " + k.dTS2(100*this.taperCMfrac) );

     screen.print(g, "CurrentMaxStretch " + k.dTS(1.0*this.maxStretchIndexNow)
                 + "  " + k.dTS2(100 * this.maxStretchNow/(this.restLength/this.slices)) + "%"
                 + " CurrentMinStretch " + k.dTS(1.0*this.minStretchIndexNow)
                 + "  " + k.dTS2(100 * this.minStretchNow/(this.restLength/this.slices)) + "%");
     screen.print(g, "MaxStretchSpring " + k.dTS(1.0*this.maxStretchIndex)
                 + "  " + k.dTS2(100 * this.maxStretch/(this.restLength/this.slices)) + "%"
                 + " MinStretchSpring " + k.dTS(1.0*this.minStretchIndex)
                 + "  " + k.dTS2(100 * this.minStretch/(this.restLength/this.slices)) + "%");

   //  screen.print(g, CurrentMessage);


  }


  public void logVelPos(String  inOrOut) {
        k.outputAdd( label + " " +
                       inOrOut  + k.dTS2( lastTime)  +
                       " velCM  " + k.dTS( lastCVX) +" " + k.dTS( lastCVY) +
                       " alt "   + k.dTS2( lastCMH) +
                       " posCM  " + k.dTS( lastCX) + " "+ k.dTS( lastCY) +
                      "\n");
  }


}

 // Copyright (c) Vincent Cate 2002

package spacetethers;

import java.awt.*;
import java.lang.Math;

// mass has a number of kg, then a xyz position and xyz velocity
public class mass extends simtype {
   public double kg=0;
   public position pos=null;
   public velocity vel=null;
   public double Cd=0;             // coefficient of drag
   public double dragArea=0;       // area for drag purposes
   public double liftOverDrag=0;
//   public boolean showText=true;   // do we display text stuff?
//   public String label = "";
   public int stage=0;          // rocket stages numbered from top down

 // Only a mass can be in 2 objects at once, so have danger of simulating twice
   private boolean sim2done=false; // if mass is in 2+ objects only sim once
   private boolean sim3done=false; // if mass is in 2+ objects only sim once

   force liftAndDrag = new force(0, 0);
   force totalForce = new force(0, 0);
   public velocity airRelativeVelocity=null;
   public double Mach;
   public double stagTemp;

   public double minBalV=k.bigNum;   // minimum velocity of this mass relative to ballast
   public double minBalD=k.bigNum;   // minimum distance to ballast
   public double minAlt=k.bigNum;    // minimum altitude above EArth
   public double maxAlt=0;           // maximum altitude above Earth
   public double velMaxAlt=0;        // velocity at maximum altitude
   public double minDistToMoon=k.bigNum;
   public double maxGs=0;
   public double Gs=0;               // really dragGs



   public double lastAlt = 0;
   public double lastVelX = 0;
   public double lastVelY = 0;
   public double lastX = 0;
   public double lastY = 0;
   public double lastTime=k.simTime;
   public double climb =0;
   public air ourAir=null;




   public mass() {
     this.kg = 0;
     this.pos = null;
     this.vel = null;
     this.Cd = 0;
     this.dragArea = 0;
     this.label="";
     findsimobject.add(this);
   }


   public mass(String label) {
     this.kg = 0;
     this.pos = null;
     this.vel = null;
     this.Cd = 0;
     this.dragArea = 0;
     this.label=label;
     findsimobject.add(this);
   }


   // make a copy of another mass so we are independant of it
   // Used when we let loose of a payload
   public mass(mass m1) {
     this.kg = m1.kg;
     this.pos = new position(m1.pos);
     this.vel = new velocity(m1.vel);
     this.Cd = m1.Cd;
     this.dragArea = m1.dragArea;
     this.label = m1.label;
     this.liftOverDrag=m1.liftOverDrag;


     this.label = m1.label;
     this.ourAir= new air(this.pos);

     findsimobject.add(this);
   }


 // No drag mass
 public mass(double kg, position p1, velocity v1, String label) throws Exception  {
     k.debug("mass start " + label);
     if (p1 == null) {
       throw new Exception(" Making mass " + label + " but no position ");
     }
     if (v1 == null) {
       throw new Exception(" Making mass " + label + " but no velocity ");
     }
     this.kg = kg;
     this.pos = new position(p1); // We often make several masses from same input
     this.vel = new velocity(v1);
     this.Cd = 0;
     this.dragArea = 0;
     this.liftOverDrag = 0;
     this.label = label;
     k.debug("mass doing air ");
     this.ourAir= new air(this.pos);
     k.debug("mass done with air");
     findsimobject.add(this);
     k.debug("mass done ");
   }

   public mass(double kg, position p1, velocity v1, double Cd,
               double dragArea, double liftOverDrag,
               String label) {
     this.kg = kg;
     this.pos = new position(p1);    // We often make several masses from same input
     this.vel = new velocity(v1);
     this.Cd = Cd;
     this.dragArea = dragArea;
     this.liftOverDrag = liftOverDrag;
     this.label = label;
     this.ourAir= new air(this.pos);
     findsimobject.add(this);
   }

   public position getPosition() {
     return (pos);
   }

   public velocity getVelocity() {
     return (vel);
   }

   public void setKg(double kg) {
     this.kg = kg;
   }

   public double getHeight() {
     return (this.pos.distance() - k.earthRadius);
   }

   // Force on this of m2's gravity
   private void addGravityForce(mass m2) {
     double r = this.pos.distance(m2.pos);
     double forceG = -this.kg * m2.kg * k.gravConst / (r * r);
     totalForce.x += forceG * (this.pos.x - m2.pos.x) / r;
     totalForce.y += forceG * (this.pos.y - m2.pos.y) / r;
     if (k.sim3D) {
        totalForce.z += forceG * (this.pos.z - m2.pos.z) / r;
     }
   }


   private void setTotalForceFromGravity() {
       totalForce.reset();
       if (this != k.earth) {
         addGravityForce(k.earth); // totalForce += Earth's Gravity
       }
       if (k.moon != null && this != k.moon) {
         addGravityForce(k.moon); // totalForce += Moon's gravity
       }
   }


  // D = Cd * A * .5 * r * V^2
  // http://www.grc.nasa.gov/WWW/K-12/airplane/drageq.html

  private void calcLiftAndDrag(air ourair) {
     double density = ourair.density;
     double drag, lift;
     double x, y;
     velocity airRelativeVelocity;

     if (k.rotatingAir) {
       airRelativeVelocity = new velocity(this.vel, ourair.vel, -1);
     } else {
       airRelativeVelocity = this.vel;  // not really relative
     }

     double v = airRelativeVelocity.magnitude();
     double xvec = airRelativeVelocity.x/v;   // unit vector
     double yvec = airRelativeVelocity.y/v;

     drag = Cd * dragArea * 0.5 * density * v*v;
     liftAndDrag.x = -drag * xvec;         // drag is negative in velocity vector
     liftAndDrag.y = -drag * yvec;

     if (liftOverDrag != 0) {
       // lift needs to be 90 degrees off of velocity in the "up" direction
       // "up" depends on which of quadrant are position is in
       lift = liftOverDrag * drag;
       double xliftsign = 1;
       double yliftsign = 1;
       if (pos.x < 0) { xliftsign = -1; }
       if (pos.y < 0) { yliftsign = -1; }
       if (liftOverDrag < 0) {        // flip if he wants down
         xliftsign = -xliftsign;
         yliftsign = -yliftsign;
       }
       liftAndDrag.y += yliftsign * Math.abs(lift * xvec); // xvec since 90 degrees off
       liftAndDrag.x += xliftsign * Math.abs(lift * yvec); // yvec since 90 degrees off
     }
  }

   private void addLiftAndDragForce() {
     force drag = null;
     if ( (this.Cd > 0) && (this.dragArea > 0)) { // we doing drag?
       double height = this.pos.height();
       ourAir.setDensity(height);          // don't worry about velocity each deltaT
           //  ourAir.setValues(this.pos);  - don't really need this
       if (height < 0) {                   // if on the ground
           this.vel = ourAir.vel;
         totalForce.set(0, 0);     // If we are on ground net force is 0
       } else {
         calcLiftAndDrag(ourAir);
         totalForce.add(liftAndDrag);
       }
     }
   }


 private void applyForce(double deltaT) {
   // F = M*A     A = F / M
   double accelX = totalForce.x / this.kg;
   double accelY = totalForce.y / this.kg;
   double accelZ=0;
     if (k.sim3D) {
       accelZ = totalForce.z / this.kg;
     }

     // deltaV = A * T
     this.vel.x += deltaT * accelX;
     this.vel.y += deltaT * accelY;
     if (k.sim3D) {
       this.vel.z += deltaT * accelZ;
     }

     // detlaX = V * T
     this.pos.x += deltaT * this.vel.x;
     this.pos.y += deltaT * this.vel.y;
     if (k.sim3D) {
       this.pos.z += deltaT * this.vel.z;
     }
  }

  public void simulate1(double deltaT)  {
     if (this.kg <= 0) {
       this.Finished=true;
       return;
     }
     setTotalForceFromGravity();
     this.sim2done=false;
     this.sim3done=false;
  }
  public void simulate2(double deltaT) {
     if (this.kg <= 0 || sim2done) {
       return;
     }
     addLiftAndDragForce();    // also sets this.ourAir
     sim2done=true;
  }

  // Tether, Rocket, SolarSail, etc can add their force during simulate3()
  //  and then we use the totalForce to calculate the new velocity/position
  //  in simulate3() - but only once even if called several times.
  public void simulate3(double deltaT) throws Exception {
     if (this.kg <= 0 || sim3done) {
       return; // don't simulate if no mass or already done
     }
     applyForce(deltaT);
     sim3done=true;
  }

  // Really setting  ourAir, airRelativeVelocity, Mach, and stagTemp;
  public void setAirRelativeVelocity(){
     this.ourAir.setValues(this.pos);
     if (this.pos.height() >= 0) {
       this.airRelativeVelocity = new velocity(this.vel, ourAir.vel, -1);
       this.Mach = airRelativeVelocity.magnitude() / ourAir.getMachSpeed();
     } else {
       if (this != k.earth) {
         this.vel = ourAir.vel;
       }
       this.airRelativeVelocity = new velocity(0,0);
       this.Mach = 0.0;
     }
     this.stagTemp = ourAir.getTempKelvin() *
           (1.0 + ( (k.airSpecificHeatRatio - 1.0) / 2.0)
            * Mach * Mach);
  }

  public void paint(Graphics g) {
     if (this.kg <= 0) {
       this.Finished=true;
       return; // don't paint if no mass
     }
     this.setAirRelativeVelocity();

     double mv = this.vel.magnitude();
     double vx = this.vel.x;
     double vy = this.vel.y;
     double mh = this.pos.height();
     double x = this.pos.x;
     double y = this.pos.y;
     String line1=null;
     if (k.sim3D) {
       if (k.view3D==2) {
         vy=this.vel.z;
         y=this.pos.z;
       }
     }
     screen.drawDot(g, x, y);
     screen.drawString(g, label, x, y );


     if (k.logPerigee) {
       if (this.pos.height()> this.lastAlt) {
        if (this.climb < 1) {
           this.climb =1;
           logVelPos ("PERIGEE");
        }
       }
       else  {   /* assumed if (this.pos.height()<= this.lastAlt) */
         if (this.climb > 0 ) {
           this.climb = -1;
           logVelPos ("APOGEE ");
         }
       }
     }


      this.lastAlt = this.pos.height();
      this.lastVelX = vx;
      this.lastVelY = vy;
      this.lastX = x;
      this.lastY = y;
      this.lastTime=k.simTime;

     if ((k.logXY) || ((k.logPerigee) && (k.simTime <21)) ) { /* log starting values */
        logVelPos ("logXY  ");
     }


     if (this.showText == false) {
       return;
     }



     if (mh > maxAlt) {
       maxAlt = mh;
       velMaxAlt = this.vel.magnitude();
     }
     if (mh < minAlt) {minAlt = mh;};

     line1= label
         + "  " + k.dTS(kg)
         + " Kg  vel " + k.dTS(mv);
     if (mh < 100000) {
       if (k.rotatingAir) {
         line1 += " vel-air " + k.dTS(airRelativeVelocity.magnitude());
       }
       line1 += " Mach " + k.dTS2(Mach);
     }

     if (mh > -100 && mh<1500000) {
       line1 += " height " + k.dTS(mh);                  // distance from Earth surface
       line1 += " Min " + k.dTS(minAlt)
              + " Max " + k.dTS(maxAlt)
              + " VMaxA " + k.dTS(velMaxAlt);
     } else {
       line1 += " radius " + k.dTS(mh + k.earthRadius);  // distance from center
       line1 += " Min " + k.dTS(minAlt+ k.earthRadius)
              + " Max " + k.dTS(maxAlt+ k.earthRadius);

     }

     if (k.showEarthRelative) {
       screen.print(g, line1);
     }
     line1 = "";

     if (k.showMoonRelative && k.moon != null && this != k.moon && this != k.earth) {
       velocity moonRelVel = new velocity(k.moon.vel, this.vel, -1);
       position moonRadius = new position(k.moon.pos, this.pos, -1);
       screen.print(g, this.label + " Moon relative velocity " +
                        k.dTS2(moonRelVel.magnitude()) +
                        " radius " +
                        k.dTS2(moonRadius.magnitude()));
     }

     if (k.showEnergy) {
       this.showEnergy(g, " ");
     }

     double dragF = this.liftAndDrag.magnitude();
     if (dragF > 0.0001) {
        Gs = dragF/this.kg/k.earthAcceleration;
        if (Gs > this.maxGs) {
          this.maxGs = Gs;
        }
        if (maxGs > .1) {
          if (this.liftOverDrag != 0) {
            line1 += " lift&drag-Gs " + k.dTS3(Gs);
          } else {
            line1 += " drag-Gs " + k.dTS3(Gs);
          }
          line1 += " maxGs " + k.dTS3(maxGs);
        } else {
          if (this.liftOverDrag != 0) {
            line1 += " lift&drag-MGs " + k.dTS3(Gs * 1000.0);
          } else {
             line1 += " drag-MGs " + k.dTS3(Gs * 1000.0);
          }
          line1 += " maxMGs " + k.dTS3(maxGs*1000.0);

          if ((k.logDrag) && (dragF > .5)) {
            k.outputAdd(this.label +
                   " time " + k.dTS2(k.simTime) +
                   " vel " + k.dTS3(mv / 1000.0) +
                   " alt " + k.dTS2(this.pos.height() / 1000) +
                   " lift&drag-MGs " + k.dTS3(Gs * 1000.0) +
                   " dragF " + k.dTS3(dragF) +
                   "\n");
          }
       }
     }

     if (mh < 100000 && this.Cd > 0) {
        line1 += " StagTemp " + k.dTS(k.kelvinToCelsius(stagTemp)) + " C  "
                             + k.dTS(k.kelvinToFahrenheit(stagTemp)) + " F ";
     }



     screen.print(g, line1);

     if (k.showXY) {
       screen.print(g, "      X=" + k.dTS(x) + "  Y=" + k.dTS(y));
       screen.print(g, "     VX=" + k.dTS(vx) + "  VY=" + k.dTS(vy));
     }



     if (k.showMassToMoon && k.moon != null) {
       position dist = new position(k.moon.pos);
       dist.subtract(this.pos);
       double distToMoonSurface = dist.magnitude() - k.moonRadius;
       if (distToMoonSurface < this.minDistToMoon) {
         this.minDistToMoon = distToMoonSurface;
       }
       screen.print(g, "  To Moon Surface=" + k.dTS(distToMoonSurface)
                    + "  min=" + k.dTS(this.minDistToMoon));
     }
     if (k.showMassToTether) {
       position dist = new position(k.lastTether.centerOfMassMass.pos);
       dist.subtract(this.pos);
       velocity deltav = new velocity(k.lastTether.centerOfMassMass.vel);
       deltav.subtract(this.vel);
       if (deltav.magnitude() < minBalV) {
         minBalV = deltav.magnitude();
       }
       screen.print(g, "  To CM:  DeltaV=" + k.dTS(deltav.magnitude())
                    + " dist=" + k.dTS(dist.magnitude()));

       dist = new position(k.lastTether.payload.pos);
       dist.subtract(this.pos);
       deltav = new velocity(k.lastTether.payload.vel);
       deltav.subtract(this.vel);
       screen.print(g, "  To Payload:  DeltaV=" + k.dTS(deltav.magnitude())
                          + " dist=" + k.dTS(dist.magnitude()));

       dist = new position(k.lastTether.ballast.pos);
       dist.subtract(this.pos);
       deltav = new velocity(k.lastTether.ballast.vel);
       deltav.subtract(this.vel);
       if (dist.magnitude()< minBalD) {minBalD = dist.magnitude();};
       screen.print(g, "Ballast:DV=" + k.dTS(deltav.magnitude())
                          + " dist=" + k.dTS(dist.magnitude()) +
                          "  MinV "+k.dTS(minBalV) +" MinD "
                          +k.dTS(minBalD));
       if (k.showXY) {
         screen.print(g, "Ballast: VX=" + k.dTS2(deltav.x) + " VY=" +
                      k.dTS2(deltav.y)+
                      " X=" + k.dTS2(dist.x) + " Y=" + k.dTS2(dist.y));
       }
     }
  }

  public void showEnergy(Graphics g, String s1)
  {
     // http://www.madsci.org/posts/archives/mar98/888873174.Ph.r.html
     double mh = this.pos.height();
     double potentialEnergy = -k.gravConst * this.kg * k.earthMass / (mh + k.earthRadius);
     double kineticEnergy = 0.5 * this.kg * this.vel.magnitude() * this.vel.magnitude();
     double totalEnergy = potentialEnergy + kineticEnergy;

     screen.print(g, s1 + " Energy " + k.dTS(totalEnergy) +
                     " KE " + k.dTS(kineticEnergy) +
                     " PE " + k.dTS(potentialEnergy));
  }

  // For tethers we just want a dot not a full paint()
  public void paintDot(Graphics g) {
     screen.drawDot(g, this.pos.x, this.pos.y);
  }

  public void paintText(Graphics g, String s) {
     screen.drawString(g, s, this.pos.x, this.pos.y);
  }


  public void setOrigin() {
      screen.setOrigin(this.pos.x, this.pos.y);
  }

  public void logVelPos(String  inOrOut) {
        k.outputAdd(this.label + "." +
                       inOrOut  + k.dTS2(this.lastTime)  +
                       " velMass " + k.dTS(this.lastVelX) +" " + k.dTS(this.lastVelY) +
                       " alt "   + k.dTS2(this.lastAlt) +
                       " posMass " + k.dTS(this.lastX) + " "+ k.dTS(this.lastY) +
                       "\n");
  }

  // Sun to left and shadow radius is Earth radius
  // Do not worry about partial shadow or reduced shadow
  public boolean inShadow() {
     return(this.pos.x > 0
            && (this.pos.y > -k.earthRadius && this.pos.y < k.earthRadius));
  }

}

package spacetethers;
import java.awt.*;
import java.lang.Math;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 *
 * This simulates a winch at the ballast end of a tether.
 * We find the tether from the label.
 * We are told how much power, maximum force, maximum speed, max dist.
 * We shorten the last spring till the next mass has reached
 *   the ballast and then add it to the ballast and move
 *   minMass index up.
 *
 * Could make the normal mode of starting a winch to do it from AtTime
 *
 * Depending on how restrictive maxPower, maxForce, and maxSpeed
 *   each are, you can get very different behaviors.
 *
 * If maxForce is too low we can let tether out till the start of
 * the tether.
 */

public class winch extends simtype {
  double maxPower;        // max watts we will use
  double maxForce;        // if more force than this we let tether out
  double maxSpeed;        // never go faster than this no matter how easy
  double maxDistance;     // most tether we will winch in, then done=true.
  double maxTime;         // after this time elapses, then done=true.
  double tensionFrac;     // fraction of averageTension as max
  double springFrac;      // fraction of current spring ability as max
  tether ourTether;       // the tether we are winching
  double elapsedTime=0;   // how long we have been active so far
  double elapsedDistance=0; // how much tether so far
  double normSpringLength;  // standard rest length for a spring
  double winchSpeed;        // how fast going right now
  boolean winchingOut;       // true if we are in letting out mode
  boolean winchingIn;       // true if in pulling in mode
  boolean tensionLimit;     // true if force limited application
  rollingaverage ourAverage=null;
  double averageTension=0;
  mass ballast=null;
  int  stopReason=0;

  public winch(String label, double maxPower, double maxForce,
              double maxSpeed, double maxDistance, double maxTime,
             double tensionPerc, double springPerc )
                 throws Exception {
   this.label= label + ".winch";
   this.maxPower=Math.abs(maxPower);
   this.maxForce=Math.abs(maxForce);
   this.maxSpeed=Math.abs(maxSpeed);
   this.maxDistance=Math.abs(maxDistance);
   this.maxTime=Math.abs(maxTime);
   this.tensionFrac = tensionPerc / 100.0; // 100 will mean use averageTension as limit
   this.springFrac = springPerc / 100.0;
   this.ourTether= (tether) findsimobject.labelToSimObjectOrDie(label);
   this.normSpringLength=ourTether.restLength/ourTether.numSprings;
   this.winchingOut = (maxSpeed < 0);
   this.winchingIn = (maxSpeed > 0);
   this.ballast=ourTether.ballast;
  }

  public void simulate1(double deltaT) throws Exception {
    if (Finished) {
      return;
    }

    elapsedTime += deltaT;
    if (elapsedTime >= maxTime) {
      Finished=true;
      return;
    }
    if (elapsedDistance >= maxDistance) {
      Finished=true;
      return;
    }
    int minMass = ourTether.minMass;
    int minSpring = ourTether.minSpring;
    int maxSpring = ourTether.maxSpring;
    double tension = ourTether.tetherSpring[minSpring].tension;
    this.stopReason = 2;  // should mean spring average was reason
    if (averageTension == 0 || ourAverage==null) {
         int numDataPoints = (int) (k.averagingSeconds / k.deltaT);
         ourAverage= new rollingaverage(numDataPoints, tension);
    }
    averageTension=ourAverage.timeAverage(tension);
    tensionLimit = false;
    if (tension > 0) {
      winchSpeed = maxSpeed; // starting guess that speed limited
    } else if (winchingOut) {
      this.stopReason = 1;
      winchSpeed = 0;        // No tension no movement when winchingOut
    }

    // Pulling in limited by power of motor
    //  winchingOut we are limited by power of generator
    double power=winchSpeed*tension;
    if (power>maxPower) {               // this is too much power
       winchSpeed=maxPower/tension;     //  back off to maxPower
       if (winchSpeed > maxSpeed) {
         winchSpeed=maxSpeed;           // in case very small tension
       }
    }

    double thisDistance = winchSpeed * deltaT;

    double springRestLength = ourTether.tetherSpring[minSpring].restLength;
    double springLength = ourTether.tetherSpring[minSpring].length();
    double springRatio = springLength/normSpringLength;
    double unstretchedDistance = thisDistance/springRatio;
    if (unstretchedDistance > thisDistance) {
      unstretchedDistance = thisDistance;
    }

    if (ballast != ourTether.tetherMass[ourTether.minMass]) {
      throw new Exception("Winch found ballast not at minmass" + ourTether.minMass);
    }

    double forceLimit = tensionFrac*averageTension;
    double springLimit = springFrac*ourTether.tetherSpring[minSpring].strength;
    if (springLimit < forceLimit) {
      forceLimit = springLimit;
      this.stopReason = 3;
    }
    if (maxForce < forceLimit) {
      forceLimit = maxForce;
      this.stopReason = 4;
    }

    // winchingIn then forceLimit is a maxForce
    if (winchingIn) {
      if (tension < forceLimit) {
         if (springRestLength - unstretchedDistance < 0.0
           && minSpring < maxSpring ) { // need to get rid of a spring
          unstretchedDistance -= springRestLength;

          ourTether.minMass++;
          ourTether.minSpring++;
          minMass = ourTether.minMass;
          minSpring = ourTether.minSpring;

          ballast.kg += ourTether.tetherMass[minMass].kg; // add mass of slice
          ourTether.tetherSpring[minSpring].restLength -= unstretchedDistance;
          ourTether.tetherSpring[minSpring].setBalSideMass(ballast);
          ourTether.tetherMass[minMass] = ballast;
          ourTether.setCMIndex();
          ballast.simulate1(deltaT);
          elapsedDistance += thisDistance;

        } else { // did not remove a slice
          ourTether.tetherSpring[minSpring].restLength -= unstretchedDistance;
          if (ourTether.tetherSpring[minSpring].restLength > 0) {
           elapsedDistance += thisDistance;
          } else {
           ourTether.tetherSpring[minSpring].restLength=0;
           Finished = true;
          }
        }
      } else {
        tensionLimit = true;
        winchSpeed = 0;
      }
    }

    // when winchingOut forceLimit is really minForce
    if (winchingOut ) {
      if (tension > forceLimit) {
        if (minMass > 0 &&
            springRestLength + unstretchedDistance > normSpringLength) {
          // we need to add back a slice of tether
          ourTether.tetherSpring[minSpring].restLength = normSpringLength;
          unstretchedDistance -= (normSpringLength - springRestLength);

          ourTether.tetherMass[minMass] = ourTether.winchMassMemory[minMass];
          ourTether.tetherMass[minMass].pos.copy(ballast.pos);
          ourTether.tetherMass[minMass].vel.copy(ballast.vel);
          ballast.kg -= ourTether.winchMassMemory[minMass].kg;
          ourTether.minMass--;
          ourTether.minSpring--;
          minMass = ourTether.minMass;
          minSpring = ourTether.minSpring;

          ourTether.tetherMass[minMass] = ballast;
          // new piece of spring only unstretchedDistance long
          ourTether.tetherSpring[minSpring].restLength = unstretchedDistance;
          // Should really have some distance but zero length spring ok
          ourTether.tetherSpring[minSpring].setMasses(
              ourTether.tetherMass[minMass], ourTether.tetherMass[minMass + 1]);
          ourTether.tetherSpring[minSpring + 1].setMasses(
              ourTether.tetherMass[minMass + 1],
              ourTether.tetherMass[minMass + 2]);
          ourTether.tetherMass[minMass].simulate1(deltaT);
          ourTether.tetherMass[minMass + 1].simulate1(deltaT);
          ourTether.setCMIndex();
          elapsedDistance += thisDistance;
        }
        else if (springRestLength + unstretchedDistance < normSpringLength) {
          ourTether.tetherSpring[ourTether.minSpring].restLength
              += unstretchedDistance;
          elapsedDistance += thisDistance;
        } else { // at the end of our rope
          ourTether.tetherSpring[ourTether.minSpring].restLength =
              normSpringLength;
          Finished = true;
        }
      } else {
        tensionLimit = true;
        winchSpeed = 0;
      }
    }

  }

// winchDistance is distance from ballast to spot we winch to
  public static void winchIn(String label, double winchDistance) throws Exception {
    int i=0;

    tether ourTether = (tether) findsimobject.labelToSimObjectOrDie(label);
    double length=ourTether.ballast.pos.distance(ourTether.payload);
    double lengthPerSlice = length/ourTether.slices;
    int slices = (int) (winchDistance/lengthPerSlice);
    if (slices > ourTether.maxMass) {
      slices = ourTether.maxMass;
    }
    try {
      for (i = ourTether.minMass + 1; i < slices; i++) {
        ourTether.ballast.kg += ourTether.tetherMass[i].kg;
      }
      int maxMass = ourTether.maxMass;  // i-1 since did not count payload mass
      int minMass = slices -1;
      int minSpring = minMass;

      ourTether.tetherMass[minMass] = ourTether.ballast;
      ourTether.tetherSpring[minSpring].setBalSideMass(ourTether.ballast);
      if (minMass == maxMass-1) {
        ourTether.tetherMass[maxMass].pos.copy(ourTether.ballast.pos);
        ourTether.tetherMass[maxMass].vel.copy(ourTether.ballast.vel);
        ourTether.tetherSpring[minSpring].restLength=0.1;
      } else {
        // Calculate position and velocity deltas for tether slices
        // Expect to get the same average stretch
        position deltaPos = new position(ourTether.payload.pos);
        deltaPos.subtract(ourTether.ballast.pos);
        deltaPos.divide(ourTether.slices);

        velocity deltaVel = new velocity(ourTether.payload.vel);
        deltaVel.subtract(ourTether.ballast.vel);
        deltaVel.divide(ourTether.slices);
        int j=1;
        for (i=minMass+1; i<=maxMass; i++) {
           ourTether.tetherMass[i].pos = new position(ourTether.ballast.pos, deltaPos, j);
           ourTether.tetherMass[i].vel = new velocity(ourTether.ballast.vel, deltaVel, j);
           j++;
        }
      }

      ourTether.minMass = minMass;
      ourTether.minSpring = minSpring;
      ourTether.setCMIndex();

    } catch (java.lang.ArrayIndexOutOfBoundsException e1) {
      throw new Exception("winchAllIn failed i=" + i
                          + " maxMass=" + ourTether.maxMass
                          + " maxSpring=" + ourTether.maxSpring
                          + e1.toString());
    }
  }

  public void paint(Graphics g) {
    String direction = "";
    if (this.winchingIn) {
       direction = "  IN  ";
    }
    if (this.winchingOut) {
       direction = "  OUT  ";
    }

    int minSpring = ourTether.minSpring;
    String extraInfo="";
    if (tensionLimit) {
      extraInfo = " sr " + this.stopReason;
    }
    screen.print(g, label + direction + " time "
                 + k.dTS(elapsedTime)
                 + " left "
                 + k.dTS(maxTime - elapsedTime)
                 + "  distance "
                 + k.dTS(elapsedDistance)
                 + "  togo "
                 + k.dTS(maxDistance - elapsedDistance)
                 + "  tens/avg "
                 + k.dTS(ourTether.tetherSpring[minSpring].tension)
                 + " / "
                 + k.dTS(averageTension)
                 + "  speed/avg "
                 + k.dTS2(winchSpeed)
                 + " / "
                 + k.dTS2(elapsedDistance/elapsedTime)
                 + extraInfo
                 + " minSpring "
                 + minSpring
  //                + "  spring-len "
 //                + k.dTS2(ourTether.tetherSpring[minSpring].length())
                 );
    k.outputAdd( label + " " + k.dTS2( k.simTime)
    + " MnSpStrTenSpAvgPwr "
    + minSpring +" "
    + k.dTS(ourTether.tetherSpring[minSpring].strength)+" "
    + k.dTS(ourTether.tetherSpring[minSpring].tension)+" "
    + k.dTS2(winchSpeed)+direction
    + k.dTS2(elapsedDistance/elapsedTime)+" "
    + k.dTS(winchSpeed*ourTether.tetherSpring[minSpring].tension)
    + "\n" );

  }

}






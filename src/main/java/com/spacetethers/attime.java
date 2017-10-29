package com.spacetethers;

import java.awt.*;
import java.util.*;

/*
             second    Kg      fromLabel   toLabel
 AtTime 120 toss 5 from T1.Ballast to M1  EndAtTime
 AtTime 110 toss 5 from T1.Payload to M2  EndAtTime
 AtTime 130 toss 5 from T1.20 to M3       EndAtTime
 AtTime 120 toss 5 from M1 to T1.Payload  EndAtTime
 AtTime 120 toss 5 from M2 to T1.Ballast  EndAtTime
 AtTime 120 toss 5 from M1 to T1.CenterOfMass EndAtTime
 AtTime 500 connect M1 to M1 with 0.5 cm-diameter tether EndAtTime
 AtTime 120 logXY true EndAtTime
 AtTime 200 showText M1 false EndAtTime
 AtTime 500 screenOrigin M1 EndAtTime
 AtTime 600 :  Any random comment  EndAtTime
 AtTime 500 panNewmassTime  M  10  EndAtTime
 AtTime 600 zoomNewscaleTime 400000 60  EndAtTime

 Comment will display while it is the next on the queue.

 Any of the global variables can be set with AtTime (logXY etc).

 Maybe in future:
      AtTime High Toss 5 from T1.Payload to space    EndAtTime
      AtTime Low Toss 5 from M1 to T1.Payload        EndAtTime

 We keep a queue of atTime objects and only check the object that
 is next in the queue.  So simulate1() is a static method, unlike
 in all other objects.

    */

public class attime  {
  double time;
  String command;
  String result;
  bootstrap bootStrapObject;

  // We only have 1 screen so only 1 pan at a time is ok
  static double panTimeTotal=0;
  static double panTimeLeft=0;
  static mass panFromMass=null;
  static mass panToMass=null;

  static double zoomTimeLeft=0;
  static double zoomScale=0;
  static double zoomDScaleDt=0;
  static double lastZoomScale=0;

  static Vector attimeQueue = new Vector();
  static String lastResult="";

 public attime(double time, String command, bootstrap bootStrapObject) {
    this.time=time;
    this.command=command;
    this.result="";
    this.bootStrapObject=bootStrapObject;
    addToQueue(this);
  }
  public attime(double time, String command) {
    this.time=time;
    this.command=command;
    this.result="";
    this.bootStrapObject=null;
    addToQueue(this);
  }

  private void addToQueue(attime item) {
    int i=0;
    for(Enumeration c=attimeQueue.elements(); c.hasMoreElements(); ) {
       attime at1 = (attime)c.nextElement();
       if (item.time <= at1.time) {
         attimeQueue.insertElementAt(item,i);   // add before this one
         return;                     // all done here
       }
       i++;
    }
    attimeQueue.addElement(item);           // add at the end
  }

  public static void clearQueue() {
    attimeQueue.removeAllElements();
    panTimeLeft=0;
    zoomTimeLeft=0;
    panFromMass=null;
    panToMass=null;
    lastResult="";
  }

  public static void simulate1(double deltaT) throws Exception{
    if (!attimeQueue.isEmpty()) {
      attime at1 = (attime) attimeQueue.firstElement();
      if (k.simTime >= at1.time) {
        try {
          at1.parseCommand(deltaT);
          lastResult = at1.result;
        } catch (Exception e1) {
          lastResult = e1.toString();
        }
        attimeQueue.removeElement(at1);
      }
    }
  }

  public static void simulate2(double deltaT) {
  }

  public static void simulate3(double deltaT) throws Exception {
    if (panTimeLeft > 0) {
      panTimeLeft -= deltaT;
      if (panTimeLeft <= 0) {
         k.setOriginMass(panToMass);
      }
    }
    if (zoomTimeLeft > 0) {
      zoomScale += zoomDScaleDt * deltaT;
      k.setGScale(zoomScale);
      zoomTimeLeft -= deltaT;
    }
  }


  // command is the string between the time and the EndAtTime
  private void parseCommand(double deltaT) throws Exception{
    double tossTime, tossKg;
    String fromLabel, toLabel, nt;

    StringTokenizer parseLine = new StringTokenizer(this.command);
    nt = parseLine.nextToken();

    // toss 5 from T1.Ballast to M1
    if ("toss".equals(nt)) {
      tossKg = k.readDouble(parseLine.nextToken());
      if ("from".equals(parseLine.nextToken())) {
        fromLabel = parseLine.nextToken();
        if ("to".equals(parseLine.nextToken())) {
          toLabel = parseLine.nextToken();
          doToss(tossKg, fromLabel, toLabel);
          return;
        }
      }
    }
    // connect foo to bar fromDiameter 0.1 toDiameter 0.2
    if ("connect".equals(nt)) {
      fromLabel = parseLine.nextToken();
      if ("to".equals(parseLine.nextToken())) {
        toLabel = parseLine.nextToken();
        if ("fromDiameter".equals(parseLine.nextToken())) {
          double fromDiameter = 0.01 * k.readDouble(parseLine.nextToken());
          if ("toDiameter".equals(parseLine.nextToken())) {
            double toDiameter = 0.01 * k.readDouble(parseLine.nextToken());
              tether t1 = connect.masses(fromLabel,toLabel,
                                         fromDiameter, toDiameter);
              if (t1 != null) {
                SpaceTethers.addSimObject(t1);
                t1.simulate1(deltaT);
                this.result = "status good " + SpaceTethers.getNumSimObjects();
              } else {
                this.result = "null tether";
              }
              return;
          }
        }
      }
    }

    if (k.isGlobal(nt)) {
      k.setGlobal(nt, parseLine.nextToken());
      return;
    }
    if ("showText".equals(nt)) {
      String label = parseLine.nextToken();
      boolean showIt = "true".equals(parseLine.nextToken());
      simtype s1 = findsimobject.labelToSimObjectOrDie(label);
      s1.showText = showIt;
      return;
    }
    if (":".equals(nt)) {
      return; // Just a comment
    }

    if ("panNewmassTime".equals(nt)) {
      panFromMass = k.originMass;
      mass m2 = findsimobject.labelToMassOrDie(parseLine.nextToken());
      panToMass = m2;
      panTimeTotal = k.readDouble(parseLine.nextToken());
      panTimeLeft = panTimeTotal;
      k.setOrigin(k.PAN);
      return;
    }

    if ("zoomNewscaleTime".equals(nt)) {
      double newScale = k.readDouble(parseLine.nextToken());
      double zoomTime = k.readDouble(parseLine.nextToken());
      zoomTimeLeft = zoomTime;
      zoomDScaleDt = (newScale - k.scaleYMeters) / zoomTime;
      zoomScale = k.scaleYMeters;
      return;
    }

    if ("stop".equals(nt)) {
       SpaceTethers.doStop();
       return;
    }

    if ("remove".equals(nt)) {
       simtype s1 = findsimobject.labelToSimObjectOrDie(parseLine.nextToken());
       SpaceTethers.removeSimObject(s1);
       findsimobject.remove(s1);
       return;
    }

    if ("winchLabelPowerForceSpeedDistTimePavgPspring".equals(nt)) {
      String label = parseLine.nextToken();
      double Power = k.readDouble(parseLine.nextToken());
      double Force = k.readDouble(parseLine.nextToken());
      double Speed = k.readDouble(parseLine.nextToken());
      double Distance = k.readDouble(parseLine.nextToken());
      double Time = k.readDouble(parseLine.nextToken());
      double TensionPerc = k.readDouble(parseLine.nextToken());
      double SpringPerc = k.readDouble(parseLine.nextToken());
      winch w1 = new winch(label, Power, Force, Speed,
                           Distance, Time, TensionPerc, SpringPerc);
      SpaceTethers.addSimObject(w1);
      return;
    }

    if ("winchIn".equals(nt)) {
      String label = parseLine.nextToken();
      double winchDistance = k.readDouble(parseLine.nextToken());
      winch.winchIn(label, winchDistance);
      return;
    }

    if ("bootstrap".equals(nt)) {
      String bootEvent = "";
      while (parseLine.hasMoreElements()) {
        bootEvent += parseLine.nextElement() + " ";
      }
      bootStrapObject.handleEvent(bootEvent);
      return;
    }

    if ("orionBlast".equals(nt)) {
      String oblabel = parseLine.nextToken();
      orionblast ob = (orionblast) findsimobject.labelToSimObjectOrDie(oblabel);
      double kg = k.readDouble(parseLine.nextToken());
      ob.doBlast(kg);
      return;
    }

    this.result = "command did not parse: " + nt ;
  }




  private void doToss(double kg, String fromLabel, String toLabel) throws Exception{
      mass fromMass = findsimobject.labelToMass(fromLabel);
      mass toMass   = findsimobject.labelToMass(toLabel);

      if (fromMass != null) {
        if (kg > fromMass.kg) {
          throw new Exception("Tossing " + k.dTS2(kg)
                              + " which is more mass than the "
                             + k.dTS2(fromMass.kg)
                             + " we have at " + fromMass.label);
        }
        fromMass.kg -= kg;
        fromMass.simulate1(k.deltaT);  // recalculate gravity - NYC on k.deltaT
      }
      if (toMass != null) {
         toMass.kg += kg;
      } else if (!"nowhere".equals(toLabel)){
         toMass = new mass(kg, fromMass.pos, fromMass.vel, toLabel);
         SpaceTethers.addSimObject(toMass);
      }

      if (toMass != null) {
        toMass.simulate1(k.deltaT); // just in case it was done already - NYC
      }
  }

  // We only paint the next atTime command and the lastResult
  public static void paint(Graphics g) {
    if (lastZoomScale != zoomScale) {
      SpaceTethers.setStatusMessage();
      lastZoomScale=zoomScale;
    }

    if (!attimeQueue.isEmpty()) {
     attime at1 = (attime) attimeQueue.firstElement();
     screen.print(g, "AtTime " + at1.time + " " + at1.command);
    }
    if (!lastResult.equals("")) {     // if something interesting print it
        screen.print(g, "AtTime lastResult: " + lastResult);
    }
  }


  public static void setOrigin() {
      double panX, panY, panDxBack, panDyBack, panLeft;

      panX = panToMass.pos.x;
      panY = panToMass.pos.y;
      panDxBack = panFromMass.pos.x - panToMass.pos.x;
      panDyBack = panFromMass.pos.y - panToMass.pos.y;
      panLeft = panTimeLeft / panTimeTotal;
      panX += panLeft * panDxBack;
      panY += panLeft * panDyBack;
      screen.setOrigin(panX, panY);
  }
}

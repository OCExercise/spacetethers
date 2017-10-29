
// Copyright (c) Vincent Cate 2002
package com.spacetethers;

import java.awt.*;
import java.lang.Math;

public class screen {

  static double originX = 0;
  static double originY = 0;
  static int nextLine=2;

  public screen() {
  }

  public static void drawLine (Graphics g, double x1, double y1, double x2, double y2) {
     g.drawLine((int)(k.xOffset+(x1-originX)/k.gScale), (int)(k.yOffset-(y1-originY)/k.gScale),
                (int)(k.xOffset+(x2-originX)/k.gScale), (int)(k.yOffset-(y2-originY)/k.gScale));
  }


  // x,y is center of line
  public static void drawAngledLine (Graphics g, double x, double y,
                                     double length, double angle) {
     double dx = Math.cos(angle)*length/2.0;
     double dy = Math.sin(angle)*length/2.0;
     double x1 = x - dx;
     double y1 = y - dy;
     double x2 = x + dx;
     double y2 = y + dy;
     screen.drawLine(g, x1, y1, x2, y2);
  }

  public static void drawString(Graphics g, String s, double x1, double y1) {
     g.drawString(s, (int)(10 + k.xOffset+(x1-originX)/k.gScale),(int)(k.yOffset-(y1-originY)/k.gScale));
  }

  public static  void drawBox(Graphics g, double x1, double y1, double size) {
     g.drawRect((int) (-0.5*size + k.xOffset+(x1-originX)/k.gScale),
                (int)(-0.5*size + k.yOffset-(y1-originY)/k.gScale),
                (int) size, (int) size);
  }


  public static void drawEarthMoon(Graphics g) {

     if (k.moon != null) {
        drawDot(g, 0, 0);
        drawOval(g, k.moon.pos.x, k.moon.pos.y, k.moonRadius);    // Moon
        drawOval(g, k.earth.pos.x, k.earth.pos.y, k.earthRadius); // Earth
     } else {
       drawOval(g, 0, 0, k.earthRadius); // Earth
       double angleRadians = 2 * k.pi * k.simTime / k.earthSecsPerRev;
       double longitude, xvec, yvec;
       // We draw long lines every 30 degrees and short on the 15s
       for (longitude = 2 * k.pi / 24; longitude < 2 * k.pi;
            longitude += 2 * k.pi / 12) {
         xvec = java.lang.Math.cos(angleRadians + longitude);
         yvec = java.lang.Math.sin(angleRadians + longitude);
         screen.drawLine(g, 0.9 * k.earthRadius * xvec,
                         0.9 * k.earthRadius * yvec,
                         k.earthRadius * xvec, k.earthRadius * yvec);
       }
       for (longitude = 0; longitude < 2 * k.pi; longitude += 2 * k.pi / 12) {
         xvec = java.lang.Math.cos(angleRadians + longitude);
         yvec = java.lang.Math.sin(angleRadians + longitude);
         screen.drawLine(g, 0, 0, k.earthRadius * xvec, k.earthRadius * yvec);
       }
       if (k.gOrigin == k.EARTH || k.gOrigin == k.EARTHTOP) {
         g.drawString("Earth", (int) k.xOffset + 20, (int) k.yOffset - 4);
       }
     }
  }

  public static  void drawDot(Graphics g, double x1, double y1) {
     double size=4;
     g.fillOval((int) (-0.5*size + k.xOffset+(x1-originX)/k.gScale),
                (int)(-0.5*size + k.yOffset-(y1-originY)/k.gScale),
                (int) size, (int) size);
  }

// Earth and Moon really -
// input size is radius in meters
  public static void drawOval(Graphics g, double x1, double y1, double size) {
     size = 2*size / k.gScale;
     g.drawOval( (int)( -0.5*size + k.xOffset+((x1-originX)/k.gScale)),
                (int)(-0.5*size + k.yOffset-((y1-originY)/k.gScale)),
                (int) size, (int) size);
   }


  public static void clear(Graphics g) {
     g.clearRect(0, 0, (int) k.xPixels, (int) k.yPixels);
     nextLine=2;
  }


  // Like print but always at top
  public static void printMessage(Graphics g, String s) {
     if (g != null && s != null) {
        g.drawString(s, 10, 20);
     }
  }

  public static void print(Graphics g, String s) {
     if (s.length() > 0) {
       g.drawString(s, 10, nextLine * 20);
       nextLine++;
     }
  }

  public static void setOrigin(double x, double y) {
     originX = x;
     originY = y;
  }

}

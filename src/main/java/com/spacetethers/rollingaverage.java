package com.spacetethers;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class rollingaverage {
  double history[];
  double currentTotal=0;
  int averageIndex=0;
  int numDataPoints=0;

  public rollingaverage(int numDataPoints, double initialValue) {
      this.numDataPoints = numDataPoints;

       history=new double [numDataPoints];       // allocate array
       for (int i=0; i<numDataPoints; i++) {
          history[i]=initialValue;                      // fake initial history
       }
      currentTotal= ((double) numDataPoints)*initialValue;

  }


  public double timeAverage(double value) {
   if (k.averagingSeconds == 0) {
      history=null;                // in case we had an array before
      return(value);               // no real time averaging
   }
   if (numDataPoints == 0) {
   }
   averageIndex ++;
   if (averageIndex >= numDataPoints) {
      averageIndex -= numDataPoints;
   }
    // averageIndex %= maxDataPoints;
   currentTotal -= history[averageIndex];
   history[averageIndex]= value;
   currentTotal += value;
   return(currentTotal/numDataPoints);
 }

}

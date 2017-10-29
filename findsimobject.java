package spacetethers;

import java.awt.*;
import java.util.*;
//  We keep a list of all sim objects and can return a sim objects by name
//  Used by linker.java and attime.java to find masses

public class findsimobject {
  static Vector AllSimObjects = new Vector();
  static Vector AllAliases = new Vector();

  String alias;
  String label;

  public findsimobject(String alias, String label) {
    this.alias=alias;
    this.label=label;
  }


  // For input String s return last object that has that label
  //   or null if none match.
   public static simtype labelToSimObject(String s) {
    int i;
    String search;
    findsimobject mf = aliasToMF(s);
    if (mf != null) {
      search=mf.label;              // If alias use label for it
    } else {
      search = s;                   // If no alias use s alone
    }
    for(Enumeration c=AllSimObjects.elements(); c.hasMoreElements(); ) {
      simtype m1 = (simtype)c.nextElement();
          if (search.equals(m1.label)) {
            return(m1);
          }
    }
    return(null);
    // throw new Exception("Label did not match any object");
  }
 public static simtype labelToSimObjectOrDie(String s) throws Exception{
   simtype s1 = labelToSimObject(s);
   if (s1 != null) {
     return(s1);
   }
   throw new Exception("Label " + s + " not found in labelToSimObjects");
 }

 public static mass labelToMassOrDie(String s) throws Exception{
   mass m1 = (mass) labelToSimObject(s);
   if (m1 != null) {
     return(m1);
   }
   throw new Exception("Label " + s + " not found in labelToMassOrDie ");
 }

 public static mass labelToMass(String s){
   mass m1 = (mass) labelToSimObject(s);
   return(m1);
 }

 private static findsimobject aliasToMF(String s) {
   for(Enumeration c=AllAliases.elements(); c.hasMoreElements();) {
      findsimobject mf = (findsimobject)c.nextElement();
      if (s.equals(mf.alias)) {
         return(mf);
      }
    }
    return(null);
  }

  public static boolean labelExists(String s) {
    simtype testSimObjects = labelToSimObject(s);
    boolean result= (testSimObjects != null);
    return(result);
  }

  // Only add if nothing with that label already
  public static void add(simtype m1){
    if (!labelExists(m1.label)) {
        AllSimObjects.addElement(m1);
    }
  }

  public static void addAlias(String alias, String label) {
    AllAliases.addElement(new findsimobject(alias, label));
  }
  public static void removeAlias(String alias) {
     findsimobject mf = aliasToMF(alias);
     if (mf != null) {
       AllAliases.removeElement(mf);
     }
  }
  public static void remove(simtype m1){
    AllSimObjects.removeElement(m1);
  }

  public static void clearAll() {
    AllSimObjects.removeAllElements();
    AllAliases.removeAllElements();
  }

  // Really only paint when debugging
  public static void paint(Graphics g) {

    if (1==1) {
      return; // change when debugging
    }

    screen.print(g, "findsimobjects has " + AllSimObjects.size());
    int i=0;
    String line1="";
    for(Enumeration c=AllSimObjects.elements(); i<12 && c.hasMoreElements();i++ ) {
      simtype m1 = (simtype)c.nextElement();
      line1 += m1.label + " ";
    }
    screen.print(g, line1);
    line1 = "";
    i=0;
    for(Enumeration c=AllAliases.elements(); i<12 && c.hasMoreElements();i++ ) {
      findsimobject mf = (findsimobject)c.nextElement();
      line1 += mf.alias + "=" + mf.label + " ";
    }
    screen.print(g, line1);
  }


}
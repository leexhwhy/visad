
//
// ContourControl.java
//

/*
VisAD system for interactive analysis and visualization of numerical
data.  Copyright (C) 1996 - 1998 Bill Hibbard, Curtis Rueden and Tom
Rink.
 
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 1, or (at your option)
any later version.
 
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License in file NOTICE for more details.
 
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/

package visad;

/**
   ContourControl is the VisAD class for controlling IsoContour display scalars.<P>
*/
public class ContourControl extends Control {

  private boolean mainContours;
  // for 3-D mainContours
  private float surfaceValue;
  // for 2-D mainContours
  private float contourInterval;
  private float lowLimit;
  private float hiLimit;
  private float base;
  private boolean labels;

  private boolean horizontalContourSlice;
  private boolean verticalContourSlice;

  private float horizontalSliceLow;
  private float horizontalSliceHi;
  private float horizontalSliceStep;
  private float verticalSliceLow;
  private float verticalSliceHi;
  private float verticalSliceStep;

  public ContourControl(DisplayImpl d) {
    super(d);
    mainContours = false;
    labels = false;
    surfaceValue = Float.NaN;
    contourInterval = Float.NaN;
    lowLimit = Float.NaN;
    hiLimit = Float.NaN;
    base = Float.NaN;
 
    horizontalContourSlice = false;
    verticalContourSlice = false;
 
    horizontalSliceLow = Float.NaN;
    horizontalSliceHi = Float.NaN;
    horizontalSliceStep = Float.NaN;
    verticalSliceLow = Float.NaN;
    verticalSliceHi = Float.NaN;
    verticalSliceStep = Float.NaN;
  }
 
  void setMainContours(boolean[] bvalues, float[] fvalues)
         throws VisADException {
    setMainContours(bvalues, fvalues, false);
  }

  /** noChange = true to not trigger changeControl, used by
      ScalarMap.setRange */
  void setMainContours(boolean[] bvalues, float[] fvalues, boolean noChange)
       throws VisADException {
    if (fvalues == null || fvalues.length != 5 ||
        bvalues == null || bvalues.length != 2) {
      throw new DisplayException("ContourControl.getMainContours: " +
                                 "bad array length");
    }
    mainContours = bvalues[0];
    labels = bvalues[1];
    if (surfaceValue != surfaceValue) surfaceValue = fvalues[0];
    if (contourInterval != contourInterval) contourInterval = fvalues[1];
    if (lowLimit != lowLimit) lowLimit = fvalues[2];
    if (hiLimit != hiLimit) hiLimit = fvalues[3];
    if (base != base) base = fvalues[4];
    if (!noChange) changeControl();
  }

  public void setSurfaceValue(float value) {
    surfaceValue = value;
    changeControl();
  }

  public void setContourInterval(float interval, float low,
                                 float hi, float ba) {
    contourInterval = interval;
    lowLimit = low;
    hiLimit = hi;
    base = ba;
    changeControl();
  }

  public void enableLabels(boolean on) {
    labels = on;
    changeControl();
  }

  public void enableContours(boolean on) {
    mainContours = on;
    changeControl();
  }

  public void getMainContours(boolean[] bvalues, float[] fvalues)
         throws VisADException {
    if (fvalues == null || fvalues.length != 5 ||
        bvalues == null || bvalues.length != 2) {
      throw new DisplayException("ContourControl.getMainContours: " +
                                 "bad array length");
    }
    bvalues[0] = mainContours;
    bvalues[1] = labels;
    fvalues[0] = surfaceValue;
    fvalues[1] = contourInterval;
    fvalues[2] = lowLimit;
    fvalues[3] = hiLimit;
    fvalues[4] = base;
  }

}



//
// DataDisplayLink.java
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

import java.util.*;
import java.rmi.*;

/**
   DataDisplayLink objects define connections between DataReference
   objects and Display objects.<P>
*/
class DataDisplayLink extends ReferenceActionLink {

  private ShadowType shadow;

  // used by prepareData
  private Data data;

  // ConstantMap-s specific to this Data
  private Vector ConstantMapVector = new Vector();

  // Renderer associated with this Data
  // (may be multiple Data per Renderer)
  private Renderer renderer;

  // Vector of ScalarMap-s applying to this Data
  private Vector SelectedMapVector = new Vector();

  // default values for DisplayIndices, determined by:
  // 1. this.ConstantMapVector
  // 2. Display.ConstantMapVector
  // 3. DisplayRealType.DefaultValue
  private float[] defaultValues;

  // flag per Control
  boolean[] isTransform;

  DataDisplayLink(DataReference ref, DisplayImpl local_d, Display d,
                  ConstantMap[] constant_maps, Renderer rend)
                  throws VisADException, RemoteException {
    super(ref, local_d, d);
    renderer = rend;

    if (constant_maps != null) {
      for (int i=0; i<constant_maps.length; i++) {
        Enumeration maps = ConstantMapVector.elements();
        while(maps.hasMoreElements()) {
          ScalarMap map = (ScalarMap) maps.nextElement();
          if (map.getDisplayScalar().equals(constant_maps[i].getDisplayScalar())) {
            throw new DisplayException("DataDisplayLink: two ConstantMap-s have" +
                                       " the same DisplayScalar");
          }
        }
        constant_maps[i].setDisplay(local_d);
        ConstantMapVector.addElement(constant_maps[i]);
        local_d.addDisplayScalar(constant_maps[i]);
      }
    }
  }

  DisplayImpl getDisplay() {
    return (DisplayImpl) local_action;
  }

  Renderer getRenderer() {
    return renderer;
  }

  Vector getSelectedMapVector() {
    return SelectedMapVector;
  }

  ShadowType getShadow() {
    return shadow;
  }

  void addSelectedMapVector(ScalarMap map) {
    // 'synchronized' unnecessary
    // (since prepareData is a single Thread, but ...)
    synchronized (SelectedMapVector) {
      if (!SelectedMapVector.contains(map)) {
        SelectedMapVector.addElement(map);
      }
    }
  }

  /** Prepare to render data (include feasibility check);
      return false if infeasible */
  boolean prepareData() throws VisADException, RemoteException {
    int[] indices;
    int[] display_indices;
    int[] value_indices;
    int levelOfDifficulty;
    data = ref.getData();
    MathType type = data.getType();
    SelectedMapVector.removeAllElements();

    // calculate default values for DisplayRealType-s
    // lowest priority: DisplayRealType.DefaultValue
    int n = ((DisplayImpl) local_action).getDisplayScalarCount();
    defaultValues = new float[n];
    for (int i=0; i<n; i++) {
      defaultValues[i] = (float) (((DisplayRealType)
        ((DisplayImpl) local_action).getDisplayScalar(i)).getDefaultValue());
    }
    // middle priority: DisplayImpl.ConstantMapVector
    Enumeration maps =
      ((DisplayImpl) local_action).getConstantMapVector().elements();
    while(maps.hasMoreElements()) {
      ConstantMap map = (ConstantMap) maps.nextElement();
      defaultValues[map.getDisplayScalarIndex()] = (float) map.getConstant();
    }
    // highest priority: this.ConstantMapVector
    maps = ConstantMapVector.elements();
    while(maps.hasMoreElements()) {
      ConstantMap map = (ConstantMap) maps.nextElement();
      defaultValues[map.getDisplayScalarIndex()] = (float) map.getConstant();
    }

    try {
      shadow = type.buildShadowType(this, null);
      indices = ShadowType.zeroIndices(
                  ((DisplayImpl) local_action).getScalarCount());
      display_indices = ShadowType.zeroIndices(
                  ((DisplayImpl) local_action).getDisplayScalarCount());
      value_indices = ShadowType.zeroIndices(
                  ((DisplayImpl) local_action).getValueArrayLength());
      Vector controls = ((DisplayImpl) local_action).getControlVector();
      isTransform = new boolean[controls.size()];
      for (int i=0; i<controls.size(); i++) isTransform[i] = false;
      levelOfDifficulty =
        shadow.checkIndices(indices, display_indices, value_indices,
                            isTransform, ShadowType.NOTHING_MAPPED);
      if (levelOfDifficulty == ShadowType.LEGAL) {
        // every Control isTransform for merely LEGAL
        // (i.e., the 'dots') rendering
        for (int i=0; i<controls.size(); i++) isTransform[i] = true;
      }
      shadow.checkDirect(data);
      // System.out.println(shadow);
    }
    catch (BadMappingException e) {
      ((DisplayImpl) local_action).addException(e.getMessage());
      return false;
    }
    catch (UnimplementedException e) {
      ((DisplayImpl) local_action).addException(e.getMessage());
      return false;
    }
    // can now render data based on shadow.LevelOfDifficulty and
    // shadow.isDirectManipulation
    return true;
  }

  Data getData() {
    return data;
  }

  public float[] getDefaultValues() {
    return defaultValues;
  }

}


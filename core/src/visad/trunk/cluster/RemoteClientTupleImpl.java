//
// RemoteClientTupleImpl.java
//

/*
VisAD system for interactive analysis and visualization of numerical
data.  Copyright (C) 1996 - 2000 Bill Hibbard, Curtis Rueden, Tom
Rink, Dave Glowacki, Steve Emmerson, Tom Whittaker, Don Murray, and
Tommy Jasmin.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Library General Public
License as published by the Free Software Foundation; either
version 2 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Library General Public License for more details.

You should have received a copy of the GNU Library General Public
License along with this library; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA 02111-1307, USA
*/

package visad.cluster;

import visad.*;

import java.util.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
   RemoteClientData is the class for cluster client
   VisAD data objects.<P>
*/
public class RemoteClientTupleImpl extends RemoteClientDataImpl
       implements RemoteClientTuple {

  private RemoteClusterDataImpl[] tupleComponents;

  private Tuple adaptedTuple = null;

  public RemoteClientTupleImpl(Data[] datums)
         throws VisADException, RemoteException {
    if (datums == null) {
      throw new ClusterException("datums cannot be null");
    }
    int n = datums.length;
    if (n == 0) {
      throw new ClusterException("datums.length must be > 0");
    }
    for (int i=0; i<n; i++) {
      if (!(datums[i] instanceof Scalar ||
            datums[i] instanceof RemoteClientDataImpl)) {
        throw new ClusterException("datums must be Scalar or RemoteClientDataImpl");
      }
    }
    adaptedTuple = new Tuple(datums);
  }

/*
  public RemoteClientTupleImpl(RemoteClusterDataImpl[] datums)
         throws VisADException, RemoteException {
    super(Tuple.buildTupleType(datums));
    int n = datums.length;
    tupleComponents = new RemoteClusterDataImpl[n];
    // don't copy
    for (int i=0; i<n; i++) {
      tupleComponents[i] = datums[i];
      tupleComponents[i].setParent(this);
    }
  }
*/

  public Real[] getRealComponents()
         throws VisADException, RemoteException {
    return adaptedTuple.getRealComponents();
  }

  public int getDimension() {
    return adaptedTuple.getDimension();
  }

  public Data getComponent(int i) throws VisADException, RemoteException {
    return adaptedTuple.getComponent(i);
  }

  public boolean isMissing() {
    return adaptedTuple.isMissing();
  }

  public Data binary(Data data, int op, MathType new_type,
                    int sampling_mode, int error_mode )
             throws VisADException, RemoteException {
    throw new UnimplementedException("RemoteClientTupleImpl.binary");
  }

  public Data unary(int op, MathType new_type,
                    int sampling_mode, int error_mode)
         throws VisADException, RemoteException {
    throw new UnimplementedException("RemoteClientTupleImpl.unary");
  }

  public DataShadow computeRanges(ShadowType type, DataShadow shadow)
         throws VisADException, RemoteException {
    return adaptedTuple.computeRanges(type, shadow);
  }

  public Data adjustSamplingError(Data error, int error_mode)
         throws VisADException, RemoteException {
    return adaptedTuple.adjustSamplingError(error, error_mode);
  }

  public Object clone() {
    throw new VisADError("RemoteClientTupleImpl.clone not implemented");
  }

}

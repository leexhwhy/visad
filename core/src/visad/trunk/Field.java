
//
// Field.java
//

/*
VisAD system for interactive analysis and visualization of numerical
data.  Copyright (C) 1996 - 1998 Bill Hibbard, Curtis Rueden, Tom
Rink and Dave Glowacki.
 
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
   Field is the VisAD interface for finite samplings of functions
   from R^n to a range type, where  n>0.<P>

   A Field domain type may be either a RealType (for a function with
   domain = R) or a RealTupleType (for a function with domain = R^n
   for n > 0).<P>
*/
public interface Field extends Function {

  /** set the range samples of the function; the order of range samples
      must be the same as the order of domain indices in the DomainSet;
      copy range objects if copy is true;
      should use same MathType object in each Data object in range array */
  public abstract void setSamples(Data[] range, boolean copy)
         throws VisADException, RemoteException;

  /** get the domain Set */
  public abstract Set getDomainSet() throws VisADException, RemoteException;

  /** get number of samples */
  public abstract int getLength() throws VisADException, RemoteException;

  /** get the Units of the Real components of the domain Set (SetUnits) */
  public abstract Unit[] getDomainUnits()
         throws VisADException, RemoteException;

  /** get the CoordinateSystem of the domain Set (DomainCoordinateSystem) */
  public abstract CoordinateSystem getDomainCoordinateSystem()
         throws VisADException, RemoteException;

  /** get the range value at the index-th sample */
  public abstract Data getSample(int index)
         throws VisADException, RemoteException;

  /** set the range value at the sample nearest to domain */
  public abstract void setSample(RealTuple domain, Data range)
         throws VisADException, RemoteException;

  /** set the range value at the index-th sample */
  public abstract void setSample(int index, Data range)
         throws VisADException, RemoteException;

  /** assumes the range type of this is a Tuple and returns
      a Field with the same domain as this, but whose range
      samples consist of the specified Tuple component of the
      range samples of this; in shorthand, this[].component */
  public abstract Field extract(int component)
         throws VisADException, RemoteException;

  /** combine domains of two outermost nested Fields into a single
      domain and Field */
  public Field domainMultiply()
         throws VisADException, RemoteException;

  /** combine domains to depth, if possible */
  public Field domainMultiply(int depth)
         throws VisADException, RemoteException;

  /** factor Field domain into domains of two nested Fields */
  public Field domainFactor( RealType factor )
         throws VisADException, RemoteException;

  /** get the 'Flat' components of this Field's range values
      in their default range Units (as defined by the range of
      the Field's FunctionType); if the range type is a RealType
      it is a 'Flat' component, if the range type is a TupleType
      its RealType components and RealType components of its
      RealTupleType components are all 'Flat' components; the
      return array is dimensioned:
      double[number_of_flat_components][number_of_range_samples] */
  public abstract double[][] getValues()
         throws VisADException, RemoteException;
 
  /** get String values for Text components */
  public String[][] getStringValues()
         throws VisADException, RemoteException;

  /** get default range Unit-s for 'Flat' components */
  public abstract Unit[] getDefaultRangeUnits()
         throws VisADException, RemoteException;

  /** get range Unit-s for 'Flat' components;
      second index may enumerate samples, if they differ */
  public abstract Unit[][] getRangeUnits()
         throws VisADException, RemoteException;

  /** get range CoordinateSystem for 'RealTuple' range;
      index may enumerate samples, if they differ */
  public abstract CoordinateSystem[] getRangeCoordinateSystem()
         throws VisADException, RemoteException;

  /** get range CoordinateSystem for 'RealTuple' components;
      index may enumerate samples, if they differ */
  public abstract CoordinateSystem[] getRangeCoordinateSystem(int component)
         throws VisADException, RemoteException;

  /** return true if this a FlatField or a RemoteField adapting a FlatField */
  public abstract boolean isFlatField()
         throws VisADException, RemoteException;


/**
<PRE>
   Here's how to use this:

   for (Enumeration e = field.domainEnumeration() ; e.hasMoreElements(); ) {
     RealTuple domain_sample = (RealTuple) e.nextElement();
     Data range = field.evaluate(domain_sample);
   }
</PRE>
*/
  public abstract Enumeration domainEnumeration()
         throws VisADException, RemoteException;

}


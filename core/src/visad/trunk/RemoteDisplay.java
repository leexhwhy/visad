
//
// RemoteDisplay.java
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

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.Vector;

/**
   RemoteDisplay is the interface for Remote Display-s.<P>
*/
public interface RemoteDisplay extends Remote, Display {
  public abstract String getName() throws VisADException, RemoteException;
  public abstract String getDisplayClassName() throws RemoteException;
  public abstract int getDisplayAPI() throws VisADException, RemoteException;
  public abstract String getDisplayRendererClassName() throws RemoteException;
  public abstract Vector getMapVector() throws VisADException, RemoteException;
  public abstract Vector getConstantMapVector()
	throws VisADException, RemoteException;
  public abstract RemoteGraphicsModeControl getGraphicsModeControl()
	throws VisADException, RemoteException;
  public abstract Vector getReferenceLinks()
	throws VisADException, RemoteException;
}


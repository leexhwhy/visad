
//
// RemoteServer.java
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
 
import visad.*;
 
import java.rmi.*;

/**
   RemoteServer is the interface for serving RemoteDataReferences.
   A RemoteServerImpl should be bound to a URL via Naming.rebind,
   and accessed remotely via this RemoteServer interface.<P>
*/
public interface RemoteServer extends Remote {
 
  /** get a RemoteDataReference by index */
  public abstract RemoteDataReference getDataReference(int index)
         throws RemoteException;

  /** get a RemoteDataReference by name */
  public abstract RemoteDataReference getDataReference(String name)
         throws VisADException, RemoteException;

  /** return array of all RemoteDataReferences in this RemoteServer */
  public abstract RemoteDataReference[] getDataReferences()
         throws RemoteException;

}


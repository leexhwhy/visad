/*
 * Copyright 1997, University Corporation for Atmospheric Research
 * See file LICENSE for copying and redistribution conditions.
 *
 * $Id: UnitPrefix.java,v 1.2 1998-01-20 23:29:28 steve Exp $
 */

package visad.data.netcdf.units;


/**
 * Class for representing unit prefixes.
 */
public class
UnitPrefix
    implements	java.io.Serializable
{
    /**
     * The name of the prefix:
     */
    public final String	name;

    /**
     * The value of the prefix:
     */
    public final double	value;


    /**
     * Construct.
     *
     * @param name	The name of the prefix (e.g. "mega", "M").
     * @param value	The value of the prefix (e.g. 1e6).
     * @require		The arguments shall be non-null.
     * @exception IllegalArgumentException	One of the arguments was null.
     */
    public
    UnitPrefix(String name, double value)
    {
	if (name == null)
	    throw new IllegalArgumentException("Null prefix name");

	this.name = name;
	this.value = value;
    }
}


//
// OffsetUnit.java
//

/*
 * Copyright 1997, University Corporation for Atmospheric Research
 * See file LICENSE for copying and redistribution conditions.
 *
 * $Id: OffsetUnit.java,v 1.10 2000-04-25 20:14:36 steve Exp $
 */

package visad;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A class that represents a scaled unit with an offset.
 * @author Steven R. Emmerson
 *
 * This is part of Steve Emerson's Unit package that has been
 * incorporated into VisAD.
 *
 * Instances are immutable.
 */
public final class OffsetUnit
    extends	Unit
    implements	Serializable
{
    /**
     * The associated (unoffset) scaled unit.
     */
    final ScaledUnit	scaledUnit;

    /**
     * The offset for this unit (e.g. 273.15 for the celsius unit when
     * the kelvin unit is associated scaled unit).
     */
    final double	offset;

    /**
     * The date formatter.
     * @serial
     */
    private static SimpleDateFormat	dateFormat;

    /**
     * The arbitrary time origin.
     * @serial
     */
    private static double		offsetUnitOrigin;

    /**
     * The millisecond unit.
     */
    private static Unit			millisecond;

    static
    {
	try
	{
	    dateFormat =
		(SimpleDateFormat)DateFormat.getDateInstance(
		    DateFormat.SHORT, Locale.US);
	    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.SSS 'UTC'");
	    Calendar	calendar =
		new GregorianCalendar(TimeZone.getTimeZone("UTC"));
	    calendar.clear();
	    calendar.set(2001, 0, 1, 0, 0, 0);	// data.netcdf.units.UnitParser
	    offsetUnitOrigin = calendar.getTime().getTime();
	    millisecond = SI.second.scale(0.001).clone("ms");
	}
	catch (Exception e)
	{
	    System.err.println(
		"OffsetUnit.<clinit>: Couldn't initialize class: " + e);
	    System.exit(1);
	}
    }


    /**
     * Construct an offset, dimensionless unit.  The identifier will be empty.
     *
     * @param offset	The amount of offset.
     */
    public OffsetUnit(double offset)
    {
	  this(offset, "");
    }

    /**
     * Construct an offset, dimensionless unit with an identifier.
     *
     * @param offset		The amount of offset.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     *
     */
    public OffsetUnit(double offset, String identifier)
    {
	super(identifier);
	this.offset = offset;
	scaledUnit = new ScaledUnit(1.0);
    }

    /**
     * Construct an offset unit from a base unit.  The identifier will be that
     * of the base unit if the offset is zero; otherwise, the identifier will
     * be <code>null</code>.
     *
     * @param offset	The amount of offset.
     * @param unit	The base unit.
     */
    public OffsetUnit(double offset, BaseUnit that)
    {
	this(offset, that, offset == 0 ? that.getIdentifier() : null);
    }

    /**
     * Construct an offset unit from a base unit and an identifier.
     *
     * @param offset		The amount of offset.
     * @param unit		The base unit.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     */
    public OffsetUnit(double offset, BaseUnit that, String identifier)
    {
	super(identifier);
	this.offset = offset;
	scaledUnit = new ScaledUnit(1.0, that);
    }

    /**
     * Construct an offset unit from a derived unit.  The identifier will be
     * that of the derived unit if the offset is 0; otherwise; the identifier
     * will be <code>null</code>.
     *
     * @param offset	The amount of offset.
     * @param unit	The derived unit.
     */
    public OffsetUnit(double offset, DerivedUnit that)
    {
	this(offset, that, offset == 0 ? that.getIdentifier() : null);
    }

    /**
     * Construct an offset unit from a derived unit and an identifier.
     *
     * @param offset		The amount of offset.
     * @param unit		The derived unit.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     */
    public OffsetUnit(double offset, DerivedUnit that, String identifier)
    {
	super(identifier);
	this.offset = offset;
	scaledUnit = new ScaledUnit(1.0, that);
    }

    /**
     * Construct an offset unit from a scaled unit.  The identifier will be
     * that of the scaled unit if the offset is 0; otherwise; the identifier
     * will be <code>null</code>.
     *
     * @param offset	The amount of offset.
     * @param unit	The scaled unit.
     */
    public OffsetUnit(double offset, ScaledUnit that)
    {
	this(offset, that, offset == 0 ? that.getIdentifier() : null);
    }

    /**
     * Construct an offset unit from a scaled unit and an identifier.
     *
     * @param offset		The amount of offset.
     * @param unit		The scaled unit.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     */
    public OffsetUnit(double offset, ScaledUnit that, String identifier)
    {
	super(identifier);
	this.offset = offset;
	scaledUnit = that;
    }

    /**
     * Construct an offset unit from an offset unit.  The identifier will be
     * that of the offset unit if the offset is 0; otherwise; the identifier
     * will be <code>null</code>.
     *
     * @param offset	The amount of offset.
     * @param unit	The given unit.
     */
    public OffsetUnit(double offset, OffsetUnit that)
    {
	this(offset, that, offset == 0 ? that.getIdentifier() : null);
    }

    /**
     * Construct an offset unit from an offset unit and an identifier..
     *
     * @param offset		The amount of offset.
     * @param unit		The given unit.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     */
    public OffsetUnit(double offset, OffsetUnit that, String identifier)
    {
	super(identifier);
	this.offset = offset + that.offset;
	scaledUnit = that.scaledUnit;
    }

    /**
     * Indicates if this instance is a unit of time.
     */
    protected boolean isTime()
    {
	return SI.second.isConvertible(this);
    }

    /**
     * Clones this unit, changing the identifier.
     * @param identifier	The name or abbreviation for the cloned unit.
     *				May be <code>null</code> or empty.
     */
    protected Unit protectedClone(String identifier)
    {
	return new OffsetUnit(0, this, identifier);
    }

    /**
     * Raise an offset unit to a power.
     *
     * @param power		The power to raise this unit by.
     */
    public Unit pow(int power)
    {
	return scaledUnit.pow(power);
    }

    /**
     * Raise an offset unit to a power.
     *
     * @param power		The power to raise this unit by.
     */
    public Unit pow(double power)
    {
	return scaledUnit.pow(power);
    }

    /**
     * Return the definition of this unit.
     *
     * @return          The definition of this unit (e.g. "K @ 273.15" for
     *			degree celsius).
     */
    public String getDefinition()
    {
	String	definition;
	String	scaledString = scaledUnit.toString();

	if (scaledString.indexOf(' ') != -1)
	    scaledString = "(" + scaledString + ")";
	if (!isTime())
	{
	    definition = scaledString + " @ " + offset;
	}
	else
	{
	    try
	    {
		definition =
		    scaledString + " since " +
		    dateFormat.format(
			new Date((long)(
			    millisecond.toThis(
				offset, scaledUnit) + offsetUnitOrigin)));
	    }
	    catch (UnitException e)
	    {
		definition = e.toString();
	    }		// can't happen
	}
	return definition;
    }

    /**
     * Multiply an offset unit by another unit.
     *
     * @param that		The unit with which to multiply this unit.
     * @exception UnitExcepting	Can't multiply units.
     */
    public Unit multiply(Unit that)
	throws UnitException
    {
	return that.multiply(scaledUnit);
    }

    /**
     * Divide an offset unit by another unit.
     *
     * @param that		The unit to divide into this unit.
     * @exception UnitException	Can't divide units.
     */
    public Unit divide(Unit that)
	throws UnitException
    {
	return that.divideInto(scaledUnit);
    }

    /**
     * Divide an offset unit into another unit.
     *
     * @param that      The unit to be divide by this unit.
     * @return          The quotient of the two units.
     * @promise		Neither unit has been modified.
     * @throws UnitException	Meaningless operation.
     */
    protected Unit divideInto(Unit that)
	throws UnitException
    {
	return that.divide(scaledUnit);
    }

    /**
     * Convert values to this unit from another unit.
     *
     * @param values	The values to be converted.
     * @param that      The unit of <code>values</code>.
     * @return          The converted values in units of this unit.
     * @require		The units are convertible.
     * @promise		Neither unit has been modified.
     * @exception	The units are not convertible.
     */
    public double[] toThis(double[] values, Unit that)
	throws UnitException
    {
	double[]	newValues = that.toThat(values, scaledUnit);
	for (int i = 0; i < newValues.length; ++i)
	    newValues[i] -= offset;
	return newValues;
    }

    /**
     * Convert values to this unit from another unit.
     *
     * @param values	The values to be converted.
     * @param that      The unit of <code>values</code>.
     * @return          The converted values in units of this unit.
     * @require		The units are convertible.
     * @promise		Neither unit has been modified.
     * @exception	The units are not convertible.
     */
    public float[] toThis(float[] values, Unit that)
        throws UnitException
    {
	float[]	newValues = that.toThat(values, scaledUnit);
	for (int i = 0; i < newValues.length; ++i)
	    newValues[i] -= offset;
	return newValues;
    }

    /**
     * Convert values from this unit to another unit.
     *
     * @param values	The values to be converted in units of this unit.
     * @param that      The unit to which to convert the values.
     * @return          The converted values.
     * @require		The units are convertible.
     * @promise		Neither unit has been modified.
     * @exception	The units are not convertible.
     */
    public double[] toThat(double values[], Unit that)
	throws UnitException
    {
	double[]	newValues = (double[])values.clone();
	for (int i = 0; i < newValues.length; ++i)
	    newValues[i] += offset;
	return that.toThis(newValues, scaledUnit);
    }

    /**
     * Convert values from this unit to another unit.
     *
     * @param values	The values to be converted in units of this unit.
     * @param that      The unit to which to convert the values.
     * @return          The converted values.
     * @require		The units are convertible.
     * @promise		Neither unit has been modified.
     * @exception	The units are not convertible.
     */
    public float[] toThat(float values[], Unit that)
        throws UnitException
    {
	float[]	newValues = (float[])values.clone();
	for (int i = 0; i < newValues.length; ++i)
	    newValues[i] += offset;
	return that.toThis(newValues, scaledUnit);
    }

    /**
     * Gets the absolute unit of this unit.  An interval in the underlying
     * physical quantity has the same numeric value in an absolute unit of a
     * unit as in the unit itself -- but an absolute unit is always referenced
     * to the physical origin of the underlying physical quantity.  For
     * example, the absolute unit corresponding to degrees celsius is degrees
     * kelvin -- and calling this method on a degrees celsius unit obtains a
     * degrees kelvin unit.
     * @return		The absolute unit corresponding to this unit.
     */
    public Unit
    getAbsoluteUnit()
    {
      return scaledUnit.getAbsoluteUnit();
    }

    /**
     * Indicate whether this unit is convertible with another unit.  If one unit
     * is convertible with another, then the <code>toThis(...)</code>/ and
     * <code>toThat(...)</code> methods will not throw a UnitException.  Unit A
     * is convertible with unit B if and only if unit B is convertible with unit
     * A; hence, calling-order is irrelevant.
     *
     * @param unit	The other unit.
     * @return		True if and only if this unit is convertible with the
     *			other unit.
     */
    public boolean isConvertible(Unit unit)
    {
      return scaledUnit.isConvertible(unit);
    }

    /**
     * Test this class.
     *
     * @param args		Arguments (ignored).
     * @exception UnitException	A problem occurred.
     */
    public static void main(String[] args)
	throws UnitException
    {
	BaseUnit	degK = SI.kelvin;
	Unit		degC = new OffsetUnit(273.15, degK);
	ScaledUnit	degR = new ScaledUnit(1/1.8, degK);
	Unit		degF = new OffsetUnit(459.67, degR);

	System.out.println("degC=\"" + degC + "\"");

	System.out.println("degF=\"" + degF + "\"");

	System.out.println("degF.toThis(0,degC)=" +
	    degF.toThis(0,degC));

	System.out.println("degF.toThat(32,degC)=" +
	    degF.toThat(32,degC));

	double[] values;

	values = degF.toThis(new double[] {0,100},degC);
	System.out.println("degF.toThis({0,100},degC)=" +
	    values[0] + "," + values[1]);

	values = degF.toThat(new double[] {32,212},degC);
	System.out.println("degF.toThat({32,212},degC)=" +
	    values[0] + "," + values[1]);

	System.out.println("degF.pow(2)=" + degF.pow(2));
	System.out.println("degF.multiply(degC)=" + degF.multiply(degC));
	System.out.println("degC.multiply(degF)=" + degC.multiply(degF));
	System.out.println("degF.divide(degC)=" + degF.divide(degC));
	System.out.println("degC.divide(degF)=" + degC.divide(degF));

	System.out.println("new OffsetUnit(0,SI.second).toString()=" +
	    new OffsetUnit(0,SI.second).toString());
	System.out.println("new OffsetUnit(-1.8e9,SI.second).toString()=" +
	    new OffsetUnit(-1.8e9,SI.second).toString());
    }

  public boolean equals(Unit unit) {
    return (unit instanceof OffsetUnit) &&
           scaledUnit.equals(((OffsetUnit) unit).scaledUnit) &&
           (offset == ((OffsetUnit) unit).offset);
  }
}


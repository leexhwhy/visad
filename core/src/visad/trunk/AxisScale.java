package visad;

import visad.*;
import java.awt.Color;
import java.rmi.RemoteException;

public class AxisScale implements java.io.Serializable {
    /** X_AXIS identifier */
    public final static int X_AXIS = 0;
    /** Y_AXIS identifier */
    public final static int Y_AXIS = 1;
    /** Z_AXIS identifier */
    public final static int Z_AXIS = 2;

    private VisADLineArray scaleArray;
    private ScalarMap scalarMap;
    private String label;
    private Color myColor = Color.white;
    private static final double SCALE = 0.06;
    private static final double OFFSET = 1.05;
    private double[] dataRange = new double[2];
    private int myAxis = -1;
    private DisplayImpl display;
    private int axisOrdinal = -1;
    private String myLabel;

    /**
     * Construct a new AxisScale for the given ScalarMap
     * @param map  ScalarMap to monitor.  Must be mapped to one of
     *             Display.XAxis, Display.YAxis, Display.ZAxis
     * @throws  VisADException  bad ScalarMap or other VisAD problem
     */
    public AxisScale(ScalarMap map)
        throws VisADException
    {
        scalarMap = map;
        DisplayRealType displayScalar = scalarMap.getDisplayScalar();
        if (!displayScalar.equals(Display.XAxis) &&
            !displayScalar.equals(Display.YAxis) &&
            !displayScalar.equals(Display.ZAxis)) 
        throw new DisplayException("AxisSale: DisplayScalar " +
                                   "must be XAxis, YAxis or ZAxis");
        myAxis = (displayScalar.equals(Display.XAxis)) ? X_AXIS :
             (displayScalar.equals(Display.YAxis)) ? Y_AXIS : Z_AXIS;
        myLabel = scalarMap.getScalarName();
        boolean ok = makeScale();
    }

    /**
     * Get the position of this AxisScale on the Axis (first, second, third).
     *
     * @return  position from the axis (first = 0, second = 1, etc)
     */
    public int getAxisOrdinal()
    {
        return axisOrdinal;
    }

    /**
     * Set the position of this AxisScale on the axis.  Should only
     * be called by ScalarMap
     *
     * @param  ordinalValue  axis position (0 = first, 1 = second, etc)
     */
    protected void setAxisOrdinal(int ordinalValue)
    {
        axisOrdinal = ordinalValue;
    }

    /** 
     * Set the label to be used for this axis.  The default is the
     * ScalarName of the ScalarMap.
     *
     * @param  label  label to be used
     */
    public void setLabel(String label)
    {
        myLabel = label;
        if (!scalarMap.getScalarName().equals(label))
            scalarMap.setScalarName(label);
    }

    /**
     * Get the label of the AxisScale.
     *
     * @return label
     */
    public String getLabel()
    {
        return myLabel;
    }

    /**
     * Get axis that the scale will be displayed on.
     *
     * @return  axis  (X_AXIS, Y_AXIS or Z_AXIS)
     */
    public int getAxis()
    {
        return myAxis;
    }

    /**
     * Get the Scale to pass to the renderer.
     *
     * @return  VisADLineArray representing the scale
     */
    public VisADLineArray getScaleArray()
    {
        return scaleArray;
    }

    /**
     * Create the scale.
     * @return  true if scale was successfully created, otherwise false
     */
    public boolean makeScale()
            throws VisADException {
      DisplayImpl display = scalarMap.getDisplay();
      if (display == null) return false;
      DisplayRenderer displayRenderer = display.getDisplayRenderer();
      if (displayRenderer == null) return false;
      if (axisOrdinal < 0) {
        axisOrdinal = displayRenderer.getAxisOrdinal(myAxis);
      }
      dataRange = scalarMap.getRange();
      VisADLineArray[] arrays = new VisADLineArray[4];
      boolean twoD = displayRenderer.getMode2D();
  
  // now create scale along axis at axisOrdinal position in array
  // twoD may help define orientation
  
      // compute graphics positions
      // these are {x, y, z} vectors
      double[] base = null; // vector from one character to another
      double[] up = null; // vector from bottom of character to top
      double[] startn = null; // -1.0 position along axis
      double[] startp = null; // +1.0 position along axis
  
      double XMIN = -1.0;
      double YMIN = -1.0;
      double ZMIN = -1.0;
  
      double line = 2.0 * axisOrdinal * SCALE;
  
      double ONE = 1.0;
      if (dataRange[0] > dataRange[1]) ONE = -1.0; // inverted range
      if (myAxis == X_AXIS) {
        base = new double[] {SCALE, 0.0, 0.0};
        up = new double[] {0.0, SCALE, SCALE};
        startp = new double[] {ONE, YMIN * (OFFSET + line), ZMIN * (OFFSET + line)};
        startn = new double[] {-ONE, YMIN * (OFFSET + line), ZMIN * (OFFSET + line)};
      }
      else if (myAxis == Y_AXIS) {
        base = new double[] {0.0, -SCALE, 0.0};
        up = new double[] {SCALE, 0.0, SCALE};
        startp = new double[] {XMIN * (OFFSET + line), ONE, ZMIN * (OFFSET + line)};
        startn = new double[] {XMIN * (OFFSET + line), -ONE, ZMIN * (OFFSET + line)};
      }
      else if (myAxis == Z_AXIS) {
        base = new double[] {0.0, 0.0, -SCALE};
        up = new double[] {SCALE, SCALE, 0.0};
        startp = new double[] {XMIN * (OFFSET + line), YMIN * (OFFSET + line), ONE};
        startn = new double[] {XMIN * (OFFSET + line), YMIN * (OFFSET + line), -ONE};
      }
      if (twoD) {
        // zero out z coordinates
        base[2] = 0.0;
        up[2] = 0.0;
        startn[2] = 0.0;
        startp[2] = 0.0;
        if (myAxis == 2) return false;
      }
  
      // compute tick mark values
      double range = Math.abs(dataRange[1] - dataRange[0]);
      double min = Math.min(dataRange[0], dataRange[1]);
      double max = Math.max(dataRange[0], dataRange[1]);
      double tens = 1.0;
      if (range < tens) {
        tens /= 10.0;
        while (range < tens) tens /= 10.0;
      }
      else {
        while (10.0 * tens <= range) tens *= 10.0;
      }
      // now tens <= range < 10.0 * tens;
      double ratio = range / tens;
      if (ratio < 2.0) {
        tens /= 5.0;
      }
      else if (ratio < 4.0) {
        tens /= 2.0;
      }
      // now tens = interval between tick marks
  
      long bot = (int) Math.ceil(min / tens);
      long top = (int) Math.floor(max / tens);
      if (bot == top) {
        if (bot < 0) top++;
        else bot--;
      }
      // now bot * tens = value of lowest tick mark, and
      // top * tens = values of highest tick mark

      arrays[0] = new VisADLineArray();
      int nticks = (int) (top - bot) + 1;
      // coordinates has three entries for (x, y, z) of each point
      // two points determine a line segment,
      // hence 6 coordinates entries per segment
      float[] coordinates = new float[6 * (nticks + 1)];
      // draw base line
      for (int i=0; i<3; i++) { // loop over x, y & z coordinates
        coordinates[i] = (float) startn[i];
        coordinates[3 + i] = (float) startp[i];
      }
      // now coordinates[0], [1] and [2]
  
      // draw tick marks
      int k = 6;
      for (long j=bot; j<=top; j++) {
        double val = j * tens;
        double a = (val - min) / (max - min);
        for (int i=0; i<3; i++) {
          if ((k + 3 + i) < coordinates.length) {
            // guard against error that cannot happen, but was seen?
            coordinates[k + i] = (float) ((1.0 - a) * startn[i] + a * startp[i]);
            coordinates[k + 3 + i] = (float) (coordinates[k + i] - 0.5 * up[i]);
          }
        }
        k += 6;
      }
      arrays[0].vertexCount = 2 * (nticks + 1);
      arrays[0].coordinates = coordinates;
  
      double[] startbot = new double[3];
      double[] starttop = new double[3];
      double[] startlabel = new double[3];
      // compute positions along axis of low and high tick marks
      double botval = bot * tens;
      double topval = top * tens;
      double abot = (botval - min) / (max - min);
      double atop = (topval - min) / (max - min);
      for (int i=0; i<3; i++) {
        startbot[i] = (1.0 - abot) * startn[i] + abot * startp[i] - 1.5 * up[i];
        starttop[i] = (1.0 - atop) * startn[i] + atop * startp[i] - 1.5 * up[i];
        startlabel[i] = 0.5 * (startn[i] + startp[i]) - 1.5 * up[i];
      }
      // all labels rendered with 'true' for centered
  
      // draw RealType name
      arrays[1] = PlotText.render_label(myLabel, startlabel,
                                        base, up, true);
  
      String botstr = PlotText.shortString(botval);
      String topstr = PlotText.shortString(topval);
      if (RealType.Time.equals(scalarMap.getScalar())) {
        RealType rtype = (RealType) scalarMap.getScalar();
        botstr = new Real(rtype, botval).toValueString();
        topstr = new Real(rtype, topval).toValueString();
      }
      // draw number at bottom tick mark
      arrays[2] = PlotText.render_label(botstr, startbot, base, up, true);
      // draw number at top tick mark
      arrays[3] = PlotText.render_label(topstr, starttop, base, up, true);
  
      scaleArray = VisADLineArray.merge(arrays);
      return true;
    }
  
    /**
     * Get the color of this axis scale.
     *
     * @return  Color of the scale.
     */
    public Color getColor()
    {
        return myColor;
    }

    /**
     * Set the color of this axis scale.
     */
    public void setColor(Color color) 
    {
        myColor = color;
    }
  
    /** 
     * Set the color of this axis scale.
     * @param   color   array of red, green, and blue values in 
     *                  the range (0.0 - 1.0). color must be float[3].
     */
    public void setColor(float[] color) 
    {
      setColor(new Color(color[0], color[1], color[2]));
    }
}

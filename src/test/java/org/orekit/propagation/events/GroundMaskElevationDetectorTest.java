/* Copyright 2002-2014 CS Systèmes d'Information
 * Licensed to CS Systèmes d'Information (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.propagation.events;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.orekit.Utils;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.EquinoctialOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.EcksteinHechlerPropagator;
import org.orekit.propagation.events.handlers.StopOnIncreasing;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

@Deprecated
public class GroundMaskElevationDetectorTest {

    private double mu;
    private double ae;
    private double c20;
    private double c30;
    private double c40;
    private double c50;
    private double c60;

    @Test
    public void testEvent() throws OrekitException {

        final TimeScale utc = TimeScalesFactory.getUTC();
        final Vector3D position = new Vector3D(-6142438.668, 3492467.56, -25767.257);
        final Vector3D velocity = new Vector3D(505.848, 942.781, 7435.922);
        final AbsoluteDate date = new AbsoluteDate(2003, 9, 16, utc);
        final Orbit orbit = new EquinoctialOrbit(new PVCoordinates(position,  velocity),
                                                 FramesFactory.getEME2000(), date, mu);

        Propagator propagator =
            new EcksteinHechlerPropagator(orbit, ae, mu, c20, c30, c40, c50, c60);

        // Earth and frame
        double ae =  6378137.0; // equatorial radius in meter
        double f  =  1.0 / 298.257223563; // flattening
        Frame ITRF2005 = FramesFactory.getITRF(IERSConventions.IERS_2010, true); // terrestrial frame at an arbitrary date
        BodyShape earth = new OneAxisEllipsoid(ae, f, ITRF2005);
        GeodeticPoint point = new GeodeticPoint(FastMath.toRadians(48.833),
                                                FastMath.toRadians(2.333),
                                                0.0);
        TopocentricFrame topo = new TopocentricFrame(earth, point, "Gstation");
        double [][] masque = {{FastMath.toRadians(0),FastMath.toRadians(5)},
                              {FastMath.toRadians(30),FastMath.toRadians(4)},
                              {FastMath.toRadians(60),FastMath.toRadians(3)},
                              {FastMath.toRadians(90),FastMath.toRadians(2)},
                              {FastMath.toRadians(120),FastMath.toRadians(3)},
                              {FastMath.toRadians(150),FastMath.toRadians(4)},
                              {FastMath.toRadians(180),FastMath.toRadians(5)},
                              {FastMath.toRadians(210),FastMath.toRadians(6)},
                              {FastMath.toRadians(240),FastMath.toRadians(5)},
                              {FastMath.toRadians(270),FastMath.toRadians(4)},
                              {FastMath.toRadians(300),FastMath.toRadians(3)},
                              {FastMath.toRadians(330),FastMath.toRadians(4)}};
        GroundMaskElevationDetector detector =
            new GroundMaskElevationDetector(masque, topo).
            withHandler(new StopOnIncreasing<GroundMaskElevationDetector>());

        AbsoluteDate startDate = new AbsoluteDate(2003, 9, 15, 20, 0, 0, utc);
        propagator.resetInitialState(propagator.propagate(startDate));
        propagator.addEventDetector(detector);
        final SpacecraftState fs = propagator.propagate(startDate.shiftedBy(Constants.JULIAN_DAY));
        double elevation = topo.getElevation(fs.getPVCoordinates().getPosition(), fs.getFrame(), fs.getDate());
        Assert.assertEquals(0.065, elevation, 2.0e-5);

    }

    @Test
    public void testMasque() throws OrekitException {

        // Earth and frame
        BodyShape earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                                               Constants.WGS84_EARTH_FLATTENING,
                                               FramesFactory.getITRF(IERSConventions.IERS_2010, true));
        TopocentricFrame topo = new TopocentricFrame(earth, new GeodeticPoint(0.0, 0.0, 0.0), "");
        double [][] masque = {{FastMath.toRadians(  0),FastMath.toRadians(5)},
                              {FastMath.toRadians(180),FastMath.toRadians(3)},
                              {FastMath.toRadians(-90),FastMath.toRadians(4)}};
        GroundMaskElevationDetector detector = new GroundMaskElevationDetector(masque, topo);

        double azimuth = FastMath.toRadians(90);
        double elevation = detector.getElevation(azimuth);
        Assert.assertEquals(FastMath.toRadians(4), elevation, 1.0e-15);

    }

    @Test(expected=IllegalArgumentException.class)
    public void testException() throws OrekitException {

        // Earth and frame
        BodyShape earth = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                                               Constants.WGS84_EARTH_FLATTENING,
                                               FramesFactory.getITRF(IERSConventions.IERS_2010, true));
        TopocentricFrame topo = new TopocentricFrame(earth, new GeodeticPoint(0.0, 0.0, 0.0), "");
        double [][] masque = {{FastMath.toRadians(   0),FastMath.toRadians(5)},
                              {FastMath.toRadians( 360),FastMath.toRadians(4)}};
        new GroundMaskElevationDetector(masque, topo);

    }

    @Before
    public void setUp() {
        Utils.setDataRoot("regular-data");
        mu  = 3.9860047e14;
        ae  = 6.378137e6;
        c20 = -1.08263e-3;
        c30 = 2.54e-6;
        c40 = 1.62e-6;
        c50 = 2.3e-7;
        c60 = -5.5e-7;
    }

    @After
    public void tearDown() {
        mu   = Double.NaN;
        ae   = Double.NaN;
        c20  = Double.NaN;
        c30  = Double.NaN;
        c40  = Double.NaN;
        c50  = Double.NaN;
        c60  = Double.NaN;
    }

}


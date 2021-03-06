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
package org.orekit.attitudes;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.errors.OrekitMessages;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.AngularCoordinates;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.PVCoordinatesProvider;


/**
 * Base class for ground pointing attitude providers.
 *
 * <p>This class is a basic model for different kind of ground pointing
 * attitude providers, such as : body center pointing, nadir pointing,
 * target pointing, etc...
 * </p>
 * <p>
 * The object <code>GroundPointing</code> is guaranteed to be immutable.
 * </p>
 * @see     AttitudeProvider
 * @author V&eacute;ronique Pommier-Maurussane
 */
public abstract class GroundPointing implements AttitudeProvider {

    /** Serializable UID. */
    private static final long serialVersionUID = -1459257023765594793L;

    /** Body frame. */
    private final Frame bodyFrame;

    /** Default constructor.
     * Build a new instance with arbitrary default elements.
     * @param bodyFrame the frame that rotates with the body
     */
    protected GroundPointing(final Frame bodyFrame) {
        this.bodyFrame = bodyFrame;
    }

    /** Get the body frame.
     * @return body frame
     */
    public Frame getBodyFrame() {
        return bodyFrame;
    }

    /** Compute the target point in specified frame.
     * @param pvProv provider for PV coordinates
     * @param date date at which target point is requested
     * @param frame frame in which observed ground point should be provided
     * @return observed ground point position in specified frame
     * @throws OrekitException if some specific error occurs,
     * such as no target reached
     */
    protected abstract Vector3D getTargetPoint(final PVCoordinatesProvider pvProv,
                                               final AbsoluteDate date, final Frame frame)
        throws OrekitException;

    /** Compute the target point position/velocity in specified frame.
     * <p>The default implementation use a simple two points finite differences scheme,
     * it may be replaced by more accurate models in specialized implementations.</p>
     * @param pvProv provider for PV coordinates
     * @param date date at which target point is requested
     * @param frame frame in which observed ground point should be provided
     * @return observed ground point position/velocity in specified frame
     * @throws OrekitException if some specific error occurs,
     * such as no target reached
     */
    protected PVCoordinates getTargetPV(final PVCoordinatesProvider pvProv,
                                        final AbsoluteDate date, final Frame frame)
        throws OrekitException {

        // target point position in same frame as initial pv
        final Vector3D intersectionP = getTargetPoint(pvProv, date, frame);

        // velocity of target point due to satellite and target motions
        final double h  = 0.1;
        final double scale = 1.0 / (2 * h);
        final Vector3D intersectionM1h = getTargetPoint(pvProv, date.shiftedBy(-h), frame);
        final Vector3D intersectionP1h = getTargetPoint(pvProv, date.shiftedBy( h), frame);
        final Vector3D intersectionV   = new Vector3D(scale, intersectionP1h, -scale, intersectionM1h);

        return new PVCoordinates(intersectionP, intersectionV);

    }


    /** {@inheritDoc} */
    public Attitude getAttitude(final PVCoordinatesProvider pvProv, final AbsoluteDate date,
                                final Frame frame)
        throws OrekitException {

        // Construction of the satellite-target position/velocity vector at t-h, t and t+h
        final double h = 0.1;

        final AbsoluteDate dateM1H = date.shiftedBy(-h);
        final PVCoordinates pvM1H  = pvProv.getPVCoordinates(dateM1H, frame);
        final Vector3D deltaPM1h   = getTargetPoint(pvProv, dateM1H, frame).subtract(pvM1H.getPosition());

        final PVCoordinates pv0    = pvProv.getPVCoordinates(date, frame);
        final Vector3D deltaP0     = getTargetPoint(pvProv, date, frame).subtract(pv0.getPosition());

        final AbsoluteDate dateP1H = date.shiftedBy(h);
        final PVCoordinates pvP1H  = pvProv.getPVCoordinates(dateP1H, frame);
        final Vector3D deltaPP1h   = getTargetPoint(pvProv, dateP1H, frame).subtract(pvP1H.getPosition());

        // New orekit exception if null position.
        if (deltaP0.equals(Vector3D.ZERO)) {
            throw new OrekitException(OrekitMessages.SATELLITE_COLLIDED_WITH_TARGET);
        }

        // Attitude rotation:
        // line of sight -> z satellite axis,
        // satellite velocity -> x satellite axis.
        final Rotation rot    = new Rotation(deltaP0,   pv0.getVelocity(),   Vector3D.PLUS_K, Vector3D.PLUS_I);

        // Attitude spin
        final Rotation rotM1h = new Rotation(deltaPM1h, pvM1H.getVelocity(), Vector3D.PLUS_K, Vector3D.PLUS_I);
        final Rotation rotP1h = new Rotation(deltaPP1h, pvP1H.getVelocity(), Vector3D.PLUS_K, Vector3D.PLUS_I);
        final Vector3D spin   = AngularCoordinates.estimateRate(rotM1h, rotP1h, 2 * h);

        return new Attitude(date, frame, rot, spin);

    }

}

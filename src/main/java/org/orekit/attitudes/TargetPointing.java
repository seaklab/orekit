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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.PVCoordinatesProvider;



/**
 * This class handles target pointing attitude provider.

 * <p>
 * This class represents the attitude provider where the satellite z axis is
 * pointing to a ground point target.</p>
 * <p>
 * The target position is defined in a body frame specified by the user.
 * It is important to make sure this frame is consistent.
 * </p>
 * <p>
 * The object <code>TargetPointing</code> is guaranteed to be immutable.
 * </p>
 * @see     GroundPointing
 * @author V&eacute;ronique Pommier-Maurussane
 */
public class TargetPointing extends GroundPointing {

    /** Serializable UID. */
    private static final long serialVersionUID = -8002434923471977301L;

    /** Target in body frame. */
    private final PVCoordinates target;


    /** Creates a new instance from body frame and target expressed in cartesian coordinates.
     * @param bodyFrame body frame.
     * @param target target position in body frame
     */
    public TargetPointing(final Frame bodyFrame, final Vector3D target) {
        super(bodyFrame);
        this.target = new PVCoordinates(target, Vector3D.ZERO);
    }

    /** Creates a new instance from body shape and target expressed in geodetic coordinates.
     * @param targetGeo target defined as a geodetic point in body shape frame
     * @param shape body shape
     */
    public TargetPointing(final GeodeticPoint targetGeo, final BodyShape shape) {
        super(shape.getBodyFrame());
        // Transform target from geodetic coordinates to position-velocity coordinates
        target = new PVCoordinates(shape.transform(targetGeo), Vector3D.ZERO);
    }

    /** {@inheritDoc} */
    protected Vector3D getTargetPoint(final PVCoordinatesProvider pvProv,
                                      final AbsoluteDate date, final Frame frame)
        throws OrekitException {
        return getBodyFrame().getTransformTo(frame, date).transformPosition(target.getPosition());
    }

    /** {@inheritDoc} */
    @Override
    protected PVCoordinates getTargetPV(final PVCoordinatesProvider pvProv,
                                        final AbsoluteDate date, final Frame frame)
        throws OrekitException {
        return getBodyFrame().getTransformTo(frame, date).transformPVCoordinates(target);
    }

}

/* Copyright 2002-2010 Centre National d'Études Spatiales
 * Licensed to CS Communication & Systèmes (CS) under one or more
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
package org.orekit.propagation.numerical;

import java.io.Serializable;

import org.orekit.attitudes.AttitudeProvider;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.time.AbsoluteDate;

/** Interface defining mapping between simple state arrays and {@link SpacecraftState} instances.
 *
 * @see org.orekit.propagation.SpacecraftState
 * @see org.orekit.propagation.numerical.NumericalPropagator
 * @author V&eacute;ronique Pommier-Maurussane
 * @version $Revision$ $Date$
 */
public interface StateMapper extends Serializable {

    /** Set the attitude provider.
     * @param attitudeProvider attitude provider
     */
    void setAttitudeProvider(final AttitudeProvider attitudeProvider);

    /** Convert spacecraft state to state array.
    * @param s spacecraft state to map
    * @param stateVector flat array into which the state vector should be mapped */
    void mapStateToArray(SpacecraftState s, double[] stateVector);

     /** Convert state array to space dynamics objects (AbsoluteDate and OrbitalParameters).
     * @param array state as a flat array
     * @param date integration date
     * @param mu central attraction coefficient used for propagation (m<sup>3</sup>/s<sup>2</sup>)
     * @param frame frame in which integration is performed
     * @return state corresponding to the flat array as a space dynamics object
     * @exception OrekitException if the attitude state cannot be determined
     * by the attitude provider
     */
    SpacecraftState mapArrayToState(final double[] array, final AbsoluteDate date,
                                    final double mu, final Frame frame)
        throws OrekitException;

}
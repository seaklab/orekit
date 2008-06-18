/* Copyright 2002-2008 CS Communication & Systèmes
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.propagation.numerical.forces.perturbations;

import org.apache.commons.math.geometry.Vector3D;
import org.orekit.bodies.ThirdBody;
import org.orekit.errors.OrekitException;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.OrekitSwitchingFunction;
import org.orekit.propagation.numerical.TimeDerivativesEquations;
import org.orekit.propagation.numerical.forces.ForceModel;


/** Third body attraction force model.
 *
 * @author Fabien Maussion
 * @author Véronique Pommier-Maurussane
 * @version $Revision:1665 $ $Date:2008-06-11 12:12:59 +0200 (mer., 11 juin 2008) $
 */
public class ThirdBodyAttraction implements ForceModel {

    /** Serializable UID. */
    private static final long serialVersionUID = 9017402538195695004L;

    /** The body to consider. */
    private final ThirdBody body;

    /** Simple constructor.
     * @param body the third body to consider
     * (ex: {@link org.orekit.models.bodies.Sun} or
     * {@link org.orekit.models.bodies.Moon})
     */
    public ThirdBodyAttraction(final ThirdBody body) {
        this.body = body;
    }

    /** {@inheritDoc} */
    public void addContribution(final SpacecraftState s, final TimeDerivativesEquations adder)
        throws OrekitException {

        // compute bodies separation vectors and squared norm
        final Vector3D centralToBody = body.getPosition(s.getDate(), s.getFrame());
        final double r2Central       = Vector3D.dotProduct(centralToBody, centralToBody);
        final Vector3D satToBody     = centralToBody.subtract(s.getPVCoordinates().getPosition());
        final double r2Sat           = Vector3D.dotProduct(satToBody, satToBody);

        // compute relative acceleration
        final Vector3D gamma =
            new Vector3D(body.getMu() * Math.pow(r2Sat, -1.5), satToBody,
                        -body.getMu() * Math.pow(r2Central, -1.5), centralToBody);

        // add contribution to the ODE second member
        adder.addXYZAcceleration(gamma.getX(), gamma.getY(), gamma.getZ());

    }

    /** {@inheritDoc} */
    public OrekitSwitchingFunction[] getSwitchingFunctions() {
        return new OrekitSwitchingFunction[0];
    }

}

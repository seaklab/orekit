/* Copyright 2002-2008 CS Communication & Systèmes
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
package org.orekit.forces;

import org.apache.commons.math.geometry.Vector3D;
import org.orekit.forces.drag.DraggingSpacecraft;
import org.orekit.forces.radiation.SolarRadiationPressureSpacecraft;

/** This class represents the features of a simplified spacecraft.
 * <p>The model of this spacecraft is a simple spherical model, this
 * means that all coefficients are constant and do not depend of
 * the direction. As such, it is a simple container that returns the
 * values set in the constructor.</p>
 *
 * @author Édouard Delente
 * @author Fabien Maussion
 * @version $Revision:1665 $ $Date:2008-06-11 12:12:59 +0200 (mer., 11 juin 2008) $
 */
public class SphericalSpacecraft
    implements SolarRadiationPressureSpacecraft, DraggingSpacecraft {

    /** Serializable UID. */
    private static final long serialVersionUID = 5457704222311833198L;

    /** Surface (m<sup>2</sup>). */
    private double surface;

    /** Drag coefficient. */
    private double dragCoeff;

    /** Absorption coefficient. */
    private double absorptionCoeff;

    /** Specular reflection coefficient. */
    private double reflectionCoeff;

    /** Simple constructor.
     * @param surface Surface (m<sup>2</sup>)
     * @param dragCoeff Drag coefficient
     * @param absorptionCoeff coefficient Absorption coefficient
     * @param reflectionCoeff Specular reflection coefficient
     */
    public SphericalSpacecraft(final double surface, final double dragCoeff,
                               final double absorptionCoeff, final double reflectionCoeff) {

        this.surface = surface;
        this.dragCoeff = dragCoeff;
        this.absorptionCoeff = absorptionCoeff;
        this.reflectionCoeff = reflectionCoeff;
    }

    /** Get the surface.
     * @param direction direction of the flux
     * (ignored in this implementation)
     * @return surface (m<sup>2</sup>)
     */
    public double getSurface(final Vector3D direction) {
        return surface;
    }

    /** Get the drag coefficients vector.
     * @param direction direction of the atmospheric flux
     * @return drag coefficients vector
     */
    public Vector3D getDragCoef(final Vector3D direction) {
        return new Vector3D(dragCoeff, direction);
    }

    /** Get the absorption coefficients vector.
     * @param direction direction of the light flux
     * @return absorption coefficients vector
     */
    public Vector3D getAbsCoef(final Vector3D direction) {
        return new Vector3D(absorptionCoeff, direction);
    }

    /** Get the specular reflection coefficients vector.
     * @param direction direction of the light flux
     * @return specular reflection coefficients vector
     */
    public Vector3D getReflectionCoef(final Vector3D direction) {
        return new Vector3D(reflectionCoeff, direction);
    }

    /** Set the surface.
     * @param surface surface (m<sup>2</sup>)
     */
    public void setSurface(final double surface) {
        this.surface = surface;
    }

    /** Set the drag coefficient.
     * @param dragCoeff coefficient drag coefficient
     */
    public void setDragCoeff(final double dragCoeff) {
        this.dragCoeff = dragCoeff;
    }

    /** Set the absorption coefficient.
     * @param absorptionCoeff coefficient absorption coefficient
     */
    public void setAbsorptionCoeff(final double absorptionCoeff) {
        this.absorptionCoeff = absorptionCoeff;
    }

    /** Set the specular reflection coefficient.
     * @param reflectionCoeff coefficient specular reflection coefficient
     */
    public void setReflectionCoeff(final double reflectionCoeff) {
        this.reflectionCoeff = reflectionCoeff;
    }

}

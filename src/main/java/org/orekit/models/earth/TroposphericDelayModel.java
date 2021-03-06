/* Copyright 2011-2012 Space Applications Services
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
package org.orekit.models.earth;

import java.io.Serializable;

/** Defines a tropospheric model, used to calculate the path delay imposed to
 * electro-magnetic signals between an orbital satellite and a ground station.
 * @author Thomas Neidhart
 */
public interface TroposphericDelayModel extends Serializable {

    /** Calculates the tropospheric path delay for the signal path from a ground
     * station to a satellite.
     *
     * @param elevation the elevation of the satellite in degrees
     * @param height the height of the station in m above sea level
     * @return the path delay due to the troposphere in m
     */
    double calculatePathDelay(final double elevation, final double height);

    /** Calculates the tropospheric signal delay for the signal path from a
     * ground station to a satellite. This method exists only for convenience
     * reasons and returns the same as
     *
     * <pre>
     *   {@link SaastamoinenModel#calculatePathDelay(double, double)}/{@link org.orekit.utils.Constants#SPEED_OF_LIGHT}
     * </pre>
     *
     * @param elevation the elevation of the satellite in degrees
     * @param height the height of the station in m above sea level
     * @return the signal delay due to the troposphere in s
     */
    double calculateSignalDelay(final double elevation, final double height);

}

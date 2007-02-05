package fr.cs.aerospace.orekit.propagation;

import java.io.Serializable;
import org.spaceroots.mantissa.geometry.Rotation;
import org.spaceroots.mantissa.geometry.Vector3D;
import fr.cs.aerospace.orekit.attitudes.AttitudeKinematics;
import fr.cs.aerospace.orekit.frames.Frame;
import fr.cs.aerospace.orekit.orbits.Orbit;
import fr.cs.aerospace.orekit.orbits.OrbitalParameters;
import fr.cs.aerospace.orekit.time.AbsoluteDate;
import fr.cs.aerospace.orekit.utils.PVCoordinates;

/** This class is the representation of the state to propagate with the 
 * {@link NumericalPropagator}.
 *  
 * <p> It contains an {@link OrbitalParameters orbital state} at a current 
 * {@link AbsoluteDate} both handled by an {@link Orbit}, plus the current
 * mass and attitude.
 * </p>
 * <p>
 * The instance <code>SpacecraftState</code> is guaranted to be immutable.
 * </p>
 * @see NumericalPropagator
 * @author F. Maussion
 */
public class SpacecraftState 
  implements Serializable {

  /** Create a new instance from orbital state and mass.
   * @param orbit the orbit
   * @param mass the mass (kg)
   * @param ak the attitude kinematics
   */
  public SpacecraftState(Orbit orbit, double mass, AttitudeKinematics ak) {
    this.orbit = orbit;
    this.mass  = mass;
    this.attitude = ak;
  }

  /** Create a new instance from orbital state and mass.
   * Initialize the attitude to the identity.
   * @param orbit the orbit
   * @param mass the mass (kg)
   */
  public SpacecraftState(Orbit orbit, double mass) {
    this.orbit = orbit;
    this.mass  = mass;
    this.attitude = new AttitudeKinematics();
  }

  /** Create a new instance from orbital state only 
   * (gives an arbitrary value (1000 kg) for the mass and 
   * initialize the attitude to the identity).
   * @param orbit the orbit
   */
  public SpacecraftState(Orbit orbit) {
    this.orbit = orbit;
    this.mass  = 1000;
    this.attitude = new AttitudeKinematics();
  }

  /** Gets the current orbit.
   * @return the orbit
   */
  public Orbit getOrbit() {
    return orbit;
  }

  /** Gets the current mass.
   * @return the mass (kg)
   */
  public double getMass() {
    return mass;
  }

  /** Get the date.
   * @return date
   */
  public AbsoluteDate getDate() {
    return orbit.getDate();
  }

  /** Get the orbital parameters.
   * @return orbital parameters
   */
  public OrbitalParameters getParameters() {
    return orbit.getParameters();
  }

  /** Get the inertial frame.
   * @return the frame
   */
  public Frame getFrame() {
    return orbit.getFrame();
  }

  /** Gets the attitude representation.
   * @return the attitude kinematics.
   */
  public AttitudeKinematics getAttitudeKinematics() {
    return attitude;
  }

  /** Get the attitude quaternion.
   * <p> The {@link Rotation} returned by this method represents the rotation
   * to apply to a vector expressed in spacecraft frame to obtain the same vector 
   * defined in the orbit inertial frame </p> 
   * @return the attitude rotation of the spacecraft
   */
  public Rotation getAttitude() {
    return attitude.getAttitude();
  }

  /** Get the attitute rotation derivative.
   * <p> The {@link Vector3D} returned by this method represents the instant rotation
   * to apply to a velocity vector expressed in spacecraft frame to obtain the same
   * vector defined in the orbit inertial frame </p> 
   * @return the instant rotation of the spacecraft
   */
  public Vector3D getspinAxis() {
    return attitude.getspinAxis();    
  }

  /** Get the semi-major axis.
   * @return semi-major axis (m)
   */
  public double getA() {
    return orbit.getA();
  }

  /** Get the first component of the eccentricity vector (as per equinoctial parameters).
   * @return e cos(&omega; + &Omega;), first component of eccentricity vector
   * @see #getE()
   */
  public double getEx(){
    return orbit.getParameters().getEquinoctialEx();
  }

  /** Get the second component of the eccentricity vector (as per equinoctial parameters).
   * @return e sin(&omega; + &Omega;), second component of the eccentricity vector
   * @see #getE()
   */
  public double getEy(){
    return orbit.getParameters().getEquinoctialEy();
  }

  /** Get the first component of the inclination vector (as per equinoctial parameters).
   * @return tan(i/2) cos(&Omega;), first component of the inclination vector
   * @see #getI()
   */
  public double getHx(){
    return orbit.getHx();
  }

  /** Get the second component of the inclination vector (as per equinoctial parameters).
   * @return tan(i/2) sin(&Omega;), second component of the inclination vector
   * @see #getI()
   */
  public double getHy(){
    return orbit.getHy();
  }

  /** Get the true latitude argument (as per equinoctial parameters).
   * @return v + &omega; + &Omega; true latitude argument (rad)
   * @see #getLE()
   * @see #getLM()
   */
  public double getLv(){
    return orbit.getLv();
  }

  /** Get the eccentric latitude argument (as per equinoctial parameters).
   * @return E + &omega; + &Omega; eccentric latitude argument (rad)
   * @see #getLv()
   * @see #getLM()
   */
  public double getLE(){
    return orbit.getLE();
  }

  /** Get the mean latitude argument (as per equinoctial parameters).
   * @return M + &omega; + &Omega; mean latitude argument (rad)
   * @see #getLv()
   * @see #getLE()
   */
  public double getLM(){
    return orbit.getLM();
  }  

  // Additional orbital elements

  /** Get the eccentricity.
   * @return eccentricity
   * @see #getEx()
   * @see #getEy()
   */
  public double getE() {
    return orbit.getE();
  }

  /** Get the inclination.
   * @return inclination (rad)
   * @see #getHx()
   * @see #getHy()
   */
  public double getI() {
    return orbit.getI();
  }

  /** Get the {@link PVCoordinates}.
   * Compute the position and velocity of the satellite. This method caches its
   * results, and recompute them only when the method is called with a new value
   * for mu. The result is provided as a reference to the internally cached 
   * {@link PVCoordinates}, so the caller is responsible to copy it in a separate
   * {@link PVCoordinates} if it needs to keep the value for a while.
   * @param mu central attraction coefficient (m<sup>3</sup>/s<sup>2</sup>)
   * @return pvCoordinates in inertial frame (reference to an
   * internally cached pvCoordinates which can change)
   */
  public PVCoordinates getPVCoordinates(double mu) {
    return orbit.getPVCoordinates(mu);
  }

  /** Orbital state */
  private final Orbit orbit;

  /** Current mass (kg)*/
  private final double mass;

  /** Attitude */
  private final AttitudeKinematics attitude;

  private static final long serialVersionUID = 4660942382447513104L;

}
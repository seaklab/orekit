package fr.cs.aerospace.orekit.models.spacecraft;

public interface KeplerianSpacecraft {
	  /** Get the mass.
	   * @return mass (kg)
	   */
	  public double getMass();

	  /** Set the mass.
	   * @param mass new mass (kg)
	   */
	  public void setMass(double mass);
}
package fr.cs.aerospace.orekit.iers;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.ParseException;

import fr.cs.aerospace.orekit.FindFile;
import fr.cs.aerospace.orekit.errors.OrekitException;
import fr.cs.aerospace.orekit.iers.IERSData;
import fr.cs.aerospace.orekit.time.AbsoluteDate;
import fr.cs.aerospace.orekit.time.UTCScale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IERSDataTest extends TestCase {

  private static final File rootDir;
  static {
    try {
      rootDir = FindFile.find("/tests-src/fr/cs/aerospace/orekit/data", "/");
    } catch (FileNotFoundException fnfe) {
      throw new RuntimeException("unexpected failure");
    }
  }

  public void testNoDirectory() {
    checkFailure("inexistant-directory");
  }

  public void testNotADirectory() {
    checkFailure("regular-data/UTC-TAI.history");
  }

  public void testMissingMonths() {
    checkFailure("missing-months");
  }

  public void testDuplicatedData() {
    checkFailure("duplicated-data");
  }

  public void testRegular() throws OrekitException {
    checkSuccess("regular-data");
    assertEquals(-32.0, UTCScale.getInstance().offsetFromTAI(946684800), 10e-8);    
  }

  public void testCompressed() throws OrekitException {
    checkSuccess("compressed-data");
    assertEquals(-32.0, UTCScale.getInstance().offsetFromTAI(946684800), 10e-8);    
  }

  public void testNoData() throws OrekitException {
    checkSuccess("empty-directory");
    assertEquals(0.0, UTCScale.getInstance().offsetFromTAI(946684800), 10e-8);    
  }
  
  public void testFirstDate() throws OrekitException, ParseException {
	  checkSuccess("regular-data");
	  double delta = IERSData.getInstance().getFirstDate().minus(new AbsoluteDate("2002-01-01T00:00:00", UTCScale.getInstance()));
	  assertEquals(0, delta, 0);
  }
  
  public void testEndDate() throws OrekitException, ParseException {
	  checkSuccess("regular-data");
	  double delta = IERSData.getInstance().getEndDate().minus(new AbsoluteDate("2005-12-31T00:00:00", UTCScale.getInstance()));
	  assertEquals(0, delta, 0);
  }
  
  public void testUTCDate() throws OrekitException, ParseException {
	  checkSuccess("regular-data");
	  double delta = IERSData.getInstance().getUTCStartDate().minus(new AbsoluteDate("1972-01-01T00:00:00", UTCScale.getInstance()));
	  assertEquals(0, delta, 0);
  }

  private void checkSuccess(String directoryName) {
    try {
      System.setProperty("orekit.iers.directory",
                         new File(rootDir, directoryName).getAbsolutePath());
      assertNotNull(IERSData.getInstance());
    } catch (OrekitException e) {
      fail(e.getMessage());
    }
  }

  private void checkFailure(String directoryName) {
    try {
      System.setProperty("orekit.iers.directory",
                         new File(rootDir, directoryName).getAbsolutePath());
      IERSData.getInstance();
      fail("an exeption should have been thrown");
    } catch (OrekitException e) {
      // expected behaviour
    } catch (Exception e) {
      fail("wrong exception caught");
    }
  }

  public void tearDown() {
    try {
      // resetting the singletons to null
      Field instance = UTCScale.class.getDeclaredField("instance");
      instance.setAccessible(true);
      instance.set(null, null);
      instance.setAccessible(false);

      instance = IERSData.class.getDeclaredField("instance");
      instance.setAccessible(true);
      instance.set(null, null);
      instance.setAccessible(false);

    } catch (Exception e) {
      // ignored
    }
    
  }

  public static Test suite() {
    return new TestSuite(IERSDataTest.class);
  }
  
  
}
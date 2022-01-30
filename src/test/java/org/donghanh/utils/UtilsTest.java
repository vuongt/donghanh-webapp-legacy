package org.donghanh.utils;

import junit.framework.TestCase;

import static org.donghanh.utils.Utils.getJuryDistributionFR;

public class UtilsTest extends TestCase {

    public void testNbJuries() {
    }

    public void testGetJuryDistributionSG() {
    }

    public void testGetJuryDistributionFR() {
        assertEquals(getJuryDistributionFR(0, 7, 12), "G0G1G2G3G4G5G6G7G");
        assertEquals(getJuryDistributionFR(1, 7, 12), "G0G8G9G10G11G12G1G2G");
        assertEquals(getJuryDistributionFR(2, 7, 12), "G0G3G4G5G6G7G8G9G");
        assertEquals(getJuryDistributionFR(3, 7, 12), "G0G10G11G12G1G2G3G4G");
    }
}
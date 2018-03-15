package be.somedi.printen.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static be.somedi.printen.util.FormatUtil.*;
import static org.junit.Assert.assertEquals;

public class FormatUtilTest {

    @Test
    public void testFormatRiziv() {
        String riziv = "11261797003";
        String result = formatRiziv(riziv);
        assertEquals("1/12617/97/003", result);
    }

    @Test
    public void testformatStringWithBlanks() {
        String expected = "Kenneth                 ";
        String result = formatStringWithBlanks("Kenneth", 24);
        assertEquals(expected.length(), result.length());
        assertEquals(expected, result);
    }

    @Test
    public void testformatStringWithMaxChars() {
        String expected = "MijnBelachelijkL";
        String result = formatStringWithMaxChars("MijnBelachelijkLangeVoornaam", 16);
        assertEquals(expected, result);

        assertEquals("Jos", formatStringWithMaxChars("Jos", 16));
    }

    @Test
    public void testFormatDate() {
        LocalDateTime ldt = LocalDateTime.of(2018, Month.APRIL, 8, 8, 22);
        assertEquals("1804080822", formatDateAndTime(ldt));

        String date = "30071962";
        String result = formatDate(date);
        assertEquals("620730", result);
    }

    @Test
    public void testFormatGender() {
        String female = "V-198000";
        String male = "M151581";
        assertEquals("X", formatGender(female));
        assertEquals("Y", formatGender(male));
    }
}

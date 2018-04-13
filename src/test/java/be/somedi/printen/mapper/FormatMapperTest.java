package be.somedi.printen.mapper;

import be.somedi.printen.model.UMFormat;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormatMapperTest {

    @Test
    public void mapToFormat() {
        assertEquals(UMFormat.MEDIDOC, FormatMapper.mapToFormat("Medidoc"));
        assertEquals(UMFormat.MEDAR, FormatMapper.mapToFormat("MEDAR"));
        assertEquals(UMFormat.MEDICARD, FormatMapper.mapToFormat("medicard"));
    }
}
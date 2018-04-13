package be.somedi.printen.mapper;

import be.somedi.printen.model.UMFormat;

import java.util.Arrays;

public class FormatMapper {

    public static UMFormat mapToFormat(String format){
        return Arrays.stream(UMFormat.values()).filter(umFormat -> umFormat.name().equalsIgnoreCase(format)).findFirst().orElse(UMFormat.MEDIDOC);
    }
}

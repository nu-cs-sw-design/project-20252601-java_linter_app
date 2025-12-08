package datasource;

import domain.internal_representation.Context;

import java.util.Map;

/**
 * Target interface (Adapter pattern) for reading bytecode and converting it to ClassInfo.
 * This interface allows for different bytecode reading implementations.
 */
public interface DataModelConverter {

    Context buildContext(Map<String, byte[]> classFiles, String folderPath);
}
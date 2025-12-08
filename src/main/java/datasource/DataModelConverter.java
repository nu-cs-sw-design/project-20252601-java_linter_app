package datasource;

import domain.internal_representation.ClassInfo;

/**
 * Target interface (Adapter pattern) for reading bytecode and converting it to ClassInfo.
 * This interface allows for different bytecode reading implementations.
 */
public interface DataModelConverter {

    ClassInfo convertClass(byte[] bytes);
}
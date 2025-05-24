package com.uoroot.sgi.application.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.util.ReflectionUtils;

import com.uoroot.sgi.domain.exception.BadRequestException;

/**
 * Utility class for handling partial updates of entities.
 */
public class PartialUpdateUtils {

    /**
     * Applies non-null fields from the source object to the target object.
     * Only fields that are not null in the source will be copied to the target.
     * 
     * @param <T> The type of the objects
     * @param source The source object containing the updated fields
     * @param target The target object to update
     * @throws BadRequestException if there's an error during the update process
     */
    public static <T> void applyPartialUpdate(T source, T target) {
        try {
            // Get all declared fields from the source class
            Map<String, Field> sourceFields = Arrays.stream(source.getClass().getDeclaredFields())
                    .collect(Collectors.toMap(Field::getName, field -> field));
            
            // For each field in the target class
            Arrays.stream(target.getClass().getDeclaredFields()).forEach(targetField -> {
                Field sourceField = sourceFields.get(targetField.getName());
                
                // If the field exists in both classes
                if (sourceField != null) {
                    // Make fields accessible
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    
                    try {
                        // Get value from source
                        Object value = sourceField.get(source);
                        
                        // Only update if the source value is not null
                        if (value != null) {
                            targetField.set(target, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new BadRequestException("Error al actualizar el campo: " + targetField.getName());
                    }
                }
            });
        } catch (Exception e) {
            throw new BadRequestException("Error al aplicar la actualización parcial: " + e.getMessage());
        }
    }
    
    /**
     * Applies a map of field updates to a target object.
     * 
     * @param <T> The type of the target object
     * @param updates Map of field names to their new values
     * @param target The target object to update
     * @param afterUpdateAction Optional action to execute after the update
     * @throws BadRequestException if there's an error during the update process
     */
    public static <T> void applyFieldUpdates(Map<String, Object> updates, T target, Consumer<T> afterUpdateAction) {
        try {
            updates.forEach((fieldName, value) -> {
                // Find the field in the target class
                Field field = ReflectionUtils.findField(target.getClass(), fieldName);
                
                if (field == null) {
                    throw new BadRequestException("Campo no encontrado: " + fieldName);
                }
                
                // Make the field accessible
                field.setAccessible(true);
                
                try {
                    // Set the new value
                    field.set(target, value);
                } catch (IllegalAccessException e) {
                    throw new BadRequestException("Error al actualizar el campo: " + fieldName);
                }
            });
            
            // Execute after update action if provided
            if (afterUpdateAction != null) {
                afterUpdateAction.accept(target);
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error al aplicar las actualizaciones: " + e.getMessage());
        }
    }
}

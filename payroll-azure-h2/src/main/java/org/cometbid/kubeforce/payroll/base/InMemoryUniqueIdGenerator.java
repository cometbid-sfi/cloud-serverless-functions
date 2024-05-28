/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.cometbid.kubeforce.payroll.base;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

/**
 *
 * @author samueladebowale
 */
public class InMemoryUniqueIdGenerator implements UniqueIdGenerator<UUID> {

    static final long LEFT_LIMIT = 1L;
    static final long RIGHT_LIMIT = 100_000_000_000_000L;
    
    @Override
    public UUID getNextUniqueId() {
        return UUID.randomUUID();
    }

    public static String getUniqueStringId() {
        return UUID.randomUUID().toString();
    }
    
    public static long getNextUniqueLongId() {
        return RandomGenerator.getDefault().nextLong(LEFT_LIMIT, RIGHT_LIMIT);
    }
    
    public static Long generateUniqueLongId() {
        return new Random().nextLong();
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ejunk
 */
// 1. Create a shared event bus for cross-component communication
public class EventBus {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    public static void submitTask(Runnable task) {
        executor.execute(task);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import java.util.logging.Level;
import static org.bukkit.Bukkit.getLogger;

/**
 *
 * @author doria
 */
public class log {
    public static void info(String message) {
        getLogger().log(Level.INFO, "[CubeAssemble " + gameConfig.version + "] {0}", message);
    }
    
    public static void error(String message) {
        getLogger().log(Level.SEVERE, "[CubeAssemble " + gameConfig.version + "] {0}", message);
    }
    
    public static void warning(String message) {
        getLogger().log(Level.WARNING, "[CubeAssemble " + gameConfig.version + "] {0}", message);
    }
}

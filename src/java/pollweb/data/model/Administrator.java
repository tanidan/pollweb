/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.model;

import java.util.List;

/**
 *
 * @author venecia2
 */
public interface Administrator extends Manager{
    
    List<Manager> getManagers();
    
    void setManagers(List<Manager> managers);
    
}

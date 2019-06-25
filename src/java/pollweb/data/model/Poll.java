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
public interface Poll {
    
    int getKey();

    void setKey(int key);
    
    String getTitle();

    void setTitle(String title);
    
    Manager getManager();

    void setManager(Manager manager);
            
    String getOpenText();
    
    void setOpenText(String text);
    
    String getCloseText();
    
    void setCloseText(String text);
    
    boolean isReserved();
    
    void setReserved(boolean reserve);
    
}

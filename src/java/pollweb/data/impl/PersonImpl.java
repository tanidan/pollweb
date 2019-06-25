/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.data.impl;

import pollweb.data.model.Person;

/**
 *
 * @author venecia2
 */
public abstract class PersonImpl implements Person {
    
     int key;
     String password;
     String email;
      

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int newKey) {
        key = newKey;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String newEmail) {
        email = newEmail;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String newPassword) {
        password = newPassword;
    }
    
}

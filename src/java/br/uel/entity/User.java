/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uel.entity;

/**
 *
 * @author Vanessa
 */
public class User {
    private String login;
    private Integer userId;
    private String name;
    private String password;
    private String dtOfBirth;
    private String city;
    private String state;
    private String street;
    private Integer number;
    private String complement;
    private String neighborhood;
    private String  zipCode;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDtOfBirth() {
        return dtOfBirth;
    }

    public void setDtOfBirth(String dtOfBirth) {
        this.dtOfBirth = dtOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        if (number == null )
        {
            return 0;
        }
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

   
    
}

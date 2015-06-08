package eu.w4.contrib.genactors;

import eu.w4.contrib.genactors.TWFTassignation;
import fr.w4.buildtime.dynamic.TWFactor;

public class TWFfullActor extends TWFactor {

  private String password;
  private TWFTassignation roles;

  public TWFfullActor() {
    super();
    password = "";
    roles = new TWFTassignation();
  }

  public String getPassword() {
    return password;
  }

  public TWFTassignation getRole() {
    return roles;
  }

  public TWFTassignation getRoles() {
    return roles;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(TWFTassignation roles) {
    this.roles = roles;
  }

  public void setRoles(TWFTassignation roles) {
    this.roles = roles;
  }

}

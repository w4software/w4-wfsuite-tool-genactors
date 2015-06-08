package eu.w4.contrib.genactors;

import fr.w4.TWFexception;
import fr.w4.buildtime.ref.TWFactorRef;
import fr.w4.buildtime.ref.TWFdomainRef;
import fr.w4.buildtime.ref.TWFroleRef;

public class TWFassignation
{
  private TWFroleRef roleRef;
  private TWFdomainRef domainRef;

  public TWFassignation()
  {
    roleRef = new TWFroleRef();
    domainRef = new TWFdomainRef();
  }

  public TWFassignation(String s)
  {
    this();
    fromString(s);
  }

  public void fromString(String s)
  {
    if (s.contains(":")) {
      String[] tokens = s.split(":", 2);
      domainRef = new TWFdomainRef(tokens[0]);
      roleRef = new TWFroleRef(tokens[1]);
    } else {
      domainRef = new TWFdomainRef();
      roleRef = new TWFroleRef(s);
    }
  }

  @Override
  public String toString()
  {
    if (domainRef.getStr()==null || "".equals(domainRef.getStr()))
    {
      return roleRef.getStr();
    } else {
      return domainRef.getStr() + ":" + roleRef.getStr();
    }
  }

  public TWFroleRef getRoleRef()
  {
    return roleRef;
  }

  public TWFdomainRef getDomainRef()
  {
    return domainRef;
  }

  public void wfAssignToActor(TWFactorRef actorRef) throws TWFexception
  {
    if ((domainRef.getStr()==null || "".equals(domainRef.getStr())) && (domainRef.getId()==0))
    {
      actorRef.wfAssignRoleToActor(roleRef);
    } else {
      actorRef.wfAssignRoleToActorOnDomain(roleRef, domainRef);
    }
  }

}

package eu.w4.contrib.genactors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import eu.w4.contrib.genactors.TWFassignation;
import fr.w4.TWFexception;
import fr.w4.basic.TWFcollection;
import fr.w4.buildtime.ref.TWFTdomainRef;
import fr.w4.buildtime.ref.TWFTroleRef;
import fr.w4.buildtime.ref.TWFactorRef;

public class TWFTassignation extends TWFcollection
{

  private List<TWFassignation> delegate;

  public TWFTassignation()
  {
    delegate = new ArrayList<TWFassignation>();
  }

  @Override
  public int getNbItems()
  {
    return delegate.size();
  }

  @Override
  public int nbItems()
  {
    return getNbItems();
  }

  @Override
  public int indexOf(Object o)
  {
    return delegate.indexOf(o);
  }

  @Override
  public Enumeration elements()
  {
    return Collections.enumeration(delegate);
  }

  public void add(TWFassignation o)
  {
    delegate.add(o);
  }

  public void remove(TWFassignation o)
  {
    delegate.remove(o);
  }

  public TWFassignation assignationAt(int index)
  {
    return delegate.get(index);
  }

  public TWFTroleRef getRoles()
  {
    TWFTroleRef tRoles = new TWFTroleRef();
    for(TWFassignation a: delegate)
    {
      tRoles.add(a.getRoleRef());
    }
    return tRoles;
  }

  public TWFTdomainRef getDomains()
  {
    TWFTdomainRef tDomains = new TWFTdomainRef();
    for(TWFassignation a: delegate)
    {
      tDomains.add(a.getDomainRef());
    }
    return tDomains;
  }

  public void wfAssignToActor(TWFactorRef actorRef) throws TWFexception
  {
    for(TWFassignation a: delegate)
    {
      a.wfAssignToActor(actorRef);
    }
  }
}

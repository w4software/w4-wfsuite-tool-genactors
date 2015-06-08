package eu.w4.contrib.genactors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.beanutils.BeanMap;

import eu.w4.contrib.genactors.cli.CliPrinter;
import eu.w4.contrib.genactors.TWFTassignation;
import eu.w4.contrib.genactors.TWFassignation;
import eu.w4.contrib.genactors.TWFfullActor;
import fr.w4.TWFexception;
import fr.w4.basic.TWFname;
import fr.w4.buildtime.basic.TWFoperator;
import fr.w4.buildtime.basic.TWFsortMode;
import fr.w4.buildtime.dynamic.TWFTactor;
import fr.w4.buildtime.dynamic.TWFTdomain;
import fr.w4.buildtime.dynamic.TWFTrole;
import fr.w4.buildtime.dynamic.TWFdomain;
import fr.w4.buildtime.dynamic.TWFrole;
import fr.w4.buildtime.ref.TWFactorRef;
import fr.w4.buildtime.ref.TWFdomainRef;
import fr.w4.buildtime.ref.TWFroleRef;
import fr.w4.search.TWFTactorSelection;
import fr.w4.search.TWFTdomainSelection;
import fr.w4.search.TWFTroleSelection;
import fr.w4.search.TWFactorCrit;
import fr.w4.search.TWFactorSortBy;
import fr.w4.search.TWFdomainCrit;
import fr.w4.search.TWFdomainSortBy;
import fr.w4.search.TWFroleCrit;
import fr.w4.search.TWFroleSortBy;
import fr.w4.session.TWFnativeSession;
import fr.w4.session.TWFsession;

public class GenerateActors {

  private final String serverName;
  private final String instanceName;
  private final String login;
  private final String password;
  private boolean doCreateActors = true;
  private boolean doCreateDomains = true;
  private boolean doCreateRoles = true;
  private boolean doUpdateActors = true;
  private boolean doUpdateActorPasswords = true;
  private boolean doUpdateDomains = true;
  private boolean doUpdateRoles = true;
  private boolean doIncludeReferencedRoles = false;
  private boolean doIncludeReferencedDomains = false;
  private boolean doUnassignRoles = false;
  private boolean doAssignRoles = true;

  public GenerateActors(final String serverName, final String instanceName, final String login, final String password) {
    super();
    this.serverName = serverName;
    this.instanceName = instanceName;
    this.login = login;
    this.password = password;
  }

  public void write(List objects) throws TWFexception {
    TWFsession session = new TWFnativeSession();
    session.setServerName(serverName);
    session.setInstanceName(instanceName);
    session.openConnection();

    List<TWFfullActor> actors = new LinkedList<TWFfullActor>();

    TWFTrole roles = new TWFTrole();
    TWFTdomain domains = new TWFTdomain();

    for(Object o: objects)
    {
      if (o instanceof TWFfullActor) {
        actors.add((TWFfullActor) o);
      } else if (o instanceof TWFrole) {
        roles.add((TWFrole) o);
      } else if (o instanceof TWFdomain) {
        domains.add((TWFdomain) o);
      }
    }

    if(doIncludeReferencedDomains) {
      for (int i = 0; i < actors.size(); i++) {
        TWFfullActor fullActor = actors.get(i);
        for(int j=0; j<fullActor.getRoles().getNbItems();j++) {
          TWFdomainRef domain = fullActor.getRoles().assignationAt(j).getDomainRef();
          if (!domains.contains(domain)) {
            TWFdomain newDomain = new TWFdomain();
            newDomain.setStr(domain.getStr());
            newDomain.setUpperDomainRef(new TWFdomainRef("global"));
            domains.add(newDomain);
          }
        }
      }
    }

    if (doIncludeReferencedRoles) {
      for (int i = 0; i < actors.size(); i++) {
        TWFfullActor fullActor = actors.get(i);
        for(int j=0; j<fullActor.getRoles().getNbItems();j++) {
          TWFroleRef role = fullActor.getRoles().assignationAt(j).getRoleRef();
          if (!roles.contains(role)) {
            TWFrole newRole = new TWFrole();
            newRole.setStr(role.getStr());
            roles.add(newRole);
          }
        }
      }
    }

    try {
      session.login(login, password);

      if(doCreateDomains || doUpdateDomains) {
        for(int i=0; i<domains.getNbItems(); i++) {
          TWFdomain domain = domains.domainAt(i);
          CliPrinter.print("Importing domain " + domain.getStr() + "...");
          TWFTdomainSelection tSelection = new TWFTdomainSelection();
          tSelection.addSelection(TWFdomainCrit.WF_CRIT_NAME, TWFoperator.WF_EQUAL, domain.getStr());
          TWFTdomain existingDomains = TWFTdomain.wfSearchDomain(tSelection, TWFdomainSortBy.WF_NOSORT, TWFsortMode.WF_NOSORT, (short) 1);
          if (existingDomains.getNbItems()==0)
          {
            if (doCreateDomains) {
              domain.wfCreateDomain();
              CliPrinter.println("created.");
            } else {
              CliPrinter.println("not existing! ignored.");
            }
          } else {
            if (doUpdateDomains && existingDomains.domainAt(0).getId() > 100) {
              domain.setId(existingDomains.domainAt(0).getId());
              domain.wfModifyDomain();
              CliPrinter.println("updated.");
            } else {
              CliPrinter.println("already existing! ignored.");
            }
          }
        }
      }

      if (doCreateRoles || doUpdateRoles) {
        for(int i=0; i<roles.getNbItems(); i++) {
          TWFrole role = roles.roleAt(i);
          CliPrinter.print("Importing role " + role.getStr() + "...");
          TWFTroleSelection tSelection = new TWFTroleSelection();
          tSelection.addSelection(TWFroleCrit.WF_CRIT_NAME, TWFoperator.WF_EQUAL, role.getStr());
          TWFTrole existingRoles = TWFTrole.wfSearchRole(tSelection, TWFroleSortBy.WF_NOSORT, TWFsortMode.WF_NOSORT, (short) 1);
          if (existingRoles.getNbItems()==0)
          {
            if (doCreateRoles) {
              role.wfCreateRole();
              CliPrinter.println("created.");
            } else {
              CliPrinter.println("not existing! ignored.");
            }
          } else {
            if (doUpdateRoles && existingRoles.roleAt(0).getId()>100) {
              role.setId(existingRoles.roleAt(0).getId());
              role.wfModifyRole();
              CliPrinter.println("updated.");
            } else {
              CliPrinter.println("already existing! ignored.");
            }
          }
        }
      }

      for (int i = 0; i < actors.size(); i++) {
        TWFfullActor fullActor = actors.get(i);
        try {
          String status = "";
          CliPrinter.print("Importing actor " + fullActor.getStr() + "...");

          TWFTactorSelection tSelection = new TWFTactorSelection();
          tSelection.addSelection(TWFactorCrit.WF_CRIT_NAME, TWFoperator.WF_EQUAL, fullActor.getStr());
          TWFTactor existingActors = TWFTactor.wfSearchActor(tSelection, TWFactorSortBy.WF_NOSORT, TWFsortMode.WF_NOSORT, (short) 1);

          boolean actorExists = existingActors.getNbItems()>0;

          if (!actorExists)
          {
            if (doCreateActors) {
              fullActor.wfCreateActor(new TWFname(fullActor.getPassword()));
              actorExists = true;
              status = "created.";
            } else {
              status = "not existing! ignored.";
            }
          } else {
            if (doUpdateActors && existingActors.actorAt(0).getId()>100) {
              fullActor.setId(existingActors.actorAt(0).getId());
              fullActor.wfModifyActor();
              if (doUpdateActorPasswords && fullActor.getPassword()!=null && !"".equals(fullActor.getPassword())) {
                fullActor.wfChangePassword(new TWFname(""), new TWFname(fullActor.getPassword()), (short) 1);
              }
              status = "updated.";
            } else {
              status = "already existing! ignored.";
            }
          }
          CliPrinter.println(status);

          if (doUnassignRoles && actorExists) {
            CliPrinter.print("Unassigning all roles to " + fullActor.getStr() + "...");
            int removedCount = 0;
            TWFTdomainSelection tDomainsForActorSelection = new TWFTdomainSelection();
            tDomainsForActorSelection.addSelection (TWFdomainCrit.WF_CRIT_ACTOR, TWFoperator.WF_EQUAL, fullActor.getStr());
            TWFTdomain tDomainsForActor = TWFTdomain.wfSearchDomain (tDomainsForActorSelection, TWFdomainSortBy.WF_NOSORT, TWFsortMode.WF_NOSORT, (short)0);
            for(int j=0;j<tDomainsForActor.getNbItems();j++) {
              TWFdomain currentDomain = tDomainsForActor.domainAt(j);
              TWFTroleSelection tRolesForDomainSelection = new TWFTroleSelection();
              tRolesForDomainSelection.addSelection(TWFroleCrit.WF_CRIT_DOMAIN, TWFoperator.WF_EQUAL, currentDomain.getStr());
              tRolesForDomainSelection.addSelection(TWFroleCrit.WF_CRIT_ACTOR, TWFoperator.WF_EQUAL, fullActor.getStr());
              TWFTrole actorDomainRoles = TWFTrole.wfSearchRole(tRolesForDomainSelection, TWFroleSortBy.WF_NOSORT, TWFsortMode.WF_NOSORT, (short) 0);
              if (actorDomainRoles.getNbItems()>0) {
                new TWFactorRef(fullActor.getStr()).wfDeleteTroleOfActorOnDomain(actorDomainRoles.toNameArray(), new TWFdomainRef(currentDomain.getStr()));
                removedCount += actorDomainRoles.getNbItems();
              }
            }
            CliPrinter.println(""+removedCount+" unassigned");
          }

          if (doAssignRoles && actorExists) {
            CliPrinter.print("Assigning roles to " + fullActor.getStr() + "...");
            if (fullActor.getRoles().getNbItems() > 0) {
              fullActor.getRoles().wfAssignToActor(fullActor);
              status = "" + fullActor.getRoles().getNbItems() + " assigned";
            } else {
              status = "unchanged";
            }
            CliPrinter.println(status);
          }

        } catch (TWFexception e) {
          CliPrinter.println("Error " + e.getErrorCode());
          continue;
        }
      }
    } finally {
      session.closeConnection();
    }
  }

  public List read(File file, int sheetNumber) throws BiffException, IOException {
    // chargement du classeur
    final Workbook xlsWorkbook = Workbook.getWorkbook(file);
    final Sheet xlsSheet = xlsWorkbook.getSheet(sheetNumber);

    final int rows = xlsSheet.getRows();
    final int columns = xlsSheet.getColumns();
    final String[] headers;

    if (rows < 2) {
      throw new RuntimeException("Sheet must contains at least two lines (header and one content line");
    }

    Map<String, Integer> mapColumns = new HashMap<String, Integer>();
    headers = new String[columns];
    for (int c = 0; c < columns; c++) {
      String header = xlsSheet.getCell(c, 0).getContents();
      if (header == null || "".equals(header)) {
        headers[c] = null;
      } else {
        headers[c] = Helper.findField(header);
        mapColumns.put(header, c);
      }
    }

    List result = new ArrayList();

    // lecture des autres lignes et construction du résultat
    lrows: for (int r = 1; r < rows; r++) {
      final int cstart;
      final Object object;
      if ("TYPE".equalsIgnoreCase(headers[0])) {
        String typeContent = xlsSheet.getCell(0, r).getContents();
        if ("ACTOR".equalsIgnoreCase(typeContent))
        {
          object = new TWFfullActor();
        } else if ("ROLE".equalsIgnoreCase(typeContent)) {
          object = new TWFrole();
        } else if ("DOMAIN".equalsIgnoreCase(typeContent)) {
          object = new TWFdomain();
        } else {
          System.err.println("ERROR: Unknown object type <" + typeContent +"> on line " + r);
          continue lrows;
        }
        cstart = 1;
      } else {
        object = new TWFfullActor();
        cstart = 0;
      }

      Map<String, Object> map = new BeanMap(object);

      lcolumns: for (int c = cstart; c < columns; c++) {
        if (headers[c] == null)
          continue;
        Class<?> type = ((BeanMap) map).getType(headers[c]);
        String cellContent = xlsSheet.getCell(c, r).getContents();
        if (!map.containsKey(headers[c])) continue lcolumns;
        if (map.get(headers[c]) instanceof TWFTassignation) {
          if (cellContent!=null && !"".equals(cellContent)) {
            if (cellContent.contains(":")) {
              String[] tokens = cellContent.split(":", 2);
              if(tokens[0].startsWith("$"))
              {
                String domainCol = tokens[0].substring(1);
                String domainVal = xlsSheet.getCell(mapColumns.get(domainCol), r).getContents();

                String cont = domainVal+":"+tokens[1];
                ((TWFTassignation) map.get(headers[c])).add(new TWFassignation(cont));
              }
              else
              {
                ((TWFTassignation) map.get(headers[c])).add(new TWFassignation(cellContent));
              }
            }
          }
        } else if (TWFname.class.isAssignableFrom(type)) {
          TWFname name;
          try
          {
            name = (TWFname) type.newInstance();
            name.setStr(cellContent);
          }
          catch (Exception e)
          {
            System.err.println("ERROR: Could not handle property <" + headers[c] + "> of type <" + type + "> IGNORING...");
            headers[c]=null;
            continue;
          }
          map.put(headers[c], name);
        } else {
          map.put(headers[c], cellContent);
        }
      }

      result.add(object);
    }

    CliPrinter.println("" + (rows - 1) + " actors read from Excel file");
    return result;
  }

  public void process(File file, int sheetNumber) {
    try {
      write(read(file, sheetNumber));
    } catch (BiffException e) {
      throw new RuntimeException(e);
    } catch (TWFexception e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void setDoAssignRoles(boolean doAssignRoles) {
    this.doAssignRoles = doAssignRoles;
  }

  public void setDoCreateActors(boolean doCreateActors) {
    this.doCreateActors = doCreateActors;
  }

  public void setDoCreateRoles(boolean doCreateRoles) {
    this.doCreateRoles = doCreateRoles;
  }

  public void setDoCreateDomains(boolean doCreateDomains)
  {
    this.doCreateDomains = doCreateDomains;
  }

  public void setDoIncludeReferencedDomains(boolean doIncludeReferencedDomains)
  {
    this.doIncludeReferencedDomains = doIncludeReferencedDomains;
  }

  public void setDoIncludeReferencedRoles(boolean doIncludeReferencedRoles)
  {
    this.doIncludeReferencedRoles = doIncludeReferencedRoles;
  }

  public void setDoUnassignRoles(boolean doUnassignRoles)
  {
    this.doUnassignRoles = doUnassignRoles;
  }

  public void setDoUpdateActorPasswords(boolean doUpdateActorPasswords)
  {
    this.doUpdateActorPasswords = doUpdateActorPasswords;
  }

  public void setDoUpdateActors(boolean doUpdateActors)
  {
    this.doUpdateActors = doUpdateActors;
  }

  public void setDoUpdateDomains(boolean doUpdateDomains)
  {
    this.doUpdateDomains = doUpdateDomains;
  }

  public void setDoUpdateRoles(boolean doUpdateRoles)
  {
    this.doUpdateRoles = doUpdateRoles;
  }


}
